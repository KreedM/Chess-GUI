package logic;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundFX {
	
	public static boolean mute;
	
	private static void playSound(URL sound) {
		try {
			if(!mute) {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);
				Clip move = AudioSystem.getClip();
				move.open(audioIn);
				move.start();
			}
		}
	    catch (UnsupportedAudioFileException e) {
	    	e.printStackTrace();
	    }
        catch (IOException e) {
        	e.printStackTrace();
        }
        catch (LineUnavailableException e) {
        	e.printStackTrace();
        }
	}
	
	public void gameStartSound() {
		playSound(getClass().getResource("/sound/gameStart.wav"));
	}
	
	public void gameEndSound() {
		playSound(getClass().getResource("/sound/gameEnd.wav"));
	}
	
	public void moveSound() {
		playSound(getClass().getResource("/sound/move.wav"));
	}
	
	public void captureSound() {
		playSound(getClass().getResource("/sound/capture.wav"));
	}
	
	public void checkSound() {
		playSound(getClass().getResource("/sound/check.wav"));
	}
	
	public void castleSound() {
		playSound(getClass().getResource("/sound/castle.wav"));
	}
	
	public void mute() {
		mute = true;
	}
	
	public void unmute() {
		mute = false;
	}
}
