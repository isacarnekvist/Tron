package ann;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NeuronTest {
	
	private Neuron n1;

	@Before
	public void setUp() throws Exception {
		n1 = new Neuron();
	}

	@Test
	public void testInputOutput() {
		n1.input(1);
		n1.input(1);
		double res = n1.getOutput();
		assertTrue(res > 0 && res < 1);
		n1.reset();
		assertTrue(n1.getOutput() == 0);
		n1.input(-1);
		n1.input(-1);
		n1.input(0);
		res = n1.getOutput();
		assertTrue(res > -1 && res < 0);
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
