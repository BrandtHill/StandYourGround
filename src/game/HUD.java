package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import game.Weapons.Gun;

public class HUD {
	private static Player player;
	private static SpawnSystem spawnSys;
	private String gunInfo, levelInfo;
	private Color[] colors;
	private Color sel, unsel;
	
	public HUD() {
		player = Program.player;
		spawnSys = Program.spawnSys;
		colors = new Color[3];
		sel = new Color(255, 255, 255, 255);
		unsel = new Color(255, 255, 255, 65);
	}
	

	public void tick() {
		Gun gun = player.getGunWielded();
		gunInfo = gun.getName() + "    " + gun.getAmmoLoaded() + "/" + gun.getMagSize() + "   " + gun.getAmmoExtra();
		levelInfo = "Level " + spawnSys.getLevel() + "    " + "Zombies Remaining: " + spawnSys.getRemaining() +	"     $"+ player.getMoney();
		for (int i = 0; i < 3; i++) {
			colors[i] = (i == player.getGunWeildedIndex()) ? sel : unsel;
		}		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(100,20,20,127));
		g.fillRect(40, 20, 290, 30);
		g.fillRect(370, 20, 370, 30);
		
		g.setColor(new Color(100,20,20));
		g.draw3DRect(40, 20, 290, 30, true);
		g.draw3DRect(370, 20, 370, 30, true);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(gunInfo, 60, 42);
		g.drawString(levelInfo, 390, 42);
		
		g.setColor(colors[0]);
		g.drawString("1", 270, 42);
		g.setColor(colors[1]);
		g.drawString("2", 290, 42);
		g.setColor(colors[2]);
		g.drawString("3", 310, 42);
	}
}
