package exception;

/**
 * Thrown when a transaction amount is invalid (negative, zero, or not a valid denomination).
 */
public class InvalidAmountException extends Exception {

    public InvalidAmountException(String message) {
        super(message);
    }
}
