public class Utility extends Space{
    private int price;
    private int rent;
    private int multiplier;
    private Player owner;

    public Utility(String name, int price, int rent) {
        this.isProperty = true;
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null; // Initially, no one owns the city.
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public Player getOwner() {
        return owner;
    }

    public void increaseMult() {
        multiplier *= 2;
    }

    public void setMult(int mult) {
        multiplier = mult;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    // Check if city is available to purchase
    public boolean isAvailable() {
        return owner == null;
    }

    @Override
    public void action(Player player) {
        System.out.println("You have landed on: " + this.name);
        if (isAvailable()) {
            // Purchase City? option appears on GUI
            System.out.println( player.getName() + " initially has $" + player.getMoney());
            player.buyUtility(this);
            System.out.println("This utility is available for purchase at a price of " + price);
            System.out.println("After Purchasing, the balance amount you have is " + player.getMoney());
            /*
            // Purchase City? option appears on the terminal after entering Y/N, the player pieces move
            System.out.println("This utility is available for purchase at a price of " + price);
            if (player.wantToBuyUtility(this)) {
                player.buyUtility(this);
                System.out.println("Congratulations! You have successfully purchased " + this.name);
            } else {
                System.out.println("You chose not to purchase " + this.name);
            }


             */

        }
        else {
            System.out.println("This property is owned by: " + owner.getName());
            // player.payRent(rent);
            // owner.receiveRent(rent);

            int rent = this.rent;
            System.out.println("Rent to be paid: $" + rent);
            System.out.println( player.getName() + " initially has $" + player.getMoney());
            player.payRent(rent);
            owner.receiveRent(rent);
            System.out.println("Amount left after paying rent is: $" + player.getMoney());
            System.out.println("After receiving the rent, Owner(" + owner.getName() + ") has $" + owner.getMoney());
        }
    }
}
