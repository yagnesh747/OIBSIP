package exception;

/**
 * Thrown when a withdrawal or transfer exceeds the available account balance.
 */
public class InsufficientFundsException extends Exception {

    private final double currentBalance;
    private final double requestedAmount;

    public InsufficientFundsException(double currentBalance, double requestedAmount) {
        super(String.format("Insufficient funds. Current balance: ₹%.2f, Requested: ₹%.2f",
                currentBalance, requestedAmount));
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }
}
