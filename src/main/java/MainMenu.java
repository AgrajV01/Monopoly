import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MainMenu {
    private static JButton newGameButton;
    private static JButton loadGameButton;
    private static JButton settingsButton;
    private static JButton rulesButton;
    private static JButton quitButton;
    private static JFrame frame;
    private static JLayeredPane layeredPane;
    private JLabel bgLabel;
    private GameFactory factory;

    MainMenu(boolean tutor) {
        layeredPane = new JLayeredPane();

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("MainMenu.png"));

        if (bgIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error loading image.");
        }

        Image bgImage = bgIcon.getImage();
        int imgWidth = (int)(bgImage.getWidth(null) * 1.2);
        int imgHeight = (int)(bgImage.getHeight(null) * 1.2);

        layeredPane.setPreferredSize(new Dimension(imgWidth, imgHeight));

        bgLabel = new JLabel(new ImageIcon(bgImage));
        bgLabel.setBounds(0, 0, imgWidth, imgHeight);
        layeredPane.add(bgLabel, new Integer(0));

        frame = new JFrame();
        frame.setSize(imgWidth, imgHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);

        GUI2 a = new GUI2(true);
        factory = new CustomGameFactory(4,2,2000,"Classic", a);
        //defaultSettings();

        setNewGameButton(a);
        setLoadGameButton(a);
        setSettingsButton();
        setRulesButton();
        setQuitButton();

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);

        buttonPanel.add(newGameButton, gbc);
        buttonPanel.add(loadGameButton, gbc);
        buttonPanel.add(settingsButton, gbc);
        buttonPanel.add(rulesButton, gbc);
        buttonPanel.add(quitButton, gbc);

        layeredPane.add(buttonPanel, new Integer(1));

        frame.add(layeredPane, BorderLayout.CENTER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buttonPanel.setBounds(frame.getWidth() / 4, frame.getHeight() / 4, frame.getWidth() / 2, frame.getHeight() / 2);
                bgLabel.setIcon(new ImageIcon(bgImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH)));
                bgLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            }
        });

        frame.setVisible(true);
    }

    void defaultSettings() {
        factory.setNumPlayers(4);
        factory.setNumOfAiPlayers(2);
        factory.setCash(2000);
        factory.setBoardStyle("Classic");
    }

    public void setNewGameButton(GUI2 a) {
        newGameButton = new JButton("New Game");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            Game game = new Game(factory, a);
            a.initializeTheBoard(game, factory);
            game.subscribeToPlayers(a);

            frame.dispose();
        });
    }

    public void setLoadGameButton(GUI2 a) {
        loadGameButton = new JButton("Load Game");
        styleButton(loadGameButton);
        loadGameButton.addActionListener(e -> {
            // To Do
            frame.dispose();
        });
    }

    public void setSettingsButton() {
        settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        settingsButton.addActionListener(e -> showSettingsDialog());
    }

    public void showSettingsDialog() {
        System.out.println("boardStyleSelection: " + factory.getBoardStyle());
        JDialog settingsDialog = new JDialog();
        settingsDialog.setTitle("Game Settings");
        settingsDialog.setLayout(new BorderLayout());
        settingsDialog.setSize(800, 600);
        settingsDialog.setLocationRelativeTo(null);

        Color darkerBlue = new Color(0, 90, 160);
        Color inputBackground = new Color(0, 102, 204);
        Color inputForeground = Color.WHITE;
        Color focusColor = Color.GRAY;

        Font standardFont = new Font("Dialog", Font.PLAIN, 16);
        Font labelFont = new Font("Dialog", Font.BOLD, 20);

        int currentCash = factory.getCash();
        String currentBoardStyle = factory.getBoardStyle();
        int currentNumOfAiPlayers = factory.getNumOfAiPlayers();

        String[] boardStyles = {
                "Classic", "Bass-Fishing", "Breaking-Bad", "Chtulhu", "Ukraine",
                "Gay", "Ketchup", "Sponge Bob", "Ted Lasso", "David Bowie"
        };

        JComboBox<String> boardStyleSelection = new JComboBox<>(boardStyles);
        boardStyleSelection.setSelectedItem(currentBoardStyle);
        boardStyleSelection.setFont(standardFont);
        boardStyleSelection.setBackground(inputBackground);
        boardStyleSelection.setForeground(inputForeground);
        boardStyleSelection.setPreferredSize(new Dimension(200, 30));
        boardStyleSelection.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                boardStyleSelection.setBackground(focusColor);
            }
            public void focusLost(FocusEvent e) {
                boardStyleSelection.setBackground(inputBackground);
            }
        });

        JTextField cashInput = new JTextField(String.valueOf(currentCash));
        cashInput.setFont(standardFont);
        cashInput.setBackground(inputBackground);
        cashInput.setForeground(inputForeground);
        cashInput.setPreferredSize(new Dimension(200, 30));
        cashInput.setBorder(BorderFactory.createRaisedBevelBorder());
        cashInput.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                cashInput.setBackground(focusColor);
            }
            public void focusLost(FocusEvent e) {
                cashInput.setBackground(inputBackground);
            }
        });

        Integer[] aiPlayerOptions = {0, 1, 2, 3, 4};  // AI players up to 4
        JComboBox<Integer> aiPlayersSelector = new JComboBox<>(aiPlayerOptions);
        aiPlayersSelector.setSelectedItem(currentNumOfAiPlayers);
        aiPlayersSelector.setFont(standardFont);
        aiPlayersSelector.setBackground(inputBackground);
        aiPlayersSelector.setForeground(inputForeground);
        aiPlayersSelector.setPreferredSize(new Dimension(200, 30));
        aiPlayersSelector.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                aiPlayersSelector.setBackground(focusColor);
            }
            public void focusLost(FocusEvent e) {
                aiPlayersSelector.setBackground(inputBackground);
            }
        });

        JLabel boardStyleLabel = new JLabel("Board Style: ");
        boardStyleLabel.setFont(labelFont);
        boardStyleLabel.setForeground(Color.WHITE);

        JLabel cashLabel = new JLabel("Starting Cash: ");
        cashLabel.setFont(labelFont);
        cashLabel.setForeground(Color.WHITE);

        JLabel aiPlayersLabel = new JLabel("Number of AI Players: ");
        aiPlayersLabel.setFont(labelFont);
        aiPlayersLabel.setForeground(Color.WHITE);

        JPanel settingsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        settingsPanel.add(boardStyleLabel);
        settingsPanel.add(boardStyleSelection);
        settingsPanel.add(cashLabel);
        settingsPanel.add(cashInput);
        settingsPanel.add(aiPlayersLabel);
        settingsPanel.add(aiPlayersSelector);

        settingsPanel.setBackground(darkerBlue);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton applyButton = new JButton("Apply");
        styleButton(applyButton);
        applyButton.addActionListener(e -> {
            factory.setCash(Integer.parseInt(cashInput.getText()));
            factory.setBoardStyle((String) boardStyleSelection.getSelectedItem());
            factory.setNumOfAiPlayers((Integer) aiPlayersSelector.getSelectedItem());
            settingsDialog.dispose();
        });

        JButton leaveButton = new JButton("Leave");
        styleButton(leaveButton);
        leaveButton.addActionListener(e -> settingsDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(applyButton);
        buttonPanel.add(leaveButton);
        buttonPanel.setBackground(darkerBlue);

        settingsDialog.getContentPane().setBackground(darkerBlue);
        settingsDialog.add(settingsPanel, BorderLayout.CENTER);
        settingsDialog.add(buttonPanel, BorderLayout.SOUTH);
        settingsDialog.setVisible(true);
    }

    public void setRulesButton() {
        rulesButton = new JButton("Rules");
        styleButton(rulesButton);
        rulesButton.addActionListener(e -> showRulesDialog());
    }

    public void showRulesDialog() {
        JDialog rulesDialog = new JDialog();
        rulesDialog.setTitle("Monopoly Rules");
        rulesDialog.setLayout(new BorderLayout());
        rulesDialog.setSize(800, 600);
        rulesDialog.setLocationRelativeTo(null);

        Color darkerBlue = new Color(0, 90, 160);  // Darker blue color

        JTextArea rulesText = new JTextArea();
        rulesText.setText(
                "Monopoly Game Rules:\n\n" +

                        "• Setup:\n" +
                        "   - Each player selects a token and places it on the 'Go' space.\n" +
                        "   - Players are given starting money: two each of $500s, $100s, $50s; and six $20s.\n\n" +

                        "• On Your Turn:\n" +
                        "   - Roll two six-sided dice and move your token that number of spaces.\n" +
                        "   - If you roll doubles, you get another turn. However, rolling doubles three times in a row sends you to jail.\n\n" +

                        "• Buying Property:\n" +
                        "   - If you land on an unowned property, you may buy it for the price listed on its card.\n" +
                        "   - If you choose not to buy it, it remains unowned.\n\n" +

                        "• Paying Rent:\n" +
                        "   - If you land on a property owned by another player, you must pay them rent based on the property's deed card.\n\n" +

                        "• Special Spaces:\n" +
                        "   - 'Go to Jail': Move your token to the Jail space and do not collect $200.\n" +
                        "   - 'Free Parking': Nothing happens. Consider it a free space.\n" +
                        "   - 'Income Tax': Pay the bank $200 or 10% of your total cash (excluding properties), whichever you prefer.\n" +
                        "   - 'Luxury Tax': Pay the bank $100.\n\n" +

                        "• Houses & Hotels:\n" +
                        "   - Before buying houses or hotels, you must own all the properties in its color group.\n" +
                        "   - Houses must be built evenly. For instance, you can't have three houses on one property and one on another in the same group.\n\n" +

                        "• Going Bankrupt:\n" +
                        "   - If you owe more money than you can pay either to another player or the Bank, you are declared bankrupt.\n" +
                        "   - If your debt is to another player, you give them everything you have, and they can choose to auction off any of your properties.\n" +
                        "   - If you're in debt to the Bank, everything you have is returned to the Bank and all properties you own are returned to the Bank's title deed card pile.\n\n" +

                        "• Winning:\n" +
                        "   - The game ends when all but one player has gone bankrupt. The remaining player wins the game.\n\n" +

                        "Note: Trading between players is not allowed in this version."
        );
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        rulesText.setCaretPosition(0);
        rulesText.setEditable(false);
        rulesText.setFont(new Font("Arial", Font.PLAIN, 16));
        rulesText.setMargin(new Insets(20, 20, 20, 20));
        rulesText.setForeground(Color.WHITE);
        rulesText.setBackground(darkerBlue);

        JScrollPane rulesScrollPane = new JScrollPane(rulesText);
        rulesScrollPane.setBorder(null);
        rulesScrollPane.setViewportBorder(null);
        rulesScrollPane.setBackground(darkerBlue);

        // Styling the scrollbar (remains unchanged)
        JScrollBar scrollBar = rulesScrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
        scrollBar.setBackground(new Color(0, 76, 153));
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 128, 255);
                this.trackColor = new Color(0, 76, 153);
            }
        });

        JButton gotItButton = new JButton("Got It!");
        styleButton(gotItButton);
        gotItButton.addActionListener(e -> rulesDialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(darkerBlue);
        bottomPanel.add(gotItButton);

        rulesDialog.getContentPane().setBackground(darkerBlue);
        rulesDialog.add(rulesScrollPane, BorderLayout.CENTER);
        rulesDialog.add(bottomPanel, BorderLayout.SOUTH);
        rulesDialog.setVisible(true);
    }

    public void setQuitButton() {
        quitButton = new JButton("Quit");
        styleButton(quitButton);
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 20));

        button.setPreferredSize(new Dimension(320, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}