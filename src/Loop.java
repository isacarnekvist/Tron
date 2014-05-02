import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

public class Loop {
	
	private static int fps = 60;
	private final int screenPixelHeight = 1000;
	private double aspectRatio;
	private GameController gc;
	
	private Loop() {
		
		initGraphics();
		gc = new GameController((int)(screenPixelHeight/aspectRatio), screenPixelHeight);
				
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			gc.render(1000/fps);
			
			Display.sync(fps);
			Display.update();		
		}
		Display.destroy();
		System.exit(0);
	}
	
	private void initGraphics() {
		// LWJGL Setup
		try {
			Display.setFullscreen(true);
			//Display.setDisplayMode(new DisplayMode(800, 640));
			Display.setTitle("TRON-SNAKE");
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
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
