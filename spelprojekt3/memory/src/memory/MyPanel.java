package memory;

import java.awt.Graphics; 
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MyPanel extends JPanel {

	private static Image background;

	public MyPanel(){}

	public void paintComponent(Graphics g ) {
		super.paintComponent(g);

		g.drawImage( background, 5, 5, SingletonView.getBoardDimension(), SingletonView.getBoardDimension(), this );
	}

	public static void setImage( ImageIcon imgIcon ) {
		background = imgIcon.getImage();
	}
}