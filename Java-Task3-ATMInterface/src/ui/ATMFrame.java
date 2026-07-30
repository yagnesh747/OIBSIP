package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main application window that hosts all UI panels using a CardLayout.
 * It provides navigation between login, dashboard, and operation screens.
 */
public class ATMFrame extends JFrame {

    public static final String CARD_LOGIN = "login";
    public static final String CARD_DASHBOARD = "dashboard";
    public static final String CARD_WITHDRAW = "withdraw";
    public static final String CARD_DEPOSIT = "deposit";
    public static final String CARD_TRANSFER = "transfer";
    public static final String CARD_HISTORY = "history";
    public static final String CARD_CHANGE_PIN = "changePin";
    public static final String CARD_BALANCE = "balance";

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public ATMFrame() {
        super("Secure Bank ATM");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConstants.BG_PRIMARY);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(UIConstants.BG_PRIMARY);

        // Initialise panels
        initPanels();

        add(cardPanel);
        setVisible(true);

        // Confirm exit on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        ATMFrame.this,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    private void initPanels() {
        // Login panel creates a reference back to the frame for navigation
        LoginPanel loginPanel = new LoginPanel(this);
        DashboardPanel dashboardPanel = new DashboardPanel(this);
        WithdrawPanel withdrawPanel = new WithdrawPanel(this);
        DepositPanel depositPanel = new DepositPanel(this);
        TransferPanel transferPanel = new TransferPanel(this);
        HistoryPanel historyPanel = new HistoryPanel(this);
        ChangePinPanel changePinPanel = new ChangePinPanel(this);
        BalancePanel balancePanel = new BalancePanel(this);

        cardPanel.add(loginPanel, CARD_LOGIN);
        cardPanel.add(dashboardPanel, CARD_DASHBOARD);
        cardPanel.add(withdrawPanel, CARD_WITHDRAW);
        cardPanel.add(depositPanel, CARD_DEPOSIT);
        cardPanel.add(transferPanel, CARD_TRANSFER);
        cardPanel.add(historyPanel, CARD_HISTORY);
        cardPanel.add(changePinPanel, CARD_CHANGE_PIN);
        cardPanel.add(balancePanel, CARD_BALANCE);

        // Show login first
        showCard(CARD_LOGIN);
    }

    /**
     * Switches the visible panel.
     */
    public void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }
}
