package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Handcrafted design system for Task 2 — Number Guessing Game.
 * Features a playful yet clean forest green theme (#14532D) with warm stone (#FAFAF9),
 * orange accents (#F97316), and crisp typography.
 */
public final class UIConstants {

    // Palette
    public static final Color BG_DARK = new Color(0xFA, 0xFA, 0xF9);     // Warm Stone White
    public static final Color BG_CARD = new Color(0xFF, 0xFF, 0xFF);     // Pure White Card
    public static final Color BG_INPUT = new Color(0xF5, 0xF5, 0xF4);    // Warm Gray Input
    public static final Color PRIMARY_GREEN = new Color(0x14, 0x53, 0x2D);// Forest Green
    public static final Color SECONDARY_GREEN = new Color(0x16, 0x65, 0x34);// Slate Green
    public static final Color ACCENT_ORANGE = new Color(0xF9, 0x73, 0x16); // Vibrant Orange Accent
    public static final Color ACCENT_GREEN = new Color(0x15, 0x80, 0x3D);  // Emerald Success
    public static final Color ACCENT_RED = new Color(0xDC, 0x26, 0x26);    // Coral Danger
    public static final Color ACCENT_YELLOW = new Color(0xD9, 0x77, 0x06); // Warm Amber
    public static final Color TEXT_MAIN = new Color(0x1F, 0x29, 0x37);    // Dark Charcoal Text
    public static final Color TEXT_SUB = new Color(0x6B, 0x72, 0x80);     // Muted Subtitle Gray
    public static final Color BORDER_COLOR = new Color(0xE7, 0xE5, 0xE4); // Soft Border

    // Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BIG_NUM = new Font("Segoe UI", Font.BOLD, 32);

    private UIConstants() {}

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_HEADING);
        field.setForeground(TEXT_MAIN);
        field.setBackground(BG_INPUT);
        field.setCaretColor(TEXT_MAIN);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }
}
