package exception;

/**
 * General-purpose exception for transaction processing failures
 * that don't fall under more specific exception categories.
 */
public class TransactionException extends Exception {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
