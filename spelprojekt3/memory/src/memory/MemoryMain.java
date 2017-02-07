package memory;

public class MemoryMain {
		
	public static void main(String[] args){
	
		SetupView setup = new SetupView();		
		Game game = new MemoryModel();
		SingletonView view = SingletonView.getInstance();
		SingletonController controller = SingletonController.getInstance(game, setup, view);
	}
}