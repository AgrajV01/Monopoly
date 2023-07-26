import java.util.Random;

/**
 * The Main class serves as the entry point for starting the Monopoly game.
 * It creates an instance of the game with custom settings and initializes the user interface.
 */

private JButton newGameButton, loadGameButton, settingsButton, rulesButton, quitButton;
private JFrame frame;
private JLayeredPane layeredPane;

GameFactory default(GUI2 a){
    int numPlayers = 4;
    int numOfAiPlayers = 2;
    int cash = 2000;
    String boardStyle = "Classic";
    return new CustomGameFactory(numPlayers, numOfAiPlayers, cash, boardStyle, a);
}

public void setNewGameButton(GameFactory factory, GUI2 a) {
    newGameButton = new JButton("New Game");
    newGameButton.setBounds(400,400, 300, 100);

    layeredPane.add(newGameButton, new Integer(1));

    buyCityButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Game game = new Game(factory, a);
            Random random = new Random();
            a.initializeTheBoard(game);
            game.subscribeToPlayers(a);
        }
    }
}
 
public void setLoadGameButton(GameFactory factory, GUI2 a) {
    loadGameButton = new JButton("Load Game");
    loadGameButton.setBounds(400,500, 300, 100);

    layeredPane.add(loadGameButton, new Integer(1));

    buyCityButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //To Do
        }
    }
}
                                    
public void setSettingsButton(GameFactory factory) {
    settingsButton = new JButton("Settings");
    settingsButton.setBounds(400,600, 300, 100);

    layeredPane.add(settingsButton, new Integer(1));

    buyCityButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //To Do
        }
    }
}
                                    
public void setRulesButton() {
    rulesButton = new JButton("Rules");
    rulesButton.setBounds(400,700, 300, 100);

    layeredPane.add(rulesButton, new Integer(1));

    rulesButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showRulesDialog();
        }
    });
}

public void showRulesDialog() {
    JDialog rulesDialog = new JDialog();
    rulesDialog.setTitle("Monopoly Rules");
    rulesDialog.setLayout(null);
    rulesDialog.setSize(800, 600); 
    rulesDialog.setLocationRelativeTo(null);

    JTextArea rulesText = new JTextArea();
    rulesText.setText("Monopoly rules go here...");
    rulesText.setBounds(10, 10, 770, 480);
    rulesText.setWrapStyleWord(true);
    rulesText.setLineWrap(true);
    rulesText.setCaretPosition(0);
    rulesText.setEditable(false);
    JScrollPane rulesScrollPane = new JScrollPane(rulesText);

    JButton gotItButton = new JButton("Got It!");
    gotItButton.setBounds(350,500, 100, 50);
    gotItButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            rulesDialog.dispose();
        }
    });

    rulesDialog.add(rulesScrollPane);
    rulesDialog.add(gotItButton);
    rulesDialog.setVisible(true);
}
                                    
public void setQuitButton() {
    JButton quit = new JButton("Quit");
    quit.setBounds(400,600, 300, 100);
    layeredPane.add(quit, new Integer(1));
    quit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });
}
                                    
public void setQuitButton() {
    quitButton = new JButton("Quit");
    quitButton.setBounds(400,800, 300, 100);

    layeredPane.add(quitButton, new Integer(1));

    rulesButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });
}

public class Main {
    public static void main(String[] args) {
        GUI2 a = new GUI2(); // must add to the factory
        GameFactory factory = default(GUI2 a);
        setNewGameButton(factory, a);
        setLoadGameButton(factory, a);
        setSettingsButton(factory);
        setRulesButton();
        setQuitButton();
    }
}
