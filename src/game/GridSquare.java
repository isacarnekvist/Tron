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
		double flash = 0.35*Math.pow(Math.E, t);
		Color c = new Color(
			(float) (flash + 0.3 + 0.3*Math.sin((double)System.nanoTime()/3E9)),
			(float) (flash + 0.3 + 0.3*Math.sin(4*Math.PI/6 + (double)System.nanoTime()/3E9)),
			(float) (flash + 0.3 + 0.3*Math.sin(8*Math.PI/6 + (double)System.nanoTime()/3E9)),
			1f
		);
		c.bind();
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
