package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Centralized logging utility for the ATM application.
 * Writes log entries to both the console and a rotating log file.
 */
public final class AppLogger {

    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE = "atm.log";
    private static final Logger LOGGER = Logger.getLogger("ATMApplication");
    private static boolean initialized = false;

    private AppLogger() {
        // Utility class — prevent instantiation
    }

    /**
     * Initializes the logging system. Safe to call multiple times;
     * only the first invocation takes effect.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        try {
            Path logDir = Paths.get(LOG_DIRECTORY);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            FileHandler fileHandler = new FileHandler(
                    LOG_DIRECTORY + "/" + LOG_FILE,
                    1_048_576,   // 1 MB max file size
                    3,           // 3 rotating files
                    true         // append mode
            );
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            LOGGER.setUseParentHandlers(false);

            initialized = true;
            info("Logger initialized successfully.");
        } catch (IOException e) {
            System.err.println("Failed to initialize file logger: " + e.getMessage());
            // Application continues with console logging
        }
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.log(Level.SEVERE, message, throwable);
    }

    public static void debug(String message) {
        LOGGER.fine(message);
    }
}
