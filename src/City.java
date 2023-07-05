public class City extends Space {
    private int price;
    private int rent;
    private int houseCost;
    private Player owner;
    private GUI2 gui;

    public City(String name, int price, int rent, int houseCost, GUI2 gui) {
        this.isProperty = true;
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.houseCost = houseCost;
        this.owner = null; // Initially, no one owns the city.
        this.gui = gui;
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
        gui.getTextArea().setText("You have landed on: " + this.name);
        if (isAvailable()) {
            // Purchase City? option appears on GUI
            if(player.getMoney() >= this.price) {
                player.setOnCity(this);
            }
            else{
                System.out.println("This city is available for purchase at a price of " + price);
                gui.getTextArea().append("This city is available for purchase at a price of " + price + ".");
                System.out.println(player.getName() + " has $" + player.getMoney());
                gui.getTextArea().append(" " + player.getName() + " has $" + player.getMoney() +".");
                System.out.println("Insufficient funds to buy the Property");
                gui.getTextArea().append(" Insufficient funds to buy the Property");
            }
            /*
            System.out.println("This city is available for purchase at a price of $" + price);
            if (player.wantToBuyCity(this)) {
                player.buyCity(this);
                System.out.println("Congratulations! You have successfully purchased " + this.name);
            } else {
                System.out.println("You chose not to purchase " + this.name);
            }
            */
        }
        else {
            System.out.println("This property is owned by: " + owner.getName());
            //player.payRent(rent);
            //owner.receiveRent(rent);
            if(player.getMoney() >= this.rent) {
                int rent = this.rent;
                System.out.println("Rent to be paid: $" + rent);
                System.out.println(player.getName() + " initially has $" + player.getMoney());
                player.payRent(rent);
                owner.receiveRent(rent);
                System.out.println("Amount left after paying rent is: $" + player.getMoney());
                System.out.println("After receiving the rent, Owner(" + owner.getName() + ") has $" + owner.getMoney());
            }
            else{
                System.out.println(player.getName() + " has  $" + player.getMoney());
                System.out.println("Insufficient funds! The Player is Bankrupted");
                int bal = this.price - player.getMoney();
                player.payRent(this.price -bal);
                owner.receiveRent(this.price -bal);
                player.setIsBankrupted(true);
                //Game.gameOver();
            }
        }
    }
}
