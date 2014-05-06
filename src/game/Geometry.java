package game;

import org.ejml.simple.SimpleMatrix;

public class Geometry {
	
	private static final int X = 0;
	private static final int Y = 1;
	
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
		
		SimpleMatrix A = new SimpleMatrix(2, 2, true,
			l1p2.get(Y) - l1p1.get(Y),
			l1p1.get(X) - l1p2.get(X),
			l2p2.get(Y) - l2p1.get(Y),
			l2p1.get(X) - l2p2.get(X));
		SimpleMatrix B = new SimpleMatrix(2, 1, true,
			l1p1.get(X)*l1p2.get(Y) - l1p2.get(X)*l1p1.get(Y),
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


		// maybe better, maybe worse
/*		if (what(X, true, l1p1, l1p2)) { // GO 
			if(what(X, false, l1p1, l1p2)) { // CRAZY 
				if (what(X, true, l2p1, l2p2)) { // LIKE 
					if(what(X, false, l2p1, l2p2)) { // YOU 
						// now y-values
						if (what(Y, false, l1p1, l1p2)) { // JUST
							if (what(Y, true, l1p1, l1p2)) { // DON't
								if (what(Y, false, l2p1, l2p2)) { // CARE
									if (what(Y, true, l2p1, l2p2)) { // !!!
										return true;
									}
								}
							}
						}
					}
				}
			}
		}*/

	}
	
/*	boolean what(int xory, boolean big, SimpleMatrix line1, SimpleMatrix line2) {
		if (big) {
			if (XM.get(xory) >= Math.min(line1.get(xory), line2.get(xory))) {
				return true;
			}
		} else {
			if (XM.get(xory) <= Math.max(line1.get(xory), line2.get(xory))) {
				return true;
			}
		}
		return false;
	}*/
}
