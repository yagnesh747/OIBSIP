package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single game round session.
 * Stores target number, difficulty, attempt history, outcome, and earned points.
 */
public class GameRound {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final int roundNumber;
    private final DifficultyLevel difficulty;
    private final int targetNumber;
    private final List<Integer> guessHistory;
    private final LocalDateTime startTime;
    private boolean won;
    private int scoreEarned;

    public GameRound(int roundNumber, DifficultyLevel difficulty, int targetNumber) {
        this.roundNumber = roundNumber;
        this.difficulty = difficulty;
        this.targetNumber = targetNumber;
        this.guessHistory = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        this.won = false;
        this.scoreEarned = 0;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public List<Integer> getGuessHistory() {
        return Collections.unmodifiableList(guessHistory);
    }

    public void addGuess(int guess) {
        guessHistory.add(guess);
    }

    public int getAttemptsUsed() {
        return guessHistory.size();
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getScoreEarned() {
        return scoreEarned;
    }

    public void setScoreEarned(int scoreEarned) {
        this.scoreEarned = scoreEarned;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getFormattedStartTime() {
        return startTime.format(TIME_FORMATTER);
    }

    @Override
    public String toString() {
        return String.format("Round %d [%s] | Target: %d | Guesses: %d/%d | %s | Score: %d pts",
                roundNumber, difficulty.getDisplayName(), targetNumber,
                getAttemptsUsed(), difficulty.getMaxAttempts(),
                won ? "WON 🏆" : "LOST ❌", scoreEarned);
    }
}
