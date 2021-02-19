package game.SpawnSystem;

import java.util.Arrays;
import java.util.List;

public class Level {

	private List<Wave> waves;
	private int delayMillis;
	
	public Level(int delayMillis, Wave...waves) {
		this.delayMillis = delayMillis;
		this.waves = Arrays.asList(waves);
	}
	
	public List<Wave> getWaves() {return waves;}
	public int getDelay() {return delayMillis;}
	public int getTotalInLevel() {return waves.stream().mapToInt(Wave::getNumSpawned).sum();}
}
