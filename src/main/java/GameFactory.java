import java.util.List;

public interface GameFactory {
    Board createBoard(GUI2 gui);
    Board createBoard();
    List<Player> createPlayers();
    int getNumPlayers();
    int getNumOfAiPlayers();

    int getCash();
    void setCash(int cash);

    String getBoardStyle();
    void setBoardStyle(String style);
}

