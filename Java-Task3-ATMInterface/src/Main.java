import ui.ATMFrame;
import util.AppLogger;

import javax.swing.*;

/**
 * Application entry point.
 * Initializes logging, configures Swing Look and Feel, and launches the ATM frame
 * on the Event Dispatch Thread (EDT) for thread safety.
 */
public class Main {

    public static void main(String[] args) {
        // Initialize Logger
        AppLogger.initialize();
        AppLogger.info("Starting ATM Interface Application...");

        // Ensure Swing UI runs on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Apply system look and feel for native frame styling
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                AppLogger.warning("Could not set system Look and Feel: " + e.getMessage());
            }

            new ATMFrame();
        });
    }
}
