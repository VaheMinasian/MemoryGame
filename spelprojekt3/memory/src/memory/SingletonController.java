package memory;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import memory.Card.CardState;
import memory.MemoryModel;

public class SingletonController {

	private static SingletonController singletonControlObject = null;

	private static SingletonView singletonView;
	private static SetupView setupView;
	private static MemoryModel singletonModel;
	private static Timer timer;
	private static String doSwitch;
	private static int xIndexComp;
	private static int yIndexComp;
	private boolean lostTurn;

	public static SingletonController getInstance(Game model, SetupView setup, SingletonView view){

		if (singletonControlObject==null) {

			singletonControlObject = new SingletonController();

			setupView = setup;
			singletonView=view;
			singletonModel=(MemoryModel)model;

			setupView.addSetupListener(new SetupListener());
		}
		return  singletonControlObject;
	}

	private SingletonController(){}


	static class SetupListener implements ActionListener {

		String player1, player2, player3;   

		public void actionPerformed(ActionEvent e){

			if(e.getSource()== setupView.getStartGame()){

				//Play in solo mode
				if(setupView.getSoloButton().isSelected()) {
					if(SetupView.getListModel().getSize()!=1){
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "You should have only one name!", "Memory",
								JOptionPane.INFORMATION_MESSAGE);	
						return;
					}
					player1 = SetupView.getListModel().getElementAt(0);
					Player soloPlayer = new HumanPlayer(player1);	
					soloPlayer.setActive(true);
					//MemoryModel.sets(soloPlayer);
					MemoryModel.addToPlayersList(soloPlayer);
				} 
				// Play against computer
				if (setupView.getComputerButton().isSelected()){
					if(SetupView.getListModel().getSize()!=1){
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "You should have only one name!", "Memory",
								JOptionPane.INFORMATION_MESSAGE);	
						return;
					}

					player1 = SetupView.getListModel().getElementAt(0);
					player3 = "Computer";
					Player humanPlayer = new HumanPlayer(player1);
					Player computerPlayer = new ComputerPlayer(player3);
					humanPlayer.setActive(true);
					MemoryModel.addToPlayersList(humanPlayer);
					MemoryModel.addToPlayersList(computerPlayer);					

				}
				//Play between 2 human
				else if (setupView.getHumanButton().isSelected()){
					if(SetupView.getListModel().getSize()!=2){
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "Please register 2 player names.", "Memory",
								JOptionPane.INFORMATION_MESSAGE);	
						return;
					}
					player1 = SetupView.getListModel().getElementAt(0);
					player2 = SetupView.getListModel().getElementAt(1);
					Player humanPlayer1 = new HumanPlayer(player1);
					Player humanPlayer2 = new HumanPlayer(player2);
					humanPlayer1.setActive(true);
					MemoryModel.addToPlayersList(humanPlayer1);
					MemoryModel.addToPlayersList(humanPlayer2);

				}

				//	singletonView.setPlayerNames(player1, player2, player3);

				if(setupView.getSqrFour().isSelected()) {
					singletonView.setBoard(4,player1, player2, player3 );
					MemoryModel.createCards(4);
				}
				else if (setupView.getSqrSix().isSelected()){
					singletonView.setBoard(6,player1, player2, player3);
					MemoryModel.createCards(6);
				}
				else if (setupView.getSqrEight().isSelected()){
					singletonView.setBoard(8,player1, player2, player3);
					MemoryModel.createCards(8);
				}
				else if (setupView.getSqrTen().isSelected()){
					singletonView.setBoard(10,player1, player2, player3);
					MemoryModel.createCards(10);
				}

				//singletonView.repaint();
				singletonView.addSingletonViewListener(new GameListener());
				setupView.frame.dispose();
			}
		}

		static class GameListener implements ActionListener {
			final String ISMATCHED = "Match";
			public void actionPerformed(ActionEvent e){
				//Double loop to find the klicked button
				for (int i = 0; i<SingletonView.getEmojiButtons().length; i++){
					for (int j = 0; j<SingletonView.getEmojiButtons()[i].length; j++){
						//selecting the card/button clicked
						if (e.getSource() == SingletonView.getEmojiButtons()[i][j]){
							MemoryModel.setSelectedCard(MemoryModel.getCards()[i][j]);

							try{
								buttonIsOpen();

								//controls if card will be opened, and if yes opens it and does necesary asignments
								//for Card and it's icon.

								if (((singletonModel.getSecondNumber())== 0)&&(singletonModel.move(i, j))){
									//if(singletonModel.move(i, j));

									updateCardBoard(i,j);

									if (singletonModel.getSecondNumber()!=0){

										doSwitch = updateModel(singletonModel.getStatus(i, j));
										updateScoreBoard();
										if(doSwitch.equals("switch"))
											switchActivePlayer();
									}
								}

								if(singletonModel.getActivePlayer().getName().equals("Computer")){
									
									timer = new Timer(1000, PlayTheComputer);
									timer.setRepeats(false);
									timer.start();
								}
							}
							catch (ButtonNotAvailableException e1) {
								System.err.println("IndexOutOfBoundsException: " + e1.getMessage());
							}
						}
					}
				}
			}
		}
		static String updateModel(String s){
			if (s.equals("Match")) {
				if(singletonModel.getActivePlayer().getName().equals("Computer")){
					timer = new Timer(200, RemoveCards);
					timer.setRepeats(false);
					timer.start();
				}
				else{
					timer = new Timer(800, RemoveCards);
					timer.setRepeats(false);
					timer.start();}
				singletonModel.setFirstNumber(0);
				singletonModel.setSecondNumber(0);
				MemoryModel.setSelectedCard(null);
				singletonModel.getActivePlayer().incrementScore();
				singletonModel.getActivePlayer().incrementTries();
				singletonModel.getFirstCard().updateCard(CardState.NONE);
				singletonModel.getSecondCard().updateCard(CardState.NONE);
				System.out.println("inside match of updateModel()\n");
				return "0";}	
			else if(!s.equals("Match")){
				if(singletonModel.getActivePlayer().getName().equals("Computer")){
					timer = new Timer(200, CloseCards);
					timer.setRepeats(false);
					timer.start();
				}
				else{
					timer = new Timer(800, CloseCards);
					timer.setRepeats(false);
					timer.start();}
				singletonModel.setFirstNumber(0);
				singletonModel.setSecondNumber(0);
				singletonModel.getActivePlayer().incrementTries();
				MemoryModel.setSelectedCard(null);

				//MemoryModel.getSelectedCard().updateCard(CardState.CLOSED);
				singletonModel.getFirstCard().updateCard(CardState.CLOSED);
				singletonModel.getSecondCard().updateCard(CardState.CLOSED);
				System.out.println("inside mismatch of updateModel()\n");	
				return "switch";
			}return "switch";
		}

		static void updateScoreBoard(){
			if((singletonModel.getActivePlayer().getName()).equals((singletonModel.getPlayers().get(0).getName()))) 
				singletonView.setScore1Label(singletonModel.getActivePlayer().getScore(), singletonModel.getActivePlayer().getTries());					
			else
				singletonView.setScore2Label(singletonModel.getActivePlayer().getScore(), singletonModel.getActivePlayer().getTries());					
		}

		static ActionListener PlayTheComputer = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				do {
					singletonControlObject.lostTurn=true;
				System.out.println("inside computer block");
				for(int c=0; c<2; c++){
				if(!singletonModel.gameOver()&&(singletonModel.getActivePlayer().play())){
						xIndexComp = singletonModel.getActivePlayer().getCardIndexX();
						yIndexComp = singletonModel.getActivePlayer().getCardIndexY();
						System.out.println("after assigning x and y are " +xIndexComp + " and "+yIndexComp);
						MemoryModel.setSelectedCard(MemoryModel.getCards()[xIndexComp][yIndexComp]);
						if (((singletonModel.getSecondNumber())== 0)&&(singletonModel.move(xIndexComp, yIndexComp)))
							updateCardBoard(xIndexComp,yIndexComp);
						if (singletonModel.getSecondNumber()!=0){
							doSwitch = updateModel(singletonModel.getStatus(xIndexComp, yIndexComp));
							updateScoreBoard();
							if(doSwitch.equals("switch"))
								switchActivePlayer();
							else
								singletonControlObject.lostTurn=false;
						}
					}
				}
			}while(!singletonControlObject.lostTurn);
			}
		};	

		static void updateCardBoard(int i, int j){
			Icon image;
			int iconName;
			iconName = SingletonView.getCurrentIconsNames().get(i*SingletonView.getCellDimension()+j);
			image = new ImageIcon(SingletonController.class.getResource("/" + iconName + ".png"));
			singletonView.getEmojiButton(i,j).setIcon(image);
			System.out.println("status inside updateCardBoard()" + singletonModel.getStatus(i, j)+"\n");
		}

		public static void buttonIsOpen() throws ButtonNotAvailableException {
			if (singletonModel.getFirstCard().equals(singletonModel.getSelectedCard())&&(singletonModel.getCounter()!=0)){
				System.out.println(singletonModel.getFirstCard().equals(singletonModel.getSelectedCard()));
				throw new ButtonNotAvailableException("You can't click on an open card!\n");
			}
		}

		public static void switchActivePlayer(){
			if (singletonModel.getPlayers().size()>1){
				System.out.println("Players switched");
				if(singletonModel.getPlayers().indexOf( singletonModel.getActivePlayer())==0){
					singletonModel.getPlayers().get(1).setActive(true);
					singletonModel.getActivePlayer().setActive(false);
				}
				else{
					singletonModel.getPlayers().get(0).setActive(true);
					singletonModel.getPlayers().get(1).setActive(false);
				}
			}
		}

		static ActionListener RemoveCards = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println(" Inside Remove Cards: firstX = "+ singletonModel.getButtonsIndex()[0] + ", firstY = "+ 
						singletonModel.getButtonsIndex()[1] + "  -  secondX = "+ singletonModel.getButtonsIndex()[2] + ", secondY = "+ singletonModel.getButtonsIndex()[3]);
				singletonView.removeButton(singletonModel.getButtonsIndex()[0],singletonModel.getButtonsIndex()[1]);
				singletonView.removeButton(singletonModel.getButtonsIndex()[2],singletonModel.getButtonsIndex()[3]);
				singletonModel.nullifyButtonsIndex();
				singletonModel.resetCounter();
				if(singletonModel.gameOver()){
					System.out.println("game over method working");
					singletonModel.getMessage();
				}
				System.out.println("Outside Remove Cards\n");
			}
		};
		static ActionListener CloseCards = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println(" Inside Close Cards: firstX = "+ singletonModel.getButtonsIndex()[0] + ", firstY = "+ 
						singletonModel.getButtonsIndex()[1] + "  -  secondX = "+ singletonModel.getButtonsIndex()[2] + ", secondY = "+ singletonModel.getButtonsIndex()[3]);
				singletonView.closeButtons(singletonModel.getButtonsIndex()[0],singletonModel.getButtonsIndex()[1]);
				singletonView.closeButtons(singletonModel.getButtonsIndex()[2],singletonModel.getButtonsIndex()[3]);
				singletonModel.nullifyButtonsIndex();
				singletonModel.resetCounter();
				System.out.println("Outside Close Cards\n");
				System.out.println(" Outside Close Cards: firstX = "+ singletonModel.getButtonsIndex()[0] + ", firstY = "+ 
						singletonModel.getButtonsIndex()[1] + "  -  secondX = "+ singletonModel.getButtonsIndex()[2] + ", secondY = "+ singletonModel.getButtonsIndex()[3]);
				System.out.println("FIRST CARD STATE "+singletonModel.getFirstCard().getState() + ", SECOND CARD STATE " + singletonModel.getSecondCard().getState()+""); 
				System.out.println(singletonModel.getFirstCard().equals(singletonModel.getSelectedCard()));

			}
		};	
	}
}