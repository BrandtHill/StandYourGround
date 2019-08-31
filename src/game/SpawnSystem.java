package game;

import game.Pieces.Player;
import game.Program.STATE;

public class SpawnSystem {

	private static Handler handler;
	private static Player player;
	private boolean doneCommencing, doneSpawning, zedsDead;
	private int level, wave, ticks, delayMillis;

	public enum REGION {
		UP, DOWN, LEFT, RIGHT
	}

	public enum ZOMBIE {
		NORMAL, DODGING, FAST
	}

	public SpawnSystem() {
		level = 1;
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
		player.setLevel(level);
		player.setMoneyAtRoundStart(player.getMoney());

		handler.removeBlood();
		handler.removeBrass();
		handler.removeProjectiles();
		handler.removeZombies();
	}

	public void commenceLevel() {
		handler.removeBlood();
		handler.removeBrass();
		handler.removeProjectiles();
		handler.removeZombies();
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
			player.zombiesLeft = 16;
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
			player.zombiesLeft = 34;
			delayMillis = 500;
			break;

		case 9:
			player.zombiesLeft = 38;
			delayMillis = 1000;
			break;

		case 10:
			player.zombiesLeft = 20;
			delayMillis = 2000;
			break;

		default:
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
		case 1:
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
		case 2:
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				delayMillis = 1000;
				break;

			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				delayMillis = 2000;
				break;

			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 1);
				delayMillis = 3000;
				break;

			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				doneSpawning = true;
				break;
			}
			break;

		case 3:
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				delayMillis = 2500;
				break;

			case 2:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 6);
				doneSpawning = true;
				break;
			}
			break;

		case 4:
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 5000;
				break;

			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;

		case 5:
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 15);
				delayMillis = 6000;
				break;

			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 10);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;
				break;
			}
			break;

		case 6:
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 3);
				delayMillis = 7500;
				break;

			case 2:
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 4);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 5);
				doneSpawning = true;
				break;
			}
			break;

		case 7:
			switch (wave) {
			case 1:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 7);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 7);
				delayMillis = 7500;
				break;

			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 7);
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 7);
				delayMillis = 5000;
				break;

			case 3:
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				doneSpawning = true;
				break;
			}
			break;

		case 8:
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				delayMillis = 2250;
				break;

			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				delayMillis = 2250;
				break;

			case 3:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				delayMillis = 2250;
				break;

			case 4:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				delayMillis = 2250;
				break;

			case 5:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 3);
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 2);
				doneSpawning = true;
				break;
			}
			break;

		case 9:
			switch (wave) {
			case 1:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
				spawnZombies(REGION.DOWN, ZOMBIE.FAST, 1);
				delayMillis = 6000;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 8000;
				break;
			
			case 3:
				spawnZombies(REGION.RIGHT, ZOMBIE.DODGING, 6);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 6);
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 7);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 7);
				doneSpawning = true;
				break;
			}
			break;

		case 10:
			switch (wave) {
			case 1:
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 12);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 2);
				delayMillis = 10000;
				break;

			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 5);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 1);
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

	private void spawnZombies(REGION region, ZOMBIE zombie, int num) {
		for (int i = 0; i < num; i++) {
			handler.addZombie(zombie, region);
		}
	}

	public int getRemaining() {
		return player.zombiesLeft;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int l) {
		level = l;
	}

}
