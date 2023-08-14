import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Scanner;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private int consecutiveMoves;
    private int turnsInJail;
    private boolean isBankrupted;
    private List<City> ownedCities;
    private GUI2 gui; // added this to be able to print elements on screen

    private List<Utility> ownedUtilities;

    private boolean inJail;
    private int jailCards; // number of get out of jail free cards this player has

    private List<PlayerObserver> subscribers;

    public GUI2 getGui() {
        return gui;
    }

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
        consecutiveMoves = 0;
        turnsInJail = 0;
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
    public int getConsecutiveMoves() { return consecutiveMoves; }
    public void setConsecutiveMoves(int count) { consecutiveMoves = count; }
    public boolean getIsBankrupted(){
        return isBankrupted;
    }
    public void setIsBankrupted(boolean ans){
        isBankrupted = ans;
    }
    public void setPosition(int position) { this.position = position; }

    public int getTurnsInJail() { return turnsInJail; }

    public void setTurnsInJail(int count) { turnsInJail = count; }

    public void sendToJail() {
//        setPosition(10);
//        inJail = true;

        // Notify the player about being sent to jail before the delay

        gui.getTextArea().setText(name + " is being sent to jail!");
        consecutiveMoves = 0;

        // Introduce a delay of 2 seconds (2000 milliseconds) before sending the player to jail
        int delayMilliseconds = 1000;
        Timer timer = new Timer(delayMilliseconds, (ActionEvent e) -> {

            // Notify the player again after the delay

            Audio.playAudio("src/main/resources/jailSiren.wav");
            gui.getTextArea().setText(name + " has been sent to jail!");

        });


        //setPosition(10);
        inJail = true;

        // Start the timer
        timer.setRepeats(false);
        timer.start();
    }
    public List<City> getOwnedCities() {
        return ownedCities;
    }

    public void move(int steps) {
        /*
        if (inJail) {
            inJail = false;
            consecutiveMoves = 0;
        }

         */

        if (!this.getType().equals("Player")) Audio.playAudio("src/main/resources/diceRoll.wav");
        consecutiveMoves++;

        int temp = position;
        position = Math.floorMod(position + steps, 40);  // Assuming the board size is 40
        positionDiff = Math.abs(position - temp);
        inJail = false; // remove this line once the jail delay is set correctly
        if (steps > 0 && position < temp) {

            Audio.playAudio("src/main/resources/gotCash.wav");
            gui.getTextArea().setText("You have passed Go! You collect 200$.\n");

            money += 200;
        }
        notifyObservers();
    }


    public void buyCity(City city) {
        if(city.getPrice() > money) {

            gui.getTextArea().setText("Not enough money to buy this city");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!\n");
            return;
        }
        money -= city.getPrice();
        ownedCities.add(city);
        city.setOwner(this);
        notifyObservers();
    }

    public void buyUtility(Utility utility) {
        if (utility.getPrice() > money) {

            gui.getTextArea().setText("Not enough money to buy this utility");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!");

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

            //System.out.println("Not enough money to pay rent. Transferring assets and going bankrupt.");

            money = 0;
            playerbankrupted();
            return;
        }
        notifyObservers();
    }

    public void transferAssetsBank() {
        for (City city : ownedCities) {
            city.setOwner(null);
        }

        for (Utility utility : ownedUtilities) {
            utility.setOwner(null);
        }
    }

    public void transferAssets(Player receiver) {
        for (City city : ownedCities) {
            city.setOwner(receiver);
        }

        for (Utility utility : ownedUtilities) {
            utility.setOwner(receiver);
        }

        receiver.setJailCards(receiver.getJailCards() + this.getJailCards());
    }

    private void playerbankrupted(){
        System.out.println(name + " is Bankrupted!");
        gui.getTextArea().setText(name + " is Bankrupted!");
        Game.gameOver(gui);
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

    public void leaveJail() { inJail = false; }

    // displays the player's money before and after purchasing the utility
    public boolean wantToBuyUtility(Utility utility) {
        Scanner scanner = new Scanner(System.in);

        gui.getTextArea().setText("Current Money: $" + money + ".");

        gui.getTextArea().append(" Do you want to buy " + utility.getName() + " for $" + utility.getPrice() + "? (Y/N)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("y")) {
            int remainingMoney = money - utility.getPrice();

            gui.getTextArea().setText("Remaining Money: $" + remainingMoney);
            return true;
        } else {
            return false;
        }
    }
    // displays the player's money before and after purchasing the city
    public boolean wantToBuyCity(City city){
        Scanner scanner = new Scanner(System.in);

        gui.getTextArea().setText("Current Money: $" + money +".");

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

    public boolean ownsSameColor(City city) {
        for (City i : ownedCities) {
            if (i.getColor() == city.getColor()) return true;
        }

        return false;
    }

    public void buyHouse(City city) {
        payRent(city.getHouseCost());
//        city.addHouse();
    }

    public void buyHotel(City city) {
        payRent(city.getHouseCost());
        city.addHotel();
    }

    public boolean makeDecision() {
        return true;
    }

}
