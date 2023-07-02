import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int money;
    private int position;
    private List<City> ownedCities;

    private List<PlayerObserver> subscribers;

    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money in Monopoly
        this.position = 0; // Starting at 'GO'
        this.ownedCities = new ArrayList<City>();
        this.subscribers = new ArrayList<PlayerObserver>();
    }

    public void subscribe(PlayerObserver p){
        subscribers.add(p);
    }

    public void notifyObservers(){
        for(PlayerObserver p : subscribers){
            p.onPlayerState(this);
        }
    }

    public void notifyGameOver(){
        for(PlayerObserver p : subscribers){
            p.onGameOver();
        }
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getPosition() {
        return position;
    }

    public List<City> getOwnedCities() {
        return ownedCities;
    }

    public void move(int steps) {
        position = (position + steps) % 10;  // Assuming the board size is 10
        notifyObservers();
    }

    public void buyCity(City city) {
        if(city.getPrice() > money) {
            System.out.println("Not enough money to buy this city");
            return;
        }
        money -= city.getPrice();
        ownedCities.add(city);
        city.setOwner(this);
        notifyObservers();
    }

    public void payRent(int rent) {
        money -= rent;
        if (money < 0)
        {
            notifyGameOver();
            return;
        }
        notifyObservers();
    }
}
