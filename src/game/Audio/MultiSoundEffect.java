package game.Audio;

public class MultiSoundEffect implements ISound {
	private final int NUM_CLIPS = 8;
	private int effectIndex;
	private String filename;
	private SoundEffect[] effects;
	
	public MultiSoundEffect(String filename) {
		this.filename = filename;
		this.effects = new SoundEffect[NUM_CLIPS];
		this.effectIndex = 0;
	}
	
	public MultiSoundEffect init() {
		for (int i = 0; i < NUM_CLIPS; i++) {
			effects[i] = new SoundEffect(filename);
			effects[i].init();
		}
		return this;
	}
	
	public void start() {
		effects[effectIndex].start();
		increment();
	}
	
	public void resume() {
		effects[effectIndex].resume();
		increment();
	}

	public void pause() {
		effects[effectIndex].pause();
		increment();
	}
	
	public void reset() {
		effects[effectIndex].reset();
		increment();
	}
	
	private void increment() {
		effectIndex++;
		effectIndex %= NUM_CLIPS;
	}
}
