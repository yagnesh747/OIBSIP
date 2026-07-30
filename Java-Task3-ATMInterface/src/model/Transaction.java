package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable representation of a single ATM transaction.
 * Records every financial operation for audit and history purposes.
 */
public final class Transaction {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String transactionId;
    private final String accountId;
    private final TransactionType type;
    private final double amount;
    private final double balanceBefore;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(String transactionId, String accountId, TransactionType type,
                       double amount, double balanceBefore, double balanceAfter,
                       String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    /**
     * Constructor with explicit timestamp — used when loading from persistent storage.
     */
    public Transaction(String transactionId, String accountId, TransactionType type,
                       double amount, double balanceBefore, double balanceAfter,
                       LocalDateTime timestamp, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DISPLAY_FORMAT);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Serializes this transaction to CSV format for persistent storage.
     */
    public String toCsvRow() {
        return String.join(",",
                transactionId,
                accountId,
                type.name(),
                String.valueOf(amount),
                String.valueOf(balanceBefore),
                String.valueOf(balanceAfter),
                timestamp.toString(),
                description.replace(",", ";")
        );
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | ₹%.2f | Balance: ₹%.2f → ₹%.2f",
                getFormattedTimestamp(), transactionId, type.getDisplayName(),
                amount, balanceBefore, balanceAfter);
    }
}
