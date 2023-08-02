import java.util.List;

public interface GameFactory {
    Board createBoard(GUI2 gui);

    Board createBoard();

    List<Player> createPlayers(Game game);
    int getNumPlayers();
    int getNumOfAiPlayers();
}
