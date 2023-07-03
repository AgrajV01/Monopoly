import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener {
    private Point[] boardPositions;
    private static Die die = new Die();
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
                die.roll();
                layeredPane.remove(diceLabel1);
                layeredPane.remove(diceLabel2);
                // Move the current player (you'll need to keep track of whose turn it is)
                game.makeMove(die);

                // Retrieve the current player's position after making a move
                int currentPlayerPosition = game.getPrevPlayer().getPosition();
                // Retrieve the player's piece
                JLabel currentPlayerIcon = playerIcons.get(game.getPrevPlayerIndex());
                // Animate the piece to the new position
                animateMovement(currentPlayerIcon, boardPositions[currentPlayerPosition], 50);

                // Display new dice values
                displayDice();
                // Refresh the frame
                frame.repaint();
            }
        });
    }

    private void setBoardPositions() {
        int cellsPerSide = 10;
        Point[] positions = new Point[cellsPerSide * 4];
        int boardSize = 800;
        int squareSize = boardSize/cellsPerSide; // Size of a square (900/10 assuming board height and width is 900)
        int xDisplacement = 100;
        int yDisplacement = 20;

        // Set up positions along the bottom of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[i] = new Point(boardSize - (i + 1) * squareSize + xDisplacement, boardSize - squareSize + yDisplacement);
        }
        // Set up positions along the right side of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[cellsPerSide + i] = new Point(0 + xDisplacement, boardSize - (i + 1) * squareSize + yDisplacement);
        }
        // Set up positions along the top of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[2 * cellsPerSide + i] = new Point(i  * squareSize + xDisplacement, 0 + yDisplacement);
        }
        // Set up positions along the left side of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[3 * cellsPerSide + i] = new Point(boardSize - squareSize + xDisplacement,i  * squareSize + yDisplacement);
        }
        boardPositions = positions;
    }


    private void animateMovement(JLabel piece, Point newPosition, int delay) {
        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int speed = 20; // Move 5 pixels at a time
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if piece has reached horizontal position
                die.roll();
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



    public void displayDice() {
        String dice1 = String.valueOf(die.diceOne) + ".png"; // create relevant file names
        String dice2 = String.valueOf(die.diceTwo) + ".png";

        System.out.println(dice1);
        System.out.println(dice2);

        ImageIcon originalIcon1 = new ImageIcon(getClass().getResource(dice1)); // gts images of dice
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

        setBoardPositions();

        ImageIcon icon = new ImageIcon(getClass().getResource("board.png")); // gets images of dice
        Image image = icon.getImage();
        image = image.getScaledInstance(820, -1, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        JLabel boardImage = new JLabel(icon);

        boardImage.setBounds(0, 0+MOVEUP, 1000, 1000);

        layeredPane.add(boardImage, new Integer(1)); // add to layeredPane on lower layer

        setOkButton(game);

        setBackdrop(black);

        displayPlayers(game);
        displayCards(5,6);
        displayDice();

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

            layeredPane.add(playerIcons.get(i - 1), new Integer(6)); // add to layeredPane on lower layer
        }

        frame.setVisible(true); // must come at the very end
    }

    public void displayCards(int chance, int chest) {
        System.out.println("adding cards");

        for(int i = 1; i < chance+1; i++) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("chance" + ".png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel chanceIcon = new JLabel(resizedIcon);

            chanceIcon.setBounds(510 + (i - 1) * DISTCARDS, 510 + MOVEUP + (i - 1) * DISTCARDS, 320, 320);

            layeredPane.add(chanceIcon, new Integer(4));
        }

        for(int i = 1; i < chest+1; i++) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("chest" + ".png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel chestIcon = new JLabel(resizedIcon);

            chestIcon.setBounds(170 + (i - 1) * DISTCARDS, 170 + MOVEUP + (i - 1) * DISTCARDS, 250, 250);

            layeredPane.add(chestIcon, new Integer(4));
        }

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // for ok button

        frame.dispose(); // closes UI

    }

}
