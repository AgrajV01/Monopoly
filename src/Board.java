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
        theBoard.add(1, new City("City 1", 60, 10));
        theBoard.add(2, new ActionCard("Chance")); // chance
        theBoard.add(3, new City("City 2", 60, 10));
        theBoard.add(4, new OtherSpace("Income tax")); // income tax
        theBoard.add(5, new City("City 3", 60, 10));
        theBoard.add(6, new City("City 4", 60, 10));
        theBoard.add(7, new City("City 5", 60, 10));
        theBoard.add(8, new City("City 6", 60, 10));
        theBoard.add(9, new City("City 7", 60, 10));
        theBoard.add(10, new City("City 8", 60, 10));
        theBoard.add(11, new City("City 9", 60, 10));
        theBoard.add(12, new City("City 10", 60, 10));
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
