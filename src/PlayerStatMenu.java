import java.awt.*;
import javax.swing.*;
abstract class PlayerStatMenu {

	JLabel moneyLabel;

   	JLabel propertyLabel;
 	ImageIcon originalIcon;
	Image originalImage;
	Image resizedImage;
	ImageIcon resizedIcon;

	JLabel statDisplay;

	public PlayerStatMenu(){
		originalImage = originalIcon.getImage();
		resizedImage = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
		resizedIcon = new ImageIcon(resizedImage);

		statDisplay = new JLabel(resizedIcon);

		moneyLabel = new JLabel("hi");

        propertyLabel = new JLabel("hi"); // Setting position for property number labels
	}

	public void setPropertyBounds(int x, int y, int w, int h){
		propertyLabel.setBounds(x, y, w, h);
	}
	public void setMoneyBounds(int x, int y, int w, int h){
		moneyLabel.setBounds(x, y, w, h);
	}

	abstract public PlayerStatMenu clone();

}
