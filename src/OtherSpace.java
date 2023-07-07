/**
 * The OtherSpace class represents a type of space on the game board that is not a property.
 * It extends the Space class and defines specific actions that occur when a player lands on this type of space.
 */
public class OtherSpace extends Space {

    private GUI2 gui;

    OtherSpace(String name, GUI2 gui) {
        this.isProperty = false;
        this.name = name;
        this.gui = gui;
    }
    @Override
    public void action(Player player) {
        if (player.getPosition() == 0) {
            System.out.println("Go square!");
            gui.getTextArea().setText("Go square!");
        }
        else if (player.getPosition() == 4) {
            System.out.println("Income Tax!");
            gui.getTextArea().setText("Income Tax!");
            System.out.println(player.getName() + " Must pay $200");
            gui.getTextArea().append(player.getName() + " Must pay $200");
            player.payRent(200);
        }
        else if (player.getPosition() == 10) {
            if (!player.getJailState()) {

                System.out.println("Visiting jail!");
                gui.getTextArea().setText("Visiting jail!");
            }
            else {
                if (player.getJailCards() > 0) {
                    System.out.println("You use a get out of jail free card");
                    gui.getTextArea().append("You use a get out of jail free card");

                }
                else {
                    // player can roll or pay
                }
            }
        }
        else if (player.getPosition() == 20) {

            System.out.println("Free parking!");
            gui.getTextArea().setText("Free parking!");
        }
        else if (player.getPosition() == 30) {
            System.out.println("Oh no! Go to jail!");
            gui.getTextArea().setText("Oh no! Go to jail!");
            player.sendToJail();
        } else if (player.getPosition() == 38) {
            System.out.println("Luxury Tax!");
            gui.getTextArea().setText("Luxury Tax!");
            System.out.println(player.getName() + " Must pay $100");
            gui.getTextArea().append(player.getName() + " Must pay $100");
            player.payRent(100);
        }
    }
}
