import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Scanner;

/**
 * The Player class represents a player in the Monopoly game.
 * It holds information about the player's name, money, position, owned cities,
 * jail status, and jail cards.
 */
public class Player {
    private String name;
    private City onCity;
    private Utility onUtility;
    private int money;
    private int position;
    private int positionDiff;
    private boolean isBankrupted;
    private List<City> ownedCities;
    private GUI2 gui; // added this to be able to print elements on screen

    private List<Utility> ownedUtilities;

    private boolean inJail;
    private int jailCards; // number of get out of jail free cards this player has

    private List<PlayerObserver> subscribers;
    public void nullCityUtility(){
        this.onCity = null;
        this.onUtility = null;
    }
    public void setOnCity(City onCity){
        this.onCity = onCity;
    }
    public String getType(){
        return "Player";
    }
    public City getOnCity(){
        return onCity;
    }

    public void setOnUtility(Utility onUtility){
        this.onUtility = onUtility;
    }

    public Utility getOnUtility(){
        return onUtility;
    }

    public List<Utility> getOwnedUtilities() {
        return ownedUtilities;
    }


    //
    public Player(String name, int money, GUI2 gui) {
        this.name = name;
        this.money = money; // Starting money in Monopoly
        this.position = 0; // Starting at 'GO'
        this.positionDiff = 0;
        this.ownedCities = new ArrayList<>();
        this.ownedUtilities = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.jailCards = 0;
        this.isBankrupted = false;
        this.onCity = null;
        this.onUtility = null;
        inJail = false;
        this.gui = gui; // gui object is passed to constructor, we can print to textArea from this class

    }

    public void subscribe(PlayerObserver p){
        subscribers.add(p);
        notifyObservers();
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



    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public int getPositionDiff() { return positionDiff; }
    public boolean getIsBankrupted(){
        return isBankrupted;
    }
    public void setIsBankrupted(boolean ans){
        isBankrupted = ans;
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
        positionDiff = Math.abs(position - temp);

        if (steps > 0 && position < temp) {
            System.out.println("You have passed Go! You collect 200$.");
            gui.getTextArea().setText("You have passed Go! You collect 200$.");
            money += 200;
        }
        notifyObservers();
    }


    public void buyCity(City city) {
        if(city.getPrice() > money) {
            System.out.println("Not enough money to buy this city");
            gui.getTextArea().setText("Not enough money to buy this city");
            return;
        }
        money -= city.getPrice();
        ownedCities.add(city);
        city.setOwner(this);
        notifyObservers();
    }

    public void buyUtility(Utility utility) {
        if (utility.getPrice() > money) {
            System.out.println("Not enough money to buy this utility");
            gui.getTextArea().setText("Not enough money to buy this utility");
            return;
        }
        money -= utility.getPrice();
        ownedUtilities.add(utility);
        utility.setOwner(this);
        notifyObservers();
    }

    public void payRent(int rent) {
        if(money >= rent) {
            money -= rent;
        } else {
            /*
            System.out.println("Not enough money to pay rent. Transferring assets and going bankrupt.");
            for(City city : ownedCities) {
                city.getOwner().receiveRent(city.getPrice());
                city.setOwner(null);
            }
             */
            money =0;
            playerbankrupted();
            return;
        }
        notifyObservers();
    }
    private void playerbankrupted(){
        System.out.println(name + " is Bankrupted!");

        gui.getTextArea().setText(name + " is Bankrupted!");
        //Game.gameOver();

        Game.gameOver();

        notifyGameOver();
    }

    public void receiveRent(int rent) {
        money += rent;
        notifyObservers();
    }

    public int getJailCards() {
        return jailCards;
    }

    public void setJailCards(int count) {
        jailCards = count;
        notifyObservers();
    }

    public boolean getJailState() { return inJail; }

    // displays the player's money before and after purchasing the utility
    public boolean wantToBuyUtility(Utility utility) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Money: $" + money);
        gui.getTextArea().setText("Current Money: $" + money + ".");
        System.out.println("Do you want to buy " + utility.getName() + " for $" + utility.getPrice() + "? (Y/N)");
        gui.getTextArea().append(" Do you want to buy " + utility.getName() + " for $" + utility.getPrice() + "? (Y/N)");

        String input = scanner.nextLine().trim().toLowerCase();




        if (input.equals("y")) {
            int remainingMoney = money - utility.getPrice();
            System.out.println("Remaining Money: $" + remainingMoney);
            gui.getTextArea().setText("Remaining Money: $" + remainingMoney);
            return true;
        } else {
            return false;
        }
    }
    // displays the player's money before and after purchasing the city
    public boolean wantToBuyCity(City city){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Money: $" + money +".");
        gui.getTextArea().setText("Current Money: $" + money +".");
        System.out.println(" Do you want to buy " + city.getName() + " for $" + city.getPrice() + "? (Y/N)");
        gui.getTextArea().append(" Do you want to buy " + city.getName() + " for $" + city.getPrice() + "? (Y/N)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("y")) {
            int remainingMoney = money - city.getPrice();
            System.out.println("Remaining Money: $" + remainingMoney);
            gui.getTextArea().setText("Remaining Money: $" + remainingMoney);
            return true;
        } else {
            return false;
        }
    }

    public boolean ownsCurrentSet(City city) {
        int numOwned = 0;
        for (City i : ownedCities) {
            if (i.getColor() == city.getColor()) ++numOwned;
        }
        return (city.getColor() == propertyColor.BROWN || city.getColor() == propertyColor.DBLUE) ? numOwned == 2 : numOwned == 3;
    }

    public void buyHouse(City city, int count) {
        if (city.getHouseCost() * count > money) {
            gui.getTextArea().append("You cannot afford that many houses!");
        }

        else {
            payRent(city.getHouseCost() * count);
            city.addHouses(count);
        }
    }

    public void makeDecision() {
        return;
    }

}
