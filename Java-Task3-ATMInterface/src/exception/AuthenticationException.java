package exception;

/**
 * Thrown when user authentication fails due to invalid credentials.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }
}
