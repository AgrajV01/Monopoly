import java.util.List;

public class Game {
    private Board board;
    private Die die;
    private List<Player> players;
    private int currentPlayer, numOfPlayers;

    public Game(GameFactory factory) {
        this.board = factory.createBoard();
        this.players = factory.createPlayers();
        board = new Board();
        die = new Die();
        currentPlayer = 0; // Player 1 starts the game
        numOfPlayers = factory.getNumPlayers();
    }

    public void subscribeToPlayers(PlayerObserver o){
        for (Player p : players){
            p.subscribe(o);
        }
    }

    public void rollDiceAndMove() {
        int roll = die.roll();
        players.get(currentPlayer).move(roll);
        System.out.println("You rolled: " + die.diceOne + " + " + die.diceTwo + " = " + roll);

        // Check if player's new position is a city and it's owned by someone else
        int position = players.get(currentPlayer).getPosition();
        board.getPosition(position).action(players.get(currentPlayer)); // action is decided depending on position of the player
    }

    /*
        public void buyCurrentCity() {
            int position = players.get(currentPlayer).getPosition();
            City city = board.getCity(position);
            players.get(currentPlayer).buyCity(city);
        }
    */
    public int switchTurn() {
        if(numOfPlayers == currentPlayer + 1)
            currentPlayer = -1;
        return ++currentPlayer;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }
}
