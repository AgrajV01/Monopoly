import javax.swing.*;
import java.awt.*;
public class PlayerStats extends PlayerStatMenu{


	public PlayerStats(int i){
		super(i);
	}
	
	public PlayerStats clone(){
		return this;
	}

//	public void initializeImage(int i){
//		originalIcon = new ImageIcon(getClass().getResource("S" + i + ".png")); // get StatDisplay image
//	}

	public void setMoneyLabel(int money) {

        String moneyString = String.valueOf(money);
        moneyLabel.setText(moneyString);
    }

    public void setPropertyLabel(int size) {
        String numProperties = String.valueOf(size);
        propertyLabel.setText(numProperties);
    }
    public JLabel getMoneyLabel() {
        return super.moneyLabel;
    }

    public JLabel getPropertyLabel() {
        return super.propertyLabel;
    }
}
