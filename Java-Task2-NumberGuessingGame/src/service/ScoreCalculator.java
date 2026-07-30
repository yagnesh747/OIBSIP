package service;

import model.DifficultyLevel;

/**
 * Computes scores based on difficulty multipliers, attempts remaining,
 * and speed bonus factor.
 */
public class ScoreCalculator {

    private static final int BASE_POINTS_PER_REMAINING_ATTEMPT = 100;
    private static final int WINNING_BONUS = 500;

    /**
     * Calculates total score earned in a round.
     *
     * @param difficulty         active difficulty level
     * @param attemptsRemaining  attempts leftover when won
     * @return total calculated score
     */
    public int calculateScore(DifficultyLevel difficulty, int attemptsRemaining) {
        if (attemptsRemaining < 0) return 0;

        int baseScore = WINNING_BONUS + (attemptsRemaining * BASE_POINTS_PER_REMAINING_ATTEMPT);
        double total = baseScore * difficulty.getScoreMultiplier();
        return (int) Math.round(total);
    }
}
