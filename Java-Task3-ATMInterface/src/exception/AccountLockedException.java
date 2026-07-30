package exception;

/**
 * Thrown when an operation is attempted on a locked account.
 */
public class AccountLockedException extends Exception {

    public AccountLockedException(String userId) {
        super("Account for user '" + userId + "' is locked due to multiple failed login attempts. "
                + "Please contact your bank branch for assistance.");
    }
}
