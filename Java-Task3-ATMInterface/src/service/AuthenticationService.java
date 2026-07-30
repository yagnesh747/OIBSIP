package service;

import data.DataStore;
import exception.AccountLockedException;
import exception.AuthenticationException;
import model.Account;
import util.AppLogger;
import util.SecurityUtil;

/**
 * Handles user authentication against stored credentials.
 * Implements account lockout after consecutive failed attempts.
 */
public class AuthenticationService {

    private final DataStore dataStore;

    public AuthenticationService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Authenticates a user by verifying their User ID and PIN.
     *
     * @param userId the user's login ID
     * @param pin    the raw PIN entered by the user
     * @return the authenticated {@link Account}
     * @throws AuthenticationException if credentials are invalid
     * @throws AccountLockedException  if the account has been locked
     */
    public Account authenticate(String userId, String pin)
            throws AuthenticationException, AccountLockedException {

        Account account = dataStore.findByUserId(userId.toUpperCase())
                .orElseThrow(() -> {
                    AppLogger.warning("Login attempt with unknown user ID: " + userId);
                    return new AuthenticationException("Invalid User ID or PIN.");
                });

        if (account.isLocked()) {
            AppLogger.warning("Login attempt on locked account: " + userId);
            throw new AccountLockedException(userId);
        }

        if (!SecurityUtil.verifyPin(pin, account.getHashedPin())) {
            account.incrementFailedAttempts();
            dataStore.saveAccounts();

            int remaining = 3 - account.getFailedAttempts();
            AppLogger.warning("Failed login attempt for user: " + userId
                    + " | Remaining attempts: " + Math.max(remaining, 0));

            if (account.isLocked()) {
                throw new AccountLockedException(userId);
            }

            String message = remaining > 0
                    ? "Invalid PIN. " + remaining + " attempt(s) remaining."
                    : "Invalid PIN. Account has been locked.";
            throw new AuthenticationException(message);
        }

        // Successful authentication — reset failed attempts
        account.resetFailedAttempts();
        dataStore.saveAccounts();
        AppLogger.info("User authenticated successfully: " + userId);
        return account;
    }
}
