package game;
import org.lwjgl.input.Keyboard;
import org.ejml.simple.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class GameController {
	
	private Grid grid;
	private Bike player1, player2;
	private MusicPlayer mPlayer;
	private Sprite logo;
	private Sprite info_enter;
	private Sprite key_a, key_d, key_left, key_right;
	private int width, height;				// Screen pixels height and width
	private ArrayList<SimpleMatrix> screenBounds;
	private Random r;
	private ArrayList<Powerup> powerups;
	
	private final int LEFT 		= -1;
	private final int STRAIGHT 	= 0;
	private final int RIGHT 	= 1;

	private final int START = -1;
	private final int GAME 	= 0;
	//private final int END 	= 1;
	private int state;

	public GameController(int maxX, int maxY) {

		// Assign variables
		width = maxX;
		height = maxY;
		grid = new Grid(maxX, maxY);
		mPlayer = new MusicPlayer();
		mPlayer.playTrack(START);
		player1 = new Bike(1, maxX/2 - 508, maxY/2 + 280);
		player2 = new Bike(2, maxX/2 + 514, maxY/2 + 280);
		r = new Random();
		powerups = new ArrayList<Powerup>();

		// Load sprites
		logo = new Sprite("res/logo.png", 1024, 234);
		info_enter = new Sprite("res/info_enter.png", 1024, 40);
		key_a = new Sprite("res/key_a.png", 64, 40);
		key_d = new Sprite("res/key_d.png", 64, 40);
		key_left = new Sprite("res/key_left.png", 64, 40);
		key_right = new Sprite("res/key_right.png", 64, 40);

		// Add Screenbounds
		screenBounds = new ArrayList<SimpleMatrix>();
		screenBounds.add(new SimpleMatrix(1, 2, true, 0, 0));
		screenBounds.add(new SimpleMatrix(1, 2, true, 0, height));
		screenBounds.add(new SimpleMatrix(1, 2, true, width, height));
		screenBounds.add(new SimpleMatrix(1, 2, true, width, 0));
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
		key_right.draw((int)player2.getRotatingPoint().get(0) + 64, (int)player2.getRotatingPoint().get(1) - 60);
	}
	
	/**
	 * Renders the screen where actual playing happens.
	 * @param delta Time in ms since last frame
	 */
	private void renderGameScreen(int delta) {
		
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		
		if (powerups.size() == 0) {
			powerups.add(new Powerup(width, height));
		}

		for (Powerup p : powerups) {
			p.render();
		}
		
		player1.render(delta);
		//player1.turn(RIGHT);
		player2.render(delta);
		//player2.turn(RIGHT);

		checkForBikeCollisions();
		checkforPowerupCollisions(player1);
		checkforPowerupCollisions(player2);
	}
	
	/**
	 * Reset to start screen
	 */
	private void reset() {
		player1 = new Bike(1, width/2 - 508, height/2 + 280);
		player2 = new Bike(2, width/2 + 514, height/2 + 280);
		state = START;
		mPlayer.playTrack(START);
		powerups.clear();
	}

	/**
	 * Render current state to screen.
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
	}
	
	/**
	 * Check for collisions with all objects
	 */
	private void checkForBikeCollisions() {
		boolean P2DidHitP1 = player1.isCollision(player2.getCenter(), 60, player2.getBoundingCoordinates());
		boolean P1DidHitP2 = player2.isCollision(player1.getCenter(), 60, player1.getBoundingCoordinates());
		boolean P1Suicide = player1.checkOwnTail();
		boolean P2Suicide = player2.checkOwnTail();		
		boolean P1DidHitWall = player1.isCollision(null, width*2, screenBounds);
		boolean P2DidHitWall = player2.isCollision(null, width*2, screenBounds);
		// Decide what to do
		if (P1DidHitP2 && !P2DidHitP1) {
			// P1 hit P2:s tail
			reset();
			System.out.println("P2 won.");
		} else if (P2DidHitP1 && !P1DidHitP2) {
			// P2 hit P1:s tail
			reset();
			System.out.println("P1 won.");
		} else if (P1DidHitP2 && P2DidHitP1) {
			System.out.println("You're both dead.");
			reset();
		} else if (P1DidHitWall) {
			System.out.println("Player 1 is dead.");
			reset();
		} else if (P2DidHitWall) {
			System.out.println("Player 2 is dead.");
			reset();
		} else if (P1Suicide || P2Suicide) {
			reset();
		}
	}

	private void checkforPowerupCollisions(Bike player) {
		Iterator<Powerup> it = powerups.iterator();
		while(it.hasNext()){
			Powerup p = it.next();
			if(player.isCollision(p.getPos(), 26, p.getBoundingCoordinates())) {
				player.powerup(p);
				it.remove();
			}
		}
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
						reset();
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
	
	/**
	 * @return the size of the grid
	 */
	public SimpleMatrix getGridSize() {
		return new SimpleMatrix(width, height);
	}
}
