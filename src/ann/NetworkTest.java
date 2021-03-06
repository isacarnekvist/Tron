package ann;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NetworkTest {
	
	private Network n1, n2, n3, n4, n5;
	private Double[] in1, in2, in3, in4;

	@Before
	public void setUp() throws Exception {
		n1 = new Network(1, 0, 0, 1, true);
		n2 = new Network(2, 1, 3, 2, false);
		n3 = new Network(3, 4, 10, 3, true);
		n4 = new Network(4, 2, 10, 3, true);
		n5 = new Network(4, 2, 10, 3, true);
		n1.randomizeWeights();
		n2.randomizeWeights();
		n3.randomizeWeights();
		n4.randomizeWeights();
		n5.randomizeWeights();
		Double[] i1 = {0.5};
		Double[] i2 = {0.2, 0.7};
		Double[] i3 = {0.2, 0.7, -0.4};
		Double[] i4 = {0.2, 0.7, -0.4, 0.6};
		in1 = i1;
		in2 = i2;
		in3 = i3;
		in4 = i4;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testMix() {
		n5 = n5.mateWithR(n4);
		n4 = n4.mateWithR(n5);
		assertTrue(abs(n5.evaluate(in4)) != 0);
		assertTrue(n5.evaluate(in4).length == 3);
	}
	
	@Test
	public void testSend() {
		n1.evaluate(in1);
		assertTrue(n1.evaluate(in1).length == 1);
		assertTrue(abs(n2.evaluate(in2)) != 0);
		assertTrue(n2.evaluate(in2).length == 2);
		assertTrue(abs(n3.evaluate(in3)) != 0);
		assertTrue(n3.evaluate(in3).length == 3);
		assertTrue(abs(n4.evaluate(in4)) != 0);
		assertTrue(n4.evaluate(in4).length == 3);
		assertTrue(abs(n5.evaluate(in4)) != 0);
		assertTrue(n5.evaluate(in4).length == 3);
	}

	@Test
	public void testConstruct() {
		boolean success = false;
		try {
			new Network(0, 1, 1, 1, false);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		success = false;
		try {
			new Network(1, 1, 1, 0, false);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		new Network(1, 0, 0, 1, true);
	}

	public static void printVector(double[] v){
		System.out.print("[");
		for(double d : v) {
			System.out.printf("%.1f\t", d);
		}
		System.out.println("]");
	}
	
	private double abs(double[] v) {
		double res = 0;
		for (double d : v) {
			res += Math.pow(d, 2);
		}
		return Math.sqrt(res);
	}
}
