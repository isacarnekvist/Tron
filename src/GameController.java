import org.lwjgl.input.Keyboard;

public class GameController {
	
	private Grid grid;
	private Bike player1;
	private Bike player2;
	private MusicPlayer mPlayer;
	private Sprite logo;
	private int width, height;				// Screen pixels height and width
	
	private final int LEFT 		= -1;
	private final int STRAIGHT 	= 0;
	private final int RIGHT 	= 1;

	private final int START = -1;
	private final int GAME 	= 0;
	private final int END 	= 1;
	private int state;

	public GameController(int maxX, int maxY) {
		width = maxX;
		height = maxY;
		grid = new Grid(maxX, maxY);
		mPlayer = new MusicPlayer();
		mPlayer.playTrack(START);
		player1 = new Bike(1, maxX/2 - 508, maxY/2 + 280);
		player2 = new Bike(2, maxX/2 + 514, maxY/2 + 280);
		logo = new Sprite("res/logo.png", 234, 1024);
		state = START;
	}
	

	/**
	 * Render the start screen
	 */
	private void renderStartScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		logo.draw(width/2, 300);
		player1.render(0);
		player2.render(0);
	}
	
	private void renderGameScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		player1.render(delta);
		player1.turn(RIGHT);
		player2.render(delta);
		//player2.turn(RIGHT);
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
		
		switch (state) {
		case START:
			while(Keyboard.next()) {
				switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_RETURN:
					state = GAME;
					mPlayer.playTrack(GAME);
					break;
				default:
					break;
				}
			}
			break;
			
		case GAME:
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
				case Keyboard.KEY_ESCAPE:
					if(Keyboard.getEventKeyState()) {
						player1 = new Bike(1, width/2 - 508, height/2 + 280);
						player2 = new Bike(2, width/2 + 514, height/2 + 280);
						state = START;
						mPlayer.playTrack(START);
					}
					break;
				}
			}
			break; // GAME state ends here
		}
	}

	/**
	* @return the current state
	*/
	public int getState() {
		return state;
	}
	
}
