import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
/**
 * The Player class represents a player in the Monopoly game.
 * It holds information about the player's name, money, position, owned cities,
 * jail status, and jail cards.
 */
public class Player {
    private String name;
    private int money;
    private int position;
    private List<City> ownedCities;

    private List<Utility> ownedUtilities;

    private boolean inJail;
    private int jailCards; // number of get out of jail free cards this player has

    public Player(String name, int money) {
        this.name = name;
        this.money = money; // Starting money in Monopoly
        this.position = 0; // Starting at 'GO'
        this.ownedCities = new ArrayList<>();
        this.jailCards = 0;
        inJail = false;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) { this.position = position; }

    public void sendToJail() {
        setPosition(10);
        inJail = true;
    }
    public List<City> getOwnedCities() {
        return ownedCities;
    }

    public void move(int steps) {
        int temp = position;
        position = Math.floorMod(position + steps, 40);  // Assuming the board size is 40
        if (steps > 0 && position < temp) {
            System.out.println("You have passed Go! You collect 200$");
            money += 200;
        }
    }


    public void buyCity(City city) {
        if(city.getPrice() > money) {
            System.out.println("Not enough money to buy this city");
            return;
        }
        money -= city.getPrice();
        ownedCities.add(city);
        city.setOwner(this);
    }

    public void buyUtility(Utility utility) {
        if (utility.getPrice() > money) {
            System.out.println("Not enough money to buy this utility");
            return;
        }
        money -= utility.getPrice();
        ownedUtilities.add(utility);
        utility.setOwner(this);
    }

    public void payRent(int rent) {
        if(money >= rent) {
            money -= rent;
        } else {
            System.out.println("Not enough money to pay rent. Transferring assets and going bankrupt.");
            for(City city : ownedCities) {
                city.getOwner().receiveRent(city.getPrice());
                city.setOwner(null);
            }
            playerbankrupted();
        }
    }
    private void playerbankrupted(){
        System.out.println("Player " + name + " is Bankrupted!");
    }

    public void receiveRent(int rent) {
        money += rent;
    }

    public int getJailCards() {
        return jailCards;
    }

    public void setJailCards(int count) {
        jailCards = count;
    }

    public boolean getJailState() { return inJail; }
}
