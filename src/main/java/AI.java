/**
 * The AI class represents an AI player in a game.
 * It extends the Player class and provides additional functionality specific to AI players.
 */
public class AI extends Player{
    public AI(String name, int money, GUI2 gui) {
        super(name, money, gui);
    }
    public String getType(){
        return "AI";
    }
    // to implement: AI decision making master function
    public void makeDecision() {
        float probability = 0.8f;

        if (ownsCurrentSet(getOnCity())) {

        }
    }
}