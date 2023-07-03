/**
 * The Main class serves as the entry point for starting the Monopoly game.
 * It creates an instance of the game with custom settings and initializes the user interface.
 */
public class Main {
    public static void main(String[] args) {
        // Create the game
        int numPlayers = 4;
        int cash = 2000;
        String boardStyle = "Classic";

        GameFactory factory = new CustomGameFactory(numPlayers, cash, boardStyle);
        Game game = new Game(factory);

        // Create the UI for the game and show it
        MonopolyUI ui = new MonopolyUI(game);
        ui.showUI();

        game.subscribeToPlayers(ui);
    }
}
