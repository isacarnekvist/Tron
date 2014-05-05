package gameTest;

import static org.junit.Assert.*;
import game.Geometry;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

public class GeometryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		SimpleMatrix l1p1 = new SimpleMatrix(1, 2, true, 570, 955);
		SimpleMatrix l1p2 = new SimpleMatrix(1, 2, true, 612, 957);
		SimpleMatrix l2p1 = new SimpleMatrix(1, 2, true, 1591, 956);
		SimpleMatrix l2p2 = new SimpleMatrix(1, 2, true, 1633, 956);
		assertFalse(Geometry.linesIntersect(l1p1, l1p2, l2p1, l2p2));
	}

}
