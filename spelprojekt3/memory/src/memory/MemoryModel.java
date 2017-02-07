package memory;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import memory.Card.CardState;

public class MemoryModel implements Game{

	private static Card[][] cards;
	private static Card selectedCard;
	private static Card firstCard=null, secondCard=null;
	private static int firstNumber, secondNumber; //Icon file names
	//private static Player currentPlayer;
	private static ArrayList <Player> players = new ArrayList<>();
	private static int[] buttonsIndex = new int[4];
	private int counter=0;
	private int wincheck=0;
	private static int numberOfCells;

	public static void addToPlayersList(Player player) {
		players.add(player);
	}

	public MemoryModel(){
		firstCard = new Card();
		secondCard = new Card();
	}

	@Override
	public boolean move(int i, int j) {
		System.out.println("Entering move'1', counter= " + counter + "\n");
		System.out.println("firstCard.equals(selectedCard) is: " + (!firstCard.equals(selectedCard)));
		if (counter==0){//&&(firstCard.getState() == CardState.CLOSED) && (secondCard.getState() == CardState.CLOSED)){
			//if ((firstCard.getState() == CardState.CLOSED) && (secondCard.getState() == CardState.CLOSED)){
			firstCard = cards[i][j];
			firstNumber = SingletonView.getCurrentIconsNames().get(i*SingletonView.getCellDimension()+j);
			firstCard.updateCard(CardState.OPEN);
			buttonsIndex[0]=i; buttonsIndex[1] = j;
			System.out.println("Buton 1 index is  [" +i+ "]["+j+"]");
			System.out.println("first emoji no. "+ firstNumber + " - second emoji no. " + secondNumber);
			System.out.println("first card is \"" + firstCard.getState() + "\" - second card is \"" + secondCard.getState()+ "\"");
			counter++;
			System.out.println("Leaving move'1', counter= " + counter + "\n");
			return true;
		}

		//if ((firstCard.getState() == CardState.OPEN) && (firstCard != selectedCard) && (secondCard.getState() == CardState.CLOSED)){
		else if ((counter==1)&&(!firstCard.equals(selectedCard))){
			System.out.println("Entering move'2', counter= " + counter + "\n");
			secondCard = cards[i][j];
			secondNumber = SingletonView.getCurrentIconsNames().get(i*SingletonView.getCellDimension()+j);
			secondCard.updateCard(CardState.OPEN);
			buttonsIndex[2]=i; buttonsIndex[3] = j;
			System.out.println("Buton 2 index is [" +i+ "]["+j+"]");			
			System.out.println("first emoji no. "+ firstNumber + " - second emoji no. " + secondNumber);
			System.out.println("first card is \"" + firstCard.getState() + "\" - second card is \"" + secondCard.getState()+ "\"");
			counter++;
			System.out.println("Leaving move'2', counter= " + counter + "\n");
			return true;
		}return false;
	}	

	public int getCounter() {
		return counter;
	}

	@Override
	public String getStatus(int i, int j) {
		System.out.println("Inside getStatus().....firstNumber is: " + firstNumber + "secondNumber is: " + secondNumber);
		System.out.println("Inside getStatus(i,j) before IF....firstNumber=secondNumber is: "+ (MemoryModel.firstNumber==MemoryModel.secondNumber));
		if(MemoryModel.firstNumber==MemoryModel.secondNumber){
			System.out.println("firstnumber = secondnumber(supposedly)  " + (firstNumber==secondNumber));
			System.out.println("(matched supposedly) first number is " + firstNumber + "second number is " + secondNumber);
			return "Match";
		}
		else{
			return "";
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	@Override
	public void getMessage() {

		wincheck=players.size();
		if (wincheck==1){
			JOptionPane.showMessageDialog (null, "Congratulations " + 
					players.get(0).getName() + ", you have won! \nYour socre is " + 
					players.get(0).getScore() + " out of " + players.get(0).getTries() + " moves." , "Memory", JOptionPane.INFORMATION_MESSAGE);									
			System.exit(0);
		}
		else if (wincheck==2){

			switch (players.get(0).getScore()-players.get(1).getScore()){

			case 0:
				JOptionPane.showMessageDialog (null, "The game is drawn!" , "Memory", JOptionPane.INFORMATION_MESSAGE);									
				System.exit(0);
				break;
			default:
				int triesOfWinner = (players.get(0).getScore()>numberOfCells/4) ? MemoryModel.players.get(0).getTries() : players.get(1).getTries() ;
				int winnerScore = Math.max(players.get(0).getScore(), players.get(1).getScore());
				System.out.println(winnerScore);
				JOptionPane.showMessageDialog (null, "Congratulations " + 
						getWinnersName(winnerScore) + ", you have won! \nYour socre is " + 
						winnerScore + " out of " + triesOfWinner + " moves." , "Memory", JOptionPane.INFORMATION_MESSAGE);									
				System.exit(0);
				break;	
			}
		}
	}

	public boolean gameOver(){

		for(int i=0; i<cards.length; i++){
			for(int j=0; j<cards[i].length; j++){
				if(cards[i][j].getState() != CardState.NONE)
					return false; 				
			}
		}
		return true;
	}
	
	public void resetCounter(){
		counter=0;
	}

	public static int getFirstNumber() {
		return firstNumber;
	}
	public void setFirstNumber(int firstNumber) {
		MemoryModel.firstNumber = firstNumber;
	}

	public int getSecondNumber() {
		return secondNumber;
	}

	public void setSecondNumber(int secondNumber) {
		MemoryModel.secondNumber = secondNumber;
	}

	public void nullifyButtonsIndex(){
		for(int i=0; i<buttonsIndex.length; i++)
			buttonsIndex[i]=0;
	}

	public int[] getButtonsIndex(){
		return buttonsIndex;
	}

	public static void createCards(int dimension){
		cards = new Card[dimension][dimension];
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){

				cards[i][j] = new Card();
			}
		}
		numberOfCells=dimension*dimension;
	}


	public static Card[][] getCards() {
		return cards;
	}

	public static Card getCard(int cardNumber){
		Card temp = null;
		if (cardNumber==1)
			temp = cards[buttonsIndex[0]][buttonsIndex[1]];
		else if(cardNumber==2)
			temp = cards[buttonsIndex[2]][buttonsIndex[3]];
		return temp;	
	}

	public void setCards(Card[][] cards) {
		MemoryModel.cards = cards;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public static void setSelectedCard(Card selectedCard) {
		MemoryModel.selectedCard = selectedCard;
	}

	public Player getActivePlayer() {
		Player activePlayer; 		
		activePlayer = players.get(0).isActive() ? players.get(0) : players.get(1);
		return activePlayer;
	}

	/*public static void setCurrentPlayer(Player currentPlayer) { 
		MemoryModel.currentPlayer = currentPlayer;
	}*/
	public Card getFirstCard() {
		return firstCard;
	}

	public void setFirstCard(Card firstCard) {
		MemoryModel.firstCard = firstCard;
	}

	public Card getSecondCard() {
		return secondCard;
	}

	public void setSecondCard(Card secondCard) {
		MemoryModel.secondCard = secondCard;
	}

	public String getWinnersName(int i){
		String winner = players.get(0).getScore()==i ? players.get(0).getName() : players.get(1).getName();
		return winner;
	}

	public int getOpenCardsNo() {
		// TODO Auto-generated method stub
		return 0;
	}
}