package model;

/**
 * Represents the status of a bank account.
 * Controls whether transactions can be processed on the account.
 */
public enum AccountStatus {

    ACTIVE("Active"),
    LOCKED("Locked"),
    CLOSED("Closed");

    private final String displayName;

    AccountStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
