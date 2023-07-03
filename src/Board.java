import java.util.ArrayList;
import java.util.List;
/**
 * The Board class represents the game board in the Monopoly game.
 * It contains a list of spaces that make up the board and provides methods
 * to access and initialize the board.
 */
public class Board {
    private List<Space> theBoard;
    /**
     * Constructs a Board object and initializes the game board.
     */
    public Board() {
        theBoard = new ArrayList<>();
        initializeBoard();
    }
    /**
     * Initializes the game board by adding spaces in a specific order.
     */
    private void initializeBoard() {
        theBoard.add(0, new OtherSpace("Go!")); // start square
        theBoard.add(1, new City("Mediterranean Avenue", 60, 2, 50));
        theBoard.add(2, new ActionCard("Community Chest")); // chance
        theBoard.add(3, new City("Baltic Avenue", 60, 4, 50));
        theBoard.add(4, new OtherSpace("Income tax")); // income tax
        theBoard.add(5, new Utility("Reading Railroad", 200, 25));
        theBoard.add(6, new City("Oriental Avenue", 100, 6, 50));
        theBoard.add(7, new OtherSpace("Chance"));
        theBoard.add(8, new City("Vermont Avenue", 100, 6, 50));
        theBoard.add(9, new City("Connecticut Avenue", 120, 8, 50));
        theBoard.add(10, new OtherSpace("Jail"));
        theBoard.add(11, new City("St. Charles Place", 140, 10, 100));
        theBoard.add(12, new Utility("Electrical Company", 150, 4));
        theBoard.add(13, new City("States Avenue", 140, 10, 100));
        theBoard.add(14, new City("Virginia Avenue", 160, 12, 100));
        theBoard.add(15, new Utility("Pennsylvania Railroad", 200, 25));
        theBoard.add(16, new City("St. James Place", 180, 14, 100));
        theBoard.add(17, new ActionCard("Community Chest"));
        theBoard.add(18, new City("Tennessee Avenue", 180, 14, 100));
        theBoard.add(19, new City("New York Avenue", 200, 16, 100));
        theBoard.add(20, new OtherSpace("Free Parking"));
    }

    /**
     * Returns the space at the specified position on the board.
     *
     * @param position The position of the space on the board.
     * @return The space at the specified position.
     */
    public Space getPosition(int position) {
        return theBoard.get(position);
    }
    /**
     * Returns the size of the game board.
     *
     * @return The size of the game board.
     */
    public int getSize() {
        return theBoard.size();
    }
}
