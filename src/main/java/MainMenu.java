import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

public class MainMenu {
    private static JButton newGameButton;
    private static JButton loadGameButton;
    private static JButton settingsButton;
    private static JButton rulesButton;
    private static JButton quitButton;
    private static JFrame frame;
    private static JLayeredPane layeredPane;
    private JLabel bgLabel;

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
        GameFactory factory = defaultSettings(a);

        setNewGameButton(factory, a);
        setLoadGameButton(factory, a);
        setSettingsButton(factory);
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

    GameFactory defaultSettings(GUI2 a) {
        int numPlayers = 4;
        int numOfAiPlayers = 2;
        int cash = 2000;
        String boardStyle = "Classic";

        return new CustomGameFactory(numPlayers, numOfAiPlayers, cash, boardStyle, a);
    }

    public void setNewGameButton(GameFactory factory, GUI2 a) {
        newGameButton = new JButton("New Game");
        styleButton(newGameButton);
        newGameButton.addActionListener(e -> {
            Game game = new Game(factory, a);
            a.initializeTheBoard(game);
            game.subscribeToPlayers(a);

            frame.dispose();
        });
    }

    public void setLoadGameButton(GameFactory factory, GUI2 a) {
        loadGameButton = new JButton("Load Game");
        styleButton(loadGameButton);
        loadGameButton.addActionListener(e -> {
            // To Do
            frame.dispose();
        });
    }

    public void setSettingsButton(GameFactory factory) {
        settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        settingsButton.addActionListener(e -> {
            // To Do
        });
    }

    public void setRulesButton() {
        rulesButton = new JButton("Rules");
        styleButton(rulesButton);
        rulesButton.addActionListener(e -> showRulesDialog());
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
        gotItButton.addActionListener(e -> rulesDialog.dispose());

        rulesDialog.add(rulesScrollPane);
        rulesDialog.add(gotItButton);
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

        // Set custom font if available, fallback to Arial otherwise
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("MonopolyHandwritten.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            button.setFont(customFont);
        } catch (Exception e) {
            button.setFont(new Font("Arial", Font.BOLD, 20));
        }

        button.setPreferredSize(new Dimension(320, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}