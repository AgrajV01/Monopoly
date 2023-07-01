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
    }
}
