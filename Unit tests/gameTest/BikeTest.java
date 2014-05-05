package gameTest;
import static org.junit.Assert.*;
import game.Bike;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Display;

public class BikeTest {
	private Bike bike1;
	private Bike bike2;

	@Before
	public void setUp() throws Exception {
		Display.create();
		bike1 = new Bike(1, 200, 300);
		bike2 = new Bike(2, 400, 500);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(bike1.getRotatingPoint().get(0), 200, 1E-6);
		assertEquals(bike1.getRotatingPoint().get(1), 300, 1E-6);
		assertEquals(bike2.getRotatingPoint().get(0), 400, 1E-6);
		assertEquals(bike2.getRotatingPoint().get(1), 500, 1E-6);
		
		boolean thrown = false;
		try {
			bike1 = new Bike(0, 0, 0);
		} catch(IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			bike1 = new Bike(3, 0, 0);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}
