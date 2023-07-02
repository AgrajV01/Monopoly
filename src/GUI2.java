import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener {


    private JPanel panel;
    private JButton button;
    private JButton rollButton;
    private JLabel label;
    private JFrame frame;
    private ImageIcon image;
    private JLayeredPane layeredPane;
    private JLabel diceLabel1;
    private JLabel diceLabel2;
    private static final int DISTPLAYERS = 20;
    private static final int MOVEUP = -90;

    String jailFile = "jail.png"; // Images
    String black = "black.png";

    String _1 = "1.png"; // Dice Faces
    String _2 = "2.png";
    String _3 = "3.png";
    String _4 = "4.png";
    String _5 = "5.png";
    String _6 = "6.png";

    public GUI2() {// initializes panel and frame
        layeredPane = new JLayeredPane(); // create the layered pane
        layeredPane.setPreferredSize(new Dimension(1000, 1000)); // set size

        frame = new JFrame(); // creating frame to add panel onto
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(layeredPane); // add layeredPane to frame
    }

    public void setBackdrop(String fileName) {
        image = new ImageIcon(getClass().getResource(fileName));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(390,370+MOVEUP,200, 300); // assuming the size of backdrop is same as the frame

        layeredPane.add(pictureLabel, new Integer(2)); // add to layeredPane on second layer

    }

    public void setOkButton() {
        button = new JButton("Roll");
        button.setBounds(450,600+MOVEUP, 80, 25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When button is clicked, remove old dice labels
                layeredPane.remove(diceLabel1);
                layeredPane.remove(diceLabel2);
                // Generate new dice values
                Random random = new Random();
                int dice1 = random.nextInt(6) + 1;
                int dice2 = random.nextInt(6) + 1;
                // Display new dice values
                displayDice(dice1, dice2);
                // Refresh the frame
                frame.repaint();
            }
        });

        layeredPane.add(button, new Integer(3)); // add to layeredPane on the top layer

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

        String dice1 = String.valueOf(firstDice) + ".png"; // create relevant file names
        String dice2 = String.valueOf(secondDice) + ".png";

        System.out.println(dice1);
        System.out.println(dice2);

        ImageIcon image1 = new ImageIcon(getClass().getResource(dice1)); // gets images of dice
        ImageIcon image2  = new ImageIcon(getClass().getResource(dice2));
        diceLabel1 = new JLabel(image1);
        diceLabel2 = new JLabel(image2);

        diceLabel1.setBounds(410, 390+MOVEUP, 75, 75);
        diceLabel2.setBounds(500, 390+MOVEUP, 75, 75);

        layeredPane.add(diceLabel1, new Integer(3)); // add to layeredPane on higher layer
        layeredPane.add(diceLabel2, new Integer(3)); // add to layeredPane on higher layer

        setBackdrop(black);

        setOkButton();

    }

    public void initializeTheBoard(Game game) {
        System.out.println("initializingTheBoard");

        ImageIcon image1 = new ImageIcon(getClass().getResource("board.png")); // gets images of dice
        JLabel boardImage = new JLabel(image1);

        boardImage.setBounds(0, 0+MOVEUP, 1000, 1000);

        layeredPane.add(boardImage, new Integer(1)); // add to layeredPane on lower layer

        setOkButton();

        setBackdrop(black);

        displayPlayers(game);

        frame.setVisible(true); // must come at the very end
    }

    public void displayPlayers(Game game) {
        System.out.println("adding Players");

        List<JLabel> playerIcons = new ArrayList<JLabel>(game.getNumPlayers());

        for(int i = 1; i < game.getNumPlayers()+1; i++) {

            ImageIcon originalIcon = new ImageIcon(getClass().getResource("P" + i + ".png")); // gets images of dice
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel playerIcon = new JLabel(resizedIcon);

            playerIcon.setBounds(800 + (i - 1) * DISTPLAYERS, 800 + MOVEUP + (i - 1) * DISTPLAYERS, 50, 50);
            playerIcons.add(playerIcon);

            layeredPane.add(playerIcons.get(i - 1), new Integer(3)); // add to layeredPane on lower layer
        }

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
        // Create the game
        int numPlayers = 4;
        int cash = 2000;
        String boardStyle = "Classic";

        GameFactory factory = new CustomGameFactory(numPlayers, cash, boardStyle);
        Game game = new Game(factory);

        Random random = new Random();
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        GUI2 a = new GUI2();
        a.initializeTheBoard(game);
        a.displayDice(dice1, dice2);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // for ok button

        frame.dispose(); // closes UI

    }

}
