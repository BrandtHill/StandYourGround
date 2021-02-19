package game.SpawnSystem;

import game.SpawnSystem.SpawnSystem.REGION;
import game.SpawnSystem.SpawnSystem.ZOMBIE;

public class Spawn {
	private ZOMBIE zombie;
	private REGION region;
	private int num;
	
	public Spawn(REGION region, ZOMBIE zombie, int num) {
		this.zombie = zombie;
		this.region = region;
		this.num = num;
	}
	
	public ZOMBIE getZombie() {return zombie;}
	public REGION getRegion() {return region;}
	public int getNum() {return num;}
}
