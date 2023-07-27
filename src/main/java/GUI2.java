import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GUI2 implements ActionListener , PlayerObserver {



    private int movesMade = 0;
    private JButton buyUtilityButton, endTurnButton, buyCityButton;
    private Point[] boardPositions;
    private static Die die = new Die();
    private boolean isAnimating = false;
    private boolean gameStarted = false;
    private List<JLabel> playerIcons = new ArrayList<>();
    private JButton button;
    private JFrame frame;
    private ImageIcon image;
    private JLayeredPane layeredPane;
    private JLabel diceLabel1;
    private JLabel diceLabel2;

    private PlayerStats p1;
    private PlayerStats p2;
    private PlayerStats p3;
    private PlayerStats p4;

//    private JLabel money1; // for player stats
//    private JLabel money2;
//    private JLabel money3;
//    private JLabel money4;
//
//    private JLabel property1;
//    private JLabel property2;
//    private JLabel property3;
//    private JLabel property4;
    private PlayerStats PlayerMenuPrototype;


    private JTextArea text;;
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


    public GUI2() {// initializes panel and frame
        layeredPane = new JLayeredPane(); // create the layered pane
        layeredPane.setPreferredSize(new Dimension(1500, 1000)); // set size

        frame = new JFrame(); // creating frame to add panel onto
        frame.setSize(1500, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(layeredPane);
    }
/*
    public void setMoney1(int money) {

        String moneyString = String.valueOf(money);
        money1.setText(moneyString);

    }

    public void setMoney2(int money) {

        String moneyString = String.valueOf(money);
        money2.setText(moneyString);

    }

    public void setMoney3(int money) {

        String moneyString = String.valueOf(money);
        money3.setText(moneyString);

    }

    public void setMoney4(int money) {

        String moneyString = String.valueOf(money);
        money4.setText(moneyString);

    }

    public void setProperty1(int size) {


        String numProperties = String.valueOf(size);
        property1.setText(numProperties);

    }

    public void setProperty2(int size) {


        String numProperties = String.valueOf(size);
        property2.setText(numProperties);

    }

    public void setProperty3(int size) {


        String numProperties = String.valueOf(size);
        property3.setText(numProperties);

    }

    public void setProperty4(int size) {


        String numProperties = String.valueOf(size);
        property4.setText(numProperties);

    }

 */


    public void setBackdrop(String fileName) {
        image = new ImageIcon(getClass().getResource(fileName));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(435,415+MOVEUP,130, 170);

        layeredPane.add(pictureLabel, new Integer(4));
    }

    public void setOkButton(Game game) {
        // clears text box
        //getTextArea().setText("");
        movesMade++;

        JButton quit = new JButton("Quit");
        quit.setBounds(710, 135, 70, 50);
        layeredPane.add(quit, new Integer(5));
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

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
                /*
                if (buyCityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyCityButton);
                    buyCityButton = null;
                } else if (buyUtilityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyUtilityButton);
                    buyUtilityButton = null;
                }

                 */
                layeredPane.remove(button);
                button = null;
                layeredPane.revalidate();
                layeredPane.repaint();

                // TO DO: create setButtons function instead of checking for each individually

                // if current player is not AI
                if (game.getPrevPlayer().getType().equals("Player")) {

                    // city and utility buttons will be set accordingly
                    if (game.getPrevPlayer().getOnCity() != null)
                        setBuyCityButton(game);
                    else if (game.getPrevPlayer().getOnUtility() != null)
                        setBuyUtilityButton(game);

                    // end button will only be created if current player is not AI
                    setEndTurnButton(game);


                }
                else {
                    layeredPane.remove(endTurnButton);
                    endTurnButton = null;
                    layeredPane.revalidate();
                    frame.repaint();
                    nextTurn(game);
                }
            }
        });
    }

    private void nextTurn(Game game) {
        // clears text box
        //getTextArea().setText("");

        Timer timer = new Timer(1200, new ActionListener() { // delay for 2 seconds
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!game.getCurrentPlayer().getType().equals("Player")) {
                    movesMade++;
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

                    // TO CHANGE (AI decision making)
                    /* REPLACEMENT CODE
                    if (game.getPrevPlayer().getOnCity() != null) {
                        game.getPrevPlayer().makeDecision();
                    }

                     */
                    
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
                setOkButton(game);
            }
        });

        timer.setRepeats(false);
        if (movesMade > 0) timer.start();
        else {
            setOkButton(game);
        }
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
                getTextArea().append("\n" + game.getPrevPlayer().getName() + " has purchased " + game.getPrevPlayer().getOnCity().name + " for " + game.getPrevPlayer().getOnCity().getPrice() + "$");
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
                getTextArea().append("\n" + game.getPrevPlayer().getName() + " has purchased " + game.getPrevPlayer().getOnUtility().name + " for " + game.getPrevPlayer().getOnUtility().getPrice() + "$");
                game.cleanProperty();
                layeredPane.remove(buyUtilityButton);
                frame.repaint();
            }
        });
    }


    public void setEndTurnButton(Game game) {
        endTurnButton = new JButton("End turn");
        endTurnButton.setBounds(440, 550 + MOVEUP, 120, 25);

        layeredPane.add(endTurnButton, new Integer(5));
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layeredPane.remove(endTurnButton);

                if (buyCityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyCityButton);
                    buyCityButton = null;
                } else if (buyUtilityButton != null) {
                    game.cleanProperty();
                    layeredPane.remove(buyUtilityButton);
                    buyUtilityButton = null;
                }

                endTurnButton = null;
                layeredPane.revalidate();
                frame.repaint();
                nextTurn(game);
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
            int speed = 10;
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

        layeredPane.add(boardImage, new Integer(1)); // 1 is lowest layer

        setBackdrop(black);

        displayBackground();
        displayPlayers(game);
        displayStats(game);
        displayMan(game);
        displayTextBox(game);
        displayTextArea();
        displayCards(5,6);
        displayDice();

        game.subscribeToPlayers(this); // for updating player stats

        //setOkButton(game);
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


    public void displayStats(Game game) {
        System.out.println("initializingTheStatsDisplay");

/*
        money1 = new JLabel(""); // Setting positions for money labels
        money2 = new JLabel("hi");
        money3 = new JLabel("hi");
        money4 = new JLabel("hi");

        property1 = new JLabel("hi"); // Setting position for property number labels
        property2 = new JLabel("hi");
        property3 = new JLabel("hi");
        property4 = new JLabel("hi");

 */

        for(int i = 1; i < game.getNumPlayers()+1; i++) {

            int hShift = 0; // for display positioning
            int vShift = 0;

            // set bounds for all player menus
            PlayerMenuPrototype = new PlayerStats(i);

            if( i > 2 ) // for setting display position
                vShift = 250;
            else vShift = 0;

            if( i == 2 || i == 4)
                hShift = 200;
            else hShift = 0;

            PlayerMenuPrototype.setStatDisplay(950 + hShift, -50 + vShift, 250, 400);
            layeredPane.add(PlayerMenuPrototype.statDisplay, new Integer(6)); // add to layeredPane on lower layer

            if(i == 1) { // set money1 and so on
                p1 = PlayerMenuPrototype.clone();
                // money1.setBounds(1100 + hShift, 100+vShift, 100, 100);
                // layeredPane.add(money1, new Integer(7));
                // property1.setBounds(1120 + hShift, 155+vShift, 100, 100);
                // layeredPane.add(property1, new Integer(7));
                p1.setMoneyBounds(1100 + hShift, 100+vShift, 100, 100 );
                layeredPane.add(p1.getMoneyLabel(), new Integer(7));
                p1.setPropertyBounds(1120 + hShift, 155+vShift, 100, 100);
                layeredPane.add(p1.getPropertyLabel(), new Integer(7));
            }

            else if(i == 2) { // set money1 and so on
                p2 = PlayerMenuPrototype.clone();
//                money2.setBounds(1100 + hShift, 100+vShift, 100, 100);
//                layeredPane.add(money2, new Integer(7));
//                property2.setBounds(1120 + hShift, 155+vShift, 100, 100);
//                layeredPane.add(property2, new Integer(7));
                p2.setMoneyBounds(1100 + hShift, 100+vShift, 100, 100 );
                layeredPane.add(p2.getMoneyLabel(), new Integer(7));
                p2.setPropertyBounds(1120 + hShift, 155+vShift, 100, 100);
                layeredPane.add(p2.getPropertyLabel(), new Integer(7));
            }

            else if(i == 3) { // set money1 and so on
                 p3 = PlayerMenuPrototype.clone();
//                money3.setBounds(1100 + hShift, 100+vShift, 100, 100);
//                layeredPane.add(money3, new Integer(7));
//                property3.setBounds(1120 + hShift, 155+vShift, 100, 100);
//                layeredPane.add(property3, new Integer(7));
                p3.setMoneyBounds(1100 + hShift, 100+vShift, 100, 100 );
                layeredPane.add(p3.getMoneyLabel(), new Integer(7));
                p3.setPropertyBounds(1120 + hShift, 155+vShift, 100, 100);
                layeredPane.add(p3.getPropertyLabel(), new Integer(7));
            }

            else if(i == 4) { // set money1 and so on
                 p4 = PlayerMenuPrototype.clone();
//                money4.setBounds(1100 + hShift, 100+vShift, 100, 100);
//                layeredPane.add(money4, new Integer(7));
//                property4.setBounds(1120 + hShift, 155+vShift, 100, 100);
//                layeredPane.add(property4, new Integer(7));
                p4.setMoneyBounds(1100 + hShift, 100+vShift, 100, 100 );
                layeredPane.add(p4.getMoneyLabel(), new Integer(7));
                p4.setPropertyBounds(1120 + hShift, 155+vShift, 100, 100);
                layeredPane.add(p4.getPropertyLabel(), new Integer(7));
            }
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


    public void displayMan(Game game) {
        System.out.println("waking up the monopoly man");

        URL url = getClass().getResource("man.gif");
        ImageIcon originalIcon = new ImageIcon(url); // get StatDisplay image
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(150, 200, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel manDisplay = new JLabel(resizedIcon);
        manDisplay.setBounds(1000, 450, 700, 500);

        //same thing for text box (figure this out tomorrow)
        layeredPane.add(manDisplay, new Integer(5)); // add to layeredPane on lower layer

        ImageIcon originalIcon1 = new ImageIcon(getClass().getResource("text.png")); // get StatDisplay image
        Image originalImage1 = originalIcon.getImage();
        Image resizedImage1 = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon1 = new ImageIcon(resizedImage);

        JLabel textDisplay = new JLabel(resizedIcon);

        textDisplay.setBounds(900, 400, 700, 500);

        // now for text
        frame.setVisible(true);
    }

    public void displayTextBox(Game game) { // textbox picture

        System.out.println("placing text box");

        URL url = getClass().getResource("text.png");
        ImageIcon originalIcon = new ImageIcon(url); // get StatDisplay image
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(350, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel textDisplay = new JLabel(resizedIcon);

        textDisplay.setBounds(750, 400, 700, 500);

        //same thing for text box (figure this out tomorrow)
        layeredPane.add(textDisplay, new Integer(5)); // add to layeredPane on lower layer
    }

    public void displayTextArea() { // text output

        text = new JTextArea();
        text.setBounds(950,555, 300, 125);

        text.setText("Press Roll to Begin!");

        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        layeredPane.add(text, new Integer(6)); // add to layeredPane on lower layer
        frame.setVisible(true);
    }

    public JTextArea getTextArea() { // for passing private data member
        return text;
    }

    public void displayBackground() {

        System.out.println("setting backdrop");

        URL url = getClass().getResource("background.jpeg");
        ImageIcon originalIcon = new ImageIcon(url); // get StatDisplay image
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(1500, 1000, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel backDisplay = new JLabel(resizedIcon);

        backDisplay.setBounds(0, 0, 1500, 1000);

        layeredPane.add(backDisplay, new Integer(1)); // add to layeredPane on lower layer

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
        setBackdrop(black)
        frame.setVisible(true); // must come at the very end
    }*/

    public void onGameOver(){
        image = new ImageIcon(getClass().getResource("bankrupcy.png"));
        JLabel pictureLabel = new JLabel(image);
        pictureLabel.setBounds(435,415+MOVEUP,130, 170);

        layeredPane.add(pictureLabel, new Integer(5));
    }

    public void onPlayerState(Player p){
        String name = p.getName();

        if (name.contains("1")) {
            p1.setMoneyLabel(p.getMoney());
            p1.setPropertyLabel(p.getOwnedCities().size() + p.getOwnedUtilities().size());
            //setMoney1(p.getMoney());
            //setProperty1(p.getOwnedCities().size() + p.getOwnedUtilities().size());

        }
        else if (name.contains("2")){
            p2.setMoneyLabel(p.getMoney());
            p2.setPropertyLabel(p.getOwnedCities().size() + p.getOwnedUtilities().size());
//            setMoney2(p.getMoney());
//            setProperty2(p.getOwnedCities().size() + p.getOwnedUtilities().size());
        }
        else if (name.contains("3")){
            p3.setMoneyLabel(p.getMoney());
            p3.setPropertyLabel(p.getOwnedCities().size() + p.getOwnedUtilities().size());
//            setMoney3(p.getMoney());
//            setProperty3(p.getOwnedCities().size() + p.getOwnedUtilities().size());
        }
        else {
            p4.setMoneyLabel(p.getMoney());
            p4.setPropertyLabel(p.getOwnedCities().size() + p.getOwnedUtilities().size());
//            setMoney4(p.getMoney());
//            setProperty4(p.getOwnedCities().size() + p.getOwnedUtilities().size());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
    }

}
