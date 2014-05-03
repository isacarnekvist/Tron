import java.io.IOException;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.util.ResourceLoader;

public class MusicPlayer {
	
	private Audio start;
	private Audio game;
	private final int START = -1;
	private final int GAME = 0;
	
	public MusicPlayer() {
		start = loadFile("res/Overture.ogg");
		game = loadFile("res/Derezzed.ogg");
	}
	
	public void playTrack(int state) {
		switch (state) {
		case START:
			start.playAsMusic(1f, 1f, true);
			break;
		case GAME:
			game.playAsMusic(1, 1, true);
			break;
		}
	}
	
	public void playSoundFX() {
		
	}
	
	private Audio loadFile(String filename) {
		try {
			return AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
}
