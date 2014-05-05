package game;


import org.ejml.simple.SimpleMatrix;

public class Bike {
	
	private Sprite sprite;						// Texture and drawing
	private Tail tail;							// Tail
	private SimpleMatrix pos;					// Current xy-coordinate (at rotation point)
	private SimpleMatrix vel;					// Current velocity vector
	private double angle;						// How many radians is 'vel' rotated from x-axis (clockwise)
	private SimpleMatrix rot;					// 4x4 rotation matrix
	
	// Turning variables and constants
	private int turning;
	private final int STRAIGHT = 0;
	private final double turningSpeed = 1.3*Math.PI;	// radians per second
	
	// Other constants
	private final int X = 0;
	private final int Y = 1;
	private final double normalSpeed = 500;		// Pixels per second

	/**
	 * This represents a player and a bike as one.
	 * @param player Should be 1 or 2
	 */
	public Bike(int player, int posX, int posY) {
		
		switch (player) {
		case 1:
			sprite = new Sprite("res/bike_orange.png", 43, 105);
			tail = new Tail("res/tail_orange.png");
			pos = new SimpleMatrix(1, 2, true, posX, posY);
			break;
		case 2:
			sprite = new Sprite("res/bike_blue.png", 43, 105);
			tail = new Tail("res/tail_blue.png");
			pos = new SimpleMatrix(1, 2, true, posX, posY);
			break;
		default:
			System.err.println("Bike constructor: player should be 1 or 2.");
			System.exit(1);
			break;
		}
		
		vel = new SimpleMatrix(1, 2, true, 0, -normalSpeed);
		angle = -Math.PI/2;
		sprite.setRotationPoint(22, 22);
		sprite.rotate(angle);
		turning = 0;
	}
	
	/**
	 * Tells bike that it is currently turning in this direction.
	 * @param direction -1 = LEFT, 0 = STRAIGHT, 1 = RIGHT
	 */
	public void turn(int direction) {
		turning = direction;
	}
		
	public void render(int delta) {
		double deltaAngle = turning*turningSpeed*delta/1000;
		angle += deltaAngle;
		tail.push(pos, angle);
		tail.render();
		if(turning != STRAIGHT) {
			sprite.rotate(turning*turningSpeed*delta/1000);
			rotateVelocity(turning*turningSpeed*delta/1000);
		}
		pos = pos.plus(vel.scale((double)delta/1000));
		sprite.draw((int)pos.get(X), (int)pos.get(Y));
	}
	
	/**
	 * Rotates velocity vector by angle r.
	 * @param r radians
	 */
	private void rotateVelocity(double r) {
		rot = new SimpleMatrix(2, 2, true, Math.cos(r), Math.sin(r), Math.sin(-r), Math.cos(r));
		vel = vel.mult(rot);
	}
	
	public SimpleMatrix getRotatingPoint() {
		return pos;
	}
	
	/**
	 * @return the position of the foremost edge of bike's front wheel. 
	 */
	public SimpleMatrix getFrontCenterPos() {
		return pos.plus( sprite.getFrontLeft().plus(sprite.getFrontRight()).scale(0.5) );
	}
	
}
