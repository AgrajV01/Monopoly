public class Game {
    private Board board;
    private Die die;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Game() {
        board = new Board();
        die = new Die();
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        currentPlayer = player1; // Player 1 starts the game
    }

    public void rollDiceAndMove() {
        int roll = die.roll();
        currentPlayer.move(roll);

        // Check if player's new position is a city and it's owned by someone else
        int position = currentPlayer.getPosition();
        City city = board.getCity(position);
        if (city.getOwner() != null && city.getOwner() != currentPlayer) {
            currentPlayer.payRent(city.getRent());
        }
    }

    public void buyCurrentCity() {
        int position = currentPlayer.getPosition();
        City city = board.getCity(position);
        currentPlayer.buyCity(city);
    }

    public void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void initialize() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        // Initialize the board
        board = new Board();

        // Set the starting player
        currentPlayer = player1; // Player 1 starts the game

        // Initialize the dice
        die = new Die();
    }
}
