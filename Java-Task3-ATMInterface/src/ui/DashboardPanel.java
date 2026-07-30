package ui;

import model.Account;
import service.AccountService;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panel displayed after a user successfully authenticates.
 * Provides a grid menu to select various ATM features: Balance, Withdraw,
 * Deposit, Transfer, Transaction History, Change PIN, and Logout.
 */
public class DashboardPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final JLabel welcomeLabel;
    private final JLabel accountLabel;
    private Account currentAccount;

    public DashboardPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        setBackground(UIConstants.BG_MAIN);
        setLayout(new BorderLayout(0, 20));

        // Header Panel
        JPanel headerPanel = UIConstants.createCard();
        headerPanel.setLayout(new GridLayout(2, 1, 0, 5));

        welcomeLabel = UIConstants.createHeading("Welcome, User!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        accountLabel = UIConstants.createLabel("Account ID: ---");
        accountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(welcomeLabel);
        headerPanel.add(accountLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Options Grid Panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton balanceBtn = UIConstants.createMenuButton("Balance", "💳");
        JButton withdrawBtn = UIConstants.createMenuButton("Withdraw", "🏧");
        JButton depositBtn = UIConstants.createMenuButton("Deposit", "💵");
        JButton transferBtn = UIConstants.createMenuButton("Transfer", "💸");
        JButton historyBtn = UIConstants.createMenuButton("History", "📜");
        JButton pinChangeBtn = UIConstants.createMenuButton("Change PIN", "🔑");

        balanceBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_BALANCE));
        withdrawBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_WITHDRAW));
        depositBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_DEPOSIT));
        transferBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_TRANSFER));
        historyBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_HISTORY));
        pinChangeBtn.addActionListener(e -> navigateTo(ATMFrame.CARD_CHANGE_PIN));

        menuPanel.add(balanceBtn);
        menuPanel.add(withdrawBtn);
        menuPanel.add(depositBtn);
        menuPanel.add(transferBtn);
        menuPanel.add(historyBtn);
        menuPanel.add(pinChangeBtn);

        add(menuPanel, BorderLayout.CENTER);

        // Footer Panel (Logout Button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton logoutBtn = UIConstants.createDangerButton("Logout & Exit Session");
        logoutBtn.setPreferredSize(new Dimension(280, UIConstants.BUTTON_HEIGHT));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to end your session?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                currentAccount = null;
                parentFrame.showCard(ATMFrame.CARD_LOGIN);
            }
        });

        footerPanel.add(logoutBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        if (account != null) {
            welcomeLabel.setText("Welcome, " + account.getHolderName() + "!");
            accountLabel.setText("Account ID: " + account.getAccountId() + " | Status: " + account.getStatus());
        }
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    private void navigateTo(String cardName) {
        if (currentAccount == null) {
            parentFrame.showCard(ATMFrame.CARD_LOGIN);
            return;
        }

        // Pass context to target panel if needed before displaying
        Component targetComp = getPanelByName(cardName);
        if (targetComp instanceof BalancePanel) {
            ((BalancePanel) targetComp).updateAccountData(currentAccount);
        } else if (targetComp instanceof WithdrawPanel) {
            ((WithdrawPanel) targetComp).setAccount(currentAccount);
        } else if (targetComp instanceof DepositPanel) {
            ((DepositPanel) targetComp).setAccount(currentAccount);
        } else if (targetComp instanceof TransferPanel) {
            ((TransferPanel) targetComp).setAccount(currentAccount);
        } else if (targetComp instanceof HistoryPanel) {
            ((HistoryPanel) targetComp).loadHistory(currentAccount);
        } else if (targetComp instanceof ChangePinPanel) {
            ((ChangePinPanel) targetComp).setAccount(currentAccount);
        }

        parentFrame.showCard(cardName);
    }

    private Component getPanelByName(String cardName) {
        Container container = (Container) parentFrame.getContentPane().getComponent(0);
        for (Component c : container.getComponents()) {
            if (cardName.equals(ATMFrame.CARD_BALANCE) && c instanceof BalancePanel) return c;
            if (cardName.equals(ATMFrame.CARD_WITHDRAW) && c instanceof WithdrawPanel) return c;
            if (cardName.equals(ATMFrame.CARD_DEPOSIT) && c instanceof DepositPanel) return c;
            if (cardName.equals(ATMFrame.CARD_TRANSFER) && c instanceof TransferPanel) return c;
            if (cardName.equals(ATMFrame.CARD_HISTORY) && c instanceof HistoryPanel) return c;
            if (cardName.equals(ATMFrame.CARD_CHANGE_PIN) && c instanceof ChangePinPanel) return c;
        }
        return null;
    }
}
