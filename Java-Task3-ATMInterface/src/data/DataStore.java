package data;

import model.Account;
import model.Transaction;
import model.TransactionType;
import util.AppLogger;
import util.SecurityUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Singleton data store that manages account and transaction persistence.
 * Uses CSV files for storage to avoid external database dependencies.
 *
 * <p>Thread-safe via {@link ConcurrentHashMap} and synchronized file writes.</p>
 */
public final class DataStore {

    private static final String DATA_DIRECTORY = "data";
    private static final String ACCOUNTS_FILE = "accounts.csv";
    private static final String TRANSACTIONS_FILE = "transactions.csv";

    private static final String ACCOUNTS_HEADER =
            "accountId,userId,hashedPin,holderName,balance";
    private static final String TRANSACTIONS_HEADER =
            "transactionId,accountId,type,amount,balanceBefore,balanceAfter,timestamp,description";

    private static DataStore instance;

    private final Map<String, Account> accountsByUserId;
    private final Map<String, Account> accountsById;
    private final Map<String, List<Transaction>> transactionsByAccountId;

    private DataStore() {
        this.accountsByUserId = new ConcurrentHashMap<>();
        this.accountsById = new ConcurrentHashMap<>();
        this.transactionsByAccountId = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance, creating it on first access.
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
            instance.loadData();
        }
        return instance;
    }

    // ─── Account Operations ────────────────────────────────────────

    public Optional<Account> findByUserId(String userId) {
        return Optional.ofNullable(accountsByUserId.get(userId.toUpperCase()));
    }

    public Optional<Account> findByAccountId(String accountId) {
        return Optional.ofNullable(accountsById.get(accountId.toUpperCase()));
    }

    public Collection<Account> getAllAccounts() {
        return Collections.unmodifiableCollection(accountsById.values());
    }

    /**
     * Persists all current account states to the CSV file.
     */
    public synchronized void saveAccounts() {
        Path filePath = Paths.get(DATA_DIRECTORY, ACCOUNTS_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write(ACCOUNTS_HEADER);
            writer.newLine();

            for (Account account : accountsById.values()) {
                String row = String.join(",",
                        account.getAccountId(),
                        account.getUserId(),
                        account.getHashedPin(),
                        account.getHolderName(),
                        String.valueOf(account.getBalance())
                );
                writer.write(row);
                writer.newLine();
            }
            AppLogger.debug("Accounts saved successfully.");
        } catch (IOException e) {
            AppLogger.error("Failed to save accounts", e);
        }
    }

    // ─── Transaction Operations ────────────────────────────────────

    /**
     * Records a transaction in memory and appends it to the CSV file.
     */
    public synchronized void addTransaction(Transaction transaction) {
        transactionsByAccountId
                .computeIfAbsent(transaction.getAccountId(), k -> new ArrayList<>())
                .add(transaction);
        appendTransactionToFile(transaction);
    }

    /**
     * Returns all transactions for a given account, sorted newest first.
     */
    public List<Transaction> getTransactions(String accountId) {
        List<Transaction> transactions = transactionsByAccountId
                .getOrDefault(accountId, Collections.emptyList());
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns the most recent N transactions for a given account.
     */
    public List<Transaction> getRecentTransactions(String accountId, int count) {
        return getTransactions(accountId).stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Generates a unique transaction ID based on the current count.
     */
    public String generateTransactionId() {
        long totalCount = transactionsByAccountId.values().stream()
                .mapToLong(List::size)
                .sum();
        return String.format("TXN%06d", totalCount + 1);
    }

    // ─── Data Loading ──────────────────────────────────────────────

    private void loadData() {
        ensureDataDirectoryExists();
        loadAccounts();
        loadTransactions();
    }

    private void ensureDataDirectoryExists() {
        try {
            Path dataDir = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
                AppLogger.info("Created data directory.");
            }
        } catch (IOException e) {
            AppLogger.error("Failed to create data directory", e);
        }
    }

    private void loadAccounts() {
        Path filePath = Paths.get(DATA_DIRECTORY, ACCOUNTS_FILE);

        if (!Files.exists(filePath)) {
            AppLogger.info("No accounts file found. Seeding default accounts...");
            seedDefaultAccounts();
            saveAccounts();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                parseAccountRow(line);
            }
            AppLogger.info("Loaded " + accountsById.size() + " accounts from file.");
        } catch (IOException e) {
            AppLogger.error("Failed to load accounts. Seeding defaults.", e);
            seedDefaultAccounts();
            saveAccounts();
        }
    }

    private void parseAccountRow(String row) {
        String[] parts = row.split(",", 5);
        if (parts.length < 5) {
            AppLogger.warning("Skipping malformed account row: " + row);
            return;
        }

        try {
            String accountId = parts[0].trim();
            String userId = parts[1].trim().toUpperCase();
            String hashedPin = parts[2].trim();
            String holderName = parts[3].trim();
            double balance = Double.parseDouble(parts[4].trim());

            Account account = new Account(accountId, userId, hashedPin, holderName, balance);
            accountsById.put(accountId.toUpperCase(), account);
            accountsByUserId.put(userId, account);
        } catch (NumberFormatException e) {
            AppLogger.warning("Skipping account row with invalid balance: " + row);
        }
    }

    private void loadTransactions() {
        Path filePath = Paths.get(DATA_DIRECTORY, TRANSACTIONS_FILE);

        if (!Files.exists(filePath)) {
            AppLogger.info("No transactions file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                parseTransactionRow(line);
            }

            long totalTransactions = transactionsByAccountId.values().stream()
                    .mapToLong(List::size).sum();
            AppLogger.info("Loaded " + totalTransactions + " transactions from file.");
        } catch (IOException e) {
            AppLogger.error("Failed to load transactions", e);
        }
    }

    private void parseTransactionRow(String row) {
        String[] parts = row.split(",", 8);
        if (parts.length < 8) {
            AppLogger.warning("Skipping malformed transaction row: " + row);
            return;
        }

        try {
            String transactionId = parts[0].trim();
            String accountId = parts[1].trim();
            TransactionType type = TransactionType.valueOf(parts[2].trim());
            double amount = Double.parseDouble(parts[3].trim());
            double balanceBefore = Double.parseDouble(parts[4].trim());
            double balanceAfter = Double.parseDouble(parts[5].trim());
            LocalDateTime timestamp = LocalDateTime.parse(parts[6].trim());
            String description = parts[7].trim().replace(";", ",");

            Transaction transaction = new Transaction(
                    transactionId, accountId, type, amount,
                    balanceBefore, balanceAfter, timestamp, description
            );

            transactionsByAccountId
                    .computeIfAbsent(accountId, k -> new ArrayList<>())
                    .add(transaction);
        } catch (IllegalArgumentException e) {
            AppLogger.warning("Skipping transaction row with invalid data: " + row);
        }
    }

    private void appendTransactionToFile(Transaction transaction) {
        Path filePath = Paths.get(DATA_DIRECTORY, TRANSACTIONS_FILE);
        try {
            boolean fileExists = Files.exists(filePath);
            try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

                if (!fileExists) {
                    writer.write(TRANSACTIONS_HEADER);
                    writer.newLine();
                }
                writer.write(transaction.toCsvRow());
                writer.newLine();
            }
        } catch (IOException e) {
            AppLogger.error("Failed to append transaction to file", e);
        }
    }

    // ─── Default Data Seeding ──────────────────────────────────────

    private void seedDefaultAccounts() {
        addAccount("ACC001", "USR001", "1234", "Yagnesh Patel", 50_000.00);
        addAccount("ACC002", "USR002", "5678", "Priya Sharma", 1_25_000.00);
        addAccount("ACC003", "USR003", "9012", "Rahul Kumar", 75_000.00);
        AppLogger.info("Seeded 3 default accounts.");
    }

    private void addAccount(String accountId, String userId, String rawPin,
                            String holderName, double balance) {
        String hashedPin = SecurityUtil.hashPin(rawPin);
        Account account = new Account(accountId, userId, hashedPin, holderName, balance);
        accountsById.put(accountId.toUpperCase(), account);
        accountsByUserId.put(userId.toUpperCase(), account);
    }
}
