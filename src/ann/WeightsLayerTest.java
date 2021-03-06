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
		w1 = new WeightsLayer(1, 1, true);
		w2 = new WeightsLayer(1, 10, false);
		w3 = new WeightsLayer(10, 1, true);
		w4 = new WeightsLayer(10, 10, false);
		w5 = new WeightsLayer(10, 10, false);
		w6 = new WeightsLayer(5, 7, true);
		w7 = new WeightsLayer(5, 7, true);
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
		WeightsLayer w8 = w6.mateWithR(w7);
		w7 = w8.mateWithS(w6, 20);
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
		new WeightsLayer(1,1, false);
		
		boolean success = false;
		try {
			new WeightsLayer(0, 0, true);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		success = false;
		try {
			new WeightsLayer(1, -1, false);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
		
		success = false;
		try {
			new WeightsLayer(-1, 1, true);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
	}

}
