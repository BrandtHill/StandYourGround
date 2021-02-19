package game.SpawnSystem;

import java.util.Arrays;
import java.util.List;

public class Wave {

	private List<Spawn> spawns;
	private int delayMillis;
	
	public Wave(int delayMillis, Spawn...spawns) {
		this.delayMillis = delayMillis;
		this.spawns = Arrays.asList(spawns);
	}
	
	public List<Spawn> getSpawns() {return spawns;}
	public int getDelay() {return delayMillis;}
	public int getNumSpawned() {return spawns.stream().mapToInt(Spawn::getNum).sum();}
}
