public class City extends Space {
    private int price;
    private int rent;
    private int houseCost;
    private Player owner;


    public City(String name, int price, int rent, int houseCost) {
        this.isProperty = true;
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.houseCost = houseCost;
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
            //player.buyCity(this);
            System.out.println("This city is available for purchase at a price of $" + price);
            if (player.wantToBuyCity(this)) {
                player.buyCity(this);
                System.out.println("Congratulations! You have successfully purchased " + this.name);
            } else {
                System.out.println("You chose not to purchase " + this.name);
            }

        }
        else {
            System.out.println("This property is owned by: " + owner.getName());
            player.payRent(rent);
            owner.receiveRent(rent);
        }
    }
}
