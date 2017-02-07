package memory;

import java.util.Random;
import memory.Card.CardState;

public class ComputerPlayer extends Player {

	Random randomNo;
	int cardIndexX=-1, cardIndexY=-1;
	
	public int getCardIndexX() {
		return cardIndexX;
	}

	public int getCardIndexY() {
		return cardIndexY;
	}

	public ComputerPlayer(String player) {
		super(player);
	}

	@Override
	boolean play() {
			randomNo = new Random();	
		do{
			cardIndexX =  randomNo.nextInt(SingletonView.getCellDimension());
			cardIndexY =  randomNo.nextInt(SingletonView.getCellDimension());
		}
		while (MemoryModel.getCards()[cardIndexX][cardIndexY].getState()!=CardState.CLOSED);
		
		return true;
	}
}