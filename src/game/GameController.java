package game;
import org.lwjgl.input.Keyboard;
import org.ejml.simple.*;

import ann.Network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;

public class GameController {
	
	private Grid grid;
	private Bike player1, player2;
	private MusicPlayer mPlayer;
	private int winner;						// Who won the last game?
	private int width, height;				// Screen pixels height and width
	private ArrayList<SimpleMatrix> screenBounds;
	private ArrayList<Powerup> powerups;
	private long futureTimeMark;			// Used for knowing when to switch from end to start screen
	
	// AI
	private LinkedList<Network> anns;		// Artificial neural networks
	private LinkedList<Network> winners; 	// Which networks won?
	private Network player1AI;
	private Network player2AI;
	
	// Sprites
	private Sprite logo;
	private Sprite info_enter;
	private Sprite key_a, key_d, key_left, key_right;
	private Sprite end_winner, end_loser, end_tie;
	
	// Constants
	private final boolean AI_TRAINING = true;
	
	private final int LEFT 		= -1;
	private final int STRAIGHT 	= 0;
	private final int RIGHT 	= 1;

	private final int START = -1;
	private final int GAME 	= 0;
	private final int END 	= 1;
	private final int AI_STATE = 2;
	
	private final int NOGAMEPLAYED = 0;
	private final int PLAYER1 = 1;
	private final int PLAYER2 = 2;
	private final int NOSURVIVOR = 3;
	private int state;

	public GameController(int maxX, int maxY) {

		// Assign variables
		width = maxX;
		height = maxY;
		grid = new Grid(maxX, maxY);
		mPlayer = new MusicPlayer();
		mPlayer.playState(START);
		player1 = new Bike(1, maxX/2 - 508, maxY/2 + 280);
		player2 = new Bike(2, maxX/2 + 514, maxY/2 + 280);
		powerups = new ArrayList<Powerup>();

		// Load sprites
		logo = new Sprite("res/logo.png", 1024, 234);
		info_enter = new Sprite("res/info_enter.png", 1024, 60);
		key_a = new Sprite("res/key_a.png", 64, 40);
		key_d = new Sprite("res/key_d.png", 64, 40);
		key_left = new Sprite("res/key_left.png", 64, 40);
		key_right = new Sprite("res/key_right.png", 64, 40);
		end_winner = new Sprite("res/end_winner.png", 512, 112);
		end_loser = new Sprite("res/end_loser.png", 512, 112);
		end_tie = new Sprite("res/end_tie.png", 512, 512);
		
		// Add Screenbounds
		screenBounds = new ArrayList<SimpleMatrix>();
		screenBounds.add(new SimpleMatrix(1, 2, true, 0, 0));
		screenBounds.add(new SimpleMatrix(1, 2, true, 0, height));
		screenBounds.add(new SimpleMatrix(1, 2, true, width, height));
		screenBounds.add(new SimpleMatrix(1, 2, true, width, 0));
		state = START;
		
		// AI
		anns = new LinkedList<Network>();
		for (int i = 0; i < 8; i++) {
			Network n = new Network(24, 2, 15, 1);
			n.randomizeWeights();
			anns.addLast(n);
		}
		winners = new LinkedList<Network>();
		if(AI_TRAINING) {
			state = AI_STATE;
		}
		player1AI = anns.getFirst();
		player2AI = anns.getFirst();
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
		case END:
			renderEndScreen(delta);
			break;
		case AI_STATE:
			renderAIScreen(delta);
			break;
		default:
			break;
		}		
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
		player1.render();
		key_d.draw((int)player1.getRotatingPoint().get(0) + 63, (int)player1.getRotatingPoint().get(1) - 60);

		// Player 2
		key_left.draw((int)player2.getRotatingPoint().get(0) - 67, (int)player2.getRotatingPoint().get(1) - 60);
		player2.render();
		key_right.draw((int)player2.getRotatingPoint().get(0) + 64, (int)player2.getRotatingPoint().get(1) - 60);
		
		switch (winner) {
		case NOGAMEPLAYED:
			
			break;
		case PLAYER1:
			end_winner.draw(player1.getCenter().plus(new SimpleMatrix(1, 2, true, 0, 200)));
			end_loser.draw(player2.getCenter().plus(new SimpleMatrix(1, 2, true, 0, 200)));
			break;
			
		case PLAYER2:
			end_winner.draw(player2.getCenter().plus(new SimpleMatrix(1, 2, true, 0, 200)));
			end_loser.draw(player1.getCenter().plus(new SimpleMatrix(1, 2, true, 0, 200)));
			break;
		case NOSURVIVOR:
			end_tie.draw(width/2, height/2 + 450);
			break;
		}
	}
	
	/**
	 * Renders the screen where actual playing happens.
	 * @param delta Time in ms since last frame
	 */
	private void renderGameScreen(int delta) {
		
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos()); 
		
		while (powerups.size() < 0) {
			Powerup p = new Powerup(width, height);
			if(!player1.isCollision(p.getPos(), 200, p.getBoundingCoordinates()) &&
					!player2.isCollision(p.getPos(), p.getRadius(), p.getBoundingCoordinates())) {
				powerups.add(p);
			}
		}

		for (Powerup p : powerups) {
			p.render();
		}
		
		player1.calculate(delta);
		player2.calculate(delta);
		player1.render();
		player2.render();

		checkForBikeCollisions();
		checkforPowerupCollisions(player1);
		checkforPowerupCollisions(player2);
	}
	
	/**
	 * 
	 * @param delta
	 */
	public void renderEndScreen(int delta) {
		grid.render(player1.getFrontCenterPos(), player2.getFrontCenterPos());
		player1.render();
		player2.render();
		
		if(System.nanoTime() > futureTimeMark) {
			reset();
		}
	}
	
	public void renderAIScreen(int delta) {
		// Gather info for ANN:s
		Double[] args1 = getAIArgs(player1, player2);
		Double[] args2 = getAIArgs(player2, player1);
		
		// Give info to ANN:s
		// Ask them what to do
		double[] ans1 = player1AI.evaluate(args1);
		double[] ans2 = player2AI.evaluate(args2);
		
		if(Math.abs(ans1[0]) < 0.1) {
			player1.turn(STRAIGHT);
		} else {
			player1.turn((int)Math.signum(ans1[0]));
		}
		
		if(Math.abs(ans2[0]) < 0.1) {
			player2.turn(STRAIGHT);
		} else {
			player2.turn((int)Math.signum(ans2[0]));
		}
		
		// Render as usual
		renderGameScreen(delta);
	}
	
	private void handleANNsOnGameOver() {
		switch (winner) {
		case PLAYER1:
			winners.addLast(player1AI);
			break;
		case PLAYER2:
			winners.addLast(player2AI);
			break;
		case NOSURVIVOR:
			// Delete them?
			break;
		}
		anns.remove(player1AI);
		anns.remove(player2AI);
		if(anns.size() < 2) { // Not enough ANNS left to continue
			while(winners.size() > 1) {
				Network n1 = winners.pollFirst();
				Network n2 = winners.pollFirst();
				anns.addLast(n1);
				anns.addLast(n2);
				anns.addLast(n1.mateWith(n2));
				System.out.println("Three new babies");
				while(anns.size() < 8 && winners.size() < 2) {
					anns.addLast(n1.mateWith(n2));
				}
			}
		}
		Random rand = new Random(System.nanoTime());
		while(anns.size() > 8) {
			anns.remove(rand.nextInt(anns.size()));
		}
		player1AI = anns.remove(rand.nextInt(anns.size()));
		player1AI.mutate(2);
		player2AI = anns.pollFirst();
	}
	
	/**
	 * Fetch all arguments needed for the ANN
	 * @param player
	 * @param otherPlayer
	 * @return
	 */
	private Double[] getAIArgs(Bike player, Bike otherPlayer){
		double myAngle = player.getAngle();
		double angle = Geometry.angle(player.getVelocity(), otherPlayer.getVelocity());
		TreeSet<SimpleMatrix> closestToP = new TreeSet<SimpleMatrix>(new VectorComparator(player.getCenter()));
		for (SimpleMatrix s : player.tailSamples()) {
			closestToP.add(s);
		}
		for (SimpleMatrix s : otherPlayer.tailSamples()) {
			closestToP.add(s);
		}
		// x   y   width - x   height - y   myAngle   angle   myVel   otherVel   closest8(x1, y1, x2, y2,...)) = 23 inputs
		ArrayList<Double> args = new ArrayList<Double>();
		args.add(player.getCenter().get(0));
		args.add(player.getCenter().get(1));
		args.add(width - player.getCenter().get(0));
		args.add(height - player.getCenter().get(1));
		args.add(myAngle);
		args.add(angle);
		args.add(player.getVelocity().normF());
		args.add(otherPlayer.getVelocity().normF());
		for (int i = 0; i < 8; i++) {
			SimpleMatrix temp = closestToP.pollFirst();
			if(temp != null) {
				args.add(temp.get(0));
				args.add(temp.get(1));
			} else {
				args.add(0d);
				args.add(0d);
			}
		}
		Double[] res = new Double[24];
		args.toArray(res);
		return res;
	}


	/**
	 * Reset to start screen
	 */
	private void reset() {
		player1 = new Bike(1, width/2 - 508, height/2 + 280);
		player2 = new Bike(2, width/2 + 514, height/2 + 280);
		state = START;
		while(Keyboard.next()) {
			// Empties keyboard cue
		}
		mPlayer.playState(START);
		powerups.clear();
	}

	/**
	 * Check for collisions with all objects
	 */
	private void checkForBikeCollisions() {
		boolean P2DidHitP1 = player1.isCollision(player2.getCenter(), 60, player2.getBoundingCoordinates());
		boolean P1DidHitP2 = player2.isCollision(player1.getCenter(), 60, player1.getBoundingCoordinates());
		boolean P1Suicide = player1.checkIfTailCollision(player1.getCenter(), 60, player1.getBoundingCoordinates());
		boolean P2Suicide = player2.checkIfTailCollision(player2.getCenter(), 60, player2.getBoundingCoordinates());		
		boolean P1DidHitWall = player1.isCollision(null, width*2, screenBounds);
		boolean P2DidHitWall = player2.isCollision(null, width*2, screenBounds);
		// Decide what to do
		if (P1DidHitP2 && !P2DidHitP1) {
			// P1 hit P2:s tail
			winner = PLAYER2;
			prepareEndScreen();
		} else if (P2DidHitP1 && !P1DidHitP2) {
			// P2 hit P1:s tail
			winner = PLAYER1;
			prepareEndScreen();
		} else if (P1DidHitP2 && P2DidHitP1) {
			winner = NOSURVIVOR;
			prepareEndScreen();
		} else if (P1DidHitWall) {
			winner = PLAYER2;
			prepareEndScreen();
		} else if (P2DidHitWall) {
			winner = PLAYER1;
			prepareEndScreen();
		} else if (P1Suicide) {
			winner = PLAYER2;
			prepareEndScreen();
		} else if (P2Suicide) {
			winner = PLAYER1;
			prepareEndScreen();
		}
	}
	
	private void prepareEndScreen() {
		mPlayer.playState(END);
		if(AI_TRAINING) {
			handleANNsOnGameOver();
			reset();
			state = AI_STATE;
		} else {
			state = END;
		}
		futureTimeMark = System.nanoTime() + 2*(long)1E9;
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
			if(Keyboard.next() && Keyboard.getEventKeyState()) {
				state = GAME;
				mPlayer.playState(GAME);
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
}
