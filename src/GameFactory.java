import java.util.List;

public interface GameFactory {
    Board createBoard();
    List<Player> createPlayers();
    int getNumPlayers();
}
