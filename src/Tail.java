import java.util.LinkedList;
import org.ejml.simple.SimpleMatrix;

/**
 * Responsible for the tail that follows the bike.
 *
 */
public class Tail {
	
	private LinkedList<SimpleMatrix> tail;

	public Tail() {
		tail = new LinkedList<SimpleMatrix>();
	}
	
	public void push(SimpleMatrix bikePos) {
		tail.addFirst(bikePos);
		
		// TODO Make sure length is correct and that prolonging works
		
		tail.removeLast();
	}
	
	public void render() {
		
	}
	
}