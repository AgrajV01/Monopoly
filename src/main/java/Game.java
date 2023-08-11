import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The Game class represents a game instance with players, a board, and dice.
 */
public class Game {
    public static String SAVE_FILE = "SaveGame.json";
    public static int STARTMONEY;
    private Board board;
    private Die die;

    public static List<Player> players;

    private GUI2 gui;


    public static class GameState{

        public GameState(int currentPlayer, int numOfPlayers, String boardStyle, List<String> playerNames){
            this.currentPlayer = currentPlayer;
            this.numOfPlayers = numOfPlayers;
            this.boardStyle = boardStyle;
            this.playerNames = playerNames;
        }
        public GameState() {
        }
        public int currentPlayer;
        public int numOfPlayers;
        public String boardStyle;

        public List<String> playerNames;
    }

    private GameState state;

    public String getBoardStyle() { return state.boardStyle; }
    public void cleanProperty(){
        for (Player p : players)
            p.nullCityUtility();
    }

    public Game(GameFactory factory, GUI2 gui) {
        STARTMONEY = factory.getCash();
        this.board = factory.createBoard(gui);
        this.players = factory.createPlayers(this);

        board = new Board(gui);
        die = new Die();
        this.gui = gui;
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < factory.getNumPlayers(); i++){
            names.add(this.players.get(i).getName());
        }

        this.state = new GameState(
                0,
                factory.getNumPlayers(),
                factory.getBoardStyle(),
                names
        );
    }
    public Game(GameState state, GameFactory factory, GUI2 gui) {

        this.state = state;
        STARTMONEY = factory.getCash();
        this.board = factory.createBoard(gui);

        Game.players = new ArrayList<Player>();
        this.gui = gui;
        this.board = new Board(gui);
        this.die = new Die();

        for (String name : this.state.playerNames){
            Player p = Player.loadPlayer(name, gui);
            Game.players.add(p);
            System.out.println(p.getPosition());
        }
    }

    public void subscribeToPlayers(PlayerObserver o){
        for (Player p : players){
            p.subscribe(o);
        }
    }

    public static Game loadGame(GameFactory factory, GUI2 gui) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        GameState state = mapper.readValue(new File(SAVE_FILE), GameState.class);

        return new Game(state, factory, gui);

    }


    public void saveGame(){
        System.out.print("Saving game...");
        for (Player p : players){
            p.saveState();
        }
        try{
            ObjectMapper mapper = new ObjectMapper();

            String saveString = mapper.writeValueAsString(this.state);

            FileWriter f = new FileWriter(SAVE_FILE);
            f.write(saveString);
            f.close();
        }
        catch(Exception e)
        {
            System.out.print("Exception: ");
            System.out.println(e);
        }
   }


    // unused
    public void rollDiceAndMove() {
        int roll = die.roll();
        players.get(state.currentPlayer).move(roll);
        System.out.println("You rolled: " + die.diceOne + " + " + die.diceTwo + " = " + roll);
        if (die.isDouble()) {
            System.out.println("You rolled a double!");
        }

        // Check if player's new position is a city and it's owned by someone else
        int position = players.get(state.currentPlayer).getPosition();
        // action is decided depending on position of the player
        board.getPosition(position).action(players.get(state.currentPlayer));
    }



    /*
        public void buyCurrentCity() {
            int position = players.get(currentPlayer).getPosition();
            City city = board.getCity(position);
            players.get(currentPlayer).buyCity(city);
        }
    */
    public int switchTurn() {
        if(state.numOfPlayers == state.currentPlayer + 1)
            state.currentPlayer = -1;
        return ++state.currentPlayer;
    }

    public void makeMove(Die roll) {
        gui.getTextArea().setText(getCurrentPlayer().getName() + " rolled a " + (roll.diceOne + roll.diceTwo) + "\n");
        players.get(state.currentPlayer).move(roll.diceOne+ roll.diceTwo);

        if (roll.isDouble()) gui.getTextArea().append(getCurrentPlayer().getName() + " rolled a doubles! They can roll again\n");

        // Check if player's new position is a city and it's owned by someone else
        int position = players.get(state.currentPlayer).getPosition();

        // if current player is not an AI, call the action() function
        // otherwise, call the AI makeDecision() function later
        //if (getCurrentPlayer().getType().equals("Player"))
        board.getPosition(position).action(getCurrentPlayer());

/*
        if(board.getPosition(position) instanceof City) {
            City city = (City) board.getPosition(position);
            if(city.getOwner() != null && city.getOwner() != players.get(currentPlayer)) {
                players.get(currentPlayer).payRent(city.getRent());
                city.getOwner().receiveRent(city.getRent());
            }
        }

 */


        // Switch the turn to the next player
        for(Player pl : players) {
            if (pl.getIsBankrupted())
                return;
        }

        // switch turns if the player did not roll doubles
        // switchTurn();
    }


    public Player getCurrentPlayer() {
        return players.get(state.currentPlayer);
    }
    public Player getPrevPlayer() {
        if(state.currentPlayer != 0)
            return players.get(state.currentPlayer-1);
        else return players.get(3);
    }


    public int getNumPlayers() {
        return state.numOfPlayers;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return state.currentPlayer;
    }


    public int getPrevPlayerIndex() {
        if(state.currentPlayer != 0)
            return state.currentPlayer-1;
        else return 3;
    }
    public static void gameOver() {
        // Determine the player with the highest value
        Player winner = getPlayerWithHighestValue();
        int propertyMoney =calculateTotalValue(winner) - winner.getMoney();
        // Print the winner and other players in descending order of value
        System.out.println("Game Over!");
        System.out.println("Player with the highest value: " + winner.getName());
        System.out.println(winner.getName() + " has total $" + calculateTotalValue(winner) +
                "($" + winner.getMoney() + " in money and $" + propertyMoney + "in properties)");
        System.out.println("Other players:");
        players.stream()
                .filter(player -> player != winner)
                .sorted((p1, p2) -> Integer.compare(calculateTotalValue(p2), calculateTotalValue(p1)))
                .forEach(p -> System.out.println(p.getName() + "-- Total Value(Including properties and money): "
                        + calculateTotalValue(p)));
        System.exit(0);
    }

    private static Player getPlayerWithHighestValue() {
        Player winner = players.get(0);
        int highestValue = calculateTotalValue(winner);

        for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);
            int playerValue = calculateTotalValue(player) ;

            if (playerValue > highestValue) {
                winner = player;
                highestValue = playerValue;
            }
        }

        return winner;
    }
    private static int calculateTotalValue(Player player) {
        int totalValue = player.getMoney();

        for (City city : player.getOwnedCities()) {
            totalValue += city.getPrice();
        }

        for (Utility utility : player.getOwnedUtilities()) {
            totalValue += utility.getPrice();
        }

        return totalValue;
    }
}
