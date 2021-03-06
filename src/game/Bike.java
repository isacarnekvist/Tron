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
	private double turningSpeed = 1.6*Math.PI;	// radians per second
	
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
	 * @return The angle cw from x-axis
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Render this bike.
	 */
	public void render() {
		sprite.draw(pos.get(X), pos.get(Y));
		tail.render();
	}
	
	/**
	 * Update everything before rendering
	 * @param delta Time since last call in ms
	 */
	public void calculate(int delta) {
		if (delta < 0) {
			throw new IllegalArgumentException("delta should be > 0"); // Undefined behavior
		}
		double deltaAngle = turning*turningSpeed*delta/1000;
		angle += deltaAngle;
		tail.push(pos, angle);
		if(turning != STRAIGHT) {
			sprite.rotate(turning*turningSpeed*delta/1000);
			rotateVelocity(turning*turningSpeed*delta/1000);
		}
		pos = pos.plus(vel.scale((double)delta/1000));
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
		edges.add(getRotatingPoint().plus(sprite.getBackLeft()));
		edges.add(getRotatingPoint().plus(sprite.getBackRight()));
		edges.add(getRotatingPoint().plus(sprite.getFrontRight()));
		edges.add(getRotatingPoint().plus(sprite.getFrontLeft()));
				
		return edges;
	}
	
	/**
	 * @param otherPos Center coordinates of object to check against. OK to send null.
	 * @param radius Approximate radius of circle around object to check against.
	 * @param other List of coordinates around the object to check against.
	 * @return
	 */
	public boolean isCollision(SimpleMatrix otherPos, double radius, ArrayList<SimpleMatrix> other) {
		
		boolean bodyProximity;
		
		if (otherPos == null) {
			bodyProximity = true;
		} else {
			bodyProximity = getCenter().minus(otherPos).normF() < 100 + radius;
		}
		if(bodyProximity) {
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
		
		if(otherPos != null) {
			return tail.isCollision(otherPos, radius, other);
		} else {
			return false;
		}
	}

	/**
	 * @param p The power up to apply to this bike
	 */
	public void powerup(Powerup p) {
		switch (p.getType()) {
		case "Speed":
			vel = vel.scale(1.07);
			turningSpeed *= 1.07;
			break;
		case "Slow":
			vel = vel.scale(0.5);
			turningSpeed *= 0.5;
			break;
		case "Longer":
			tail.setLength((int)(tail.getLength()*1.2));
			break;
		case "Shorter":
			tail.setLength(tail.getLength() - 50);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @return An array of spread out (i.e. not all) coordinates from the tail.
	 * @param distance how many points in tail to skip in between every sample
	 */
	public ArrayList<SimpleMatrix> tailSamples(int distance) {
		return tail.tailSamples(distance);
	}
	
	/**
	 * @return The current velocity vector.
	 */
	public SimpleMatrix getVelocity() {
		return vel;
	}
	
	/**
	 * @param otherPos Center coordinates of object to check against.
	 * @param radius Approximate radius of circle around object to check against.
	 * @param other List of coordinates around the object to check against.
	 * @return
	 */
	public boolean checkIfTailCollision(SimpleMatrix otherPos, double radius, ArrayList<SimpleMatrix> other) {
		return tail.isCollision(otherPos, 100, other);
	}
}
