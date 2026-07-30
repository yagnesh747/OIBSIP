package exception;

/**
 * Thrown when a user guess is outside the allowed range or invalid.
 */
public class InvalidGuessException extends Exception {

    public InvalidGuessException(String message) {
        super(message);
    }
}
