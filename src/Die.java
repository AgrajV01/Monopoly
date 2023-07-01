import java.util.Random;

public class Die {
    public int diceOne, diceTwo;
    private Random random;

    public Die() {
        random = new Random();
    }

    public int roll() {
        diceOne = random.nextInt(6) + 1;
        diceTwo = random.nextInt(6) + 1;
        return diceOne + diceTwo;
    }

    public boolean isDouble() {
        return diceOne == diceTwo;
    }
}
