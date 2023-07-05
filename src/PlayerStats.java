import javax.swing.*;
import java.awt.*;
public class PlayerStats extends PlayerStatMenu{


	public PlayerStats(){

		super();
	}
	
	public PlayerStats clone(){
		return new PlayerStats();	
	}

	public void initializeImage(int i){
		originalIcon = new ImageIcon(getClass().getResource("S" + i + ".png")); // get StatDisplay image
	}

	public void setMoneyLabel(int money) {

        String moneyString = String.valueOf(money);
        moneyLabel.setText(moneyString);
    }

    public void setPropertyLabel(int size) {
        String numProperties = String.valueOf(size);
        propertyLabel.setText(numProperties);
    }
}
