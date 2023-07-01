import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Space> theBoard;

    public Board() {
        theBoard = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        theBoard.add(0, new OtherSpace()); // start square
        theBoard.add(1, new City("City 1", 60, 10));
        theBoard.add(2, new ActionCard()); // chance
        theBoard.add(3, new City("City 2", 60, 10));
        theBoard.add(4, new OtherSpace()); // income tax
        theBoard.add(5, new City("City 3", 60, 10));
        theBoard.add(6, new City("City 4", 60, 10));
        theBoard.add(7, new City("City 5", 60, 10));
        theBoard.add(8, new City("City 6", 60, 10));
        theBoard.add(9, new City("City 7", 60, 10));
        theBoard.add(10, new City("City 8", 60, 10));
        theBoard.add(11, new City("City 9", 60, 10));
        theBoard.add(12, new City("City 10", 60, 10));
    }

    public Space getPosition(int position) {
        return theBoard.get(position);
    }

    public int getSize() {
        return theBoard.size();
    }
}
