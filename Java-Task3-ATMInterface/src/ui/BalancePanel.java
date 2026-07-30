package ui;

import model.Account;
import model.Transaction;
import service.AccountService;
import service.TransactionService;
import util.CurrencyFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * UI panel for balance inquiry. Displays large balance text alongside
 * a mini-statement (last 5 transactions).
 */
public class BalancePanel extends JPanel {

    private final ATMFrame parentFrame;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private Account currentAccount;

    private final JLabel balanceDisplayLabel;
    private final JLabel holderLabel;
    private final JPanel miniStatementPanel;

    public BalancePanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();

        setBackground(UIConstants.BG_MAIN);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Balance Card Header
        JPanel cardHeader = UIConstants.createCard();
        cardHeader.setLayout(new GridLayout(3, 1, 0, 8));

        holderLabel = UIConstants.createHeading("Account Balance");
        holderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        balanceDisplayLabel = new JLabel("₹0.00", SwingConstants.CENTER);
        balanceDisplayLabel.setFont(UIConstants.FONT_BALANCE);
        balanceDisplayLabel.setForeground(UIConstants.ACCENT_GREEN);

        JLabel subtext = UIConstants.createLabel("Available Funds");
        subtext.setHorizontalAlignment(SwingConstants.CENTER);

        cardHeader.add(holderLabel);
        cardHeader.add(balanceDisplayLabel);
        cardHeader.add(subtext);

        add(cardHeader, BorderLayout.NORTH);

        // Mini Statement Center Panel
        JPanel centerCard = UIConstants.createCard();
        centerCard.setLayout(new BorderLayout(0, 10));

        JLabel statementHeading = UIConstants.createHeading("Recent Transactions (Mini Statement)");
        centerCard.add(statementHeading, BorderLayout.NORTH);

        miniStatementPanel = new JPanel();
        miniStatementPanel.setLayout(new BoxLayout(miniStatementPanel, BoxLayout.Y_AXIS));
        miniStatementPanel.setOpaque(false);

        centerCard.add(miniStatementPanel, BorderLayout.CENTER);
        add(centerCard, BorderLayout.CENTER);

        // Bottom Action Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        JButton backBtn = UIConstants.createSecondaryButton("← Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(220, UIConstants.BUTTON_HEIGHT));
        backBtn.addActionListener(e -> parentFrame.showCard(ATMFrame.CARD_DASHBOARD));

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateAccountData(Account account) {
        this.currentAccount = account;
        if (account != null) {
            holderLabel.setText(account.getHolderName() + " (" + account.getAccountId() + ")");
            balanceDisplayLabel.setText(CurrencyFormatter.format(account.getBalance()));
            accountService.recordBalanceInquiry(account);
            loadMiniStatement();
        }
    }

    private void loadMiniStatement() {
        miniStatementPanel.removeAll();
        if (currentAccount == null) return;

        List<Transaction> recent = transactionService.getMiniStatement(currentAccount.getAccountId());

        if (recent.isEmpty()) {
            JLabel emptyLabel = UIConstants.createLabel("No recent transactions found.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            miniStatementPanel.add(Box.createVerticalStrut(15));
            miniStatementPanel.add(emptyLabel);
        } else {
            for (Transaction txn : recent) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

                JLabel left = UIConstants.createLabel(txn.getFormattedTimestamp() + "  " + txn.getType().getDisplayName());
                left.setFont(UIConstants.FONT_SMALL);

                String amtText = txn.getAmount() > 0
                        ? CurrencyFormatter.formatSigned(txn.getAmount(), txn.getType().isCredit())
                        : "—";

                JLabel right = new JLabel(amtText);
                right.setFont(UIConstants.FONT_SMALL);
                right.setForeground(txn.getType().isCredit() ? UIConstants.ACCENT_GREEN : UIConstants.TEXT_PRIMARY);

                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);

                miniStatementPanel.add(row);
                miniStatementPanel.add(UIConstants.createSeparator());
            }
        }

        miniStatementPanel.revalidate();
        miniStatementPanel.repaint();
    }
}
