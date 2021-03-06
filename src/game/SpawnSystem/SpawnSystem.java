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
		NORMAL, DODGING, FAST, THICC, CHARGING
	}

	public SpawnSystem() {
		lvNum = 1;
		//lvNum = 20;
		currLevel = Levels.getLevel(lvNum);
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
				AudioPlayer.playSound("LevelEnd");
			}
			if (zedsDead && ticks > delayTicks()) completeLevel();
		}
	}

	private int delayTicks() {
		return (int) ((60f / 1000) * delayMillis);
	}
	
	private void resetBoard() {
		Main.handler.removeBlood();
		Main.handler.removeDeadZeds();
		Main.handler.removeBrass();
		Main.handler.removeBombs();
		Main.handler.removeProjectiles();
		Main.handler.removeZombies();
		Main.handler.removeObstacles();
		Main.player.setX(Main.WIDTH / 2 - 10);
		Main.player.setY(Main.HEIGHT / 2 - 30);
		Main.player.resetAllAmmo();
		Main.player.autoWield();
		Main.player.setMoneyAtRoundStart(Main.player.getMoney());
		Main.player.setBombsAtRoundStart(Main.player.getBombs());
	}
	
	public void completeLevel() {
		doneCommencing = false;
		doneSpawning = false;
		Main.gameState = STATE.StoreMenu;
		lvNum++;

		resetBoard();
	}
	
	public void commence() {
		currLevel = Levels.getLevel(lvNum);
		spawnLevel(currLevel);
	}

	private void spawnLevel(Level level) {
		resetBoard();
		if (lvNum >= 11 && lvNum <= 15) addObstaclesLvl11_15();
		else if (lvNum >= 16 && lvNum <= 20) addObstaclesLvl16_20();
		else if (lvNum >= 21) addObstaclesLvl21_25();
		
		this.ticks = 0;
		this.wvNum = 0;
		this.doneCommencing = true;
		this.doneSpawning = false;
		this.zedsDead = false;
		if (level == null) {
			System.out.println("Game Over. You Win.");
			Main.gameState = STATE.GameOverWin;
		} else {
			this.zombiesRemaining = level.getTotalInLevel();
			this.delayMillis = level.getDelay();
			this.currWave = level.getWaves().get(wvNum);
		}
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
	
	private void addObstaclesLvl16_20() {
		Main.handler.addObjectAsync(new Obstacle(346, 458, 139, 87));
		Main.handler.addObjectAsync(new Obstacle(500, 458, 139, 87));
		Main.handler.addObjectAsync(new Obstacle(512, 580, 300, 60));
		Main.handler.addObjectAsync(new Obstacle(784, -50, 50, 700));
		Main.handler.addObjectAsync(new Obstacle(253, 184, 59, 103));
	}
	
	private void addObstaclesLvl21_25() {
		Main.handler.addObjectAsync(new Obstacle(-10, -10, 353, 260));
		Main.handler.addObjectAsync(new Obstacle(476, 348, 334, 262));
		Main.handler.addObjectAsync(new Obstacle(476, -10, 324, 260));
		Main.handler.addObjectAsync(new Obstacle(-10, 348, 353, 262));
	}

	public int getRemaining() {return zombiesRemaining;}
	public void decrementRemaining() {this.zombiesRemaining--;}
	public int getLevel() {return lvNum;}
	public void setLevel(int level) {this.lvNum = level;}
	public boolean zedsDead() {return zedsDead;}
	public boolean sidearmsOnly() {return lvNum >=21 && lvNum <= 25;}
}
