package game;

import org.ejml.simple.SimpleMatrix;
import java.lang.Math;


public class Powerup {
	private Sprite Sprite;
	private SimpleMatrix pos;
	private int good; // >= 0 = good powerup, < 0 = bad powerup (unnecessary?)
	static Random r = new Random();


	private final int X = 0;
	private final int Y = 1;

	private final String type;
	private final String[] TYPES = {
		"Speed",
		"Slow",
		// "Glitch",
		"Longer",
		"Shorter"
	}

	/**
	 * @param area The area in which to spawn
	 */
	public Powerup(SimpleMatrix area) {
		good = r.nextInt();
		pos = new SimpleMatrix(
            r.nextInt(area.get(X) - 100),
            r.nextInt(area.get(Y) - 100)
        );
		type = r.nextInt(TYPES.length);
		switch (type) {
		case "Speed":
			sprite = new Sprite("res/powerup.png", 64, 64);
			break;
		case "Slow":
			sprite = new Sprite("res/powerup.png", 64, 64);
			break;
		case "Longer":
			sprite = new Sprite("res/powerup.png", 64, 64);
			break;
		case "Shorter":
			sprite = new Sprite("res/powerup.png", 64, 64);
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
     * @param otherPos Center coordinates of object to check against. OK to send null.
     * @param radius Approximate radius of circle around object to check against.
     * @param other List of coordinates around the object to check against.
     * @return
     */
    public boolean isCollision(
        SimpleMatrix otherPos,
        double radius,
        ArrayList<SimpleMatrix> other) {
        
        boolean bodyProximity;
        
        if (otherPos == null) {
            bodyProximity = true;
        } else {
            bodyProximity = getCenter().minus(otherPos).normF() < 60 + radius;
        }
        if(bodyProximity) {
            ArrayList<SimpleMatrix> me = getBoundingCoordinates();
            for (int i = 0; i < me.size(); i++) {
                for (int j = 0; j < other.size(); j++) {
                    if(Geometry.linesIntersect(me.get(i), me.get((i + 1)%me.size()), 
                                            other.get(j), other.get((j + 1) % other.size()))) {
                        return true;
                    }
                }
            }
        }
        
        if(otherPos != null) {
            return tail.isCollision(otherPos, radius, other);
        } else {
            return false;
        }
    }

    public SimpleMatrix getPos() {
        return pos;
    }

    public getType() {
        return type;
    }
}