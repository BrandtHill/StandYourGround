package game;

import game.Program.STATE;

public class SpawnSystem {

	private static Handler handler;
	private static Player player;
	private boolean doneCommencing, doneSpawning;
	private int zombiesLeft;
	private int level, wave;
	private long timer, delay;
	private enum REGION {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	private enum ZOMBIE {
		NORMAL,
		DODGING
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
			player.zombiesLeft = 8;
			delay = 1000;
			break;
		
		case 2:
			player.zombiesLeft = 16;
			delay = 1000;
			break;
		
		case 3:
			player.zombiesLeft = 24;
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
			player.zombiesLeft = 20 * level;
			for(int i = 0; i < 20 * level; i++) {
				Handler.addZombie();
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
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 4);
				delay = 5000;
				break;
				
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				doneSpawning = true;	
				break;
				
			}
			break;
		case 2:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.UP, ZOMBIE.NORMAL, 5);
				delay = 1000;
				break;
			
			case 2:
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 3);
				delay = 2000;
				break;
			
			case 3:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 4);
				delay = 3000;
				break;
				
			case 4:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				doneSpawning = true;
				break;
			}
			break;
			
		case 3:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 12);
				delay = 2500;
				break;
			
			case 2:
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 12);
				doneSpawning = true;
				break;
			}
			break;
			
		case 4:
			switch(wave) {
			case 1: 
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 4);

				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 2);	//fast
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 2);	//fast
				delay = 5000;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 4);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 4);
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
				spawnZombies(REGION.DOWN, ZOMBIE.NORMAL, 15);
				doneSpawning = true;
				break;
			}
			break;
		
		case 6:
			switch(wave) {
			case 1: 				
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 3); 	//fast
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 2);  	//fast
				delay = 2500;
				break;
			
			case 2:
				spawnZombies(REGION.RIGHT, ZOMBIE.NORMAL, 5);
				spawnZombies(REGION.LEFT, ZOMBIE.NORMAL, 5);
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
		Runnable zombieMethod = null;
		switch(region) {
		case UP: 	zombieMethod = Handler::addZombieUp;
			break;
		case DOWN: 	zombieMethod = Handler::addZombieDown;
			break;
		case LEFT: 	zombieMethod = Handler::addZombieLeft;
			break;
		case RIGHT: zombieMethod = Handler::addZombieRight;
			break;
		default:
			return;
		}
		
		for (int i = 0; i < num; i++) {
			zombieMethod.run();	
		}
	}
	
	public int getRemaining() {return zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int l) {level = l;}

}
