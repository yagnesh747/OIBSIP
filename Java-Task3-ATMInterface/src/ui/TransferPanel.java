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
 * UI panel for transferring funds from the active account to another account ID.
 */
public class TransferPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final AccountService accountService;
    private final ReceiptService receiptService;
    private Account currentAccount;

    private final JTextField recipientField;
    private final JTextField amountField;
    private final JLabel balanceLabel;
    private final JLabel errorLabel;

    public TransferPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.accountService = new AccountService();
        this.receiptService = new ReceiptService();

        setBackground(UIConstants.BG_MAIN);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new GridLayout(2, 1, 0, 5));
        JLabel heading = UIConstants.createHeading("Fund Transfer");
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
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel recLabel = UIConstants.createLabel("Recipient Account ID:");
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(recLabel, gbc);

        recipientField = UIConstants.createStyledTextField("e.g. ACC002");
        gbc.gridy = 1;
        centerPanel.add(recipientField, gbc);

        JLabel amtLabel = UIConstants.createLabel("Transfer Amount:");
        gbc.gridy = 2;
        centerPanel.add(amtLabel, gbc);

        amountField = UIConstants.createStyledTextField("Enter Amount");
        gbc.gridy = 3;
        centerPanel.add(amountField, gbc);

        errorLabel = UIConstants.createErrorLabel();
        gbc.gridy = 4;
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

        JButton confirmBtn = UIConstants.createSuccessButton("Confirm Transfer");
        confirmBtn.addActionListener(e -> processTransfer());

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

    private void processTransfer() {
        errorLabel.setText(" ");
        String recipient = recipientField.getText().trim();
        String text = amountField.getText().trim();

        if (recipient.isEmpty()) {
            errorLabel.setText("Please enter a recipient account ID.");
            return;
        }

        if (!InputValidator.isValidAmount(text)) {
            errorLabel.setText("Please enter a valid positive transfer amount.");
            return;
        }

        double amount = InputValidator.parseAmount(text);

        try {
            Transaction txn = accountService.transfer(currentAccount, recipient, amount);

            int receiptOption = JOptionPane.showConfirmDialog(
                    this,
                    "Transfer of " + CurrencyFormatter.format(amount) + " to " + recipient + " successful!\n" +
                            "New Balance: " + CurrencyFormatter.format(currentAccount.getBalance()) + "\n\n" +
                            "Would you like to print a receipt?",
                    "Transfer Success",
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
        recipientField.setText("");
        amountField.setText("");
        errorLabel.setText(" ");
    }
}
