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
	private Color[] colors;
	private Color sel, unsel;
	private Gun gun;
	
	public HUD() {
		player = Program.player;
		spawnSys = Program.spawnSys;
		colors = new Color[3];
		sel = new Color(255, 255, 255, 255);
		unsel = new Color(255, 255, 255, 65);
	}
	

	public void tick() {
		gun = player.getGunWielded();
		gunInfo = gun.getName() + "    " + gun.getAmmoLoaded() + "/" + gun.getMagSize() + "   " + gun.getAmmoExtra();
		levelInfo = "Level " + spawnSys.getLevel() + "    " + "Zombies Remaining: " + spawnSys.getRemaining() +	"     $"+ player.getMoney();
		for (int i = 0; i < colors.length; i++) {
			colors[i] = (i == player.getGunWeildedIndex()) ? sel : unsel;
		}		
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(100, 20, 20, 127));
		g.fillRect(20, 20, 350, 30);
		g.fillRect(390, 20, 370, 30);
		
		g.setColor(new Color(100, 20, 20));
		g.draw3DRect(20, 20, 350, 30, true);
		g.draw3DRect(390, 20, 370, 30, true);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(gunInfo, 40, 42);
		g.drawString(levelInfo, 410, 42);
		
		for (int i = 0; i < colors.length; i++) {
			g.setColor(colors[i]);
			g.drawString(String.valueOf(i + 1), 310 + i * 20, 42);
		}
		
		g.drawImage(gun.getSprite(), 240, 15, 80, 40, null);
	}
}
