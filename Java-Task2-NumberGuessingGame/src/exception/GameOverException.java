package exception;

/**
 * Thrown when a user attempts to guess after a round has already concluded.
 */
public class GameOverException extends Exception {

    public GameOverException(String message) {
        super(message);
    }
}
