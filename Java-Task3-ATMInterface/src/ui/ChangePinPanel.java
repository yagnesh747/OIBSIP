package ui;

import model.Account;
import service.AccountService;
import util.InputValidator;

import javax.swing.*;
import java.awt.*;

/**
 * UI panel allowing the user to update their ATM security PIN.
 * Requires verification of current PIN and matching input for the new PIN.
 */
public class ChangePinPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final AccountService accountService;
    private Account currentAccount;

    private final JPasswordField oldPinField;
    private final JPasswordField newPinField;
    private final JPasswordField confirmPinField;
    private final JLabel errorLabel;

    public ChangePinPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.accountService = new AccountService();

        setBackground(UIConstants.BG_MAIN);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header Card
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new BorderLayout());
        JLabel heading = UIConstants.createHeading("Change Account PIN");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        headerCard.add(heading, BorderLayout.CENTER);
        add(headerCard, BorderLayout.NORTH);

        // Form Grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel oldLabel = UIConstants.createLabel("Current PIN:");
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(oldLabel, gbc);

        oldPinField = UIConstants.createStyledPasswordField("Enter Current PIN");
        gbc.gridy = 1;
        formPanel.add(oldPinField, gbc);

        JLabel newLabel = UIConstants.createLabel("New PIN (4-6 digits):");
        gbc.gridy = 2;
        formPanel.add(newLabel, gbc);

        newPinField = UIConstants.createStyledPasswordField("Enter New PIN");
        gbc.gridy = 3;
        formPanel.add(newPinField, gbc);

        JLabel confirmLabel = UIConstants.createLabel("Confirm New PIN:");
        gbc.gridy = 4;
        formPanel.add(confirmLabel, gbc);

        confirmPinField = UIConstants.createStyledPasswordField("Re-enter New PIN");
        gbc.gridy = 5;
        formPanel.add(confirmPinField, gbc);

        errorLabel = UIConstants.createErrorLabel();
        gbc.gridy = 6;
        formPanel.add(errorLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setOpaque(false);

        JButton backBtn = UIConstants.createSecondaryButton("← Back");
        backBtn.addActionListener(e -> {
            clearFields();
            parentFrame.showCard(ATMFrame.CARD_DASHBOARD);
        });

        JButton confirmBtn = UIConstants.createSuccessButton("Update PIN");
        confirmBtn.addActionListener(e -> processPinChange());

        bottomPanel.add(backBtn);
        bottomPanel.add(confirmBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setAccount(Account account) {
        this.currentAccount = account;
        clearFields();
    }

    private void processPinChange() {
        errorLabel.setText(" ");
        String oldPin = new String(oldPinField.getPassword()).trim();
        String newPin = new String(newPinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();

        if (oldPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
            errorLabel.setText("All PIN fields are required.");
            return;
        }

        if (!InputValidator.isValidPin(newPin)) {
            errorLabel.setText("New PIN must be numeric (4-6 digits).");
            return;
        }

        if (!newPin.equals(confirmPin)) {
            errorLabel.setText("New PIN and confirmation do not match.");
            return;
        }

        try {
            accountService.changePin(currentAccount, oldPin, newPin);

            JOptionPane.showMessageDialog(
                    this,
                    "Your PIN has been successfully changed!",
                    "PIN Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            parentFrame.showCard(ATMFrame.CARD_DASHBOARD);

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private void clearFields() {
        oldPinField.setText("");
        newPinField.setText("");
        confirmPinField.setText("");
        errorLabel.setText(" ");
    }
}
