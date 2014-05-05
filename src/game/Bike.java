package game;

import org.ejml.simple.SimpleMatrix;
import java.util.ArrayList;

public class Bike {
	
	private Sprite sprite;						// Texture and drawing
	private Tail tail;							// Tail
	private SimpleMatrix pos;					// Current xy-coordinate (at rotation point)
	private SimpleMatrix vel;					// Current velocity vector
	private double angle;						// How many radians is 'vel' rotated from x-axis (clockwise)
	private SimpleMatrix rot;					// 4x4 rotation matrix
	
	// Turning variables and constants
	private int turning;
	private final int LEFT = -1;
	private final int STRAIGHT = 0;
	private final int RIGHT = 1;
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
			sprite = new Sprite("res/bike_orange.png", 105, 43);
			tail = new Tail("res/tail_orange.png");
			pos = new SimpleMatrix(1, 2, true, posX, posY);
			break;
		case 2:
			sprite = new Sprite("res/bike_blue.png", 105, 43);
			tail = new Tail("res/tail_blue.png");
			pos = new SimpleMatrix(1, 2, true, posX, posY);
			break;
		default:
			throw new IllegalArgumentException("Bike constructor: player should be 1 or 2.");
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
		if(direction == LEFT || direction == STRAIGHT || direction == RIGHT) {
			turning = direction;
		} else {
			throw new IllegalArgumentException("direction should be -1, 0 or 1.");
		}
	}
	
	/**
	 * Render next this bike to next frame, also calculate change depending on delta value.
	 * @param delta The time in ms since last frame >= 0
	 */
	public void render(int delta) {
		if (delta < 0) {
			throw new IllegalArgumentException("delta should be > 0"); // Undefined behavior
		}
		double deltaAngle = turning*turningSpeed*delta/1000;
		angle += deltaAngle;
		tail.push(pos, angle);
		tail.render();
		if(turning != STRAIGHT) {
			sprite.rotate(turning*turningSpeed*delta/1000);
			rotateVelocity(turning*turningSpeed*delta/1000);
		}
		pos = pos.plus(vel.scale((double)delta/1000));
		sprite.draw(pos.get(X), pos.get(Y));
	}
	
	/**
	 * Rotates velocity vector by angle r.
	 * @param r radians
	 */
	private void rotateVelocity(double r) {
		rot = new SimpleMatrix(2, 2, true, Math.cos(r), Math.sin(r), Math.sin(-r), Math.cos(r));
		vel = vel.mult(rot);
	}
	
	/**
	 * @return Coordinates relative to window context where this bikes center of rotation is.
	 */
	public SimpleMatrix getRotatingPoint() {
		return pos;
	}
	
	public SimpleMatrix getCenter() {
		return getFrontCenterPos().minus(vel.scale(0.5*sprite.getWidth()/vel.normF()));
	}
	
	/**
	 * @return the position of the foremost edge of bike's front wheel. 
	 */
	public SimpleMatrix getFrontCenterPos() {
		return pos.plus( sprite.getFrontLeft().plus(sprite.getFrontRight()).scale(0.5) );
	}
	
	public ArrayList<SimpleMatrix> getBoundingCoordinates() {
		ArrayList<SimpleMatrix> edges = new ArrayList<SimpleMatrix>();
		edges.add(getCenter().plus(sprite.getBackLeft()));
		edges.add(getCenter().plus(sprite.getBackRight()));
		edges.add(getCenter().plus(sprite.getFrontRight()));
		edges.add(getCenter().plus(sprite.getFrontLeft()));
				
		return edges;
	}
	
	/**
	 * @param otherPos Center coordinates of object to check against.
	 * @param radius Approximate radius of circle around object to check against.
	 * @param other List of coordinates around the object to check against.
	 * @return
	 */
	public boolean isCollision(SimpleMatrix otherPos, double radius, ArrayList<SimpleMatrix> other) {
		
		boolean bodyProximity = getCenter().minus(otherPos).normF() < 60 + radius;
		if(true) {
			ArrayList<SimpleMatrix> me = getBoundingCoordinates();
			for (int i = 0; i < me.size(); i++) {
				for (int j = 0; j < other.size(); j++) {
					if(Geometry.linesIntersect(me.get(i), me.get((i + 1)%me.size()), 
											other.get(j), other.get((j + 1) % other.size()))) {
						return true;
					}
				}
			}
		}
		
		/*boolean tailCollision = tail.isCollision(otherPos, radius, null);
		if(tailCollision) {
			return true;
		}*/
	
		return false;
	}
}
