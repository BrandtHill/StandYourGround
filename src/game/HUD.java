package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD {
	
	private static Handler handler;
	private static PlayerObject player;
	private SpawnSystem spawnSys;
	private String gunInfo, levelInfo;
	
	
	public HUD(Handler h, SpawnSystem s) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			e.getMessage();
		}
		spawnSys = s;
		gunInfo = levelInfo = new String();
	}
	

	public void tick() {
		Gun gun = player.getGun();
		gunInfo = gun.getName() + "    " + gun.getAmmoLoaded() + 
				"/" + gun.getMagSize() + 
				"   " + gun.getAmmoExtra();
		levelInfo = "Level " + spawnSys.getLevel() + 
				"    " + "Zombies Remaining: " + spawnSys.getRemaining() + 
				"     $"+ player.getMoney();
	}
	
	public void render(Graphics g) {
		//Graphics2D g2d = (Graphics2D)g;
		g.setColor(new Color(100,20,20,127));
		g.fillRect(20, 20, 250, 30);
		g.fillRect(350, 20, 350, 30);
		
		g.setColor(new Color(100,20,20));
		g.draw3DRect(20, 20, 250, 30, true);
		g.draw3DRect(350, 20, 350, 30, true);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(gunInfo, 40, 42);
		g.drawString(levelInfo, 370, 42);
	}
}
