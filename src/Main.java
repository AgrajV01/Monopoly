import java.util.Random;

/**
 * The Main class serves as the entry point for starting the Monopoly game.
 * It creates an instance of the game with custom settings and initializes the user interface.
 */
public class Main {
    public static void main(String[] args) {
        // Create the game
        int numPlayers = 4;
        int numOfAiPlayers = 1;
        int cash = 2000;
        String boardStyle = "Classic";

        GameFactory factory = new CustomGameFactory(numPlayers, numOfAiPlayers, cash, boardStyle);
        Game game = new Game(factory);
        for(int i = 0;i < 3;i++)
            System.out.println(game.players.get(i).getType());

        Random random = new Random();

        GUI2 a = new GUI2();
        a.initializeTheBoard(game);

    }
}
