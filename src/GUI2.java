import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.util.Random;

public class GUI2 implements ActionListener {


    private JPanel panel;
    private JButton button;
    private JButton rollButton;
    private JLabel label;
    private JFrame frame;
    private ImageIcon image;

    String jailFile = "jail.png"; // Images
    String black = "black.png";

    String _1 = "1.png"; // Dice Faces
    String _2 = "2.png";
    String _3 = "3.png";
    String _4 = "4.png";
    String _5 = "5.png";
    String _6 = "6.png";

    public GUI2() { // initializes panel and frame

        panel = new JPanel(); // panel is decorator for frame
        panel.setLayout(null);

        frame = new JFrame(); // creating frame to add panel onto
        frame.setSize(300, 375);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setBackdrop(String fileName) { // backdrop decoration

        image = new ImageIcon(getClass().getResource(fileName));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setSize(300,350);

        panel.add(pictureLabel); // image must be added to panel last in order to be in the background
        frame.add(panel); // adds panel to frame (Do I need to do this each time?)

    }

    public void setOkButton() { // button decoration

        button = new JButton("OK"); // creates button object
        button.setBounds(110, 300, 80, 25); // bounds start from upper left corner
        button.addActionListener(this); // "this" refers to "this class" and using an action method within it

        label = new JLabel("Press OK to Continue."); // label decorator for frame
        label.setBounds(80, 275, 175, 25);

        panel.add(button);
        panel.add(label);

    }

    public void displayJail() { // uses backdrop and button decorations

        //panel.removeAll(); // removes previous panel components
        setOkButton();
        setBackdrop(jailFile);  // note: backdrop must always be set last for visibility

        frame.add(panel);
        frame.setVisible(true); // must come at the very end


    }

    // binary options buttons function (Yes/No)
    // display question label



    public void displayDice(int firstDice, int secondDice) {

        //JFrame newFrame = new JFrame();
        //JPanel newPanel = new JPanel();

        String dice1 = String.valueOf(firstDice) + ".png"; // create relevant file names
        String dice2 = String.valueOf(secondDice) + ".png";

        System.out.println(dice1);
        System.out.println(dice2);

        ImageIcon image1 = new ImageIcon(getClass().getResource(dice1)); // gets images of dice
        ImageIcon image2  = new ImageIcon(getClass().getResource(dice2));
        JLabel diceLabel1 = new JLabel(image1);
        JLabel diceLabel2 = new JLabel(image2);

        diceLabel1.setBounds(40, 110, 95, 95);
        diceLabel2.setBounds(150, 110, 95, 95);

        panel.add(diceLabel1);
        panel.add(diceLabel2);

        setOkButton();


        setBackdrop(black);

        frame.add(panel);
        frame.setVisible(true); // must come at the very end

    }

    public void rollDice() {

        rollButton = new JButton("ROLL DICE"); // creates button object
        rollButton.setBounds(110, 300, 80, 25); // bounds start from upper left corner
        frame.add(panel);
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();

            }
        }); // "this" refers to "this class" and using an action method within it

        panel.add(rollButton);

        setBackdrop(black);


        frame.setVisible(true); // must come at the very end


    }

    public static void main(String[] args) {


        Random random = new Random();
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        GUI2 a = new GUI2();

        a.displayDice(dice1, dice2);

        GUI2 b = new GUI2();

        b.rollDice(); // creates new GUI object



    }

    @Override
    public void actionPerformed(ActionEvent e) { // for ok button

        frame.dispose(); // closes UI

    }

}
