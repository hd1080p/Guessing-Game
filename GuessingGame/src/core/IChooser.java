package core;

public interface IChooser {
    enum ComparisonResult {
        Correct,
        TooHigh,
        TooLow
    }

    ComparisonResult checkGuess(int guess);
}
