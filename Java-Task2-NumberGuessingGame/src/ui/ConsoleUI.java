package ui;

import model.*;
import service.GameEngine;
import service.StatisticsManager;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

import static ui.AnsiColor.*;

/**
 * Full-featured ANSI-colored console interface for the Number Guessing Game.
 * Renders styled menus, in-game feedback, statistics dashboards, and session history.
 */
public class ConsoleUI {

    private static final String LINE   = "═".repeat(54);
    private static final String THIN   = "─".repeat(54);
    private static final int MAX_ROUNDS_PER_SESSION = 10;

    private final GameEngine engine;
    private final StatisticsManager statsManager;
    private final Scanner scanner;
    private int roundCounter;

    public ConsoleUI() {
        this.engine = new GameEngine();
        this.statsManager = new StatisticsManager();
        this.scanner = new Scanner(System.in);
        this.roundCounter = 0;
    }

    // ─── Entry Point ────────────────────────────────────────────────

    public void start() {
        printBanner();
        mainMenu();
        printFarewell();
    }

    // ─── Menus ──────────────────────────────────────────────────────

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println();
            printBox("MAIN MENU", new String[]{
                    "  1 ▶  Start New Game",
                    "  2 ▶  View Statistics",
                    "  3 ▶  View Session History",
                    "  4 ▶  Reset Statistics",
                    "  5 ▶  Exit"
            });
            String choice = prompt("Select an option");

            switch (choice.trim()) {
                case "1" -> startGame();
                case "2" -> showStatistics();
                case "3" -> showHistory();
                case "4" -> confirmResetStats();
                case "5" -> {
                    if (confirmExit()) running = false;
                }
                default  -> warn("Invalid option. Enter a number between 1 and 5.");
            }
        }
    }

    private void startGame() {
        DifficultyLevel difficulty = chooseDifficulty();
        roundCounter++;

        GameRound round = engine.startRound(difficulty, roundCounter);

        printSection("ROUND " + roundCounter + " — " + difficulty.getDisplayName().toUpperCase());
        System.out.println(colorize("  Guess the number between "
                + difficulty.getMinRange() + " and " + difficulty.getMaxRange(), CYAN));
        System.out.println(colorize("  You have " + difficulty.getMaxAttempts() + " attempt(s).", YELLOW));
        System.out.println(THIN);

        playRound(round, difficulty);
    }

    private DifficultyLevel chooseDifficulty() {
        printBox("SELECT DIFFICULTY", new String[]{
                "  1 ▶  " + DifficultyLevel.EASY,
                "  2 ▶  " + DifficultyLevel.MEDIUM,
                "  3 ▶  " + DifficultyLevel.HARD
        });

        String choice = prompt("Enter difficulty (1/2/3)");
        return switch (choice.trim()) {
            case "1" -> DifficultyLevel.EASY;
            case "3" -> DifficultyLevel.HARD;
            default  -> DifficultyLevel.MEDIUM;
        };
    }

    // ─── Round Gameplay ─────────────────────────────────────────────

    private void playRound(GameRound round, DifficultyLevel difficulty) {
        while (engine.isRoundActive()) {
            int remaining = engine.getAttemptsRemaining();
            System.out.println();
            System.out.printf("  %sAttempts remaining: %s%d%s  |  Guesses so far: %s%s",
                    BOLD, remaining <= 2 ? RED : GREEN, remaining, RESET,
                    CYAN, round.getGuessHistory().toString().replace("[", "").replace("]", ""));
            System.out.println(RESET);

            String input = prompt("Enter your guess");

            try {
                GuessResult result = engine.processGuess(input);
                displayGuessResult(result);

                if (result.isCorrect()) {
                    displayWin(round);
                } else if (!engine.isRoundActive()) {
                    displayLoss(round, engine.getTargetNumber());
                }
            } catch (Exception ex) {
                warn(ex.getMessage());
            }
        }

        statsManager.recordRound(round);
        offerPlayAgain();
    }

    // ─── Feedback Display ────────────────────────────────────────────

    private void displayGuessResult(GuessResult result) {
        System.out.println();

        String feedbackColor = switch (result.getFeedback()) {
            case CORRECT  -> GREEN;
            case TOO_HIGH -> RED;
            case TOO_LOW  -> YELLOW;
        };

        System.out.println("  " + colorize("▶ " + result.getFeedback().getMessage(), feedbackColor + BOLD));

        if (!result.isCorrect()) {
            System.out.println("  " + colorize(result.getProximity().getLabel(), PURPLE));
            System.out.println("  " + colorize("\" " + result.getMotivationalQuote() + " \"", CYAN));
        }
    }

    private void displayWin(GameRound round) {
        System.out.println();
        System.out.println(colorize(LINE, GREEN));
        System.out.println(colorize("  🏆  CONGRATULATIONS! You guessed it correctly!", GREEN + BOLD));
        System.out.printf("  ✔  Attempts used: %d / %d%n", round.getAttemptsUsed(), round.getDifficulty().getMaxAttempts());
        System.out.printf("  ⭐  Score Earned : %s%d points%s%n", YELLOW + BOLD, round.getScoreEarned(), RESET);
        System.out.println(colorize("  \"" + util.MotivationalQuotes.getRandomWinQuote() + "\"", CYAN));
        System.out.println(colorize(LINE, GREEN));
    }

    private void displayLoss(GameRound round, int target) {
        System.out.println();
        System.out.println(colorize(LINE, RED));
        System.out.println(colorize("  ❌  GAME OVER! You ran out of attempts.", RED + BOLD));
        System.out.printf("  The number was: %s%d%s%n", YELLOW + BOLD, target, RESET);
        System.out.println(colorize("  \"" + util.MotivationalQuotes.getRandomLossQuote() + "\"", CYAN));
        System.out.println(colorize(LINE, RED));
    }

    // ─── Statistics & History ────────────────────────────────────────

    private void showStatistics() {
        model.GameStats stats = statsManager.getStats();
        System.out.println();
        printBox("SESSION STATISTICS", new String[]{
                String.format("  Games Played   : %s%d%s", CYAN  + BOLD, stats.getGamesPlayed(), RESET),
                String.format("  Games Won      : %s%d%s", GREEN + BOLD, stats.getGamesWon(), RESET),
                String.format("  Games Lost     : %s%d%s", RED   + BOLD, stats.getGamesLost(), RESET),
                String.format("  Win Rate       : %s%.1f%%%s", YELLOW + BOLD, stats.getWinPercentage(), RESET),
                String.format("  High Score     : %s%d pts%s", YELLOW + BOLD, stats.getHighScore(), RESET),
                String.format("  Total Score    : %s%d pts%s", CYAN  + BOLD, stats.getTotalScore(), RESET),
                String.format("  Avg Attempts   : %s%.1f%s", PURPLE + BOLD, stats.getAverageAttempts(), RESET)
        });
    }

    private void showHistory() {
        List<GameRound> history = statsManager.getHistory();
        System.out.println();
        System.out.println(colorize(LINE, BLUE));
        System.out.println(colorize("  SESSION HISTORY", BLUE + BOLD));
        System.out.println(colorize(LINE, BLUE));

        if (history.isEmpty()) {
            System.out.println(colorize("  No rounds played yet.", YELLOW));
        } else {
            for (GameRound r : history) {
                String outcome = r.isWon()
                        ? colorize("WON  🏆", GREEN + BOLD)
                        : colorize("LOST ❌", RED + BOLD);
                System.out.printf("  [%s] Round %d | %-8s | Target: %-4d | Guesses: %d/%d | %s | %s%d pts%s%n",
                        r.getFormattedStartTime(),
                        r.getRoundNumber(),
                        r.getDifficulty().getDisplayName(),
                        r.getTargetNumber(),
                        r.getAttemptsUsed(),
                        r.getDifficulty().getMaxAttempts(),
                        outcome,
                        YELLOW + BOLD, r.getScoreEarned(), RESET);
            }
        }
        System.out.println(colorize(LINE, BLUE));
    }

    private void confirmResetStats() {
        System.out.println();
        warn("This will erase all session statistics and history.");
        String confirm = prompt("Type YES to confirm reset");
        if ("YES".equalsIgnoreCase(confirm.trim())) {
            statsManager.resetStats();
            roundCounter = 0;
            success("Statistics have been reset.");
        } else {
            info("Reset cancelled.");
        }
    }

    // ─── Flow Control ────────────────────────────────────────────────

    private void offerPlayAgain() {
        System.out.println();
        String choice = prompt("Play another round? (Y/N)");
        if (choice.trim().equalsIgnoreCase("Y")) {
            if (roundCounter >= MAX_ROUNDS_PER_SESSION) {
                warn("Maximum session rounds (" + MAX_ROUNDS_PER_SESSION + ") reached. Restart the app to continue.");
            } else {
                startGame();
            }
        }
    }

    private boolean confirmExit() {
        String confirm = prompt("Are you sure you want to exit? (Y/N)");
        return confirm.trim().equalsIgnoreCase("Y");
    }

    // ─── Print Helpers ────────────────────────────────────────────────

    private void printBanner() {
        System.out.println(colorize(LINE, BLUE));
        System.out.println(colorize("       🎯  NUMBER GUESSING GAME", BLUE + BOLD));
        System.out.println(colorize("     Oasis Infobyte Java Internship — Task 2", CYAN));
        System.out.println(colorize("         Developer: Yagnesh Patel", PURPLE));
        System.out.println(colorize(LINE, BLUE));
    }

    private void printBox(String title, String[] lines) {
        System.out.println(colorize(LINE, BLUE));
        System.out.println(colorize("  " + title, BLUE + BOLD));
        System.out.println(colorize(THIN, BLUE));
        for (String line : lines) System.out.println(line + RESET);
        System.out.println(colorize(LINE, BLUE));
    }

    private void printSection(String title) {
        System.out.println();
        System.out.println(colorize(LINE, PURPLE));
        System.out.println(colorize("  " + title, PURPLE + BOLD));
        System.out.println(colorize(LINE, PURPLE));
    }

    private String prompt(String message) {
        System.out.print(colorize("  ▷ " + message + ": ", YELLOW + BOLD));
        return scanner.nextLine();
    }

    private void warn(String message) {
        System.out.println(colorize("  ⚠  " + message, RED));
    }

    private void success(String message) {
        System.out.println(colorize("  ✔  " + message, GREEN));
    }

    private void info(String message) {
        System.out.println(colorize("  ℹ  " + message, CYAN));
    }

    private void printFarewell() {
        System.out.println();
        System.out.println(colorize(LINE, GREEN));
        System.out.println(colorize("  Thanks for playing! See you next time! 👋", GREEN + BOLD));
        System.out.println(colorize(LINE, GREEN));
    }
}
