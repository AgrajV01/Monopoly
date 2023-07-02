public class City {
    private String name;
    private int price;
    private int rent;
    private Player owner;

    public City(String name, int price, int rent) {
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

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    // Check if city is available to purchase
    public boolean isAvailable() {
        return owner == null;
    }
}
