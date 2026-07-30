package model;

/**
 * Encapsulates the outcome of a single guess attempt.
 * Includes comparison feedback (Too High / Too Low / Correct),
 * distance proximity ("Burning Hot", "Warm", "Cold"), and motivational messages.
 */
public class GuessResult {

    public enum Feedback {
        TOO_HIGH("Too High! Try a lower number."),
        TOO_LOW("Too Low! Try a higher number."),
        CORRECT("BINGO! You guessed the exact number!");

        private final String message;

        Feedback(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum Proximity {
        BURNING_HOT("🔥 Burning Hot! You are super close!"),
        WARM("♨️ Warm! Getting closer..."),
        COLD("❄️ Cold! You're quite far off.");

        private final String label;

        Proximity(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final int guess;
    private final Feedback feedback;
    private final Proximity proximity;
    private final int attemptsRemaining;
    private final String motivationalQuote;

    public GuessResult(int guess, Feedback feedback, Proximity proximity,
                       int attemptsRemaining, String motivationalQuote) {
        this.guess = guess;
        this.feedback = feedback;
        this.proximity = proximity;
        this.attemptsRemaining = attemptsRemaining;
        this.motivationalQuote = motivationalQuote;
    }

    public int getGuess() {
        return guess;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public Proximity getProximity() {
        return proximity;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public String getMotivationalQuote() {
        return motivationalQuote;
    }

    public boolean isCorrect() {
        return feedback == Feedback.CORRECT;
    }
}
