package game;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Random;


public class Powerup {
	private Sprite sprite;
	private SimpleMatrix pos;
	static Random r = new Random();


	private final int X = 0;
	private final int Y = 1;

	private String type;
	private final String[] types = {
		"Speed",
		"Longer",
		// "Glitch",
		"Slow",
		"Shorter"
	};

	/**
	 * @param area The area in which to spawn
	 */
	public Powerup(int width, int height) {
		pos = new SimpleMatrix(1, 2, true,
            100 + r.nextInt(width - 200),
            100 + r.nextInt(height - 200)
        );
		type = types[r.nextInt(2)];
		switch (type) {
		case "Speed":
			sprite = new Sprite("res/powerup_speed.png", 52, 52);
			break;
		case "Longer":
			sprite = new Sprite("res/powerup_length.png", 52, 52);
			break;
		case "Slow":
			sprite = new Sprite("res/powerup_speed.png", 52, 52);
			break;
		case "Shorter":
			sprite = new Sprite("res/powerup_speed.png", 52, 52);
			break;
		default:
			break;
		}
		render();
	}

	/**
	 * Renders the pickup on the game grid
	 */
	public void render() {
		sprite.draw(pos.get(X), pos.get(Y));
	}


	/**
	 * Marks this powerup as picked up 
	 */
	public void pickup() {

	}
	
	/**
	 * @return An arraylist with coordinates surrounding this powerup.
	 */
	public ArrayList<SimpleMatrix> getBoundingCoordinates() {
		ArrayList<SimpleMatrix> res = new ArrayList<SimpleMatrix>();
		for(int i = 0; i < 6; i++) {
			res.add(new SimpleMatrix(1, 2, true, pos.get(X) + 26*Math.cos(i*Math.PI/3), 
												 pos.get(Y) + 26*Math.sin(i*Math.PI/3)));
		}
		return res;
	}

    public SimpleMatrix getPos() {
        return pos;
    }

    public String getType() {
        return type;
    }
}