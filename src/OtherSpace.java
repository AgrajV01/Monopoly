/**
 * The OtherSpace class represents a type of space on the game board that is not a property.
 * It extends the Space class and defines specific actions that occur when a player lands on this type of space.
 */
public class OtherSpace extends Space {

    OtherSpace(String name) {
        this.isProperty = false;
        this.name = name;
    }
    @Override
    public void action(Player player) {
        if (player.getPosition() == 0) {
            System.out.println("Go square!");
        }
        else if (player.getPosition() == 4) {
            System.out.println("Income Tax!");
            System.out.println(player.getName() + " Must pay $200");
            player.payRent(200);
        }
        else if (player.getPosition() == 10) {
            if (!player.getJailState()) System.out.println("Visiting jail!");
            else {
                if (player.getJailCards() > 0) {
                    System.out.println("You use a get out of jail free card");
                }
                else {
                    // player can roll or pay
                }
            }
        }
        else if (player.getPosition() == 20) System.out.println("Free parking!");
        else if (player.getPosition() == 30) {
            System.out.println("Oh no! Go to jail!");
            player.sendToJail();
        } else if (player.getPosition() == 38) {
            System.out.println("Luxury Tax!");
            System.out.println(player.getName() + " Must pay $100");
            player.payRent(100);
        }
    }
}
