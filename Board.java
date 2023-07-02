import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<City> cities;

    public Board() {
        cities = new ArrayList<>();
        initializeCities();
    }

    private void initializeCities() {
        // Here, let's create 10 cities with arbitrary names, prices and rents.
        // In actual game, you would want these values to match the original Monopoly game.
        for(int i = 1; i <= 10; i++) {
            cities.add(new City("City" + i, i * 100, i * 10));
        }
    }

    public City getCity(int position) {
        return cities.get(position);
    }

    public int getSize() {
        return cities.size();
    }
}
