package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides cryptographic hashing utilities for PIN security.
 * Uses SHA-256 to store PINs as irreversible hashes.
 */
public final class SecurityUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    private SecurityUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Hashes a PIN string using SHA-256 and returns the hex-encoded digest.
     *
     * @param pin the raw PIN to hash
     * @return the hex string representation of the SHA-256 hash
     */
    public static String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in every JVM
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies a raw PIN against a previously stored hash.
     *
     * @param rawPin     the PIN entered by the user
     * @param storedHash the hash stored in the system
     * @return true if the PIN matches the hash
     */
    public static boolean verifyPin(String rawPin, String storedHash) {
        String inputHash = hashPin(rawPin);
        return inputHash.equals(storedHash);
    }

    /**
     * Converts a byte array to its hexadecimal string representation.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
