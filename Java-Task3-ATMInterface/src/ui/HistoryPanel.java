package ui;

import model.Account;
import model.Transaction;
import service.TransactionService;
import util.CurrencyFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * UI panel displaying the complete transaction history in a dark-themed JTable
 * with color-coded credits (green) and debits (red).
 */
public class HistoryPanel extends JPanel {

    private final ATMFrame parentFrame;
    private final TransactionService transactionService;
    private Account currentAccount;

    private final JTable historyTable;
    private final DefaultTableModel tableModel;
    private final JLabel headerLabel;

    public HistoryPanel(ATMFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.transactionService = new TransactionService();

        setBackground(UIConstants.BG_PRIMARY);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Card
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new BorderLayout());
        headerLabel = UIConstants.createHeading("Transaction History");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerCard.add(headerLabel, BorderLayout.CENTER);
        add(headerCard, BorderLayout.NORTH);

        // Table Model Setup
        String[] columnNames = {"Date & Time", "Type", "Amount", "Balance After", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setBackground(UIConstants.BG_SECONDARY);
        historyTable.setForeground(UIConstants.TEXT_PRIMARY);
        historyTable.setGridColor(UIConstants.BORDER_COLOR);
        historyTable.setFont(UIConstants.FONT_BODY);
        historyTable.setRowHeight(32);
        historyTable.getTableHeader().setFont(UIConstants.FONT_BODY_BOLD);
        historyTable.getTableHeader().setBackground(UIConstants.BG_CARD);
        historyTable.getTableHeader().setForeground(UIConstants.TEXT_PRIMARY);

        // Custom Cell Renderer for Color-Coded Rows/Amounts
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? UIConstants.BG_SECONDARY : UIConstants.BG_CARD);
                c.setForeground(UIConstants.TEXT_PRIMARY);

                if (column == 2) { // Amount Column
                    String valStr = value != null ? value.toString() : "";
                    if (valStr.startsWith("+")) {
                        c.setForeground(UIConstants.ACCENT_SUCCESS);
                    } else if (valStr.startsWith("-")) {
                        c.setForeground(UIConstants.ACCENT_DANGER);
                    }
                }

                if (isSelected) {
                    c.setBackground(UIConstants.BG_HOVER);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        UIConstants.styleScrollPane(scrollPane);

        add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        JButton backBtn = UIConstants.createSecondaryButton("← Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(220, UIConstants.BUTTON_HEIGHT));
        backBtn.addActionListener(e -> parentFrame.showCard(ATMFrame.CARD_DASHBOARD));

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadHistory(Account account) {
        this.currentAccount = account;
        tableModel.setRowCount(0);

        if (account != null) {
            headerLabel.setText("Transaction History — " + account.getHolderName());
            List<Transaction> transactions = transactionService.getHistory(account.getAccountId());

            for (Transaction txn : transactions) {
                String amountStr = txn.getAmount() > 0
                        ? CurrencyFormatter.formatSigned(txn.getAmount(), txn.getType().isCredit())
                        : "—";

                tableModel.addRow(new Object[]{
                        txn.getFormattedTimestamp(),
                        txn.getType().getDisplayName(),
                        amountStr,
                        CurrencyFormatter.format(txn.getBalanceAfter()),
                        txn.getDescription()
                });
            }
        }
    }
}
