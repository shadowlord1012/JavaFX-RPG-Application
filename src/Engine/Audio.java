package Engine;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Settings.Global;

public class Audio {
	private Clip clip;
	File soundFile[] = new File[5];
	FloatControl fc;
	
	public Audio() {
		//TODO add in all audio BGM SE etc.
		soundFile[0] = new File("./Assets/Audio/BGM/TitleMusic.wav");
		soundFile[1] = new File("./Assets/Audio/BGM/19-hyrule-field-main-theme.wav");
	}
	
	public void playBGM(int BGMNumber) {
		if(this.clip != null)
			stop();
		setFile(BGMNumber);
		fc.setValue(Global.MASTER_VOLUME);
		loop();
	}
	
	public void playSE(int SENumber)
	{
		setFile(SENumber);
		fc.setValue(Global.SOUND_EFFECT_VOLUME);
		play();
	}
	/**
	 * Sets the sound file for which is to be played on the screen
	 * @param i
	 */
	private void setFile(int i)
	{
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays the Sound Clip
	 */
	private void play() {
		clip.start();
	}
	
	/**
	 * Creates a continuous loop of a clip
	 */
	private void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Stops the clip playing
	 */
	private void stop() {
		clip.stop();
	}
}
