package ui;

import model.Account;
import model.Transaction;
import service.AccountService;
import service.ReceiptService;
import util.CurrencyFormatter;
import util.InputValidator;

import javax.swing.*;
import java.awt.*;

/**
 * UI panel for depositing funds into an account.
 */
public class DepositPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final AccountService accountService;
    private final ReceiptService receiptService;
    private Account currentAccount;

    private final JTextField amountField;
    private final JLabel balanceLabel;
    private final JLabel errorLabel;

    public DepositPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.accountService = new AccountService();
        this.receiptService = new ReceiptService();

        setBackground(UIConstants.BG_PRIMARY);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new GridLayout(2, 1, 0, 5));
        JLabel heading = UIConstants.createHeading("Cash Deposit");
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        balanceLabel = UIConstants.createLabel("Available Balance: ₹0.00");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerCard.add(heading);
        headerCard.add(balanceLabel);
        add(headerCard, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = UIConstants.createLabel("Enter Deposit Amount:");
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(inputLabel, gbc);

        amountField = UIConstants.createStyledTextField("Minimum ₹100");
        gbc.gridy = 1;
        centerPanel.add(amountField, gbc);

        errorLabel = UIConstants.createErrorLabel();
        gbc.gridy = 2;
        centerPanel.add(errorLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setOpaque(false);

        JButton backBtn = UIConstants.createSecondaryButton("← Back");
        backBtn.addActionListener(e -> {
            clearFields();
            parentFrame.showCard(ATMFrame.CARD_DASHBOARD);
        });

        JButton confirmBtn = UIConstants.createSuccessButton("Confirm Deposit");
        confirmBtn.addActionListener(e -> processDeposit());

        bottomPanel.add(backBtn);
        bottomPanel.add(confirmBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setAccount(Account account) {
        this.currentAccount = account;
        if (account != null) {
            balanceLabel.setText("Available Balance: " + CurrencyFormatter.format(account.getBalance()));
        }
        clearFields();
    }

    private void processDeposit() {
        errorLabel.setText(" ");
        String text = amountField.getText().trim();

        if (!InputValidator.isValidAmount(text)) {
            errorLabel.setText("Please enter a valid positive deposit amount.");
            return;
        }

        double amount = InputValidator.parseAmount(text);

        try {
            Transaction txn = accountService.deposit(currentAccount, amount);

            int receiptOption = JOptionPane.showConfirmDialog(
                    this,
                    "Deposit of " + CurrencyFormatter.format(amount) + " successful!\n" +
                            "New Balance: " + CurrencyFormatter.format(currentAccount.getBalance()) + "\n\n" +
                            "Would you like to print a receipt?",
                    "Deposit Success",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (receiptOption == JOptionPane.YES_OPTION) {
                String receiptPath = receiptService.generateReceipt(txn, currentAccount.getHolderName());
                JOptionPane.showMessageDialog(this,
                        "Receipt saved to:\n" + receiptPath,
                        "Receipt Generated",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            clearFields();
            parentFrame.showCard(ATMFrame.CARD_DASHBOARD);

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private void clearFields() {
        amountField.setText("");
        errorLabel.setText(" ");
    }
}
