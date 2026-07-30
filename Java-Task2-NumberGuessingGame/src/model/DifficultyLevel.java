package model;

/**
 * Defines difficulty presets for the Number Guessing Game.
 * Encapsulates range bounds, maximum attempts allowed, and score multipliers.
 */
public enum DifficultyLevel {

    EASY("Easy", 1, 50, 10, 1.0),
    MEDIUM("Medium", 1, 100, 7, 1.5),
    HARD("Hard", 1, 200, 5, 2.5),
    CUSTOM("Custom", 1, 100, 10, 1.0);

    private final String displayName;
    private final int minRange;
    private final int maxRange;
    private final int maxAttempts;
    private final double scoreMultiplier;

    DifficultyLevel(String displayName, int minRange, int maxRange, int maxAttempts, double scoreMultiplier) {
        this.displayName = displayName;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.maxAttempts = maxAttempts;
        this.scoreMultiplier = scoreMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    @Override
    public String toString() {
        return String.format("%s (%d - %d, %d Attempts, %.1fx Bonus)",
                displayName, minRange, maxRange, maxAttempts, scoreMultiplier);
    }
}
