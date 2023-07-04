import java.util.ArrayList;
import java.util.List;

public class CustomGameFactory implements GameFactory {
    private int numPlayers;
    private int money;
    private String boardStyle;
    private int numOfAiPlayers;

    public CustomGameFactory(int numPlayers, int numOfAiPlayers, int cash, String boardStyle) {
        this.numPlayers = numPlayers;
        this.numOfAiPlayers = numOfAiPlayers;
        this.money = cash;
        this.boardStyle = boardStyle;
    }

    @Override
    public Board createBoard() {
        return new Board();
    }

    @Override
    public List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        for(int i=0; i<(numPlayers-numOfAiPlayers); i++) {
            String name = "Player " + i;
            players.add(new Player(name, money));
        }
        for(int i=0; i<numOfAiPlayers; i++) {
            String name = "AI " + i;
            players.add(new AI(name, money));
        }
        return players;
    }

    @Override
    public int getNumPlayers(){
        return numPlayers;
    }
    @Override
    public int getNumOfAiPlayers(){
        return numOfAiPlayers;
    }

}