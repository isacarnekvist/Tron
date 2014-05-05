package game;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Sprite {
	
	private Texture texture;
	private int fileWidth;
	private int imgHeight;
	private int imgWidth;
	private final int X = 0;
	private final int Y = 1;
	
	// Drawing coordinates
	private int rotX, rotY; // Around which point should the sprite be rotated
	
	// Vectors from rotation point to corners of image
	SimpleMatrix ul, ur, ll, lr;
	
	SimpleMatrix rotation;
	
	/**
	 * Constructor of self-drawing image.
	 * @param filename Must be of size 2^n*2^n and type *.png
	 * @param imgHeight Height of image within file, i.e <= height of file
	 * @param imgWidth Width of image within file, i.e <= width of file
	 */
	public Sprite(String filename, int imgHeight, int imgWidth) {
		texture = loadTexture(filename);
		fileWidth = texture.getImageWidth();
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		rotX = imgWidth/2;
		rotY = imgHeight/2;
		ul = new SimpleMatrix(1, 2, true, -imgWidth/2, -imgHeight/2); // Upper left
		ur = new SimpleMatrix(1, 2, true, imgWidth/2, -imgHeight/2); // Upper right
		ll = new SimpleMatrix(1, 2, true, -imgWidth/2, imgHeight/2); // Lower left
		lr = new SimpleMatrix(1, 2, true, imgWidth/2, imgHeight/2); // Lower right
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
	 * Set the coordinates for which the texture is supposed to be rotated around.
	 * @param x
	 * @param y
	 */
	public void setRotationPoint(int x, int y) {
		// Change all vectors that are pointing from rotation point to corners
		SimpleMatrix diff = new SimpleMatrix(1, 2, true, rotX - x, rotY - y);
		ul = ul.plus(diff);
		ur = ur.plus(diff);
		ll = ll.plus(diff);
		lr = lr.plus(diff);
		
		// Change rotation point
		rotX = x;
		rotY = y;
	}
	
	/**
	 * Rotate texture n radians clockwise (since our axes are inverted)
	 * @param r radians
	 */
	public void rotate(double r) {
		rotation = new SimpleMatrix(2, 2, true, Math.cos(r), Math.sin(r), Math.sin(-r), Math.cos(r));
		ul = ul.mult(rotation);
		ur = ur.mult(rotation);
		ll = ll.mult(rotation);
		lr = lr.mult(rotation);
	}
	
	/**
	 * @param posX x-coordinate of center or turning point (if set)
	 * @param posY y-coordinate of - || -
	 */
	public void draw(int posX, int posY) {
		texture.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2d(0, 0); // Upper left
			glVertex2d(posX + ul.get(X), posY + ul.get(Y));
			glTexCoord2d(0, (double)imgHeight/fileWidth); // Lower left
			glVertex2d(posX + ll.get(X), posY + ll.get(Y));
			glTexCoord2d((double)imgWidth/fileWidth, (double)imgHeight/fileWidth); // Lower right
			glVertex2d(posX + lr.get(X), posY + lr.get(Y));
			glTexCoord2d((double)imgWidth/fileWidth, 0); // Upper right
			glVertex2d(posX + ur.get(X), posY + ur.get(Y));
		}
		glEnd();
	}
	
	public int getWidth() {
		return imgWidth;
	}
	
	public int getHeight() {
		return imgHeight;
	}
	
	public SimpleMatrix getFrontLeft() {
		return ur;
	}
	
	public SimpleMatrix getFrontRight() {
		return lr;
	}
}
