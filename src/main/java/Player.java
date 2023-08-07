import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Scanner;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private boolean isBankrupted;
    private List<City> ownedCities;

    @JsonIgnore
    private GUI2 gui; // added this to be able to print elements on screen

    private List<Utility> ownedUtilities;

    private boolean inJail;

    private int jailCards; // number of get out of jail free cards this player has

    @JsonIgnore
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
    @JsonIgnore
    public String getType(){
        return "Player";
    }
    @JsonIgnore
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

    public void saveState() {

        try{
            ObjectMapper mapper = new ObjectMapper();

            String saveString = mapper.writeValueAsString(this);

            FileWriter f = new FileWriter(name+".json");
            f.write(saveString);
            f.close();
        }
        catch(Exception e)
        {
            System.out.print("Exception: ");
            System.out.println(e);

        }
    }

    public static Player loadPlayer(String name) {

        ObjectMapper mapper = new ObjectMapper();

        try{
            return mapper.readValue(new File(name+".json"), Player.class);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }

    }

    public Player(Player o) {
        this.name = o.name;
        this.money = o.money; // Starting money in Monopoly
        this.position = o.position; // Starting at 'GO'
        this.positionDiff = o.positionDiff;
        consecutiveMoves = 0;
        this.ownedCities = o.ownedCities;
        this.ownedUtilities = o.ownedUtilities;
        this.jailCards = o.jailCards;
        this.isBankrupted = o.isBankrupted;
        this.onCity = o.onCity;
        this.onUtility = o.onUtility;
        inJail = false;
    }

    public Player() {super();}
    public Player(
        String name,
        City onCity,
        Utility onUtility,
        int money,
        int position,
        int positionDiff,
        int consecutiveMoves,
        boolean isBankrupted,
        List<City> ownedCities,
        GUI2 gui, // added this to be able to print elements on screen
        List<Utility> ownedUtilities,
        boolean inJail,
        int jailCards, // number of get out of jail free cards this player has
        List<PlayerObserver> subscribers
    ) {
        this.name = name;
        this.money = money; // Starting money in Monopoly
        this.position = position; // Starting at 'GO'
        this.positionDiff = positionDiff;
        this.consecutiveMoves = consecutiveMoves;
        this.ownedCities = ownedCities;
        this.ownedUtilities = ownedUtilities;
        this.jailCards = jailCards;
        this.isBankrupted = isBankrupted;
        this.onCity = onCity;
        this.onUtility = onUtility;
        this.inJail = inJail;
        this.subscribers = subscribers;
    }

    //
    public Player(String name, int money, GUI2 gui) {
        this.name = name;
        this.money = money; // Starting money in Monopoly
        this.position = 0; // Starting at 'GO'
        this.positionDiff = 0;
        consecutiveMoves = 0;
        this.ownedCities = new ArrayList<>();
        this.ownedUtilities = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.jailCards = 0;
        this.isBankrupted = false;
        this.onCity = null;
        this.onUtility = null;
        inJail = false;
        this.gui = gui; // gui object is passed to constructor, we can print to textArea from this class
        this.subscribers = new ArrayList<>();

    }

    public void subscribe(PlayerObserver p){
        subscribers.add(p);
        notifyObservers();
    }

    public void notifyObservers(){
        if (subscribers == null)
            return;
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

    public void sendToJail() {
//        setPosition(10);
//        inJail = true;

        // Notify the player about being sent to jail before the delay


        gui.getTextArea().setText(name + " is being sent to jail!");


        // Introduce a delay of 2 seconds (2000 milliseconds) before sending the player to jail
        int delayMilliseconds = 0;
        Timer timer = new Timer(delayMilliseconds, (ActionEvent e) -> {

            // Notify the player again after the delay


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
        if (inJail) {
            inJail = false;
            consecutiveMoves = 0;
        }

        consecutiveMoves++;

        int temp = position;
        position = Math.floorMod(position + steps, 40);  // Assuming the board size is 40
        positionDiff = Math.abs(position - temp);
        inJail = false; // remove this line once the jail delay is set correctly
        if (steps > 0 && position < temp) {


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

    @JsonIgnore
    public boolean getJailState() { return inJail; }

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

    public void buyHouse(City city, int count) {
        if (city.getHouseCost() * count > money) {
            gui.getTextArea().append("You cannot afford that many houses!");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!");
        }

        else {
            payRent(city.getHouseCost() * count);
            city.addHouses(count);
        }
    }

    public void buyHotel(City city) {
        // if price of hotel (5 houses - # of current houses) exceeds player balance
        if ((city.getHouseCost() * 5) - (city.getHouseCost() * city.getNumHouses()) > money) {
            gui.getTextArea().append("You cannot afford a hotel!");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!");
        }

        else {
            payRent((city.getHouseCost() * 5) - (city.getHouseCost() * city.getNumHouses()));
            city.addHotel();
        }
    }

    public boolean makeDecision() {
        return true;
    }

}
