package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks aggregate game session statistics across multiple rounds.
 * Calculates games played, win rate percentage, total points, best score,
 * and average attempts taken.
 */
public class GameStats {

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int totalScore;
    private int highScore;
    private int totalAttemptsInWonGames;
    private final List<GameRound> roundHistory;

    public GameStats() {
        this.roundHistory = new ArrayList<>();
        reset();
    }

    public synchronized void recordRound(GameRound round) {
        gamesPlayed++;
        roundHistory.add(round);

        if (round.isWon()) {
            gamesWon++;
            totalAttemptsInWonGames += round.getAttemptsUsed();
        } else {
            gamesLost++;
        }

        totalScore += round.getScoreEarned();
        if (round.getScoreEarned() > highScore) {
            highScore = round.getScoreEarned();
        }
    }

    public synchronized void reset() {
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.totalScore = 0;
        this.highScore = 0;
        this.totalAttemptsInWonGames = 0;
        this.roundHistory.clear();
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public double getWinPercentage() {
        if (gamesPlayed == 0) return 0.0;
        return ((double) gamesWon / gamesPlayed) * 100.0;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public double getAverageAttempts() {
        if (gamesWon == 0) return 0.0;
        return (double) totalAttemptsInWonGames / gamesWon;
    }

    public List<GameRound> getRoundHistory() {
        return Collections.unmodifiableList(roundHistory);
    }
}
