import java.util.Random;

public class ActionCard implements Space {
    private Random random;

    public ActionCard() {
        random = new Random();
    }
    @Override
    public void action(Player player) {
        int cardType = random.nextInt(8);

        switch (cardType) {
            case 0:
                // receive get out of jail free card
            case 1:
                // go to jail
            case 2:
                // receive money from the bank
            case 3:
                // pay taxes to the bank
            case 4:
                // move forward X spaces
            case 5:
                // move backward X spaces
        }
    }
}
