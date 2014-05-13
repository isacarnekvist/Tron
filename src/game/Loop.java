package game;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Loop {
	
	private static int fps = 60;
	private final int screenPixelHeight = 2000;
	private double aspectRatio;
	private GameController gc;
	
	private Loop() {
		
		initGraphics();
		gc = new GameController((int)(screenPixelHeight/aspectRatio), screenPixelHeight);
				
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			gc.render(1000/fps);
			
			if(gc.getIteration() % 8 == 0) {
				Display.sync(fps*2);
				Display.update();	
			}	
		}
		Display.destroy();
		AL.destroy();
		System.exit(0);
	}
	
	private void initGraphics() {
		// LWJGL Setup
		try {
			Display.setFullscreen(true);
			//Display.setDisplayMode(new DisplayMode(1100, 700));
			//Display.setLocation(0, 0);
			Display.setTitle("TRON-SNAKE");
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		Mouse.setGrabbed(true);
		aspectRatio = (double)Display.getHeight()/Display.getWidth();
		
		// OpenGL setup
		glClearColor(0f, 0f, 0f, 1f);
		glOrtho(0, screenPixelHeight/aspectRatio, (double)screenPixelHeight, 0, 1, -1);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void main(String[] args) {
		new Loop();
	}
}
