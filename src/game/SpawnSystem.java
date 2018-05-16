package game;

import game.Program.STATE;

public class SpawnSystem {

	private Handler handler;
	private PlayerObject player;
	private boolean doneCommencing;
	private int zombiesLeft;
	private int level;
	
	public SpawnSystem(Handler h) {
		handler = h;
		level = 1;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		doneCommencing = false;
		zombiesLeft = 0;
	}
	
	public void tick() {
		if (doneCommencing) {

			zombiesLeft = player.zombiesLeft;
			if (zombiesLeft == 0) {
				
				doneCommencing = false;
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
		
		switch(level) {
		
		case 1: 
			player.zombiesLeft = 8;
			for(int i = 0; i < 4; i++) {
				handler.addZombieLeft(1.2, level);
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombieRight(1.2, level);
			}
			break;
		
		case 2:
			player.zombiesLeft = 16;
			for(int i = 0; i < 5; i++) {
				handler.addZombieUp(1.2, level);
			}
			for(int i = 0; i < 3; i++) {
				handler.addZombieDown(1.0, level);
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombieLeft(1.3, level);
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombieRight(1.2, level);
			}
			break;
		
		case 3:
			player.zombiesLeft = 24;
			for(int i = 0; i < 12; i++) {
				handler.addZombieRight(1.1, level);
			}
			for(int i = 0; i < 12; i++) {
				handler.addZombieLeft(1.25, level);
			}	
			break;
		
		case 4:
			player.zombiesLeft = 20;
			for(int i = 0; i < 4; i++) {
				handler.addZombieRight(1.3, level);
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombieLeft(1.3, level);
			}
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
			
			break;
		
		case 5:
			player.zombiesLeft = 30;
			for(int i = 0; i < 15; i++) {
				handler.addZombieUp(1.2, level);
			}
			for(int i = 0; i < 15; i++) {
				handler.addZombieDown(1.2, level);
			}	
			break;
		
		case 6:
			player.zombiesLeft = 15;
			for(int i = 0; i < 5; i++) {
				handler.addZombieRight(1.5, level);
			}
			for(int i = 0; i < 5; i++) {
				handler.addZombieLeft(1.5, level);
			}
			
			handler.addZombieRight(2, level);
			handler.addZombieRight(2.3, level);
			handler.addZombieLeft(2, level);
			handler.addZombieLeft(2.3, level);
			handler.addZombieRight(3, level);
			
			break;	
		
		default:
			player.zombiesLeft = 4*level;
			for(int i = 0; i<60; i++) {
				handler.addZombie();
			}
			for(int i = 0; i < 20*(level-3); i++) {
				handler.addZombie(-100 + i*10, Program.HEIGHT+200+(30*i), level);
			}
			break;
		}
		doneCommencing = true;
		
	}
	
	public int getRemaining() {return zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int l) {level = l;}

}
