package game;

import game.Pieces.Obstacle;
import game.Pieces.Player;
import game.Program.STATE;
import game.Audio.AudioPlayer;

public class SpawnSystem {

	private static Handler handler;
	private static Player player;
	private boolean doneCommencing, doneSpawning, zedsDead;
	private int level, wave, ticks, delayMillis;

	public enum REGION {
		UP, DOWN, LEFT, RIGHT
	}

	public enum ZOMBIE {
		NORMAL, DODGING, FAST, THICC
	}

	public SpawnSystem() {
		level = 1;
		//level = 11;
		handler = Program.handler;
		player = Program.player;
		doneCommencing = doneSpawning = false;
		ticks = 0;
		delayMillis = 0;
		wave = 1;
	}

	public void tick() {
		ticks++;
		if (doneCommencing) {
			if (!doneSpawning && ticks > delayTicks()) spawn();
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
	
	public void completeLevel() {
		doneCommencing = false;
		doneSpawning = false;
		Program.gameState = STATE.StoreMenu;
		level++;

		player.resetAllAmmo();
		player.setMoneyAtRoundStart(player.getMoney());

		handler.removeBlood();
		handler.removeBrass();
		handler.removeProjectiles();
		handler.removeZombies();
		handler.removeObstacles();
	}

	public void commenceLevel() {
		handler.removeBlood();
		handler.removeBrass();
		handler.removeProjectiles();
		handler.removeZombies();
		handler.removeObstacles();
		player.setX(Program.WIDTH / 2 - 10);
		player.setY(Program.HEIGHT / 2 - 30);
		player.resetAllAmmo();
		player.autoWield();
		player.setMoneyAtRoundStart(player.getMoney());
		wave = 1;
		doneSpawning = false;
		zedsDead = false;

		switch (level) {

		case 1:
			player.zombiesLeft = 6;
			delayMillis = 1000;
			break;

		case 2:
			player.zombiesLeft = 12;
			delayMillis = 1000;
			break;

		case 3:
			player.zombiesLeft = 14;
			delayMillis = 2500;
			break;

		case 4:
			player.zombiesLeft = 20;
			delayMillis = 500;
			break;

		case 5:
			player.zombiesLeft = 30;
			delayMillis = 1500;
			break;

		case 6:
			player.zombiesLeft = 15;
			delayMillis = 500;
			break;

		case 7:
			player.zombiesLeft = 30;
			delayMillis = 500;
			break;

		case 8:
			player.zombiesLeft = 36;
			delayMillis = 500;
			break;

		case 9:
			player.zombiesLeft = 34;
			delayMillis = 1000;
			break;

		case 10:
			player.zombiesLeft = 50;
			delayMillis = 2000;
			break;

		case 11:
			player.zombiesLeft = 16;
			delayMillis = 3000;
			addObstaclesLvl11_15();
			break;
			
		case 12:
			player.zombiesLeft = 24;
			delayMillis = 500;
			addObstaclesLvl11_15();
			break;
			
		case 13:
			player.zombiesLeft = 40;
			delayMillis = 1500;
			addObstaclesLvl11_15();
			break;
			
		case 14:
			player.zombiesLeft = 55;
			delayMillis = 1500;
			addObstaclesLvl11_15();
			break;
			
		case 15:
			player.zombiesLeft = 80;
			delayMillis = 1500;
			addObstaclesLvl11_15();
			break;
			
		default:
			addObstaclesLvl11_15();
			player.zombiesLeft = 10 * level;
			for (int i = 0; i < 10 * level; i++) {
				handler.addRandomZombie();
			}
			break;
		}

		ticks = 0;
		doneCommencing = true;

	}

	private void spawn() {
		ticks = 0;

		switch (level) {
		case 1: //6
			switch (wave) {
			case 1:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				delayMillis = 5000;
				break;

			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;
		case 2: //12
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				delayMillis = 3000;
				break;

			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				delayMillis = 4000;
				break;

			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 1);
				delayMillis = 4000;
				break;

			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				doneSpawning = true;
				break;
			}
			break;

		case 3: //14
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				delayMillis = 10000;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				delayMillis = 9000;
				break;
				
			case 3:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 5);
				doneSpawning = true;
				break;
			}
			break;

		case 4: //20
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 4);
				delayMillis = 5000;
				break;

			case 2:
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				delayMillis = 5000;
				break;
				
			case 3:
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;

		case 5: //30
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 10);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 5);
				delayMillis = 9000;
				break;

			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 10);
				delayMillis = 4000;
				
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;

		case 6: //15
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				delayMillis = 7500;
				break;
				
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 2);
				delayMillis = 10000;
				break;

			case 3:
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 3);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 1);
				delayMillis = 2000;
				break;
			
			case 4:
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 3);
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 2);
				doneSpawning = true;
				break;
			}
			break;

		case 7: //30
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 8);
				delayMillis = 12000;
				break;

			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 8);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 5000;
				break;

			case 3:
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				doneSpawning = true;
				break;
			}
			break;

		case 8: //36
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 1);
				delayMillis = 5500;
				break;

			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 1);
				delayMillis = 5500;
				break;

			case 3:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 1);
				delayMillis = 3750;
				break;

			case 4:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 1);
				delayMillis = 6000;
				break;

			case 5:
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 3);
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 3);
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 3);
				doneSpawning = true;
				break;
			}
			break;

		case 9: //34
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				delayMillis = 6500;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 12000;
				break;
			
			case 3:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				delayMillis = 6000;
				break;
			
			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				delayMillis = 9000;
				break;
				
			case 5:
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 1);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 2);
				doneSpawning = true;
				break;
			}
			break;

		case 10: //50
			switch (wave) {
			case 1:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 8);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 5000;
				break;

			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 4);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 2);
				delayMillis = 6000;
				break;
				
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 8);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				delayMillis = 18000;
				break;
				
			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 4);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 2);
				delayMillis = 4000;
				break;
				
			case 5:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 4);
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 4);
				delayMillis = 4000;
				break;
				
			case 6:
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 6);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				doneSpawning = true;
				break;
			}
			break;
			
		case 11: //16
			switch (wave) {
			case 1:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 4);
				delayMillis = 6000;
				break;
				
			case 2:
				spawnZombies(REGION.UP, ZOMBIE.FAST, 4);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				delayMillis = 6000;
				break;
				
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.THICC, 4);
				doneSpawning = true;
				break;
			}
			break;
			
		case 12: //24
			switch (wave) {
			case 1:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 6);
				delayMillis = 3000;
				break;
				
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 6);
				delayMillis = 4000;
				break;
				
			case 3:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 12);
				doneSpawning = true;
				break;
			}
			break;
			
		case 13: //40
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 2);
				delayMillis = 6000;
				break;
				
			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 2);
				delayMillis = 5000;
				break;
				
			case 3:
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 6);
				spawnZombies(REGION.UP, ZOMBIE.THICC, 6);
				delayMillis = 11000;
				break;
			
			case 4:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 4);
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				delayMillis = 5000;
				break;
			
			case 5:
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 4);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 4);
				delayMillis = 2500;
				break;
				
			case 7:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;
		
		case 14: //55
			switch (wave) {
			case 1:
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 8);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 3);
				delayMillis = 9000;
				break;
				
			case 2:
				spawnZombies(REGION.UP, ZOMBIE.THICC, 8);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 3);
				delayMillis = 9000;
				break;
				
			case 3:
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 8);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				delayMillis = 9000;
				break;
				
			case 4:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 12);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 2);
				delayMillis = 5000;
				break;
				
			case 5:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 8);
				doneSpawning = true;
				break;
			}
			break;
			
		case 15: //80
			switch (wave) {
			case 1:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				delayMillis = 9000;
				break;
				
			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.RIGHT, ZOMBIE.THICC, 2);
				delayMillis = 9000;
				break;
			
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 2);
				delayMillis = 9000;
				break;
			
			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 9000;
				break;
			
			case 5:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.DOWN, ZOMBIE.THICC, 2);
				delayMillis = 9000;
				break;
				
			case 6:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 2);
				delayMillis = 9000;
				break;
				
			case 7:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				delayMillis = 9000;
				break;
				
			case 8:
				spawnZombies(REGION.UP, ZOMBIE.FAST, 10);
				doneSpawning = true;
				break;
			}
			break;
		default:
			doneSpawning = true;
			break;
		}
		wave++;
	}

	private void addObstaclesLvl11_15() {
		handler.addObjectAsync(new Obstacle(-50, 270, 428, 17));
		handler.addObjectAsync(new Obstacle(585, 270, 265, 17));
		handler.addObjectAsync(new Obstacle(640, 60, 87, 139));
	}
	
	private void spawnZombies(REGION region, ZOMBIE zombie, int num) {
		for (int i = 0; i < num; i++) {
			handler.addZombie(region, zombie);
		}
	}

	public int getRemaining() {return player.zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}
	public boolean zedsDead() {return zedsDead;}
}
