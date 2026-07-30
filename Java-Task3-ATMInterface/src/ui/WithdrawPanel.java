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
 * UI panel for withdrawing funds from an account.
 * Offers custom input and quick-selection preset buttons (₹500, ₹1000, ₹2000, ₹5000).
 */
public class WithdrawPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final AccountService accountService;
    private final ReceiptService receiptService;
    private Account currentAccount;

    private final JTextField amountField;
    private final JLabel balanceLabel;
    private final JLabel errorLabel;

    public WithdrawPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.accountService = new AccountService();
        this.receiptService = new ReceiptService();

        setBackground(UIConstants.BG_MAIN);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new GridLayout(2, 1, 0, 5));
        JLabel heading = UIConstants.createHeading("Cash Withdrawal");
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        balanceLabel = UIConstants.createLabel("Available Balance: ₹0.00");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerCard.add(heading);
        headerCard.add(balanceLabel);
        add(headerCard, BorderLayout.NORTH);

        // Center Content (Presets + Input)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount Input Field
        amountField = UIConstants.createStyledTextField("Enter Amount (Multiple of ₹100)");

        // Presets Label
        JLabel presetLabel = UIConstants.createLabel("Select Quick Amount or Enter Below:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(presetLabel, gbc);

        // Preset buttons grid
        JPanel presetsGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        presetsGrid.setOpaque(false);

        int[] amounts = {500, 1000, 2000, 5000};
        for (int amt : amounts) {
            JButton btn = UIConstants.createSecondaryButton("₹" + amt);
            btn.addActionListener(e -> amountField.setText(String.valueOf(amt)));
            presetsGrid.add(btn);
        }

        gbc.gridy = 1;
        centerPanel.add(presetsGrid, gbc);

        gbc.gridy = 2;
        centerPanel.add(amountField, gbc);

        // Error message label
        errorLabel = UIConstants.createErrorLabel();
        gbc.gridy = 3;
        centerPanel.add(errorLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Action Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setOpaque(false);

        JButton backBtn = UIConstants.createSecondaryButton("← Back");
        backBtn.addActionListener(e -> {
            clearFields();
            parentFrame.showCard(ATMFrame.CARD_DASHBOARD);
        });

        JButton confirmBtn = UIConstants.createSuccessButton("Confirm Withdrawal");
        confirmBtn.addActionListener(e -> processWithdrawal());

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

    private void processWithdrawal() {
        errorLabel.setText(" ");
        String text = amountField.getText().trim();

        if (!InputValidator.isValidAmount(text)) {
            errorLabel.setText("Please enter a valid positive number.");
            return;
        }

        double amount = InputValidator.parseAmount(text);

        try {
            Transaction txn = accountService.withdraw(currentAccount, amount);

            // Ask for printed receipt
            int receiptOption = JOptionPane.showConfirmDialog(
                    this,
                    "Withdrawal of " + CurrencyFormatter.format(amount) + " successful!\n" +
                            "New Balance: " + CurrencyFormatter.format(currentAccount.getBalance()) + "\n\n" +
                            "Would you like to generate a printed text receipt?",
                    "Transaction Success",
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
