package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Modern dark-theme design system for the Swing version of the Number Guessing Game.
 */
public final class UIConstants {

    // Dark Palette
    public static final Color BG_DARK = new Color(15, 23, 42);
    public static final Color BG_CARD = new Color(30, 41, 59);
    public static final Color BG_INPUT = new Color(9, 13, 22);
    public static final Color ACCENT_BLUE = new Color(59, 130, 246);
    public static final Color ACCENT_GREEN = new Color(16, 185, 129);
    public static final Color ACCENT_RED = new Color(239, 68, 68);
    public static final Color ACCENT_YELLOW = new Color(245, 158, 11);
    public static final Color TEXT_MAIN = new Color(248, 250, 252);
    public static final Color TEXT_SUB = new Color(148, 163, 184);
    public static final Color BORDER_COLOR = new Color(51, 65, 85);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BIG_NUM = new Font("Segoe UI", Font.BOLD, 36);

    private UIConstants() {}

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
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
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }
}
