import java.util.ArrayList;
import java.util.List;

public class CustomGameFactory implements GameFactory {
    private int numPlayers;
    private int money;
    private String boardStyle;

    public CustomGameFactory(int numPlayers, int cash, String boardStyle) {
        this.numPlayers = numPlayers;
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
        for(int i=0; i<numPlayers; i++) {
            String name = "Player " + i;
            players.add(new Player(name, money));
        }
        return players;
    }

    @Override
    public int getNumPlayers(){
        return numPlayers;
    }
}