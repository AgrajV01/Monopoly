import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener , PlayerObserver {
    private JButton buyCityButton;
    private JButton buyUtilityButton;
    private Point[] boardPositions;
    private static Die die = new Die();
    private boolean isAnimating = false;
    private List<JLabel> playerIcons = new ArrayList<>();
    private JButton button;
    private JFrame frame;
    private ImageIcon image;
    private JLayeredPane layeredPane;
    private JLabel diceLabel1;
    private JLabel diceLabel2;
    private static final int DISTPLAYERS = 20;
    private static final int DISTCARDS = DISTPLAYERS/4;
    private static final int MOVEUP = -80;
    String black = "black.png";

    String _1 = "1.png";
    String _2 = "2.png";
    String _3 = "3.png";
    String _4 = "4.png";
    String _5 = "5.png";
    String _6 = "6.png";

    public GUI2() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1000, 1000));

        frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(layeredPane);
    }

    public void setBackdrop(String fileName) {
        image = new ImageIcon(getClass().getResource(fileName));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(435,415+MOVEUP,130, 170);

        layeredPane.add(pictureLabel, new Integer(4));
    }

    public void setOkButton(Game game) {
        button = new JButton("Roll");
        button.setBounds(460, 550 + MOVEUP, 80, 25);

        layeredPane.add(button, new Integer(5));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAnimating) return;

                die.roll();

                if (diceLabel1 != null) {
                    layeredPane.remove(diceLabel1);
                    diceLabel1 = null;
                }
                if (diceLabel2 != null) {
                    layeredPane.remove(diceLabel2);
                    diceLabel2 = null;
                }

                game.makeMove(die);

                int currentPlayerPosition = game.getPrevPlayer().getPosition();

                JLabel currentPlayerIcon = playerIcons.get(game.getPrevPlayerIndex());

                Point newPosition = new Point(boardPositions[currentPlayerPosition]);
                int yOffset = 70 / game.getNumPlayers();
                int xOffset = yOffset;
                if (currentPlayerPosition < 11)
                    newPosition.y += yOffset * game.getPrevPlayerIndex();
                else if (currentPlayerPosition < 21)
                    newPosition.x -= xOffset * game.getPrevPlayerIndex();
                else if (currentPlayerPosition < 31)
                    newPosition.y -= yOffset * game.getPrevPlayerIndex();
                else
                    newPosition.x += xOffset * game.getPrevPlayerIndex();
                animateMovement(currentPlayerIcon, newPosition, 15);

                // Display new dice values
                displayDice();
                if (buyCityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyCityButton);
                    buyCityButton = null;
                } else if (buyUtilityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyUtilityButton);
                    buyUtilityButton = null;
                }
                layeredPane.remove(button);
                button = null;
                layeredPane.revalidate();
                layeredPane.repaint();
                nextTurn(game);
                frame.repaint();
            }
        });
    }

    private void nextTurn(Game game) {
        Timer timer = new Timer(500, new ActionListener() { // delay for 2 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.getCurrentPlayer().getType().equals("Player")) {
                    die.roll();
                    if (diceLabel1 != null) {
                        layeredPane.remove(diceLabel1);
                        diceLabel1 = null;
                    }
                    if (diceLabel2 != null) {
                        layeredPane.remove(diceLabel2);
                        diceLabel2 = null;
                    }

                    game.makeMove(die);

                    int currentPlayerPosition = game.getPrevPlayer().getPosition();

                    JLabel currentPlayerIcon = playerIcons.get(game.getPrevPlayerIndex());

                    Point newPosition = new Point(boardPositions[currentPlayerPosition]);
                    int yOffset = 70 / game.getNumPlayers();
                    int xOffset = yOffset;
                    if (currentPlayerPosition < 11)
                        newPosition.y += yOffset * game.getPrevPlayerIndex();
                    else if (currentPlayerPosition < 21)
                        newPosition.x -= xOffset * game.getPrevPlayerIndex();
                    else if (currentPlayerPosition < 31)
                        newPosition.y -= yOffset * game.getPrevPlayerIndex();
                    else
                        newPosition.x += xOffset * game.getPrevPlayerIndex();
                    animateMovement(currentPlayerIcon, newPosition, 15);

                    // Display new dice values
                    displayDice();
                    if (game.getPrevPlayer().getOnCity() != null) {
                        System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                        game.getPrevPlayer().buyCity(game.getPrevPlayer().getOnCity());
                        System.out.println("This city is available for purchase at a price of " + game.getPrevPlayer().getOnCity().getPrice());
                        System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                        game.cleanProperty();
                    } else if (game.getPrevPlayer().getOnUtility() != null) {
                        System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                        game.getPrevPlayer().buyUtility(game.getPrevPlayer().getOnUtility());
                        System.out.println("This utility is available for purchase at a price of " + game.getPrevPlayer().getOnUtility().getPrice());
                        System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                        game.cleanProperty();
                    }
                    frame.repaint();
                }
                if (game.getPrevPlayer().getOnCity() != null)
                    setBuyCityButton(game);
                else if (game.getPrevPlayer().getOnUtility() != null)
                    setBuyUtilityButton(game);
                setOkButton(game);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void setBuyCityButton(Game game) {
        buyCityButton = new JButton("Buy City");
        buyCityButton.setBounds(460,520+MOVEUP, 80, 25);

        layeredPane.add(buyCityButton, new Integer(5));

        buyCityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                game.getPrevPlayer().buyCity(game.getPrevPlayer().getOnCity());
                System.out.println("This city is available for purchase at a price of " + game.getPrevPlayer().getOnCity().getPrice());
                System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                game.cleanProperty();
                layeredPane.remove(buyCityButton);
                frame.repaint();
            }
        });
    }

    public void setBuyUtilityButton(Game game) {
        buyUtilityButton = new JButton("Buy Utility");
        buyUtilityButton.setBounds(440,520+MOVEUP, 120, 25);

        layeredPane.add(buyUtilityButton, new Integer(5));

        buyUtilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(game.getPrevPlayer().getName() + " initially has $" + game.getPrevPlayer().getMoney());
                game.getPrevPlayer().buyUtility(game.getPrevPlayer().getOnUtility());
                System.out.println("This utility is available for purchase at a price of " + game.getPrevPlayer().getOnUtility().getPrice());
                System.out.println("After Purchasing, the balance amount you have is " + game.getPrevPlayer().getMoney());
                game.cleanProperty();
                layeredPane.remove(buyUtilityButton);
                frame.repaint();
            }
        });
    }
    public void displayPoints(Point[] points) {
        for (Point point : points) {
            JLabel pointLabel = new JLabel();
            pointLabel.setOpaque(true);
            pointLabel.setBackground(Color.BLACK);
            pointLabel.setBounds(point.x, point.y, 5, 5);

            layeredPane.add(pointLabel, new Integer(5));
        }
    }

    private void setBoardPositions() {
        int cellsPerSide = 10;
        Point[] positions = new Point[cellsPerSide * 4];
        int boardSize = 670;
        int squareSize = boardSize/cellsPerSide;
        int xDisplacement = 210;
        int yDisplacement = xDisplacement + MOVEUP;

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

        boardPositions = positions;
    }


    private void animateMovement(JLabel piece, Point newPosition, int delay) {
        if(isAnimating) return;

        isAnimating = true;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int speed = 60;
            @Override
            public void actionPerformed(ActionEvent e) {
                int dx = newPosition.x - piece.getX();
                int dy = newPosition.y - piece.getY();

                if (dx * dx + dy * dy <= speed * speed) {
                    piece.setLocation(newPosition.x, newPosition.y);
                    timer.stop();
                    isAnimating = false;
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
    public void displayDice() {
        String dice1 = String.valueOf(die.diceOne) + ".png";
        String dice2 = String.valueOf(die.diceTwo) + ".png";

        ImageIcon originalIcon1 = new ImageIcon(getClass().getResource(dice1));
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

        layeredPane.add(diceLabel1, new Integer(5));
        layeredPane.add(diceLabel2, new Integer(5));

        setBackdrop(black);
    }

    public void initializeTheBoard(Game game) {
        System.out.println("initializingTheBoard");

        setBoardPositions();

        ImageIcon icon = new ImageIcon(getClass().getResource("board.png"));
        Image image = icon.getImage();
        image = image.getScaledInstance(820, -1, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        JLabel boardImage = new JLabel(icon);

        boardImage.setBounds(0, 0+MOVEUP, 1000, 1000);

        layeredPane.add(boardImage, new Integer(1));

        setBackdrop(black);

        displayPlayers(game);
        displayCards(5,6);
        displayDice();

        nextTurn(game);

        frame.setVisible(true);
    }

    public void displayPlayers(Game game) {
        System.out.println("adding Players");

        for(int i = 1; i < game.getNumPlayers()+1; i++) {

            ImageIcon originalIcon = new ImageIcon(getClass().getResource("P" + i + ".png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            JLabel playerIcon = new JLabel(resizedIcon);

            playerIcon.setBounds(800 + (i - 1) * DISTPLAYERS, 800 + MOVEUP + (i - 1) * DISTPLAYERS, 50, 50);
            playerIcons.add(playerIcon);

            layeredPane.add(playerIcons.get(i - 1), new Integer(6));
        }

        frame.setVisible(true);
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

    public void onGameOver(){
        image = new ImageIcon(getClass().getResource("bankrupcy.png"));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(435,415+MOVEUP,130, 170);

        layeredPane.add(pictureLabel, new Integer(5));
    }

    public void onPlayerState(Player p){
        String name = p.getName();

        if (name.indexOf('1' ) != -1) {


        }else if (name.indexOf('2' ) != -1){

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        frame.dispose();

    }

}
