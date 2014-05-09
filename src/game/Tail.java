package game;
import java.util.ArrayList;
import java.util.LinkedList;

import org.ejml.simple.SimpleMatrix;

/**
 * Responsible for the tail that follows the bike.
 *
 */
public class Tail {
	
	private LinkedList<SimpleMatrix> tail;
	private TailSprite sprite;
	private int length;				// Length of tail
	private final int X = 0;
	private final int Y = 1;
	private final int ANGLE = 2;

	public Tail(String filename) {
		tail = new LinkedList<SimpleMatrix>();
		sprite = new TailSprite(filename);
		length = 300;
	}
	
	/**
	 * Push a new point to tail
	 * @param bikePos The current position of the bikes turning point
	 * @param angleRadians The angle of bike counted from x-axis (clockwise positive)
	 */
	public void push(SimpleMatrix bikePos, double angleRadians) {
		tail.addFirst(new SimpleMatrix(1, 3, true, bikePos.get(X), bikePos.get(Y), angleRadians));
		
		// TODO Make sure length is correct and that prolonging works
		if (tail.size() > length) {
			tail.removeLast();
		}
	}
	
	public void render() {
		for(SimpleMatrix s : tail) {
			sprite.draw(s.get(X), s.get(Y), s.get(ANGLE));
		}
	}
	
	/**
	 * @param otherPos Center coordinates of object to check against.
	 * @param radius Approximate radius of circle around object to check against.
	 * @param shape List of coordinates around the object to check against.
	 * @return
	 */
	public boolean isCollision(SimpleMatrix otherPos, double radius, ArrayList<SimpleMatrix> shape) {
		SimpleMatrix checker;
		
		if(tail.size() < 100) {
			return false;
		}
		
		for (int i = 40; i < tail.size(); i += 10) {
			checker = new SimpleMatrix(1, 2, true, tail.get(i).get(0), tail.get(i).get(1));
			if (checker.minus(otherPos).normF() < radius + 170) { // Approximate checking
				// Now, precise checking
				for(int j = i - 30; j < Math.min(tail.size() - 3, i + 30); j += 3) {
					for(int z = 0; z < shape.size(); z++) {
						if(Geometry.linesIntersect(tail.get(j - 3), tail.get(j + 3),
								shape.get(z), shape.get((z + 1) % shape.size()))) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * @return An array of spread out (i.e. not all) coordinates from the tail.
	 */
	public ArrayList<SimpleMatrix> tailSamples() {
		ArrayList<SimpleMatrix> res = new ArrayList<SimpleMatrix>();
		for(int i = 80; i < tail.size(); i += length/30) {
			res.add(new SimpleMatrix(1, 2, true, tail.get(i).get(X), tail.get(i).get(Y)));
		}
		return res;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}
}