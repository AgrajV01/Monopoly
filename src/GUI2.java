import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener {

    private Die die;
    private JPanel panel;
    private List<JLabel> playerIcons = new ArrayList<>();
    private JButton button;
    private JButton rollButton;
    private JLabel label;
    private JFrame frame;
    private ImageIcon image;
    private JLayeredPane layeredPane;
    private JLabel diceLabel1;
    private JLabel diceLabel2;
    private static final int DISTPLAYERS = 20;
    private static final int DISTCARDS = DISTPLAYERS/4;
    private static final int MOVEUP = -80;

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
        pictureLabel.setBounds(435,415+MOVEUP,130, 170); // assuming the size of backdrop is same as the frame

        layeredPane.add(pictureLabel, new Integer(4)); // add to layeredPane on second layer
    }

    public void setOkButton(Game game) {
        button = new JButton("Roll");
        button.setBounds(460,550+MOVEUP, 80, 25);

        layeredPane.add(button, new Integer(5)); // add to layeredPane on the top layer

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
                // Move the current player (you'll need to keep track of whose turn it is)
                game.makeMove(dice1 + dice2);

                // Retrieve the current player's position after making a move
                int currentPlayerPosition = game.getCurrentPlayer().getPosition();
                // Retrieve the player's piece
                JLabel currentPlayerIcon = playerIcons.get(game.getCurrentPlayerIndex());
                // Define an array of points for the board positions
                Point[] boardPositions = getBoardPositions();
                // Animate the piece to the new position
                animateMovement(currentPlayerIcon, boardPositions[currentPlayerPosition], 50);

                // Display new dice values
                displayDice(dice1, dice2);
                // Refresh the frame
                frame.repaint();
            }
        });

    }

    private Point[] getBoardPositions() {
        Point[] boardPositions = new Point[40];
        int length = 100; // Assuming your board image is 800px wide and tall
        int offset = 50; // Offset from the border of the board
        int cellWidth = length / 5; // Enlarging each cell

        for (int i = 0; i < 10; i++) {
            // Top row (0 to 9)
            boardPositions[i] = new Point(offset + i * cellWidth, offset);
            // Left column (10 to 19)
            boardPositions[i + 10] = new Point(offset, offset + i * cellWidth);
            // Bottom row (20 to 29)
            boardPositions[i + 20] = new Point(length - offset - i * cellWidth, length - offset);
            // Right column (30 to 39)
            boardPositions[i + 30] = new Point(length - offset, length - offset - i * cellWidth);
        }

        return boardPositions;
    }

    private void animateMovement(JLabel piece, Point newPosition, int delay) {
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int speed = 20; // Move 5 pixels at a time
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if piece has reached horizontal position
                if (piece.getX() != newPosition.getX()) {
                    // If not, move horizontally
                    int nextX = piece.getX() + (newPosition.getX() > piece.getX() ? speed : -speed);
                    // If we overshoot, correct the position
                    if (newPosition.getX() > piece.getX() ? nextX > newPosition.getX() : nextX < newPosition.getX()) {
                        nextX = (int) newPosition.getX();
                    }
                    piece.setLocation(nextX, piece.getY());
                } else if (piece.getY() != newPosition.getY()) {
                    // If horizontal position has been reached, move vertically
                    int nextY = piece.getY() + (newPosition.getY() > piece.getY() ? speed : -speed);
                    // If we overshoot, correct the position
                    if (newPosition.getY() > piece.getY() ? nextY > newPosition.getY() : nextY < newPosition.getY()) {
                        nextY = (int) newPosition.getY();
                    }
                    piece.setLocation(piece.getX(), nextY);
                } else {
                    // If both x and y coordinates have been reached, stop timer
                    timer.stop();
                }
            }
        });
        timer.start();
    }


    /*public void displayJail(Game game) { // uses backdrop and button decorations

        //panel.removeAll(); // removes previous panel components
        setOkButton(game);
        setBackdrop(jailFile);  // note: backdrop must always be set last for visibility

        frame.add(panel);
        frame.setVisible(true); // must come at the very end


    }*/

    // binary options buttons function (Yes/No)
    // display question label



    public void displayDice(int firstDice, int secondDice) {

        String dice1 = String.valueOf(firstDice) + ".png"; // create relevant file names
        String dice2 = String.valueOf(secondDice) + ".png";

        System.out.println(dice1);
        System.out.println(dice2);

        ImageIcon originalIcon1 = new ImageIcon(getClass().getResource(dice1)); // gets images of dice
        ImageIcon originalIcon2  = new ImageIcon(getClass().getResource(dice2));
        Image originalImage1 = originalIcon1.getImage();
        Image originalImage2 = originalIcon2.getImage();
        Image resizedImage1 = originalImage1.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
        Image resizedImage2 = originalImage2.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon1 = new ImageIcon(resizedImage1);
        ImageIcon resizedIcon2 = new ImageIcon(resizedImage2);
        diceLabel1 = new JLabel(resizedIcon1);
        diceLabel2 = new JLabel(resizedIcon2);

        diceLabel1.setBounds(445, 420+MOVEUP, 50, 50);
        diceLabel2.setBounds(505, 420+MOVEUP, 50, 50);

        layeredPane.add(diceLabel1, new Integer(5)); // add to layeredPane on higher layer
        layeredPane.add(diceLabel2, new Integer(5)); // add to layeredPane on higher layer

        setBackdrop(black);

    }

    public void initializeTheBoard(Game game) {
        System.out.println("initializingTheBoard");

        ImageIcon image1 = new ImageIcon(getClass().getResource("board.png")); // gets images of dice
        JLabel boardImage = new JLabel(image1);

        boardImage.setBounds(0, 0+MOVEUP, 1000, 1000);

        layeredPane.add(boardImage, new Integer(1)); // add to layeredPane on lower layer

        setOkButton(game);

        setBackdrop(black);

        displayPlayers(game);
        displayCards(5,6);

        frame.setVisible(true); // must come at the very end
    }

    public void displayPlayers(Game game) {
        System.out.println("adding Players");

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

    public void displayCards(int chance, int chest) {
        System.out.println("adding cards");

        for(int i = 1; i < chance+1; i++) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("chance" + ".png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(320, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel chanceIcon = new JLabel(resizedIcon);

            chanceIcon.setBounds(460 + (i - 1) * DISTCARDS, 460 + MOVEUP + (i - 1) * DISTCARDS, 320, 320);

            layeredPane.add(chanceIcon, new Integer(4));
        }

        for(int i = 1; i < chest+1; i++) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("chest" + ".png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(250, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel chestIcon = new JLabel(resizedIcon);

            chestIcon.setBounds(220 + (i - 1) * DISTCARDS, 220 + MOVEUP + (i - 1) * DISTCARDS, 250, 250);

            layeredPane.add(chestIcon, new Integer(4));
        }

        frame.setVisible(true);
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
