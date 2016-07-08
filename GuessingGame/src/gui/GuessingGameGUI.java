package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import core.*;

//Harris Dizdarevic
public class GuessingGameGUI implements ActionListener, IClient, IGuesser {

    private int[] history;
    private Configuration config;
    private RandomChooser rand;
    private int attempts;
    private Semaphore sem;

    // These components are members so they can be modified from the
    // `actionPerformed` method.
    private JLabel guessTextLabel;
    private JLabel guessNumberLabel;
    private JLabel feedbackLabel;
    private JLabel attemptLabel;
    private JButton backSpace;
    private JButton submit;



    public GuessingGameGUI(Configuration config, RandomChooser rand)
    {
        //instantiates a history of guesses
        history = new int[config.getMaxNumber()];
        attempts = 1;
        //only one thread will be acquired at a time
        sem = new Semaphore(1);
        this.config = config;
        this.rand = rand;
    }

    public void win(){
        feedbackLabel.setText("You Win!!");
        submit.setEnabled(false);
    }

    public void lose(){
        feedbackLabel.setText("You Lose!!");
        submit.setEnabled(false);
    }

    public void tooLow(int guess){
        feedbackLabel.setText("This number is too low!");
    }

    public void tooHigh(int guess){
        feedbackLabel.setText("This number is too high!");
    }

    public int nextGuess(){
        try {
            sem.acquire();
        }catch(InterruptedException ie){
            //do nothing
        }

        synchronized (this) {
            String guess = guessNumberLabel.getText();
            while (true) {
                try {
                    int val = Integer.parseInt(guess);
                    sem.release();
                    return val;
                } catch (Exception ex) {
                    feedbackLabel.setText("Not a Valid Guess");
                    guessNumberLabel.setText("0");
                }
                }
            }
    }

    private void handleBS()
    {
        String text = guessNumberLabel.getText();
        text = text.substring(0, text.length() - 1);
        if (text.equals("")) {
            text = "0";
            backSpace.setEnabled(false);
        }
        guessNumberLabel.setText(text);
    }

    private void handleSubmit()
    {
        try {
            int value = this.nextGuess();
            /*
            This is before checking history because initially the array has values of 0, which will
            cause it to print out the message "You have already guessed this number" when 0 is an invalid guess.
            Another possible way to do this is to have the submit button disabled until a value other than 0 is
            pressed.
            */

            if(value == 0 || value > config.getMaxNumber())
                throw new NumberFormatException();

            // Can't put this in compareGuesses because same guess will count as an attempt.
            // Do not want to count previous guesses as an attempt
            if (history_of_guesses(history, value, attempts)) {
                feedbackLabel.setText(String.format("%-10s", "You have already guessed this number"));
                return;
            }

            //Logic moved into a separate method for organization
            compareGuesses(value);
            return;

        } catch (NumberFormatException ex) {
            // Ignore integer parse exception...
            feedbackLabel.setText("Not in range!!!"); //Domain: [1,16]
            guessNumberLabel.setText("0");
        }

    }

    //The Logic for handleSubmit
    private void compareGuesses(int input){
        IChooser.ComparisonResult result = rand.checkGuess(input);
        if(result == IChooser.ComparisonResult.TooHigh)
            feedbackLabel.setText("Guess is too High!");
        else if(result == IChooser.ComparisonResult.TooLow)
            feedbackLabel.setText("Guess is too Low!");
        else
            this.win();

        history[attempts - 1] = input;
        attempts++;

        //Increments number of attempts in the Label
        attemptLabel.setText("Attempts: " + Integer.toString(attempts));

        if(attempts > config.getAllowedGuesses()) {
            feedbackLabel.setText(String.format("%30s", "You Have Guessed too many times!!"));
            submit.setEnabled(false);
        }
        //Resets the number rather than the user having to annoyingly press Backspace to clear the input.
        guessNumberLabel.setText("0");
    }

    private boolean history_of_guesses(int history[], int guess, int attempts)
    {
        for(int i = 0; i < attempts; i++)
            if(history[i] == guess)
                return true;
        history[attempts - 1] = guess;
        return false;
    }

    private void handleNumber(int value)
    {
        String text = guessNumberLabel.getText();
        backSpace.setEnabled(true);

        if (text.equals("0"))
            text = "";

        text += Integer.toString(value);
        guessNumberLabel.setText(text);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());

        try {
            int value = Integer.parseInt(e.getActionCommand());
            handleNumber(value);
            return;
        } catch (NumberFormatException ex) { } // Do nothing

        if (e.getActionCommand().equals("BS")) {
            handleBS();
            return;
        }

        if (e.getActionCommand().equals("submit")) {
            handleSubmit();
            return;
        }


    }
    private JFrame createFrame()
    {
        JFrame frame = new JFrame("The Guessing Game!");
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(
                new BoxLayout(
                        frame.getContentPane(),
                        BoxLayout.Y_AXIS)
        );

        return frame;
    }

    private JLabel createMainLabel() {
        JLabel mainLabel = new JLabel();
        mainLabel.setText(
                String.format("%-150s","Guess a number between 1 and " + Integer.toString(config.getMaxNumber())));
        mainLabel.setFont(
                new Font("Sans", Font.PLAIN ,22));

        return mainLabel;
    }

    private JLabel createFeedBackLabel() {
        JLabel feedbackLabel = new JLabel();
        feedbackLabel.setFont(
                new Font("Sans", Font.BOLD, 22));
        return feedbackLabel;
    }

    private JLabel createGuessNumberLabel() {

        JLabel guessLabel = new JLabel(Integer.toString(0));

        guessLabel.setFont(
                new Font("Sans", Font.BOLD, 22));
        return guessLabel;
    }

    private JLabel createGuessTextLabel() {
        JLabel guessTextLabel = new JLabel("  Guess: ");
        guessTextLabel.setFont(
                new Font("Sans", Font.BOLD, 22));

        return guessTextLabel;
    }

    private JLabel createAttemptLabel() {
        JLabel attemptTextLabel = new JLabel(String.format("%-10s","Attempts: " + attempts));
        attemptTextLabel.setFont(
                new Font("Sans", Font.BOLD, 22));

        return attemptTextLabel;
    }

    private JPanel createNumberPad()
    {
        JPanel numberPad = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));

        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            numberPad.setPreferredSize(new Dimension(2,10));
            numberPad.add(button);

            // Call `this.actionPerformed` when the button is pressed.
            button.addActionListener(this);
        }
        return numberPad;
    }

    private JPanel createBottomPanel()
    {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(50, 400));
        return bottomPanel;
    }

    private JButton createBackSpace()
    {
        JButton backSpace = new JButton("Backspace");
        backSpace.setActionCommand("BS");

        backSpace.setEnabled(false);

        backSpace.addActionListener(this);
        return backSpace;
    }

    private JButton createSubmitButton()
    {
        JButton submit = new JButton("Submit Guess");
        submit.setActionCommand("submit");

        submit.addActionListener(this);
        return submit;
    }

    public void play()
    {
        // Create all of the components.
        JFrame window = createFrame();
        submit = createSubmitButton();
        JPanel numberPad = createNumberPad();
        JLabel mainLabel = createMainLabel();
        guessTextLabel = createGuessTextLabel();
        guessNumberLabel = createGuessNumberLabel();
        attemptLabel = createAttemptLabel();
        feedbackLabel = createFeedBackLabel();
        JPanel bottomPanel = createBottomPanel();
        backSpace = createBackSpace();
        numberPad.add(backSpace);

        // Add the labels to the bottom panel.
        bottomPanel.add(attemptLabel);
        bottomPanel.add(submit);
        bottomPanel.add(guessTextLabel);
        bottomPanel.add(guessNumberLabel);
        //bottomPanel.add(guessLabel);

        // Add the components to the window.
        window.add(mainLabel);
        window.add(numberPad);
        window.add(feedbackLabel);
        window.add(bottomPanel);
        window.setVisible(true);
    }
}
