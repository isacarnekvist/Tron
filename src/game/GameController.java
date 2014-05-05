package game;
import org.lwjgl.input.Keyboard;

public class GameController {
	
	private Grid grid;
	private Bike player1;
	private Bike player2;
	private MusicPlayer mPlayer;
	private Sprite logo;
	private Sprite info_enter;
	private Sprite key_a;
	private Sprite key_d;
	private Sprite key_left;
	private Sprite key_right;
	private int width, height;				// Screen pixels height and width
	
	private final int LEFT 		= -1;
	private final int STRAIGHT 	= 0;
	private final int RIGHT 	= 1;

	private final int START = -1;
	private final int GAME 	= 0;
	//private final int END 	= 1;
	private int state;

	public GameController(int maxX, int maxY) {
		width = maxX;
		height = maxY;
		grid = new Grid(maxX, maxY);
		mPlayer = new MusicPlayer();
		mPlayer.playTrack(START);
		player1 = new Bike(1, maxX/2 - 508, maxY/2 + 280);
		player2 = new Bike(2, maxX/2 + 514, maxY/2 + 280);
		logo = new Sprite("res/logo.png", 1024, 234);
		info_enter = new Sprite("res/info_enter.png", 1024, 40);
		key_a = new Sprite("res/key_a.png", 64, 40);
		key_d = new Sprite("res/key_d.png", 64, 40);
		key_left = new Sprite("res/key_left.png", 64, 40);
		key_right = new Sprite("res/key_right.png", 64, 40);
		state = START;
	}
	

	/**
	 * Render the start screen
	 */
	private void renderStartScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		logo.draw(width/2, 300);
		info_enter.draw(width/2, 500);
		
		// Player 1
		key_a.draw((int)player1.getRotatingPoint().get(0) - 67, (int)player1.getRotatingPoint().get(1) - 60);
		player1.render(0);
		key_d.draw((int)player1.getRotatingPoint().get(0) + 63, (int)player1.getRotatingPoint().get(1) - 60);

		// Player 2
		key_left.draw((int)player2.getRotatingPoint().get(0) - 67, (int)player2.getRotatingPoint().get(1) - 60);
		player2.render(0);
		key_right.draw((int)player2.getRotatingPoint().get(0) + 63, (int)player2.getRotatingPoint().get(1) - 60);
	}
	
	private void renderGameScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		player1.render(delta);
		player1.turn(RIGHT);
		player2.render(delta);
		//player2.turn(RIGHT);
		if (player1.isCollision(player2.getCenter(), 60, player2.getBoundingCoordinates())) {
			System.out.println("Collision");
		}
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
				case Keyboard.KEY_LEFT:
					keys(Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, player2, LEFT);
					break;
				case Keyboard.KEY_RIGHT:
					keys(Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT, player2, RIGHT);
					break;
				case Keyboard.KEY_A:
					keys(Keyboard.KEY_A, Keyboard.KEY_D, player1, LEFT);
					break;
				case Keyboard.KEY_D:
					keys(Keyboard.KEY_D, Keyboard.KEY_A, player1, RIGHT);
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
	 * Checks for keyboard events and turns if a key is pressed
	 * @param key The key to check for
	 * @param oppositeKey The key that turns in the opposite direction
	 * @param player The player object that should turn
	 * @param direction The direction to turn in
	 */
	private void keys(int key, int oppositeKey, Bike player, int direction) {
		if (Keyboard.getEventKeyState()) {
			player.turn(direction);
		} else {
			if(!Keyboard.isKeyDown(oppositeKey)) {
				player.turn(STRAIGHT);
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
