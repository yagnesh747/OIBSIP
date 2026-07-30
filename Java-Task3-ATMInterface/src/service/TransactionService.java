package service;

import data.DataStore;
import model.Transaction;
import model.TransactionType;
import util.AppLogger;

import java.util.List;

/**
 * Manages the creation and retrieval of transaction records.
 * Acts as an intermediary between the business layer and the data store.
 */
public class TransactionService {

    private static final int MINI_STATEMENT_COUNT = 5;
    private final DataStore dataStore;

    public TransactionService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Creates and persists a new transaction record.
     *
     * @return the newly created Transaction
     */
    public Transaction recordTransaction(String accountId, TransactionType type,
                                         double amount, double balanceBefore,
                                         double balanceAfter, String description) {
        String transactionId = dataStore.generateTransactionId();

        Transaction transaction = new Transaction(
                transactionId, accountId, type, amount,
                balanceBefore, balanceAfter, description
        );

        dataStore.addTransaction(transaction);
        AppLogger.debug("Transaction recorded: " + transactionId + " | " + type);
        return transaction;
    }

    /**
     * Returns the complete transaction history for an account, sorted newest first.
     */
    public List<Transaction> getHistory(String accountId) {
        return dataStore.getTransactions(accountId);
    }

    /**
     * Returns a mini statement — the last N transactions.
     */
    public List<Transaction> getMiniStatement(String accountId) {
        return dataStore.getRecentTransactions(accountId, MINI_STATEMENT_COUNT);
    }
}
