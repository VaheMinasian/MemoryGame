package memory;

import memory.MemoryModel;

public abstract class Player {
	
	private String name;
	private int score;
	private int tries;
	private boolean isActive;
	
	abstract boolean play();
	
	public Player(String name){
		this.name=name;
	}
	
	void incrementScore(){
		this.score++;
	}
	
	void incrementTries(){
		this.tries++;
	}
	
	public int getTries(){
		return this.tries;
	}
		
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean value){
		this.isActive = value;
	}

	public int getCardIndices() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCardIndexX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCardIndexY() {
		// TODO Auto-generated method stub
		return 0;
	}	
}