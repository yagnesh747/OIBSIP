package model;

/**
 * Defines all supported transaction types in the ATM system.
 * Each type carries a display label and a flag indicating
 * whether it represents a credit (money in) or debit (money out) operation.
 */
public enum TransactionType {

    WITHDRAWAL("Withdrawal", false),
    DEPOSIT("Deposit", true),
    TRANSFER_IN("Transfer (Credit)", true),
    TRANSFER_OUT("Transfer (Debit)", false),
    BALANCE_INQUIRY("Balance Inquiry", false),
    PIN_CHANGE("PIN Change", false);

    private final String displayName;
    private final boolean credit;

    TransactionType(String displayName, boolean credit) {
        this.displayName = displayName;
        this.credit = credit;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isCredit() {
        return credit;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
