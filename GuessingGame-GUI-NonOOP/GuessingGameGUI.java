package homework4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//Harris Dizdarevic
public class GuessingGameGUI implements ActionListener {
    private int limit;
    private int maxInt;
    private int minInt;
    private int target;
    private int attempts;


    private int[] history;

    // These components are members so they can be modified from the
    // `actionPerformed` method.
    private JLabel guessTextLabel;
    private JLabel guessNumberLabel;
    private JLabel feedbackLabel;
    private JLabel attemptLabel;
    private JButton backSpace;
    private JButton submit;



	public GuessingGameGUI(int limit, int maxInt, int target, int minInt)
    {
        this.limit = limit;
        this.maxInt = maxInt;
        this.minInt = minInt;
        this.target = target;
        attempts = 1;             //Each instance of the Guessing Game will have an attempts start at 1
        history = new int[limit]; //In case you want to change the limit of guesses, this is here to accommodate that

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
        String text = guessNumberLabel.getText();

        try {
            int value = Integer.parseInt(text);

            /*This is before checking history because initially the array has values of 0, which will
              cause it to print out the message "You have already guessed this number" when 0 is an invalid guess.
              Another possible way to do this is to have the submit button disabled until a value other than 0 is
              pressed.*/

            if(value == 0 || value > maxInt)
                throw new NumberFormatException();

            //Cant put this in compareGuesses because same guess will count as an attempt.
            //Do not want to count previous guesses as an attempt
            if (history_of_guesses(history, value, attempts)) {
                feedbackLabel.setText(String.format("%-10s", "You have already guessed this number"));
                return;
            }

            //Logic moved into a separate method for organization
            compareGuesses(value);
            return;

        } catch (NumberFormatException ex) {
            // Ignore integer parse exception...
            // What can cause this exception to be thrown?
            feedbackLabel.setText("You're Dumb! Not in range!!!");//Domain: [1,16]
            guessNumberLabel.setText("0");
        }

    }

    //The Logic for handleSubmit
    private void compareGuesses(int input){
        if(input > target)
            feedbackLabel.setText(String.format("%29s", "Too High!"));
        else if(input < target)
            feedbackLabel.setText(String.format("%28s", "Too Low!"));
        else {
            feedbackLabel.setText(String.format("%28s", "You Win!!"));
            submit.setEnabled(false);
        }


        history[attempts - 1] = input;
        attempts++;

        //Increments number of attempts in the Label
        attemptLabel.setText("Attempts: " + Integer.toString(attempts));

        if(attempts > limit) {
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

        if (text.equals("0")) {
            text = "";

        }
        text += Integer.toString(value);
        guessNumberLabel.setText(text);
    }

    public void actionPerformed(ActionEvent e)
    {
        // You can "debug" your code by printing to stdout.
        // If you are using eclipse, the results will be written to the eclipse
        // log console.
        //
        // This command is printing out the action command for the button that
        // was pressed.
        System.out.println(e.getActionCommand());

        try {
            int value = Integer.parseInt(e.getActionCommand());
            handleNumber(value);
            return;
        } catch (NumberFormatException ex) {
            // Ignore integer parse exception...
            // This happens when "submit" or "BS" is pressed.
        }

        // Is it the backspace button?
        if (e.getActionCommand().equals("BS")) {
            handleBS();
            return;
        }

        // Is it the submit button?
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
                String.format("%-150s","Guess a number between 1 and " + Integer.toString(this.maxInt)));
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
        // The backspace will start "disabled".
        // This means you will not be able to click on it until there is
        // something to erase.
        backSpace.setEnabled(false);
        // Call `this.actionPerformed` when the button is pressed.
        backSpace.addActionListener(this);
        return backSpace;
    }

    private JButton createSubmitButton()
    {
    	JButton submit = new JButton("Submit Guess");
        submit.setActionCommand("submit");
        // Call `this.actionPerformed` when the button is pressed.
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

        // Add the backspace button to the numberPad.
        numberPad.add(backSpace);

        // Add submit button and guesslabel to the bottom panel.
        bottomPanel.add(attemptLabel);

        bottomPanel.add(submit);

        bottomPanel.add(guessTextLabel);
        bottomPanel.add(guessNumberLabel);

        // Add the components to the window.
        window.add(mainLabel);
        window.add(numberPad);
        window.add(feedbackLabel);
        window.add(bottomPanel);
        window.setVisible(true);
    }
}
