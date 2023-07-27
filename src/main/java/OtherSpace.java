/**
 * The OtherSpace class represents a type of space on the game board that is not a property.
 * It extends the Space class and defines specific actions that occur when a player lands on this type of space.
 */
public class OtherSpace extends Space {

    public static final int GO = 0;
    public static final int INCOMETAX = 4;
    public static final int JAIL = 10;
    public static final int FREEPARKING = 20;
    public static final int GOTOJAIL = 30;
    public static final int LUXTAX = 38;

    private GUI2 gui;

    OtherSpace(String name, GUI2 gui) {
        this.isProperty = false;
        this.name = name;
        this.gui = gui;
    }
    @Override
    public void action(Player player) {
        if (player.getPosition() == GO) {
            System.out.println("Go square!");
            gui.getTextArea().append("Go square!");
        }
        else if (player.getPosition() == INCOMETAX) {
            System.out.println("Income Tax!\n");
            gui.getTextArea().append("Income Tax!\n");
            System.out.println(player.getName() + " must pay $200");
            gui.getTextArea().append(player.getName() + " must pay $200");
            player.payRent(200);
        }
        else if (player.getPosition() == JAIL) {
            if (!player.getJailState()) {

                System.out.println("Visiting jail!");
                gui.getTextArea().append(player.getName() + " is visiting jail!");
            }
            else {
                if (player.getJailCards() > 0) {
                    System.out.println("You use a get out of jail free card");
                    gui.getTextArea().append(player.getName() + " uses a get out of jail free card");

                }
                else {
                    // player can roll or pay
                }
            }
        }
        else if (player.getPosition() == FREEPARKING) {

            System.out.println("Free parking!");
            gui.getTextArea().append("Free parking!");
        }
        else if (player.getPosition() == GOTOJAIL) {
            System.out.println("Oh no! Go to jail!");
            gui.getTextArea().append("Oh no! Go to jail!");
            player.sendToJail();
        } else if (player.getPosition() == LUXTAX) {
            System.out.println("Luxury Tax!");
            gui.getTextArea().append("Luxury Tax!");
            System.out.println(player.getName() + " Must pay $100");
            gui.getTextArea().append(player.getName() + " Must pay $100");
            player.payRent(100);
        }
    }
}
