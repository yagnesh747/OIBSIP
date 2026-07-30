package util;

import java.util.Random;

/**
 * Provides dynamic motivational quotes and reactions based on game progress.
 */
public final class MotivationalQuotes {

    private static final Random RANDOM = new Random();

    private static final String[] ON_GUESS = {
            "Keep going! Your mind is sharper than you think.",
            "Great attempt! Every guess narrows down the truth.",
            "Stay focused! You're closing in on the target.",
            "Trust your instincts!",
            "Analytical thinking in action — keep it up!"
    };

    private static final String[] ON_WIN = {
            "Outstanding intuition! You solved it like a pro!",
            "Victory! Mastermind performance!",
            "Incredible precision! You nailed the exact number!",
            "Champion effort! Your strategy paid off!"
    };

    private static final String[] ON_LOSS = {
            "Don't give up! Every loss is data for your next win.",
            "Close game! Failure is just the stepping stone to mastery.",
            "Shake it off! Great minds learn from every attempt.",
            "Reset and refine — victory awaits your next round!"
    };

    private MotivationalQuotes() {
        // Utility class
    }

    public static String getRandomGuessQuote() {
        return ON_GUESS[RANDOM.nextInt(ON_GUESS.length)];
    }

    public static String getRandomWinQuote() {
        return ON_WIN[RANDOM.nextInt(ON_WIN.length)];
    }

    public static String getRandomLossQuote() {
        return ON_LOSS[RANDOM.nextInt(ON_LOSS.length)];
    }
}
