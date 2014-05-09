package ann;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeightsLayerTest {
	
	private WeightsLayer w1;
	private WeightsLayer w2;
	private WeightsLayer w3;
	private WeightsLayer w4;
	private WeightsLayer w5, w6, w7;

	@Before
	public void setUp() throws Exception {
		w1 = new WeightsLayer(1, 1);
		w2 = new WeightsLayer(1, 10);
		w3 = new WeightsLayer(10, 1);
		w4 = new WeightsLayer(10, 10);
		w5 = new WeightsLayer(10, 10);
		w6 = new WeightsLayer(5, 7);
		w7 = new WeightsLayer(5, 7);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testMixing() {
		w4.randomize();
		w5.randomize();
		int fi = 4;
		int ti = 9;
		int fo = 4;
		int to = 9;
		double[][] s = w4.getWeights(fi, ti, fo, to);
		w5.insertWeights(s, fi, ti, fo, to);
		w6.randomize();
		w7.randomize();
		WeightsLayer w8 = w6.mateWith(w7);
		w6.description();
		System.out.println();
		w7.description();
		System.out.println();
		w8.description();
		System.out.println();
	}
	
	@Test
	public void testRandom() {
		w1.randomize();
		w2.randomize();
		w3.randomize();
		w4.randomize();
	}

	@Test
	public void testConstructor() {
		new WeightsLayer(1,1);
		
		boolean success = false;
		try {
			new WeightsLayer(0, 0);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		success = false;
		try {
			new WeightsLayer(1, -1);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		success = false;
		try {
			new WeightsLayer(-1, 1);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
	}

}
