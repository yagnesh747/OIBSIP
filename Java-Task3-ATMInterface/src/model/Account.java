package model;

import java.time.LocalDate;

/**
 * Represents a bank account in the ATM system.
 * Encapsulates all account-related data including credentials,
 * balance, status, and daily withdrawal tracking.
 */
public class Account {

    private final String accountId;
    private final String userId;
    private String hashedPin;
    private final String holderName;
    private double balance;
    private AccountStatus status;
    private int failedAttempts;
    private double dailyWithdrawn;
    private LocalDate lastWithdrawalDate;

    private static final int MAX_FAILED_ATTEMPTS = 3;

    public Account(String accountId, String userId, String hashedPin,
                   String holderName, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.hashedPin = hashedPin;
        this.holderName = holderName;
        this.balance = balance;
        this.status = AccountStatus.ACTIVE;
        this.failedAttempts = 0;
        this.dailyWithdrawn = 0.0;
        this.lastWithdrawalDate = LocalDate.now();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public void setHashedPin(String hashedPin) {
        this.hashedPin = hashedPin;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
        if (this.failedAttempts >= MAX_FAILED_ATTEMPTS) {
            this.status = AccountStatus.LOCKED;
        }
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public double getDailyWithdrawn() {
        resetDailyLimitIfNewDay();
        return dailyWithdrawn;
    }

    public void addToDailyWithdrawn(double amount) {
        resetDailyLimitIfNewDay();
        this.dailyWithdrawn += amount;
    }

    /**
     * Resets the daily withdrawal counter if the current date
     * is different from the last withdrawal date.
     */
    private void resetDailyLimitIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastWithdrawalDate)) {
            dailyWithdrawn = 0.0;
            lastWithdrawalDate = today;
        }
    }

    public boolean isLocked() {
        return status == AccountStatus.LOCKED;
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return String.format("Account[id=%s, holder=%s, balance=%.2f, status=%s]",
                accountId, holderName, balance, status);
    }
}
