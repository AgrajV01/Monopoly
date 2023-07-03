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
        if (player.getPosition() == 2 || player.getPosition() == 17 || player.getPosition() == 33) {
            System.out.println("You have landed on Community Chest!");
        }
        else {
            System.out.println("You have landed on Chance!");
        }
        int cardType = random.nextInt(4);

        switch (cardType) {
            case 0:
                player.setJailCards(player.getJailCards() + 1); // gives player a jail card
                System.out.println("You receive 1 get out of jail free card!");
                break;
            case 1:
                player.sendToJail(); // sends player to jail
                System.out.println("Oh no! You go to jail!");
                break;
            case 2:
                player.receiveRent(100); // player receives money from the bank
                System.out.println("You receive 100$ from the bank! Your balance is now: " + player.getMoney());
                break;
            case 3:
                player.payRent(150); // player pays taxes to the bank
                System.out.println("You must pay 150$ to the bank! Your balance is now: " + player.getMoney());
                break;
                /*
            case 4:
                player.move(3); // player moves forward 3 squares
                System.out.println("You move forward 3 squares");

                break;
            case 5:
                player.move(-3); // player moves backward 3 squares
                System.out.println("You move backwards 3 squares");
                break;

                 */
        }
    }
}
