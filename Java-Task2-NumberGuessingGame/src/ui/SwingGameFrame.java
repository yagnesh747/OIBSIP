package ui;

import exception.GameOverException;
import exception.InvalidGuessException;
import model.DifficultyLevel;
import model.GameRound;
import model.GuessResult;
import service.GameEngine;
import service.StatisticsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Modern Swing GUI for the Number Guessing Game featuring live attempt countdown,
 * feedback badges, guess history list, and stats dashboard.
 */
public class SwingGameFrame extends JFrame {

    private final GameEngine engine;
    private final StatisticsManager statsManager;
    private int roundCounter;

    private final JComboBox<DifficultyLevel> difficultyCombo;
    private final JTextField guessInput;
    private final JLabel feedbackLabel;
    private final JLabel attemptsLabel;
    private final JLabel scoreLabel;
    private final JProgressBar attemptsProgress;
    private final DefaultListModel<String> historyListModel;

    public SwingGameFrame() {
        super("🎯 Number Guessing Game — Oasis Infobyte Task 2");
        this.engine = new GameEngine();
        this.statsManager = new StatisticsManager();
        this.roundCounter = 0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout(15, 15));

        // Header Panel
        JPanel headerCard = UIConstants.createCard();
        headerCard.setLayout(new GridLayout(2, 1, 0, 4));
        JLabel title = new JLabel("🎯 Number Guessing Game", SwingConstants.CENTER);
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(UIConstants.PRIMARY_GREEN);

        JLabel subtitle = new JLabel("Oasis Infobyte Java Internship Project", SwingConstants.CENTER);
        subtitle.setFont(UIConstants.FONT_BODY);
        subtitle.setForeground(UIConstants.TEXT_SUB);

        headerCard.add(title);
        headerCard.add(subtitle);
        add(headerCard, BorderLayout.NORTH);

        // Center Panel (Controls & Output)
        JPanel centerCard = UIConstants.createCard();
        centerCard.setLayout(new BoxLayout(centerCard, BoxLayout.Y_AXIS));

        // Difficulty Selection Row
        JPanel diffRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        diffRow.setOpaque(false);
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(UIConstants.FONT_BOLD);
        diffLabel.setForeground(UIConstants.TEXT_MAIN);

        difficultyCombo = new JComboBox<>(DifficultyLevel.values());
        difficultyCombo.setFont(UIConstants.FONT_BODY);

        JButton startBtn = UIConstants.createButton("Start New Game", UIConstants.PRIMARY_GREEN);
        startBtn.addActionListener(e -> startNewRound());

        diffRow.add(diffLabel);
        diffRow.add(difficultyCombo);
        diffRow.add(startBtn);

        centerCard.add(diffRow);
        centerCard.add(Box.createVerticalStrut(15));

        // Feedback Display Card
        feedbackLabel = new JLabel("Click 'Start New Game' to begin!", SwingConstants.CENTER);
        feedbackLabel.setFont(UIConstants.FONT_HEADING);
        feedbackLabel.setForeground(UIConstants.SECONDARY_GREEN);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCard.add(feedbackLabel);

        centerCard.add(Box.createVerticalStrut(15));

        // Progress Bar
        attemptsProgress = new JProgressBar(0, 10);
        attemptsProgress.setValue(10);
        attemptsProgress.setStringPainted(true);
        attemptsProgress.setForeground(UIConstants.PRIMARY_GREEN);
        centerCard.add(attemptsProgress);

        centerCard.add(Box.createVerticalStrut(15));

        // Guess Input Row
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputRow.setOpaque(false);
        guessInput = UIConstants.createTextField();
        guessInput.setPreferredSize(new Dimension(120, 40));

        JButton submitBtn = UIConstants.createButton("Guess", UIConstants.ACCENT_ORANGE);
        submitBtn.addActionListener(e -> handleGuess());

        inputRow.add(guessInput);
        inputRow.add(submitBtn);
        centerCard.add(inputRow);

        centerCard.add(Box.createVerticalStrut(15));

        // History Log
        JLabel historyLabel = new JLabel("Guess History:");
        historyLabel.setFont(UIConstants.FONT_BOLD);
        historyLabel.setForeground(UIConstants.TEXT_SUB);
        historyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerCard.add(historyLabel);

        historyListModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyListModel);
        historyList.setBackground(UIConstants.BG_INPUT);
        historyList.setForeground(UIConstants.TEXT_MAIN);
        historyList.setFont(UIConstants.FONT_BODY);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(480, 120));
        centerCard.add(scrollPane);

        add(centerCard, BorderLayout.CENTER);

        // Footer Dashboard Panel
        JPanel footerCard = UIConstants.createCard();
        footerCard.setLayout(new GridLayout(1, 3, 10, 0));

        attemptsLabel = new JLabel("Attempts: 0/0", SwingConstants.CENTER);
        attemptsLabel.setFont(UIConstants.FONT_BOLD);
        attemptsLabel.setForeground(UIConstants.TEXT_MAIN);

        scoreLabel = new JLabel("Score: 0 pts", SwingConstants.CENTER);
        scoreLabel.setFont(UIConstants.FONT_BOLD);
        scoreLabel.setForeground(UIConstants.ACCENT_ORANGE);

        JButton statsBtn = UIConstants.createButton("View Stats", UIConstants.PRIMARY_GREEN);
        statsBtn.addActionListener(e -> showStatsDialog());

        footerCard.add(attemptsLabel);
        footerCard.add(scoreLabel);
        footerCard.add(statsBtn);

        add(footerCard, BorderLayout.SOUTH);
    }

    private void startNewRound() {
        DifficultyLevel diff = (DifficultyLevel) difficultyCombo.getSelectedItem();
        roundCounter++;
        engine.startRound(diff, roundCounter);

        attemptsProgress.setMaximum(diff.getMaxAttempts());
        attemptsProgress.setValue(diff.getMaxAttempts());
        attemptsProgress.setString(diff.getMaxAttempts() + " attempts remaining");

        feedbackLabel.setText("Guess a number between " + diff.getMinRange() + " and " + diff.getMaxRange());
        feedbackLabel.setForeground(UIConstants.PRIMARY_GREEN);
        attemptsLabel.setText("Attempts: 0/" + diff.getMaxAttempts());

        historyListModel.clear();
        guessInput.setText("");
        guessInput.requestFocus();
    }

    private void handleGuess() {
        if (!engine.isRoundActive()) {
            JOptionPane.showMessageDialog(this, "Please start a new round first!", "Game Over", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String input = guessInput.getText().trim();
            GuessResult result = engine.processGuess(input);

            historyListModel.addElement(String.format("Guess #%d: %d ➔ %s (%s)",
                    engine.getCurrentRound().getAttemptsUsed(),
                    result.getGuess(),
                    result.getFeedback().name().replace("_", " "),
                    result.getProximity().getLabel()));

            attemptsProgress.setValue(result.getAttemptsRemaining());
            attemptsProgress.setString(result.getAttemptsRemaining() + " attempts remaining");
            attemptsLabel.setText("Attempts: " + engine.getCurrentRound().getAttemptsUsed() + "/" + engine.getCurrentRound().getDifficulty().getMaxAttempts());

            if (result.isCorrect()) {
                feedbackLabel.setText("🎉 " + result.getFeedback().getMessage());
                feedbackLabel.setForeground(UIConstants.ACCENT_GREEN);
                scoreLabel.setText("Score: " + statsManager.getStats().getTotalScore() + " pts");
                statsManager.recordRound(engine.getCurrentRound());
                JOptionPane.showMessageDialog(this,
                        "🎉 You WON! Target was " + engine.getTargetNumber() + "\nScore Earned: " + engine.getCurrentRound().getScoreEarned() + " pts",
                        "Victory!", JOptionPane.INFORMATION_MESSAGE);
            } else if (!engine.isRoundActive()) {
                feedbackLabel.setText("❌ Game Over! Target was " + engine.getTargetNumber());
                feedbackLabel.setForeground(UIConstants.ACCENT_RED);
                statsManager.recordRound(engine.getCurrentRound());
                JOptionPane.showMessageDialog(this,
                        "❌ Game Over! You ran out of attempts.\nThe number was " + engine.getTargetNumber(),
                        "Game Over", JOptionPane.ERROR_MESSAGE);
            } else {
                feedbackLabel.setText(result.getFeedback().getMessage() + " " + result.getProximity().getLabel());
                feedbackLabel.setForeground(result.getFeedback() == GuessResult.Feedback.TOO_HIGH ? UIConstants.ACCENT_RED : UIConstants.ACCENT_YELLOW);
            }

            guessInput.setText("");
            guessInput.requestFocus();

        } catch (InvalidGuessException | GameOverException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showStatsDialog() {
        model.GameStats stats = statsManager.getStats();
        String message = String.format("""
                📊 SESSION STATISTICS
                ----------------------------
                Games Played : %d
                Games Won    : %d
                Games Lost   : %d
                Win Rate     : %.1f%%
                High Score   : %d pts
                Total Score  : %d pts
                Avg Attempts : %.1f
                """,
                stats.getGamesPlayed(), stats.getGamesWon(), stats.getGamesLost(),
                stats.getWinPercentage(), stats.getHighScore(), stats.getTotalScore(),
                stats.getAverageAttempts());

        JOptionPane.showMessageDialog(this, message, "Session Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
}
