import ui.ConsoleUI;
import ui.SwingGameFrame;

import javax.swing.*;

/**
 * Application entry point for Task 2 — Number Guessing Game.
 * Supports dual-mode execution (Interactive Console vs Swing GUI).
 * Pass "--gui" command-line argument or choice to launch GUI mode.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "--gui".equalsIgnoreCase(args[0])) {
            launchGui();
        } else {
            // Default interactive console launcher with option to switch to GUI
            System.out.println("Starting Number Guessing Game...");
            System.out.println("Launching Console Interface...");
            ConsoleUI console = new ConsoleUI();
            console.start();
        }
    }

    private static void launchGui() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new SwingGameFrame().setVisible(true);
        });
    }
}
