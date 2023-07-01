import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int money;
    private int position;
    private List<City> ownedCities;

    public Player(String name, int money) {
        this.name = name;
        this.money = money; // Starting money in Monopoly
        this.position = 0; // Starting at 'GO'
        this.ownedCities = new ArrayList<>();
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

    public List<City> getOwnedCities() {
        return ownedCities;
    }

    public void move(int steps) {
        position = (position + steps) % 10;  // Assuming the board size is 10
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

    public void payRent(int rent) {
        money -= rent;
        if(money < 0) {
            // The player is bankrupt, handle it according to your game rules
        }
    }
}
