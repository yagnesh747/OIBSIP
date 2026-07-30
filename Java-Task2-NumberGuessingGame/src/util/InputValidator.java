package util;

import exception.InvalidGuessException;

/**
 * Utility class for validating user input ranges and menu choices.
 */
public final class InputValidator {

    private InputValidator() {
        // Utility class
    }

    /**
     * Validates that the guess string can be parsed into an integer
     * and falls within the active game bounds [min, max].
     */
    public static int validateGuess(String input, int min, int max) throws InvalidGuessException {
        if (input == null || input.isBlank()) {
            throw new InvalidGuessException("Guess input cannot be empty.");
        }
        try {
            int guess = Integer.parseInt(input.trim());
            if (guess < min || guess > max) {
                throw new InvalidGuessException(
                        String.format("Guess must be between %d and %d.", min, max));
            }
            return guess;
        } catch (NumberFormatException e) {
            throw new InvalidGuessException("Invalid input! Please enter a valid whole number.");
        }
    }

    /**
     * Parses a string to an integer within range, returning a fallback on failure.
     */
    public static int parseChoice(String input, int min, int max, int fallback) {
        if (input == null || input.isBlank()) return fallback;
        try {
            int val = Integer.parseInt(input.trim());
            return (val >= min && val <= max) ? val : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
