package game;

import game.Program.STATE;

public class SpawnSystem {

	private static Handler handler;
	private static Player player;
	private boolean doneCommencing, doneSpawning;
	private int zombiesLeft;
	private int level, wave;
	private long timer, delay;
	public enum REGION {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	public enum ZOMBIE {
		NORMAL,
		DODGING,
		FAST
	}
	
	public SpawnSystem() {
		level = 1;
		handler = Program.handler;
		player = Program.player;
		doneCommencing = doneSpawning = false;
		zombiesLeft = 0;
		timer = System.currentTimeMillis();
		delay = 0;
		wave = 1;
	}
	
	public void tick() {
		if (doneCommencing) {
			if(!doneSpawning) {
				if (System.currentTimeMillis() - timer > delay) spawn();
			}
			
			zombiesLeft = player.zombiesLeft;
			
			if (zombiesLeft <= 0) completeLevel();
		}
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
		handler.removeProjectiles();
		handler.removeZombies();
	}
	
	public void commenceLevel() {
		handler.removeBlood();
		handler.removeProjectiles();
		handler.removeZombies();
		player.setX(Program.WIDTH/2-10);
		player.setY(Program.HEIGHT/2-30);
		player.resetAllAmmo();
		player.autoWield();
		player.setMoneyAtRoundStart(player.getMoney());
		wave = 1;
		doneSpawning = false;
		
		switch(level) {
		
		case 1: 
			player.zombiesLeft = 6;
			delay = 1000;
			break;
		
		case 2:
			player.zombiesLeft = 12;
			delay = 1000;
			break;
		
		case 3:
			player.zombiesLeft = 16;
			delay = 2500;
			break;
		
		case 4:
			player.zombiesLeft = 20;
			delay = 500;
			break;
		
		case 5:
			player.zombiesLeft = 30;
			delay = 1500;
			break;
		
		case 6:
			player.zombiesLeft = 15;
			delay = 500;
			break;	
		
		default:
			player.zombiesLeft = 10 * level;
			for(int i = 0; i < 10 * level; i++) {
				handler.addRandomZombie();
			}
			break;
		}
		
		timer = System.currentTimeMillis();
		doneCommencing = true;
		
	}
	
	private void spawn() {
		timer = System.currentTimeMillis();
		
		switch(level) {
		case 1:
			switch(wave) {
			case 1:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				delay = 5000;
				break;
				
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3);
				doneSpawning = true;	
				break;
				
			}
			break;
		case 2:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 3);
				delay = 1000;
				break;
			
			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 1);
				delay = 2000;
				break;
			
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 1);
				delay = 3000;
				break;
				
			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				doneSpawning = true;
				break;
			}
			break;
			
		case 3:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 1);
				spawnZombies(REGION.LEFT, ZOMBIE.DODGING, 2);
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 3);
				delay = 2500;
				break;
			
			case 2:
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 6);
				doneSpawning = true;
				break;
			}
			break;
			
		case 4:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.UP, ZOMBIE.FAST, 3);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 3);
				delay = 5000;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 2);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 2);
				doneSpawning = true;
				break;			
			}
			break;			
		
		case 5:	
			switch(wave) {
			case 1: 
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 15);
				delay = 6000;
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
			switch(wave) {
			case 1: 				
				spawnZombies(REGION.RIGHT, ZOMBIE.FAST, 3);
				spawnZombies(REGION.LEFT, ZOMBIE.FAST, 3);
				delay = 7500;
				break;
			
			case 2:
				spawnZombies(REGION.UP, ZOMBIE.DODGING, 4);
				spawnZombies(REGION.DOWN, ZOMBIE.DODGING, 5);
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
	
	public int getRemaining() {return zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int l) {level = l;}

}
