package cli;
import core.*;

public class MainCLI {
    public static void main(String[] args) throws Exception {

        Configuration myConfig = new Configuration(16, 4);

        // Resolve and inject dependencies.
        IClient myClient = new ClientCLI();

        // You can use a random guesser:
        // IGuesser myGuesser = new RandomGuesser(myConfig);

        // Or the command-line guesser.
        IGuesser myGuesser = new GuesserCLI(myConfig);

        IChooser myChooser = new RandomChooser(myConfig);

        Game myGame = new Game(myChooser, myGuesser, myClient, myConfig);
        myGame.play();
    }
}

