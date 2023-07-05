/**
 * The AI class represents an AI player in a game.
 * It extends the Player class and provides additional functionality specific to AI players.
 */
public class AI extends Player{
    public AI(String name, int money) {
        super(name, money);
    }
    public String getType(){
        return "AI";
    }
}