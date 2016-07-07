package homework2;

import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.Exception;

public class GuessingGame {

	static final int limit = 4;
  static final int maxInteger = 16;

	public static void main(String[] args) {
				//History of Guesses
				int[] history = new int[limit];

        // This creates a new random number generator
        Random rand = new Random();

        int target = rand.nextInt(maxInteger) + 1;

        Scanner input = new Scanner(System.in);

        System.out.printf("Guess a number between 1 and %d.\n", maxInteger);

        int attempts = 1;

        while (attempts <= limit) {
            System.out.printf("Attempt %d of %d: ", attempts, limit);

           int guess = 0;

           try{
                guess = input.nextInt();
                verifyguess(guess, history, attempts);
            }
            catch(InputMismatchException exc){
                System.out.println("You have entered a non-numeric input. Try again.");
                input.nextLine();
                continue;
            }
            catch(Exception ex){
                System.out.println(ex);
                input.nextLine();
                continue;
            }

            System.out.printf("You guessed %d\n", guess);

						history[attempts-1] = guess; //To Keep track of the guesses made
            if(guess < target) {
                System.out.println("Too low!");
            }
            else if(guess > target) {
                System.out.println("Too high!");
            }
            else {
                System.out.println("You Win!");
                return;
            }

            attempts+=1;

            if(attempts > limit)
                break;
        }
        System.out.println("You lose!");
    }

    //Separate Method to Throw Exceptions out of the ranges.
    public static void verifyguess(int guess, int history[], int attempts) throws Exception{
        if(guess < 1)
            throw new Exception("Number is below the acceptable range! (Guess < 1)");
        else if(guess > maxInteger)
            throw new Exception("Number is above the acceptable range! (Guess > 16)");

        if(pastguesses(history,guess,attempts))
            throw new Exception("Guess was already done.");
    }

    //Separate Method to check if the guess has been used already.
    public static boolean pastguesses(int history[], int guess, int attempts){
        for(int i = 0; i < attempts; i++)
            if(history[i] == guess)
                return true;
        history[attempts-1] = guess;
        return false;
    }
}
