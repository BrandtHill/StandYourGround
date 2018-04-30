package game;

import game.Program.STATE;

public class SpawnSystem {

	private Handler handler;
	private PlayerObject player;
	private boolean doneCommencing;
	private int zombiesLeft;
	private int level;//, money;
	
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
			int numZomb = 0;
			for (int i = 2; i < handler.getObjList().size(); i++) {
				if (handler.getObjectAt(i).getType() == ObjectType.Zombie)
					numZomb++;
			}
			zombiesLeft = numZomb;
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
			for(int i = 0; i < 4; i++) {
				handler.addZombie(-(i+1)*50, i*200);
				zombiesLeft++;
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombie((i+1)*50+1500, i*200);
				zombiesLeft++;
			}
			break;
		
		case 2:
			for(int i = 0; i < 5; i++) {
				handler.addZombie(200+(i*50), -(50+(i*25)));
				zombiesLeft++;
			}
			for(int i = 0; i < 3; i++) {
				handler.addZombie(100+(i*100), (Program.HEIGHT+200+(i*150)));
				zombiesLeft++;
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombie(-500-(i*50), (300+(i*25)));
				zombiesLeft++;
			}
			for(int i = 0; i < 4; i++) {
				handler.addZombie(Program.WIDTH+700+(i*100), (150+(i*100)));
				zombiesLeft++;
			}
			break;
		
		case 3:
			for(int i = 0; i < 12; i++) {
				handler.addZombie(Program.WIDTH+(i*20), (50+(i*50)));
				zombiesLeft++;
			}
			for(int i = 0; i < 12; i++) {
				handler.addZombie(-(i*20), (550-(i*50)));
				zombiesLeft++;
			}	
			break;
		
		default:
			for(int i = 0; i<60; i++) {
				handler.addZombie();
				zombiesLeft++;
			}
			for(int i = 0; i < 20*(level-3); i++) {
				handler.addZombie(-100 + i*10, Program.HEIGHT+200+(30*i));
				zombiesLeft++;
			}
			break;
		}
		doneCommencing = true;
		
	}
	
	public int getRemaining() {return zombiesLeft;}
	public int getLevel() {return level;}
	public void setLevel(int l) {level = l;}

}
