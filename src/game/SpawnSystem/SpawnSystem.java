package game.SpawnSystem;

import game.Pieces.Obstacle;
import game.Main.STATE;
import game.Main;
import game.Audio.AudioPlayer;

public class SpawnSystem {

	private boolean doneCommencing, doneSpawning, zedsDead;
	private int lvNum, wvNum, ticks, delayMillis, zombiesRemaining;
	private Level currLevel;
	private Wave currWave;

	public enum REGION {
		UP, DOWN, LEFT, RIGHT
	}

	public enum ZOMBIE {
		NORMAL, DODGING, FAST, THICC
	}

	public SpawnSystem() {
		lvNum = 1;
		currLevel = Levels.getLevel(lvNum);
		//level = 11;
		doneCommencing = doneSpawning = false;
		ticks = 0;
		delayMillis = 0;
		wvNum = 0;
	}

	public void tick() {
		ticks++;
		if (doneCommencing) {
			if (!doneSpawning && ticks > delayTicks()) spawnWave(currWave);
			if (getRemaining() <= 0 && !zedsDead) {
				delayMillis = 2000;
				ticks = 0;
				zedsDead = true;
				AudioPlayer.getSound("LevelEnd").play(1.0f, 1.0f);
			}
			if (zedsDead && ticks > delayTicks()) completeLevel();
		}
	}

	private int delayTicks() {
		return (int) ((60f / 1000) * delayMillis);
	}
	
	private void resetBoard() {
		Main.handler.removeBlood();
		Main.handler.removeBrass();
		Main.handler.removeProjectiles();
		Main.handler.removeZombies();
		Main.handler.removeObstacles();
		Main.player.setX(Main.WIDTH / 2 - 10);
		Main.player.setY(Main.HEIGHT / 2 - 30);
		Main.player.resetAllAmmo();
		Main.player.autoWield();
		Main.player.setMoneyAtRoundStart(Main.player.getMoney());
	}
	
	public void completeLevel() {
		doneCommencing = false;
		doneSpawning = false;
		Main.gameState = STATE.StoreMenu;
		currLevel = Levels.getLevel(++lvNum);

		resetBoard();
	}
	
	public void commence() {spawnLevel(currLevel);}

	private void spawnLevel(Level level) {
		resetBoard();
		this.currLevel = level;
		this.zombiesRemaining = level.getTotalInLevel();
		this.delayMillis = level.getDelay();
		this.ticks = 0;
		this.wvNum = 0;
		this.doneCommencing = true;
		this.doneSpawning = false;
		this.zedsDead = false;
		this.currWave = level.getWaves().get(wvNum);
		if (lvNum >= 11) addObstaclesLvl11_15();
	}
	
	private void spawnWave(Wave wave) {
		wave.getSpawns().stream().forEach(this::spawn);
		this.delayMillis = wave.getDelay();
		this.ticks = 0;
		if (++wvNum < currLevel.getWaves().size()) {
			currWave = currLevel.getWaves().get(wvNum);
		}
		else doneSpawning = true;
	}
	
	private void spawn(Spawn spawn) {
		for (int i = 0; i < spawn.getNum(); i++) {
			Main.handler.addZombie(spawn.getRegion(), spawn.getZombie());
		}
	}

	private void addObstaclesLvl11_15() {
		Main.handler.addObjectAsync(new Obstacle(-50, 270, 428, 17));
		Main.handler.addObjectAsync(new Obstacle(585, 270, 265, 17));
		Main.handler.addObjectAsync(new Obstacle(640, 60, 87, 139));
	}

	public int getRemaining() {return zombiesRemaining;}
	public void decrementRemaining() {this.zombiesRemaining--;}
	public int getLevel() {return lvNum;}
	public void setLevel(int level) {this.lvNum = level;}
	public boolean zedsDead() {return zedsDead;}
}
