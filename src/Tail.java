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
	
}