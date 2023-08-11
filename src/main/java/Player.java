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

/**
 * The Player class represents a player in the Monopoly game.
 * It holds information about the player's name, money, position, owned cities,
 * jail status, and jail cards.
 */
public class Player {
    private City onCity;
    private Utility onUtility;
    private List<City> ownedCities;

    public static class PlayerState{

        public PlayerState() { }
        public String name;
        public int positionDiff;
        public int money;
        public int position;
        public int consecutiveMoves;
        public boolean isBankrupted;
        public boolean inJail;
        public int jailCards; // number of get out of jail free cards this player has
    }

    private PlayerState state;

    private GUI2 gui; // added this to be able to print elements on screen

    private List<Utility> ownedUtilities;

  

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

    public void saveState() {

        try{
            ObjectMapper mapper = new ObjectMapper();

            String saveString = mapper.writeValueAsString(this.state);

            FileWriter f = new FileWriter(state.name+".json");
            f.write(saveString);
            f.close();
        }
        catch(Exception e)
        {
            System.out.print("Exception: ");
            System.out.println(e);

        }
    }

    public static Player loadPlayer(String name, GUI2 gui2) {

        ObjectMapper mapper = new ObjectMapper();

        try{
            PlayerState s = mapper.readValue(new File(name +".json"), PlayerState.class);
            return new Player(s, gui2);
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }

    }

    public Player(PlayerState state, GUI2 gui) {

        this.state = state;
        this.ownedCities = new ArrayList<>();
        this.ownedUtilities = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.onCity = null;
        this.onUtility = null;
        this.gui = gui; // gui object is passed to constructor, we can print to textArea from this class
        this.subscribers = new ArrayList<>();

    }

    
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
        this.state = new PlayerState();
        this.state.name = name;
        this.state.money = money; // Starting money in Monopoly
        this.state.position = position; // Starting at 'GO'
        this.state.positionDiff = positionDiff;
        this.state.consecutiveMoves = consecutiveMoves;
        this.ownedCities = ownedCities;
        this.ownedUtilities = ownedUtilities;
        this.state.jailCards = jailCards;
        this.state.isBankrupted = isBankrupted;
        this.onCity = onCity;
        this.onUtility = onUtility;
        this.state.inJail = inJail;
        this.subscribers = subscribers;
    }

    //
    public Player(String name, int money, GUI2 gui) {
        this.state = new PlayerState();
        this.state.name = name;
        this.state.money = money; // Starting money in Monopoly
        this.state.position = 0; // Starting at 'GO'
        this.state.positionDiff = 0;
        state.consecutiveMoves = 0;
        this.ownedCities = new ArrayList<>();
        this.ownedUtilities = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.state.jailCards = 0;
        this.state.isBankrupted = false;
        this.onCity = null;
        this.onUtility = null;
        state.inJail = false;
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
        return state.name;
    }

    public int getMoney() {
        return state.money;
    }



    public void setMoney(int money) {
        this.state.money = money;
    }

    public int getPosition() {
        return state.position;
    }

    public int getPositionDiff() { return state.positionDiff; }
    public int getConsecutiveMoves() { return state.consecutiveMoves; }
    public void setConsecutiveMoves(int count) { state.consecutiveMoves = count; }
    public boolean getIsBankrupted(){
        return state.isBankrupted;
    }
    public void setIsBankrupted(boolean ans){
        state.isBankrupted = ans;
    }
    public void setPosition(int position) { this.state.position = position; }

    public void sendToJail() {
//        setPosition(10);
//        state.inJail = true;

        // Notify the player about being sent to jail before the delay


        gui.getTextArea().setText(state.name + " is being sent to jail!");


        // Introduce a delay of 2 seconds (2000 milliseconds) before sending the player to jail
        int delayMilliseconds = 0;
        Timer timer = new Timer(delayMilliseconds, (ActionEvent e) -> {

            // Notify the player again after the delay


            gui.getTextArea().setText(state.name + " has been sent to jail!");

        });


        //setPosition(10);
        state.inJail = true;

        // Start the timer
        timer.setRepeats(false);
        timer.start();
    }
    public List<City> getOwnedCities() {
        return ownedCities;
    }

    public void move(int steps) {
        if (state.inJail) {
            state.inJail = false;
            state.consecutiveMoves = 0;
        }

        state.consecutiveMoves++;

        int temp = state.position;
        state.position = Math.floorMod(state.position + steps, 40);  // Assuming the board size is 40
        state.positionDiff = Math.abs(state.position - temp);
        state.inJail = false; // remove this line once the jail delay is set correctly
        if (steps > 0 && state.position < temp) {


            gui.getTextArea().setText("You have passed Go! You collect 200$.\n");

            state.money += 200;
        }
        notifyObservers();
    }


    public void buyCity(City city) {
        if(city.getPrice() > state.money) {

            gui.getTextArea().setText("Not enough money to buy this city");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!\n");
            return;
        }
        state.money -= city.getPrice();
        ownedCities.add(city);
        city.setOwner(this);
        notifyObservers();
    }

    public void buyUtility(Utility utility) {
        if (utility.getPrice() > state.money) {

            gui.getTextArea().setText("Not enough money to buy this utility");
            if(gui.getTutor())
                gui.getTextArea().append("\nYou can earn more money by collecting rent, " +
                        "passing go, or drawing community chest cards!");

            return;
        }
        state.money -= utility.getPrice();
        ownedUtilities.add(utility);
        utility.setOwner(this);
        notifyObservers();
    }

    public void payRent(int rent) {
        if(state.money >= rent) {
            state.money -= rent;
        } else {
            /*
            System.out.println("Not enough money to pay rent. Transferring assets and going bankrupt.");
            for(City city : ownedCities) {
                city.getOwner().receiveRent(city.getPrice());
                city.setOwner(null);
            }
             */
            state.money =0;
            playerbankrupted();
            return;
        }
        notifyObservers();
    }
    private void playerbankrupted(){
        System.out.println(state.name + " is Bankrupted!");
        gui.getTextArea().setText(state.name + " is Bankrupted!");
        //Game.gameOver();

        Game.gameOver();
        notifyGameOver();
    }

    public void receiveRent(int rent) {
        state.money += rent;
        notifyObservers();
    }

    public int getJailCards() {
        return state.jailCards;
    }

    public void setJailCards(int count) {
        state.jailCards = count;
        notifyObservers();
    }

    public boolean getJailState() { return state.inJail; }

    // displays the player's money before and after purchasing the utility
    public boolean wantToBuyUtility(Utility utility) {
        Scanner scanner = new Scanner(System.in);

        gui.getTextArea().setText("Current Money: $" + state.money + ".");

        gui.getTextArea().append(" Do you want to buy " + utility.getName() + " for $" + utility.getPrice() + "? (Y/N)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("y")) {
            int remainingMoney = state.money - utility.getPrice();

            gui.getTextArea().setText("Remaining Money: $" + remainingMoney);
            return true;
        } else {
            return false;
        }
    }
    // displays the player's money before and after purchasing the city
    public boolean wantToBuyCity(City city){
        Scanner scanner = new Scanner(System.in);

        gui.getTextArea().setText("Current Money: $" + state.money +".");

        gui.getTextArea().append(" Do you want to buy " + city.getName() + " for $" + city.getPrice() + "? (Y/N)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("y")) {
            int remainingMoney = state.money - city.getPrice();
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
        if (city.getHouseCost() * count > state.money) {
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
        if ((city.getHouseCost() * 5) - (city.getHouseCost() * city.getNumHouses()) > state.money) {
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
