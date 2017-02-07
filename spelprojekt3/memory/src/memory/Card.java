package memory;

import javax.swing.ImageIcon;

/*
 * class card which includes enums to check state of cards  
 * through enum state will be determined if card is allowed to be opened or not.
 */
public class Card {

	private CardState state;

	public enum CardState {
		OPEN, CLOSED, NONE
	}

	public Card(){
		this(CardState.CLOSED);
	}

	public Card(CardState state){
		updateCard(state);
	}

	public void updateCard(CardState state){
		this.state = state;
	}

	public CardState getState(){
		return this.state;
	}
}