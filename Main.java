public class Main {
    public static void main(String[] args) {
        // Create the game
        Game game = new Game();
        game.initialize();

        // Create the UI for the game and show it
        MonopolyUI ui = new MonopolyUI(game);
        ui.showUI();

        game.subscribeToPlayers(ui);
    }
}
