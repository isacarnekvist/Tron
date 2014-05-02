import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class TailSprite {
	
	private Texture texture;
	private final int X = 0;
	private final int Y = 1;
	private SimpleMatrix ul, ur;
	private SimpleMatrix rotatedUL, rotatedUR;
	
	/**
	 * Constructor of self-drawing tail
	 * @param filename Must be of size 8*8 and type *.png

	 */
	public TailSprite(String filename) {
		texture = loadTexture(filename);
		ul = new SimpleMatrix(1, 2, true, -6, -4);
		ur = new SimpleMatrix(1, 2, true, 6, -4);
	}
	
	private Texture loadTexture(String filename) {
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);			
		}
		
		return null;
	}	
	
	/**
	 * @param r radians
	 * @return A rotation matrix with the given angle (clockwise positive)
	 */
	public SimpleMatrix rotate(double r) {
		return new SimpleMatrix(2, 2, true, Math.cos(r), Math.sin(r), Math.sin(-r), Math.cos(r));
	}
	
	/**
	 * @param posX x-coordinate of center of tail
	 * @param posY y-coordinate of center of tail
	 * @param angleRad The angle from x-axis (downwards positive) that tail should be rotated before drawn
	 */
	public void draw(double posX, double posY, double angleRad) {
		rotatedUL = ul.mult(rotate(angleRad));
		rotatedUR = ur.mult(rotate(angleRad));
		texture.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2d(0, 0); // Upper left
			glVertex2d(posX + rotatedUL.get(X), posY + rotatedUL.get(Y));
			glTexCoord2d(0, 1); // Lower left
			glVertex2d(posX - rotatedUR.get(X), posY - rotatedUR.get(Y));
			glTexCoord2d(1, 1); // Lower right
			glVertex2d(posX - rotatedUL.get(X), posY - rotatedUL.get(Y));
			glTexCoord2d(1, 0); // Upper right
			glVertex2d(posX + rotatedUR.get(X), posY + rotatedUR.get(Y));
		}
		glEnd();
	}
}
