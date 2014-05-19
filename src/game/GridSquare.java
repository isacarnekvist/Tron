package game;
import org.newdawn.slick.Color;

/**
 * Handles a single square in grid
 */
public class GridSquare {
	
	private Sprite sprite;
	private int x;			// Position of this squares center on the screen
	private int y;			// -
	private double visitedTime;
	
	/**
	 * Constructor
	 * @param x x-coordinate of where to draw
	 * @param y y-coordinate of where to draw
	 */
	public GridSquare(int x, int y) {
		sprite = new Sprite("res/grid.png", 256, 256);
		this.x = x;
		this.y = y;
	}
	
	public void render() {
		double t = visitedTime - System.nanoTime()/1E9;
		float flash = 0.35f*(float)Math.pow(Math.E, t);
		float hue = 0.5f + 0.5f*(float)Math.sin((double)System.nanoTime()/1e10);
		int rgb = java.awt.Color.HSBtoRGB(hue, 0.8f, 0.5f + flash);
		new Color(rgb).bind();
		sprite.draw(x, y);
		Color.white.bind();
	}
	
	/**
	 * Notifies this square that a bike is on it. Make it light up!
	 */
	public void bikeOver() {
		visitedTime = System.nanoTime()/1E9;
	}
}
