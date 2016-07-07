package homework1;

import java.util.Random;
import java.util.Scanner;

public class GuessingGame {
	static final int limit = 4;
    static final int maxInteger = 16;

	public static void main(String[] args) {
        // This creates a new random number generator
        Random rand = new Random();
        int target = rand.nextInt(maxInteger) + 1;

        Scanner input = new Scanner(System.in);

        System.out.printf("Guess a number between 1 and %d.\n", maxInteger);

        // Loop while the number of attempts is less than the number of allowed guesses
        int attempts = 1;
		
        while (attempts <= limit) {
			
            System.out.printf("Attempt %d of %d: ", attempts, limit);

            int guess = input.nextInt();
			
            System.out.printf("You guessed %d\n", guess);

            if(guess < target)
                System.out.println("Too low!");
            else if(guess > target)
                System.out.println("Too high!");
            else
            {
                System.out.println("You Win!");
                return;
            }

            attempts+=1;

            if(attempts > limit)
            {
                System.out.println("You have guessed too many times!");
                break;
            }
			
        }
        System.out.println("You lose!");
    }
}
