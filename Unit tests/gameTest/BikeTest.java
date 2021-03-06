package gameTest;
import static org.junit.Assert.*;

import org.junit.After;

//import java.util.ArrayList;

import game.Bike;

//import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class BikeTest {
	private Bike bike1;
	private Bike bike2;

	@Before
	public void setUp() throws Exception {
		Display.setDisplayMode(new DisplayMode(100, 100));
		Display.create();
		bike1 = new Bike(1, 200, 300);
		bike2 = new Bike(2, 400, 500);
	}
	
	@After
	public void tearDown() {
		Display.destroy();
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
	
	@Test
	public void testBoundingCoordinates() {
		//ArrayList<SimpleMatrix> list = bike1.getBoundingCoordinates();
	}
}
