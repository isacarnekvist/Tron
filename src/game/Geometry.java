package game;

import org.ejml.simple.SimpleMatrix;

public class Geometry {
	
	private static final int X = 0;
	private static final int Y = 1;
	
	/**
	 * @param p1
	 * @param p1radius
	 * @param p2
	 * @param p2radius
	 * @return
	 */
	public static boolean circlesIntersect(SimpleMatrix p1, double p1radius,
										   SimpleMatrix p2, double p2radius) {
		
		return p1.minus(p2).normF() < p1radius + p2radius;
	}
	
	/**
	 * Checks too see if two lines intersect
	 * @param l1p1 Point 1 coordinates of line 1
	 * @param l1p2 Point 2 coordinates of line 1
	 * @param l2p1 Point 1 coordinates of line 2
	 * @param l2p2 Point 2 coordinates of line 2
	 * @return true if intersect, false otherwise
	 */
	public static boolean linesIntersect(SimpleMatrix l1p1, SimpleMatrix l1p2, 
										SimpleMatrix l2p1, SimpleMatrix l2p2) {
		
		SimpleMatrix A = new SimpleMatrix(2, 2, true, l1p2.get(Y) - l1p1.get(Y),
													  l1p1.get(X) - l1p2.get(X),
													  l2p2.get(Y) - l2p1.get(Y),
													  l2p1.get(X) - l2p2.get(X));
		SimpleMatrix B = new SimpleMatrix(2, 1, true, l1p1.get(X)*l1p2.get(Y) - l1p2.get(X)*l1p1.get(Y),
													  l2p1.get(X)*l2p2.get(Y) - l2p2.get(X)*l2p1.get(Y));
		
		if(A.determinant() == 0) {
			return false;
		}
		
		SimpleMatrix XM = A.solve(B);
		
		if(XM.get(X) >= Math.min(l1p1.get(X), l1p2.get(X))) { // GO
			if(XM.get(X) <= Math.max(l1p1.get(X), l1p2.get(X))) { // CRAZY
				if(XM.get(X) >= Math.min(l2p1.get(X), l2p2.get(X))) { // LIKE 
					if(XM.get(X) <= Math.max(l2p1.get(X), l2p2.get(X))) { // YOU
						// Now y-values
						if(XM.get(Y) <= Math.max(l1p1.get(Y), l1p2.get(Y))) { // JUST
							if(XM.get(Y) >= Math.min(l1p1.get(Y), l1p2.get(Y))) { // DON'T
								if(XM.get(Y) <= Math.max(l2p1.get(Y), l2p2.get(Y))) { // CARE
									if(XM.get(Y) >= Math.min(l2p1.get(Y), l2p2.get(Y))) { // !!!
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
