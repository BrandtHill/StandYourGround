package game.Audio;

public interface ISound {
	public ISound init();
	
	public void start();
	
	public void resume();
	
	public void pause();
	
	public void reset();
	
	public float getGain();
	
	public void setGain(float gain);
}
