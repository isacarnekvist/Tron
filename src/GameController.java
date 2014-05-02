import org.lwjgl.input.Keyboard;

public class GameController {
	
	private Grid grid;
	Bike player1;
	private Bike player2;
	
	private final int LEFT 		= -1;
	private final int STRAIGHT 	= 0;
	private final int RIGHT 	= 1;

	private final int START = -1;
	private final int GAME 	= 0;
	private final int END 	= 1;
	private int state;

	public GameController(int maxX, int maxY) {
		grid = new Grid(maxX, maxY);
		player1 = new Bike(1, 450, 450);
		player2 = new Bike(2, 960, 500);
		state = START;
	}
	

	/**
	 * Render the start screen
	 */
	private void renderStartScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		Sprite startScreen = new Sprite("res/startscreen.png", 1024, 1024);
		startScreen.draw(512, 512);
		player1.render(0);
		player2.render(0);
	}
	
	private void renderGameScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		player1.render(delta);
		player1.turn(RIGHT);
		player2.render(delta);
		player2.turn(RIGHT);
	}

	/**
	 * @param delta Time since last render in ms.
	 */
	public void render(int delta) {
		checkForEvents();
		switch (state) {
		case START:
			renderStartScreen(delta);
			break;
		case GAME:
			renderGameScreen(delta);
			break;
		default:
			break;
		}
		
		// Render
		
	}
	

	/**
	 * Checks for keyboard events and acts on them. Specifically, it tells
	 * players to turn when the corresponding keys are pressed
	 */
	public void checkForEvents() {
		while(Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_RIGHT:
				if(Keyboard.getEventKeyState()) {
					player2.turn(RIGHT);
				} else {
					if(!Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
						player2.turn(STRAIGHT);
					}
				}
				break;
			case Keyboard.KEY_LEFT:
				if(Keyboard.getEventKeyState()) {
					player2.turn(LEFT);
				} else {
					if(!Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
						player2.turn(STRAIGHT);
					}
				}
				break;
			case Keyboard.KEY_D:
				if(Keyboard.getEventKeyState()) {
					player1.turn(RIGHT);
				} else {
					if(!Keyboard.isKeyDown(Keyboard.KEY_A)) {
						player1.turn(STRAIGHT);
					}
				}
				break;
			case Keyboard.KEY_A:
				if(Keyboard.getEventKeyState()) {
					player1.turn(LEFT);
				} else {
					if(!Keyboard.isKeyDown(Keyboard.KEY_D)) {
						player1.turn(STRAIGHT);
					}
				}
				break;
			case Keyboard.KEY_RETURN:
				state = GAME;
				break;
			default:
				break;
			}
		}
	}

	/**
	* @return the current state
	*/
	public int getState() {
		return state;
	}
	
}
