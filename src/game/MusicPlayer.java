package game;
import java.io.IOException;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.util.ResourceLoader;

public class MusicPlayer {
	
	private Audio start;
	private Audio game;
	private Audio crash;
	private Audio slow;
	private final int START = -1;
	private final int GAME = 0;
	private final int END = 1;
	
	public MusicPlayer() {
		start = loadFile("OGG", "res/Overture.ogg");
		game = loadFile("OGG", "res/Derezzed.ogg");
		crash = loadFile("AIF", "res/crash.aiff");
		slow = loadFile("WAV", "res/slowdown.wav");
	}
	
	public void playState(int state) {
		switch (state) {
		case START:
			start.playAsMusic(1f, 1f, true);
			start.setPosition(52f);
			break;
		case GAME:
			game.playAsMusic(1f, 1f, true);
			game.setPosition(0.5f);
			break;
		case END:
			slow.playAsSoundEffect(1f, 1f, false);
			crash.playAsMusic(1f, 1f, true);
		}
	}
	
	private Audio loadFile(String type, String filename) {
		try {
			return AudioLoader.getAudio(type, ResourceLoader.getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
}
