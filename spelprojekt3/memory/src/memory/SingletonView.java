package memory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;


public class SingletonView extends JFrame implements ActionListener {

	private static JPanel mainPanel, namesPanel;
	private static MyPanel boardPanel;
	private JLabel player1Label, player2Label, score1Label, score2Label;
	private static int cellDimension=8;
	private static int boardDimension, fontSize;
	private static ImageIcon titleIcon, backGIcon;
	private static JButton[][] emojiButtons;
	private static final ImageIcon patternIcon = new ImageIcon( SingletonView.class.getResource("/pattern.png"));
	private ArrayList <Integer> iconsNames;
	private static ArrayList <Integer> currentIconsNames;
	private int randomNumber;
	private Icon image;


	private static SingletonView view = null;

	private SingletonView(){}	


	public static SingletonView getInstance(){
		if (view==null)
			view = new SingletonView();
		return view;
	}

	public void setBoard(int noOfCells, String player1, String player2, String player3){

		switch (noOfCells){
		case 4:
			cellDimension=4;
			setBoardDimension(300);
			fontSize = 20;
			break;
		case 6:
			cellDimension=6;
			setBoardDimension(450);
			fontSize=25;
			break;
		case 8:
			cellDimension=8;
			setBoardDimension(600);
			fontSize=30;
			break;
		case 10:
			cellDimension=10;
			setBoardDimension(730);
			fontSize=35;
			break;
		default:
			break;
		}

		mainPanel = new JPanel(); 
		boardPanel = new MyPanel();
		namesPanel = new JPanel();

		titleIcon = new ImageIcon( SetupView.class.getResource("/46.png"));		

		namesPanel.setAlignmentX(CENTER_ALIGNMENT);
		namesPanel.setPreferredSize(new Dimension(getBoardDimension(), getBoardDimension()/3));
		namesPanel.setLayout(new GridLayout(2, 2));
		namesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" P L A Y E R S     &     S C O R E S ", TitledBorder.CENTER, TitledBorder.TOP));

		player1Label = makeLabel("N");
		player1Label.setText(player1);
		player1Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score1Label = makeLabel("S");
		score1Label.setText(""+0);
		score1Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player1Label);

		player2Label = makeLabel("N");
		player2Label.setVerticalAlignment(SwingConstants.BOTTOM);
		score2Label = makeLabel("S");
		score2Label.setVerticalAlignment(SwingConstants.NORTH);
		namesPanel.add(player2Label);
		namesPanel.add(score1Label);
		namesPanel.add(score2Label);

		if (player3=="Computer"){
			player2Label.setText(player3);
			score2Label.setText(""+0);
		}
		else if (SetupView.getListModel().getSize()==2){
			player2Label.setText(player2);
			score2Label.setText(""+0);
		}

		backGIcon = getRandomBackGIcon();
		backGIcon = resizeImage(backGIcon, getBoardDimension(), getBoardDimension());

		MyPanel.setImage(backGIcon);
		boardPanel.setLayout(new GridLayout(cellDimension, cellDimension));
		boardPanel.setVisible(true);
		boardPanel.setPreferredSize(new Dimension(getBoardDimension(), getBoardDimension()));
		boardPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				" M E M O R Y      G A M E ", TitledBorder.CENTER, TitledBorder.TOP));

		currentIconsNames = new ArrayList<>(cellDimension*cellDimension);

		iconsNames = new ArrayList<>();
		for(int i=1; i<73; i++){
			iconsNames.add(i);
			System.out.println("IconNames before Shuffle"+i);
		}
		Collections.shuffle(iconsNames);


		iconsNames = new ArrayList<Integer>(iconsNames.subList(0, 72-(72-(cellDimension*cellDimension/2))));
		for(Integer number: iconsNames){
			System.out.println("SubArray iconNames" + number);
		}

		for (int n=0; n<iconsNames.size(); n++){
			currentIconsNames.add(iconsNames.get(n));
			currentIconsNames.add(iconsNames.get(n));
		}
		Collections.shuffle(currentIconsNames);

		for(Integer number: currentIconsNames)
			System.out.println("duplicates" +number);


		emojiButtons = new JButton[cellDimension][cellDimension];
		for(int i=0; i<cellDimension; i++){
			for(int j=0; j<cellDimension; j++){
				emojiButtons[i][j] = new JButton(patternIcon);
				boardPanel.add(emojiButtons[i][j]);
			}
		}
		//Collections.shuffle(Arrays.asList(emojiButtons));

		UIManager.put("ToggleButton.select", Color.BLACK);
		SwingUtilities.updateComponentTreeUI(this);



		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(boardPanel);
		mainPanel.add(namesPanel);

		view.add(mainPanel);
		view.pack();
		view.setPreferredSize(new Dimension(getBoardDimension(), getBoardDimension()+100));
		view.setIconImage(titleIcon.getImage());
		view.setLocationRelativeTo(null);
		view.setDefaultCloseOperation(EXIT_ON_CLOSE);
		view.setResizable(false);
		view.setVisible(true);
	}


	public void setCurrentIconsNames(ArrayList<Integer> currentIconsNames) {
		SingletonView.currentIconsNames = currentIconsNames;
	}


	public static int getCellDimension() {
		return cellDimension;
	}


	public static ImageIcon resizeImage(ImageIcon defaultImage, int width, int height){

		Image img = defaultImage.getImage();
		Image newImg = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
		defaultImage = new ImageIcon(newImg);

		return defaultImage;
	}
	public static void setBoardAndFont(int size){}

	public void removeButton(int x, int y){
		emojiButtons[x][y].setVisible(false);
		System.out.println("set visible false");
		boardPanel.revalidate();
	}

	public void closeButtons(int x, int y){
		image = new ImageIcon(SingletonView.class.getResource("/pattern.png"));
		emojiButtons[x][y].setIcon(image);
		//emojiButtons[x][y].setSelected(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public static int getBoardDimension() {
		return boardDimension;
	}

	public static void setBoardDimension(int boardDimension) {
		SingletonView.boardDimension = boardDimension;
	}

	public JLabel getPlayer1Label() {
		return player1Label;
	}

	public void setPlayer1Label(JLabel player1Label) {
		this.player1Label = player1Label;
	}

	public JLabel getPlayer2Label() {
		return player2Label;
	}

	public void setPlayer2Label(JLabel player2Label) {
		this.player2Label = player2Label;
	}

	public JLabel getScore1Label() {
		return score1Label;
	}

	public void setScore1Label(int newScore, int tries) {
		this.score1Label.setText(newScore+" out of " + tries);
	}

	public JLabel getScore2Label() {
		return this.score2Label;
	}

	public void setScore2Label(int newScore, int tries) {
		this.score2Label.setText(newScore+" out of "+ tries);
	}

	private JLabel makeLabel(String sort){

		JLabel temp = new JLabel();

		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setOpaque(true);
		temp.setBackground(Color.black);
		temp.setFont((new Font("dialog", Font.BOLD, fontSize)));
		if (sort=="S")
			temp.setForeground(Color.ORANGE);
		else if (sort=="N")
			temp.setForeground(Color.GRAY);
		else
			System.out.println("shouldn't apear");
		return temp;
	}

	private ImageIcon getRandomBackGIcon(){
		int big=75, small=73, random;
		Random rand = new Random();
		ImageIcon icon;

		random = (rand.nextInt(big + 1 - small) + small);
		icon = new ImageIcon(SingletonView.class.getResource("/" + random + ".jpg"));

		return icon;
	}


	public void addSingletonViewListener(ActionListener BottonListener){
		for(int i=0; i<cellDimension; i++){
			for (int j=0; j<cellDimension; j++){
				emojiButtons[i][j].addActionListener(BottonListener);			
			}
		}	
	}

	public static JButton[][] getEmojiButtons() {
		return emojiButtons;
	}

	public JButton getEmojiButton(int i, int j){
		return emojiButtons[i][j];

	}

	public static ArrayList<Integer> getCurrentIconsNames() {
		return currentIconsNames;
	}
}