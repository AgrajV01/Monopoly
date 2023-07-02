import java.util.Random;
/**
 * The ActionCard class represents an action card in the Monopoly game.
 * It extends the Space class and implements the action method to perform a specific action
 * when a player lands on an action card space.
 */
public class ActionCard extends Space {
    private Random random;

    public ActionCard(String name) {
        random = new Random();
        this.name = name;
        this.isProperty = false;
    }
    @Override
    public void action(Player player) {
        int cardType = random.nextInt(6);

        switch (cardType) {
            case 0:
                player.setJailCards(player.getJailCards() + 1); // gives player a jail card
            case 1:
                player.sendToJail(); // sends player to jail
            case 2:
                player.receiveRent(100); // player receives money from the bank
            case 3:
                player.payRent(150); // player pays taxes to the bank
            case 4:
                player.move(3); // player moves forward 3 squares
            case 5:
                player.move(-3); // player moves backward 3 squares
        }
    }
}
