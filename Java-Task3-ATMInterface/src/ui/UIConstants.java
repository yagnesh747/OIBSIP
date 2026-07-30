package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handcrafted design system for the Enterprise ATM Interface.
 * Implements a clean, realistic banking theme (Slate Blue & Warm Amber)
 * with soft cards, crisp typography, and subtle micro-interactions.
 */
public final class UIConstants {

    // ─── Professional Banking Color Palette ────────────────────────
    public static final Color BG_MAIN = new Color(0xF8, 0xFA, 0xFC);       // Soft Slate White
    public static final Color BG_CARD = new Color(0xFF, 0xFF, 0xFF);       // Pure Crisp White
    public static final Color BG_INPUT = new Color(0xF1, 0xF5, 0xF9);      // Muted Light Input
    public static final Color BG_HEADER = new Color(0x0F, 0x17, 0x2A);     // Deep Slate Header

    public static final Color PRIMARY_DARK = new Color(0x1E, 0x29, 0x3B);   // Navy Slate
    public static final Color PRIMARY_SLATE = new Color(0x33, 0x41, 0x55);  // Slate Blue

    public static final Color ACCENT_AMBER = new Color(0xD9, 0x77, 0x06);  // Warm Amber
    public static final Color ACCENT_GREEN = new Color(0x16, 0xA3, 0x4A);  // Forest Success
    public static final Color ACCENT_RED = new Color(0xDC, 0x26, 0x26);    // Banking Danger Red

    public static final Color TEXT_PRIMARY = new Color(0x0F, 0x17, 0x2A); // Dark Slate Text
    public static final Color TEXT_SECONDARY = new Color(0x64, 0x74, 0x8B);// Muted Slate Text
    public static final Color TEXT_MUTED = new Color(0x94, 0xA3, 0xB8);    // Subdued Gray

    public static final Color BORDER_COLOR = new Color(0xE2, 0xE8, 0xF0); // Soft Border Gray
    public static final Color BORDER_FOCUS = new Color(0x02, 0x84, 0xC7); // Focus Blue

    // ─── Typography (Segoe UI / Sans-Serif) ────────────────────────
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BALANCE = new Font("Segoe UI", Font.BOLD, 32);

    // ─── Dimensions & Geometry ─────────────────────────────────────
    public static final int WINDOW_WIDTH = 540;
    public static final int WINDOW_HEIGHT = 720;
    public static final int FIELD_HEIGHT = 44;
    public static final int BUTTON_HEIGHT = 46;
    public static final int CORNER_RADIUS = 12;

    private UIConstants() {
        // Utility class
    }

    // ─── Component Factories ───────────────────────────────────────

    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        styleInputField(field, placeholder);
        return field;
    }

    public static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        styleInputField(field, placeholder);
        field.setEchoChar('•');
        return field;
    }

    public static JButton createPrimaryButton(String text) {
        return createCustomButton(text, PRIMARY_DARK, new Color(0x0F, 0x17, 0x2A), Color.WHITE);
    }

    public static JButton createSuccessButton(String text) {
        return createCustomButton(text, ACCENT_GREEN, new Color(0x15, 0x80, 0x3D), Color.WHITE);
    }

    public static JButton createDangerButton(String text) {
        return createCustomButton(text, ACCENT_RED, new Color(0xB9, 0x1C, 0x1C), Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xF1, 0xF5, 0xF9) : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BUTTON);
        button.setForeground(PRIMARY_DARK);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        return button;
    }

    public static JButton createMenuButton(String text, String iconSymbol) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean hover = getModel().isRollover();
                g2.setColor(hover ? new Color(0xF1, 0xF5, 0xF9) : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                g2.setColor(hover ? ACCENT_AMBER : BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();

                // Draw Icon Symbol
                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                g2.setColor(hover ? ACCENT_AMBER : PRIMARY_SLATE);
                FontMetrics iconMetrics = g2.getFontMetrics();
                int iconX = (getWidth() - iconMetrics.stringWidth(iconSymbol)) / 2;
                g2.drawString(iconSymbol, iconX, 38);

                // Draw Text Label
                g2.setFont(FONT_BODY_BOLD);
                g2.setColor(TEXT_PRIMARY);
                FontMetrics textMetrics = g2.getFontMetrics();
                int textX = (getWidth() - textMetrics.stringWidth(text)) / 2;
                g2.drawString(text, textX, 64);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(140, 84));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    public static JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Soft background card
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Soft border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        return card;
    }

    public static JLabel createErrorLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(FONT_SMALL);
        label.setForeground(ACCENT_RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static void styleInputField(JTextField field, String placeholder) {
        field.setFont(FONT_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_INPUT);
        field.setCaretColor(TEXT_PRIMARY);
        field.setOpaque(true);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        field.setPreferredSize(new Dimension(0, FIELD_HEIGHT));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_FOCUS, 2, true),
                        new EmptyBorder(7, 13, 7, 13)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
        });
    }

    private static JButton createCustomButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hoverBg : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(fg);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        return button;
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(BG_MAIN);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        separator.setBackground(BG_MAIN);
        return separator;
    }
}
