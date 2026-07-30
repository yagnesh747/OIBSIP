package service;

import model.GameRound;
import model.GameStats;

import java.util.List;

/**
 * Manages game statistical aggregation and history tracking.
 */
public class StatisticsManager {

    private final GameStats globalStats;

    public StatisticsManager() {
        this.globalStats = new GameStats();
    }

    public void recordRound(GameRound round) {
        globalStats.recordRound(round);
    }

    public GameStats getStats() {
        return globalStats;
    }

    public void resetStats() {
        globalStats.reset();
    }

    public List<GameRound> getHistory() {
        return globalStats.getRoundHistory();
    }
}
