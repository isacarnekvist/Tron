package ann;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NeuronLayerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructor() {
		
		boolean success = false;
		try {
			new NeuronLayer(0);
		} catch (IllegalArgumentException e) {
			success = true;
		}
		assertTrue(success);
	}

}
