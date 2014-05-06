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
		length = 150;
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
		
		for (int i = 0; i < tail.size(); i += 20) {
			checker = new SimpleMatrix(1, 2, true, tail.get(i).get(0), tail.get(i).get(1));
			if (checker.minus(otherPos).normF() < radius) { // Approximate checking
				// Now more careful testing
				for(int j = 0; j < shape.size(); j++) {
					if(i + 10 < tail.size() && i - 10 >= 0){
						if(Geometry.linesIntersect(shape.get(j), shape.get((j + 1) % shape.size()),
												tail.get(i - 10), tail.get(i + 10))) {
							return true;
						}
					} else if (i - 10 < 0) {
						if(Geometry.linesIntersect(shape.get(j), shape.get((j + 1) % shape.size()),
								tail.get(i), tail.get(i + 10))) {
							return true;
						}
					} else {
						if(Geometry.linesIntersect(shape.get(j), shape.get((j + 1) % shape.size()),
								tail.get(i), tail.get(i - 10))) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
}