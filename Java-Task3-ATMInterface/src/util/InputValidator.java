package util;

/**
 * Validates user input across the ATM application.
 * Provides static methods for checking amounts, PINs, and user IDs.
 */
public final class InputValidator {

    public static final int MIN_PIN_LENGTH = 4;
    public static final int MAX_PIN_LENGTH = 6;
    public static final double MIN_WITHDRAWAL = 100.0;
    public static final double MAX_WITHDRAWAL = 25_000.0;
    public static final double DAILY_WITHDRAWAL_LIMIT = 50_000.0;
    public static final double WITHDRAWAL_DENOMINATION = 100.0;
    public static final double MIN_DEPOSIT = 100.0;
    public static final double MAX_DEPOSIT = 5_00_000.0;
    public static final double MIN_TRANSFER = 1.0;
    public static final double MAX_TRANSFER = 2_00_000.0;

    private InputValidator() {
        // Utility class — prevent instantiation
    }

    /**
     * Validates whether the given string is a valid monetary amount.
     *
     * @param input the string to validate
     * @return true if it can be parsed as a positive number
     */
    public static boolean isValidAmount(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }
        try {
            double amount = Double.parseDouble(input.trim());
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the amount is a valid denomination (multiple of 100).
     */
    public static boolean isValidDenomination(double amount) {
        return amount % WITHDRAWAL_DENOMINATION == 0;
    }

    /**
     * Validates a PIN string: must be numeric, 4-6 digits.
     */
    public static boolean isValidPin(String pin) {
        if (pin == null || pin.isBlank()) {
            return false;
        }
        return pin.matches("\\d{" + MIN_PIN_LENGTH + "," + MAX_PIN_LENGTH + "}");
    }

    /**
     * Validates a User ID: must be non-empty and alphanumeric.
     */
    public static boolean isValidUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            return false;
        }
        return userId.matches("[A-Za-z0-9]{3,20}");
    }

    /**
     * Parses a string to a double, returning -1 if parsing fails.
     */
    public static double parseAmount(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }
}
