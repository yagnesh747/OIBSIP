package service;

import exception.GameOverException;
import exception.InvalidGuessException;
import model.DifficultyLevel;
import model.GameRound;
import model.GuessResult;
import model.GuessResult.Feedback;
import model.GuessResult.Proximity;
import util.InputValidator;
import util.MotivationalQuotes;

import java.util.Random;

/**
 * Core game engine responsible for round lifecycle: generating the target number,
 * processing each guess, computing feedback/proximity, and determining win/loss.
 *
 * <p>A new {@link GameRound} must be started before accepting guesses.
 * The engine enforces max attempt limits and raises exceptions for invalid input.</p>
 */
public class GameEngine {

    private static final Random RANDOM = new Random();

    private final ScoreCalculator scoreCalculator;
    private GameRound currentRound;
    private DifficultyLevel currentDifficulty;
    private int targetNumber;
    private int attemptsRemaining;
    private boolean roundActive;

    public GameEngine() {
        this.scoreCalculator = new ScoreCalculator();
        this.roundActive = false;
    }

    // ─── Round Lifecycle ─────────────────────────────────────────────

    /**
     * Starts a new round using the given difficulty preset.
     *
     * @param difficulty the chosen difficulty level
     * @param roundNumber the sequential round number for this session
     * @return the newly created {@link GameRound}
     */
    public GameRound startRound(DifficultyLevel difficulty, int roundNumber) {
        this.currentDifficulty = difficulty;
        this.targetNumber = RANDOM.nextInt(difficulty.getMaxRange() - difficulty.getMinRange() + 1)
                + difficulty.getMinRange();
        this.attemptsRemaining = difficulty.getMaxAttempts();
        this.currentRound = new GameRound(roundNumber, difficulty, targetNumber);
        this.roundActive = true;
        return currentRound;
    }

    /**
     * Processes one guess attempt and returns full result feedback.
     *
     * @param guessInput raw string input from user
     * @return {@link GuessResult} with feedback, proximity, and remaining attempts
     * @throws InvalidGuessException if the guess is not a valid integer in range
     * @throws GameOverException     if the round has already concluded
     */
    public GuessResult processGuess(String guessInput) throws InvalidGuessException, GameOverException {
        if (!roundActive) {
            throw new GameOverException("No active round. Please start a new game.");
        }

        int guess = InputValidator.validateGuess(
                guessInput, currentDifficulty.getMinRange(), currentDifficulty.getMaxRange());

        currentRound.addGuess(guess);
        attemptsRemaining--;

        Feedback feedback;
        Proximity proximity;
        int score = 0;

        if (guess == targetNumber) {
            feedback = Feedback.CORRECT;
            proximity = Proximity.BURNING_HOT;
            score = scoreCalculator.calculateScore(currentDifficulty, attemptsRemaining);
            currentRound.setWon(true);
            currentRound.setScoreEarned(score);
            roundActive = false;
        } else {
            feedback = guess > targetNumber ? Feedback.TOO_HIGH : Feedback.TOO_LOW;
            proximity = calculateProximity(Math.abs(guess - targetNumber));

            if (attemptsRemaining <= 0) {
                roundActive = false;
                currentRound.setWon(false);
                currentRound.setScoreEarned(0);
            }
        }

        String quote = feedback == Feedback.CORRECT
                ? MotivationalQuotes.getRandomWinQuote()
                : attemptsRemaining <= 0
                    ? MotivationalQuotes.getRandomLossQuote()
                    : MotivationalQuotes.getRandomGuessQuote();

        return new GuessResult(guess, feedback, proximity, attemptsRemaining, quote);
    }

    /**
     * Returns whether the current round is still in progress.
     */
    public boolean isRoundActive() {
        return roundActive;
    }

    public GameRound getCurrentRound() {
        return currentRound;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    // ─── Private Helpers ─────────────────────────────────────────────

    /**
     * Maps the absolute distance from target to a proximity category.
     * Thresholds are scaled relative to the current difficulty range.
     */
    private Proximity calculateProximity(int distance) {
        int range = currentDifficulty.getMaxRange() - currentDifficulty.getMinRange();
        double ratio = (double) distance / range;

        if (ratio <= 0.10) return Proximity.BURNING_HOT;
        if (ratio <= 0.25) return Proximity.WARM;
        return Proximity.COLD;
    }
}
