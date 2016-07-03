package gui;

import core.*;

public class MainGUI {
    //Assuming the user does not want to change the rules of the game
    //Keeping this static will only allow for these conditions
    //Max Int = [1,16] Attempt Limit = [1,4]

    public static void main(String[] args) throws Exception{
        //Sets random target
        Configuration config = new Configuration(16,4);
        RandomChooser rand = new RandomChooser(config);

        GuessingGameGUI gui = new GuessingGameGUI(config,rand);
        gui.play();
    }

}
