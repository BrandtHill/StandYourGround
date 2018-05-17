package game;

import game.Program.STATE;

public class SpawnSystem {

	private Handler handler;
	private PlayerObject player;
	private boolean doneCommencing, doneSpawning;
	private int zombiesLeft;
	private int level, wave;
	private long timer, delay;
	
	public SpawnSystem(Handler h) {
		handler = h;
		level = 1;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		doneCommencing = doneSpawning = false;
		zombiesLeft = 0;
		timer = System.currentTimeMillis();
		delay = 0;
		wave = 1;
	}
	
	public void tick() {
		if (doneCommencing) {
			
			if(!doneSpawning) {
				if(System.currentTimeMillis() - timer > delay)
					spawn();
			}
			
			zombiesLeft = player.zombiesLeft;
			if (zombiesLeft <= 0) {
				
				doneCommencing = false;
				doneSpawning = false;
				Program.gameState = STATE.StoreMenu;
				level++;
				
				player.resetAllAmmo();
				player.setLevel(level);
				player.setMoneyAtRoundStart(player.getMoney());
				
			}
		}
	}
	
	public void commenceLevel() {
		
		handler.removeProjectiles();
		handler.removeZombies();
		player.setX(Program.WIDTH/2-10);
		player.setY(Program.HEIGHT/2-30);
		player.resetAllAmmo();
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
			player.zombiesLeft = 20*level;
			for(int i = 0; i<60; i++) {
				handler.addZombie();
			}
			for(int i = 0; i < 20*(level-3); i++) {
				handler.addZombie(-100 + i*10, Program.HEIGHT+200+(30*i), level);
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
				for(int i = 0; i < 4; i++) {
					handler.addZombieLeft(1.2, level);
				}
				delay = 5000;
				break;
				
			case 2:
				for(int i = 0; i < 4; i++) {
					handler.addZombieRight(1.2, level);
				}
				break;
				
			default: 
				doneSpawning = true;	
				break;
				
			}
			break;
		case 2:
			switch(wave) {
			case 1: 
				for(int i = 0; i < 5; i++) {
					handler.addZombieUp(1.2, level);
				}
				delay = 1000;
				break;
			
			case 2:
				for(int i = 0; i < 3; i++) {
					handler.addZombieDown(1.0, level);
				}
				delay = 2000;
				break;
			
			case 3:
				for(int i = 0; i < 4; i++) {
					handler.addZombieLeft(1.3, level);
				}
				delay = 3000;
				break;
				
			case 4:
				for(int i = 0; i < 4; i++) {
					handler.addZombieRight(1.2, level);
				}
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}
			
			break;
			
		case 3:
			switch(wave) {
			case 1: 
				for(int i = 0; i < 12; i++) {
					handler.addZombieRight(1.1, level);
				}
				delay = 2500;
				break;
			
			case 2:
				for(int i = 0; i < 12; i++) {
					handler.addZombieLeft(1.25, level);
				}				
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}
			
			break;
			
		case 4:
			switch(wave) {
			case 1: 
				for(int i = 0; i < 4; i++) {
					handler.addZombieRight(1.3, level);
				}
				for(int i = 0; i < 4; i++) {
					handler.addZombieLeft(1.3, level);
				}				
				handler.addZombieRight(2.5, level);
				handler.addZombieRight(2.5, level);
				handler.addZombieLeft(2.5, level);
				handler.addZombieLeft(2.5, level);
				delay = 5000;
				break;
			
			case 2:
				for(int i = 0; i < 4; i++) {
					handler.addZombieRight(1.3, level);
				}
				for(int i = 0; i < 4; i++) {
					handler.addZombieLeft(1.3, level);
				}	
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}
			
			break;			
		
		case 5:	
			switch(wave) {
			case 1: 
				for(int i = 0; i < 15; i++) {
					handler.addZombieUp(1.2, level);
				}
				delay = 6000;
				break;
			
			case 2:
				for(int i = 0; i < 15; i++) {
					handler.addZombieDown(1.2, level);
				}
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}
			
			break;
		
		case 6:
			switch(wave) {
			case 1: 				
				handler.addZombieRight(2, level);
				handler.addZombieRight(2.3, level);
				handler.addZombieLeft(2, level);
				handler.addZombieLeft(2.3, level);
				handler.addZombieRight(3, level);
				delay = 2500;
				break;
			
			case 2:
				for(int i = 0; i < 5; i++) {
					handler.addZombieRight(1.5, level);
				}
				for(int i = 0; i < 5; i++) {
					handler.addZombieLeft(1.5, level);
				}
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}
			
			break;			
			
		default:
			/*switch(wave) {
			case 1: 
				
				break;
			
			case 2:
				
				break;
				
			default:
				doneSpawning = true;
				break;
			
			}*/
			doneSpawning = true;
			break;			
			
		}
		
		wave++;
	}
	
	public int getRemaining() {return zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int l) {level = l;}

}
