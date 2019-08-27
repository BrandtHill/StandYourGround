package game.Audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundEffect implements ISound {
	private String filename;
	private long position;
	private Clip clip;
	private FloatControl gain;
	
	public SoundEffect(String filename) {
		this.filename = filename;
		this.position = 0;
	}
	
	public SoundEffect init() {
		try {
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename)); 
			clip.open(ais);
			gain = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void start() {
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	public void resume() {
		clip.start();
	}

	public void pause() {
		clip.stop();
	}
	
	public void reset() {
		clip.setMicrosecondPosition(0);
	}
	
	public float getGain() {
		return gain.getValue();
	}
	
	public void setGain(float gain) {
		this.gain.setValue(gain);
	}
}
