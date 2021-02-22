package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import game.Main;
import game.Main.STATE;

public class Reticle extends GameObject {
	private static Player player;
	private boolean wantToLimitRange; //functionality exists
	private static Color COLOR_TRUE_COORDINATES = new Color(255,255,255,11);
	private static BufferedImage[] spriteSheet = new BufferedImage[8];
	private int ticks, spriteIndex;
	
	public Reticle(double x, double y) {
		super(x, y);
		player = Main.player;
	}

	public void tick() {
		x = Main.clamp(x, 0, Main.WIDTH * Main.getXScale());
		y = Main.clamp(y, 0, Main.HEIGHT * Main.getYScale());
		
		if (Main.gameState == STATE.InGame) {
			if (wantToLimitRange) limitRange(200);
		}
		if (ticks++ % 4 == 0) spriteIndex++;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (player.isReloading()) {
			g2d.drawImage(spriteSheet[spriteIndex%8], (int)x - 10, (int)y - 10, 21, 21, null);
		} else {
			g2d.drawImage(spriteSheet[0], (int)x - 10, (int)y - 10, 21, 21, null);
		}
		g2d.setColor(COLOR_TRUE_COORDINATES);
		g2d.drawOval((int)x-3, (int)y-3, 6, 6);
	}
	
	private void limitRange(double range) {
		double angle = Math.atan2(x - (player.getX() + 10), y - (player.getY() + 10));
		double xW = Math.abs(range * Math.sin(angle));
		double yW = Math.abs(range * Math.cos(angle));
		double xP = player.getX() + 10;
		double yP = player.getY() + 10;
		x = Main.clamp(x, xP - xW, xP + xW);
		y = Main.clamp(y, yP - yW, yP + yW);
	}
	
	public static void loadAssets() {
		try (FileInputStream fis = new FileInputStream("./res/Reticle.png")) {
			BufferedImage img = ImageIO.read(fis);
			for (int i = 0; i < 8; i++) spriteSheet[i] = img.getSubimage(0, i * 21, 21, 21);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
