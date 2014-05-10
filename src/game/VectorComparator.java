package game;

import java.util.Comparator;

import org.ejml.simple.SimpleMatrix;

public class VectorComparator implements Comparator<SimpleMatrix> {
	
	private SimpleMatrix ref; // The vectors closest to this one will be sorted highest
	
	/**
	 * @param ref Vectors will be sorted after which one is closest to 'ref'
	 */
	public VectorComparator(SimpleMatrix ref) {
		this.ref = ref;
	}

	public int compare(SimpleMatrix v1, SimpleMatrix v2) {
		return (int)Math.signum(v1.minus(ref).normF() - v2.minus(ref).normF());
	}
}
