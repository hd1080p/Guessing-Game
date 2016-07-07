package homework4;

import java.util.Random;

public class MainGUI {
    //Assuming the user does not want to change the rules of the game
    //Keeping this static will only allow for these conditions
    //Max Int = [1,16] Attempt Limit = [1,4]
    static final int limit = 4;
    static final int maxVal = 16;
    static final int minVal = 1;

    public static void main(String[] args) {
        //Sets random target
        Random rand = new Random();
        int target = rand.nextInt(maxVal) + 1;

    	GuessingGameGUI gui = new GuessingGameGUI(limit , maxVal, target, minVal);
    	gui.play();
    }
}
