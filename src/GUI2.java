import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener , PlayerObserver {
    private JButton buyCityButton;  // the "Buy" button
    private JButton buyUtilityButton;  // the "Buy" button
    private Point[] boardPositions;
    private static Die die = new Die();
    private boolean isAnimating = false;
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
                if(isAnimating) return;
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
                Point newPosition = boardPositions[currentPlayerPosition];
                if(currentPlayerPosition < 10)
                    newPosition.y += 20*game.getPrevPlayerIndex();
                else if(currentPlayerPosition < 20)
                    newPosition.x -= 10*game.getPrevPlayerIndex();
                else if(currentPlayerPosition < 30)
                    newPosition.y -= 20*game.getPrevPlayerIndex();
                else
                    newPosition.x += 10*game.getPrevPlayerIndex();
                animateMovement(currentPlayerIcon, newPosition, 15);

                // Display new dice values
                displayDice();
                if (buyCityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyCityButton);
                    buyCityButton = null;  // avoid holding onto a button that's been removed
                    layeredPane.revalidate();  // recheck the layout
                    layeredPane.repaint();  // repaint the panel
                }
                else if (buyUtilityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyUtilityButton);
                    buyUtilityButton = null;  // avoid holding onto a button that's been removed
                    layeredPane.revalidate();  // recheck the layout
                    layeredPane.repaint();  // repaint the panel
                }
                if(game.getPrevPlayer().getOnCity() != null)
                    setBuyCityButton(game);
                else if(game.getPrevPlayer().getOnUtility() != null)
                    setBuyUtilityButton(game);
                frame.repaint();
            }
        });
    }

    public void setBuyCityButton(Game game) {
        buyCityButton = new JButton("Buy City");
        buyCityButton.setBounds(460,520+MOVEUP, 80, 25);

        layeredPane.add(buyCityButton, new Integer(5)); // add to layeredPane on the top layer

        buyCityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                game.getPrevPlayer().buyCity(game.getPrevPlayer().getOnCity());
                System.out.println("This city is available for purchase at a price of " + game.getPrevPlayer().getOnCity().getPrice());
                System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                game.cleanProperty();
                layeredPane.remove(buyCityButton);
                // Refresh the frame
                frame.repaint();
            }
        });
    }

    public void setBuyUtilityButton(Game game) {
        buyUtilityButton = new JButton("Buy Utility");
        buyUtilityButton.setBounds(440,520+MOVEUP, 120, 25);

        layeredPane.add(buyUtilityButton, new Integer(5)); // add to layeredPane on the top layer

        buyUtilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                game.getPrevPlayer().buyUtility(game.getPrevPlayer().getOnUtility());
                System.out.println("This utility is available for purchase at a price of " + game.getPrevPlayer().getOnUtility().getPrice());
                System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                game.cleanProperty();
                layeredPane.remove(buyUtilityButton);
                // Refresh the frame
                frame.repaint();
            }
        });
    }
    public void displayPoints(Point[] points) {
        for (Point point : points) {
            // Create a JLabel to represent the point
            JLabel pointLabel = new JLabel();
            pointLabel.setOpaque(true);
            pointLabel.setBackground(Color.BLACK);
            pointLabel.setBounds(point.x, point.y, 5, 5);

            // Add the point label to the layeredPane on the top layer
            layeredPane.add(pointLabel, new Integer(5));
        }
    }

    private void setBoardPositions() {
        int cellsPerSide = 10;
        Point[] positions = new Point[cellsPerSide * 4];
        int boardSize = 670;
        int squareSize = boardSize/cellsPerSide; // Size of a square (900/10 assuming board height and width is 900)
        int xDisplacement = 210;
        int yDisplacement = xDisplacement + MOVEUP;
        int displacement = 80;

        // Set up positions along the bottom of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[i] = new Point(boardSize - (i + 1) * squareSize + xDisplacement, boardSize - squareSize + yDisplacement);
        }
        // Set up positions along the right side of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[cellsPerSide + i] = new Point(xDisplacement - squareSize, boardSize - (i + 1) * squareSize + yDisplacement);
        }
        // Set up positions along the top of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[2 * cellsPerSide + i] = new Point((i - 1)  * squareSize + xDisplacement, yDisplacement - squareSize);
        }
        // Set up positions along the left side of the board
        for (int i = 0; i < cellsPerSide; i++) {
            positions[3 * cellsPerSide + i] = new Point(boardSize - squareSize + xDisplacement,(i - 1)  * squareSize + yDisplacement);
        }
        //displayPoints(positions);
        boardPositions = positions;
    }


    private void animateMovement(JLabel piece, Point newPosition, int delay) {
        if(isAnimating) return; // If an animation is already playing, don't start a new one.

        isAnimating = true; // Animation starts

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int speed = 15; // Adjust the speed accordingly
            @Override
            public void actionPerformed(ActionEvent e) {
                int dx = newPosition.x - piece.getX();
                int dy = newPosition.y - piece.getY();

                if (dx * dx + dy * dy <= speed * speed) {
                    piece.setLocation(newPosition.x, newPosition.y);
                    timer.stop();
                    isAnimating = false; // Animation ends
                    return;
                }

                double angle = Math.atan2(dy, dx);
                int nextX = piece.getX() + (int) (speed * Math.cos(angle));
                int nextY = piece.getY() + (int) (speed * Math.sin(angle));
                piece.setLocation(nextX, nextY);
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

        //System.out.println(dice1);
        //System.out.println(dice2);

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

    /*public void rollDice() {

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


    }*/

    public void onGameOver(){
        image = new ImageIcon(getClass().getResource("bankrupcy.png"));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(435,415+MOVEUP,130, 170); // assuming the size of backdrop is same as the frame

        layeredPane.add(pictureLabel, new Integer(5)); // add to layeredPane on second layer
    }

    public void onPlayerState(Player p){
        String name = p.getName();

        if (name.indexOf('1' ) != -1) {
            // update UI for player one

        }else if (name.indexOf('2' ) != -1){
            // update UI for player two
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) { // for ok button

        frame.dispose(); // closes UI

    }

}
