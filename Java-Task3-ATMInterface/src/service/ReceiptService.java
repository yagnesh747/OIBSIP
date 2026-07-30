package service;

import model.Transaction;
import util.AppLogger;
import util.CurrencyFormatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generates formatted transaction receipts as text files.
 * Each receipt is saved to the {@code receipts/} directory.
 */
public class ReceiptService {

    private static final String RECEIPT_DIRECTORY = "receipts";
    private static final DateTimeFormatter FILE_NAME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String SEPARATOR = "═".repeat(44);
    private static final String THIN_SEPARATOR = "─".repeat(44);

    public ReceiptService() {
        ensureReceiptDirectoryExists();
    }

    /**
     * Generates a receipt file for the given transaction.
     *
     * @param transaction  the transaction to generate a receipt for
     * @param holderName   the account holder's name
     * @return the path to the generated receipt file
     */
    public String generateReceipt(Transaction transaction, String holderName) {
        String fileName = "receipt_" + transaction.getTransactionId()
                + "_" + LocalDateTime.now().format(FILE_NAME_FORMAT) + ".txt";
        Path filePath = Paths.get(RECEIPT_DIRECTORY, fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(buildReceiptContent(transaction, holderName));
            AppLogger.info("Receipt generated: " + filePath);
            return filePath.toString();
        } catch (IOException e) {
            AppLogger.error("Failed to generate receipt", e);
            return null;
        }
    }

    private String buildReceiptContent(Transaction transaction, String holderName) {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n");
        receipt.append(SEPARATOR).append("\n");
        receipt.append("          SECURE BANK ATM RECEIPT\n");
        receipt.append(SEPARATOR).append("\n\n");

        receipt.append(String.format("  Date       : %s%n", transaction.getFormattedTimestamp()));
        receipt.append(String.format("  Txn ID     : %s%n", transaction.getTransactionId()));
        receipt.append(String.format("  Account    : %s%n", transaction.getAccountId()));
        receipt.append(String.format("  Name       : %s%n", holderName));

        receipt.append("\n").append(THIN_SEPARATOR).append("\n\n");

        receipt.append(String.format("  Type       : %s%n", transaction.getType().getDisplayName()));

        if (transaction.getAmount() > 0) {
            receipt.append(String.format("  Amount     : %s%n",
                    CurrencyFormatter.format(transaction.getAmount())));
        }

        receipt.append(String.format("  Prev Bal   : %s%n",
                CurrencyFormatter.format(transaction.getBalanceBefore())));
        receipt.append(String.format("  New Bal    : %s%n",
                CurrencyFormatter.format(transaction.getBalanceAfter())));

        receipt.append("\n").append(THIN_SEPARATOR).append("\n\n");
        receipt.append("  " + transaction.getDescription()).append("\n\n");
        receipt.append(SEPARATOR).append("\n");
        receipt.append("     Thank you for banking with us!\n");
        receipt.append("      For support: 1800-XXX-XXXX\n");
        receipt.append(SEPARATOR).append("\n");

        return receipt.toString();
    }

    private void ensureReceiptDirectoryExists() {
        try {
            Path dir = Paths.get(RECEIPT_DIRECTORY);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            AppLogger.error("Failed to create receipts directory", e);
        }
    }
}
