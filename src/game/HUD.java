package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD {
	
	private Handler handler;
	private PlayerObject player;
	private SpawnSystem spawnSys;
	private String gunInfo, levelInfo;
	private Color[] colors;
	private Color sel, unSel;
	
	
	public HUD(Handler h, SpawnSystem s) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		} catch(Exception e) {
			e.getMessage();
		}
		spawnSys = s;
		gunInfo = levelInfo = new String();
		colors = new Color[3];
		sel = new Color(255, 255, 255, 255);
		unSel = new Color(255, 255, 255, 65);
	}
	

	public void tick() {
		Gun gun = player.getGunWielded();
		gunInfo = gun.getName() + "    " + gun.getAmmoLoaded() + 
				"/" + gun.getMagSize() + 
				"   " + gun.getAmmoExtra();
		levelInfo = "Level " + spawnSys.getLevel() + 
				"    " + "Zombies Remaining: " + spawnSys.getRemaining() + 
				"     $"+ player.getMoney();
		int gunIndex = player.getGunWeildedIndex();
		for(int i = 0; i < 3; i++) {
			if(i == gunIndex) 
				colors[i] = sel;
			else
				colors[i] = unSel;
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(100,20,20,127));
		g.fillRect(20, 20, 290, 30);
		g.fillRect(350, 20, 350, 30);
		
		g.setColor(new Color(100,20,20));
		g.draw3DRect(20, 20, 290, 30, true);
		g.draw3DRect(350, 20, 350, 30, true);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(gunInfo, 40, 42);
		g.drawString(levelInfo, 370, 42);
		
		g.setColor(colors[0]);
		g.drawString("1", 250, 42);
		g.setColor(colors[1]);
		g.drawString("2", 270, 42);
		g.setColor(colors[2]);
		g.drawString("3", 290, 42);
	}
}
