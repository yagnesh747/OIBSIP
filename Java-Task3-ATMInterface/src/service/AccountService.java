package service;

import data.DataStore;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import exception.TransactionException;
import model.Account;
import model.Transaction;
import model.TransactionType;
import util.AppLogger;
import util.CurrencyFormatter;
import util.InputValidator;
import util.SecurityUtil;

/**
 * Core banking service that processes withdrawals, deposits, transfers,
 * and PIN changes. Enforces business rules like daily limits, denomination
 * validation, and minimum balance requirements.
 */
public class AccountService {

    private static final double MINIMUM_BALANCE = 500.0;

    private final DataStore dataStore;
    private final TransactionService transactionService;

    public AccountService() {
        this.dataStore = DataStore.getInstance();
        this.transactionService = new TransactionService();
    }

    /**
     * Processes a cash withdrawal from the given account.
     *
     * @param account the account to withdraw from
     * @param amount  the amount to withdraw
     * @return the recorded Transaction
     * @throws InvalidAmountException     if amount is invalid or not a valid denomination
     * @throws InsufficientFundsException if account lacks sufficient funds
     * @throws TransactionException       if daily withdrawal limit is exceeded
     */
    public Transaction withdraw(Account account, double amount)
            throws InvalidAmountException, InsufficientFundsException, TransactionException {

        validateWithdrawalAmount(account, amount);

        double balanceBefore = account.getBalance();
        double newBalance = balanceBefore - amount;

        if (newBalance < MINIMUM_BALANCE) {
            throw new InsufficientFundsException(balanceBefore, amount);
        }

        account.setBalance(newBalance);
        account.addToDailyWithdrawn(amount);
        dataStore.saveAccounts();

        Transaction transaction = transactionService.recordTransaction(
                account.getAccountId(),
                TransactionType.WITHDRAWAL,
                amount,
                balanceBefore,
                newBalance,
                "Cash withdrawal of " + CurrencyFormatter.format(amount)
        );

        AppLogger.info("Withdrawal processed: " + CurrencyFormatter.format(amount)
                + " from account " + account.getAccountId());
        return transaction;
    }

    /**
     * Processes a cash deposit into the given account.
     *
     * @param account the account to deposit into
     * @param amount  the amount to deposit
     * @return the recorded Transaction
     * @throws InvalidAmountException if the amount is invalid
     */
    public Transaction deposit(Account account, double amount)
            throws InvalidAmountException {

        validateDepositAmount(amount);

        double balanceBefore = account.getBalance();
        double newBalance = balanceBefore + amount;

        account.setBalance(newBalance);
        dataStore.saveAccounts();

        Transaction transaction = transactionService.recordTransaction(
                account.getAccountId(),
                TransactionType.DEPOSIT,
                amount,
                balanceBefore,
                newBalance,
                "Cash deposit of " + CurrencyFormatter.format(amount)
        );

        AppLogger.info("Deposit processed: " + CurrencyFormatter.format(amount)
                + " to account " + account.getAccountId());
        return transaction;
    }

    /**
     * Transfers funds from one account to another.
     *
     * @param fromAccount     the sender's account
     * @param toAccountId     the recipient's account ID
     * @param amount          the amount to transfer
     * @return the sender's Transaction record
     * @throws InvalidAmountException     if the amount is invalid
     * @throws InsufficientFundsException if sender lacks sufficient funds
     * @throws TransactionException       if the recipient account is not found
     */
    public Transaction transfer(Account fromAccount, String toAccountId, double amount)
            throws InvalidAmountException, InsufficientFundsException, TransactionException {

        validateTransferAmount(amount);

        if (fromAccount.getAccountId().equalsIgnoreCase(toAccountId)) {
            throw new TransactionException("Cannot transfer to the same account.");
        }

        Account toAccount = dataStore.findByAccountId(toAccountId.toUpperCase())
                .orElseThrow(() -> new TransactionException(
                        "Recipient account '" + toAccountId + "' not found."));

        if (!toAccount.isActive()) {
            throw new TransactionException("Recipient account is not active.");
        }

        double senderBalanceBefore = fromAccount.getBalance();
        double senderNewBalance = senderBalanceBefore - amount;

        if (senderNewBalance < MINIMUM_BALANCE) {
            throw new InsufficientFundsException(senderBalanceBefore, amount);
        }

        double recipientBalanceBefore = toAccount.getBalance();
        double recipientNewBalance = recipientBalanceBefore + amount;

        // Update balances atomically
        fromAccount.setBalance(senderNewBalance);
        toAccount.setBalance(recipientNewBalance);
        dataStore.saveAccounts();

        // Record both sides of the transfer
        Transaction senderTransaction = transactionService.recordTransaction(
                fromAccount.getAccountId(),
                TransactionType.TRANSFER_OUT,
                amount,
                senderBalanceBefore,
                senderNewBalance,
                "Transfer to " + toAccountId + " (" + toAccount.getHolderName() + ")"
        );

        transactionService.recordTransaction(
                toAccount.getAccountId(),
                TransactionType.TRANSFER_IN,
                amount,
                recipientBalanceBefore,
                recipientNewBalance,
                "Transfer from " + fromAccount.getAccountId()
                        + " (" + fromAccount.getHolderName() + ")"
        );

        AppLogger.info("Transfer processed: " + CurrencyFormatter.format(amount)
                + " from " + fromAccount.getAccountId() + " to " + toAccountId);
        return senderTransaction;
    }

    /**
     * Changes the PIN for the given account.
     *
     * @param account the account to update
     * @param oldPin  the current PIN for verification
     * @param newPin  the new PIN to set
     * @throws TransactionException if the old PIN is incorrect or the new PIN is invalid
     */
    public void changePin(Account account, String oldPin, String newPin)
            throws TransactionException {

        if (!SecurityUtil.verifyPin(oldPin, account.getHashedPin())) {
            throw new TransactionException("Current PIN is incorrect.");
        }

        if (!InputValidator.isValidPin(newPin)) {
            throw new TransactionException(
                    "New PIN must be " + InputValidator.MIN_PIN_LENGTH
                            + "-" + InputValidator.MAX_PIN_LENGTH + " digits.");
        }

        if (oldPin.equals(newPin)) {
            throw new TransactionException("New PIN must be different from the current PIN.");
        }

        account.setHashedPin(SecurityUtil.hashPin(newPin));
        dataStore.saveAccounts();

        transactionService.recordTransaction(
                account.getAccountId(),
                TransactionType.PIN_CHANGE,
                0,
                account.getBalance(),
                account.getBalance(),
                "PIN changed successfully"
        );

        AppLogger.info("PIN changed for account: " + account.getAccountId());
    }

    /**
     * Records a balance inquiry in the transaction log.
     */
    public void recordBalanceInquiry(Account account) {
        transactionService.recordTransaction(
                account.getAccountId(),
                TransactionType.BALANCE_INQUIRY,
                0,
                account.getBalance(),
                account.getBalance(),
                "Balance inquiry"
        );
    }

    // ─── Validation Helpers ────────────────────────────────────────

    private void validateWithdrawalAmount(Account account, double amount)
            throws InvalidAmountException, InsufficientFundsException, TransactionException {

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }

        if (amount < InputValidator.MIN_WITHDRAWAL) {
            throw new InvalidAmountException(
                    "Minimum withdrawal amount is "
                            + CurrencyFormatter.format(InputValidator.MIN_WITHDRAWAL) + ".");
        }

        if (amount > InputValidator.MAX_WITHDRAWAL) {
            throw new InvalidAmountException(
                    "Maximum single withdrawal is "
                            + CurrencyFormatter.format(InputValidator.MAX_WITHDRAWAL) + ".");
        }

        if (!InputValidator.isValidDenomination(amount)) {
            throw new InvalidAmountException(
                    "Amount must be a multiple of ₹"
                            + (int) InputValidator.WITHDRAWAL_DENOMINATION + ".");
        }

        double totalAfterWithdrawal = account.getDailyWithdrawn() + amount;
        if (totalAfterWithdrawal > InputValidator.DAILY_WITHDRAWAL_LIMIT) {
            double remaining = InputValidator.DAILY_WITHDRAWAL_LIMIT - account.getDailyWithdrawn();
            throw new TransactionException(
                    "Daily withdrawal limit exceeded. Remaining limit: "
                            + CurrencyFormatter.format(Math.max(remaining, 0)));
        }

        if (amount > account.getBalance() - MINIMUM_BALANCE) {
            throw new InsufficientFundsException(account.getBalance(), amount);
        }
    }

    private void validateDepositAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        if (amount < InputValidator.MIN_DEPOSIT) {
            throw new InvalidAmountException(
                    "Minimum deposit amount is "
                            + CurrencyFormatter.format(InputValidator.MIN_DEPOSIT) + ".");
        }
        if (amount > InputValidator.MAX_DEPOSIT) {
            throw new InvalidAmountException(
                    "Maximum deposit amount is "
                            + CurrencyFormatter.format(InputValidator.MAX_DEPOSIT) + ".");
        }
    }

    private void validateTransferAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }
        if (amount < InputValidator.MIN_TRANSFER) {
            throw new InvalidAmountException(
                    "Minimum transfer amount is "
                            + CurrencyFormatter.format(InputValidator.MIN_TRANSFER) + ".");
        }
        if (amount > InputValidator.MAX_TRANSFER) {
            throw new InvalidAmountException(
                    "Maximum transfer amount is "
                            + CurrencyFormatter.format(InputValidator.MAX_TRANSFER) + ".");
        }
    }
}
