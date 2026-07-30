package util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formats monetary values in Indian Rupee (INR) format.
 * Uses the Indian numbering system (lakhs, crores) with the ₹ symbol.
 */
public final class CurrencyFormatter {

    private static final NumberFormat INR_FORMAT;

    static {
        INR_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));
    }

    private CurrencyFormatter() {
        // Utility class — prevent instantiation
    }

    /**
     * Formats a double value as Indian currency.
     * Example: 150000.0 → "₹1,50,000.00"
     */
    public static String format(double amount) {
        return INR_FORMAT.format(amount);
    }

    /**
     * Formats with a sign prefix for transaction display.
     * Credits show "+₹X", debits show "-₹X".
     */
    public static String formatSigned(double amount, boolean isCredit) {
        String formatted = INR_FORMAT.format(Math.abs(amount));
        return isCredit ? "+" + formatted : "-" + formatted;
    }
}
