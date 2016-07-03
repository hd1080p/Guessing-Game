package core;
import java.util.Random;

public class RandomGuesser implements IGuesser {
    Random rand = new Random();
    Configuration config;

    public RandomGuesser(Configuration config)
    {
        this.config = config;
    }

    public int nextGuess()
    {
        int guess = this.rand.nextInt(config.getMaxNumber()) + 1;
        return guess;
    }
}
