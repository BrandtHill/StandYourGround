package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import game.Pieces.Player;
import game.Weapons.Gun;

public class HUD {
	private static Player player;
	private static SpawnSystem spawnSys;
	private String gunInfo, levelInfo;
	private int selectedIndex;
	private double alpha, endAlpha;
	private Gun gun;
	
	public HUD() {
		player = Program.player;
		spawnSys = Program.spawnSys;
	}
	

	public void tick() {
		gun = player.getGunWielded();
		gunInfo = gun.getName() + "    " + gun.getAmmoLoaded() + "/" + gun.getMagSize() + "   " + gun.getAmmoExtra();
		levelInfo = "Level " + spawnSys.getLevel() + "    " + "Zombies Remaining: " + spawnSys.getRemaining() +	"     $"+ player.getMoney();
		selectedIndex = player.getGunWeildedIndex();
		alpha = calcAlpha();
		endAlpha = spawnSys.zedsDead() ? endAlpha + 1 : 0;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(100, 20, 20, (int) (127 * alpha)));
		g.fillRect(20, 20, 350, 30);
		g.fillRect(390, 20, 370, 30);
		
		g.setColor(new Color(100, 20, 20, (int) (255 * alpha)));
		g.draw3DRect(20, 20, 350, 30, true);
		g.draw3DRect(390, 20, 370, 30, true);
		
		g.setColor(new Color(255,255,255,(int) (255 * alpha)));
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(gunInfo, 40, 42);
		g.drawString(levelInfo, 410, 42);
		
		for (int i = 0; i < 3; i++) {
			g.setColor(new Color(255,255,255, (int) ((i == selectedIndex ? 255 : 63) * alpha)));
			g.drawString(String.valueOf(i + 1), 310 + i * 20, 42);
		}
		
		g.drawImage(gun.getSprite(), 240, 20, 60, 30, null);
		
		if (spawnSys.zedsDead()) {
			g.setColor(new Color(0, 0, 0, (int)endAlpha));
			g.fillRect(0, 0, Program.WIDTH, Program.HEIGHT);
		}
	}
	
	private double calcAlpha() {
		double delta = player.getY() < 50 ? 0.95 : 1.05;
		return Program.clamp(delta * alpha, 0.25, 1);
	}
}
