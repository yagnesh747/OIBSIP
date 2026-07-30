package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Centralized design system for the ATM Interface.
 * Defines all colors, fonts, dimensions, and reusable component factories
 * to ensure visual consistency across the application.
 */
public final class UIConstants {

    // ─── Color Palette (Dark Theme) ────────────────────────────────
    public static final Color BG_PRIMARY = new Color(0x1A, 0x1A, 0x2E);
    public static final Color BG_SECONDARY = new Color(0x16, 0x21, 0x3E);
    public static final Color BG_CARD = new Color(0x1E, 0x29, 0x4A);
    public static final Color BG_INPUT = new Color(0x0A, 0x0E, 0x1F);
    public static final Color BG_HOVER = new Color(0x25, 0x33, 0x5A);

    public static final Color ACCENT_PRIMARY = new Color(0x0F, 0x34, 0x60);
    public static final Color ACCENT_HIGHLIGHT = new Color(0xE9, 0x45, 0x60);
    public static final Color ACCENT_SUCCESS = new Color(0x00, 0xD0, 0x9C);
    public static final Color ACCENT_WARNING = new Color(0xFF, 0xB8, 0x00);
    public static final Color ACCENT_DANGER = new Color(0xE9, 0x45, 0x60);

    public static final Color TEXT_PRIMARY = new Color(0xEA, 0xEA, 0xEA);
    public static final Color TEXT_SECONDARY = new Color(0x88, 0x92, 0xB0);
    public static final Color TEXT_MUTED = new Color(0x5A, 0x63, 0x80);

    public static final Color BORDER_COLOR = new Color(0x2A, 0x2A, 0x4A);
    public static final Color BORDER_FOCUS = new Color(0x3B, 0x82, 0xF6);

    // Gradient colors for buttons
    public static final Color GRADIENT_START = new Color(0x3B, 0x82, 0xF6);
    public static final Color GRADIENT_END = new Color(0x1D, 0x4E, 0xD8);
    public static final Color GRADIENT_SUCCESS_START = new Color(0x10, 0xB9, 0x81);
    public static final Color GRADIENT_SUCCESS_END = new Color(0x05, 0x96, 0x69);
    public static final Color GRADIENT_DANGER_START = new Color(0xEF, 0x44, 0x44);
    public static final Color GRADIENT_DANGER_END = new Color(0xDC, 0x26, 0x26);

    // ─── Typography ────────────────────────────────────────────────
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_BALANCE = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 13);

    // ─── Dimensions ────────────────────────────────────────────────
    public static final int WINDOW_WIDTH = 520;
    public static final int WINDOW_HEIGHT = 700;
    public static final int FIELD_HEIGHT = 44;
    public static final int BUTTON_HEIGHT = 46;
    public static final int CARD_PADDING = 24;
    public static final int SECTION_GAP = 16;
    public static final int CORNER_RADIUS = 12;

    private UIConstants() {
        // Utility class — prevent instantiation
    }

    // ─── Component Factories ───────────────────────────────────────

    /**
     * Creates a styled text field with dark theme appearance.
     */
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleInputField(field);
        field.setToolTipText(placeholder);
        return field;
    }

    /**
     * Creates a styled password field with dark theme appearance.
     */
    public static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleInputField(field);
        field.setToolTipText(placeholder);
        field.setEchoChar('●');
        return field;
    }

    /**
     * Creates a primary action button with gradient background and hover effects.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = createGradientButton(text, GRADIENT_START, GRADIENT_END);
        return button;
    }

    /**
     * Creates a success-styled button (green gradient).
     */
    public static JButton createSuccessButton(String text) {
        return createGradientButton(text, GRADIENT_SUCCESS_START, GRADIENT_SUCCESS_END);
    }

    /**
     * Creates a danger-styled button (red gradient).
     */
    public static JButton createDangerButton(String text) {
        return createGradientButton(text, GRADIENT_DANGER_START, GRADIENT_DANGER_END);
    }

    /**
     * Creates a secondary (outline) button.
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(ACCENT_HIGHLIGHT);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(TEXT_PRIMARY);
                button.repaint();
            }
        });

        return button;
    }

    /**
     * Creates a dashboard-style menu button with icon character.
     */
    public static JButton createMenuButton(String text, String icon) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_HOVER : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();

                // Draw icon
                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
                g2.setColor(GRADIENT_START);
                FontMetrics iconMetrics = g2.getFontMetrics();
                int iconX = (getWidth() - iconMetrics.stringWidth(icon)) / 2;
                g2.drawString(icon, iconX, 45);

                // Draw label
                g2.setFont(FONT_BODY_BOLD);
                g2.setColor(getModel().isRollover() ? TEXT_PRIMARY : TEXT_SECONDARY);
                FontMetrics textMetrics = g2.getFontMetrics();
                int textX = (getWidth() - textMetrics.stringWidth(text)) / 2;
                g2.drawString(text, textX, 72);
                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(140, 90));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Creates a styled label with the standard text color.
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    /**
     * Creates a heading label.
     */
    public static JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates a styled panel with card-like appearance.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        CORNER_RADIUS, CORNER_RADIUS);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1,
                        CORNER_RADIUS, CORNER_RADIUS);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(CARD_PADDING, CARD_PADDING,
                CARD_PADDING, CARD_PADDING));
        return card;
    }

    /**
     * Creates an error label (red text, initially hidden).
     */
    public static JLabel createErrorLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(FONT_SMALL);
        label.setForeground(ACCENT_DANGER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Creates a success label (green text).
     */
    public static JLabel createSuccessLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(FONT_SMALL);
        label.setForeground(ACCENT_SUCCESS);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // ─── Private Helpers ───────────────────────────────────────────

    private static void styleInputField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_INPUT);
        field.setCaretColor(TEXT_PRIMARY);
        field.setOpaque(false);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        field.setPreferredSize(new Dimension(0, FIELD_HEIGHT));

        // Focus highlight
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

    private static JButton createGradientButton(String text, Color startColor, Color endColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, startColor,
                        getWidth(), getHeight(), endColor
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                        CORNER_RADIUS, CORNER_RADIUS);

                // Hover overlay
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                            CORNER_RADIUS, CORNER_RADIUS);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));

        return button;
    }

    /**
     * Applies the dark theme look to a scroll pane.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(BG_PRIMARY);
        scrollPane.getViewport().setBackground(BG_PRIMARY);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Creates a horizontal separator line.
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        separator.setBackground(BG_PRIMARY);
        return separator;
    }
}
