package ui;

import model.Account;
import service.AuthenticationService;
import util.AppLogger;
import util.InputValidator;
import util.SecurityUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that collects the User ID and PIN, authenticates via {@link AuthenticationService},
 * and forwards the authenticated {@link Account} to the {@link DashboardPanel}.
 */
public class LoginPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final JTextField userIdField;
    private final JPasswordField pinField;
    private final JLabel errorLabel;
    private final AuthenticationService authService;

    public LoginPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authService = new AuthenticationService();
        setBackground(UIConstants.BG_MAIN);
        setLayout(new GridBagLayout());

        // Title
        JLabel title = UIConstants.createHeading("Welcome to Secure Bank ATM");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // User ID field
        userIdField = UIConstants.createStyledTextField("User ID");
        userIdField.setColumns(15);

        // PIN field
        pinField = UIConstants.createStyledPasswordField("PIN");
        pinField.setColumns(15);

        // Login button
        JButton loginBtn = UIConstants.createPrimaryButton("Login");
        loginBtn.addActionListener(new LoginActionListener());

        // Error label
        errorLabel = UIConstants.createErrorLabel();

        // Layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        add(userIdField, gbc);

        gbc.gridy++;
        add(pinField, gbc);

        gbc.gridy++;
        add(loginBtn, gbc);

        gbc.gridy++;
        add(errorLabel, gbc);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText().trim().toUpperCase();
            String pin = new String(pinField.getPassword()).trim();

            // Basic validation before calling service
            if (!InputValidator.isValidUserId(userId)) {
                showError("Enter a valid User ID (3-20 alphanumeric characters).");
                return;
            }
            if (!InputValidator.isValidPin(pin)) {
                showError("PIN must be 4‑6 digits.");
                return;
            }

            try {
                Account account = authService.authenticate(userId, pin);
                // Successful login – transition to dashboard
                Container cardPanel = (Container) parentFrame.getContentPane().getComponent(0);
                DashboardPanel dashboard = (DashboardPanel) cardPanel.getComponent(1);
                dashboard.setCurrentAccount(account);
                parentFrame.showCard(ATMFrame.CARD_DASHBOARD);
                clearFields();
                errorLabel.setText(" "); // clear any previous error
                AppLogger.info("User " + userId + " logged in successfully.");
            } catch (Exception ex) {
                // Show the message from custom exceptions or generic fallback
                showError(ex.getMessage());
                AppLogger.warning("Login failed for user " + userId + ": " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private void clearFields() {
        userIdField.setText("");
        pinField.setText("");
    }
}
