public class OtherSpace implements Space {

    @Override
    public void action(Player player) {
        if (player.getPosition() == 0) System.out.println("Go square!");
        else if (player.getPosition() == 4) System.out.println("Income Tax!");
        else if (player.getPosition() == 10) System.out.println("Visiting jail!");
        else if (player.getPosition() == 20) System.out.println("Free parking!");
        else if (player.getPosition() == 30) System.out.println("Oh no! Go to jail!");
        else if (player.getPosition() == 38) System.out.println("Luxury Tax!");
    }
}
