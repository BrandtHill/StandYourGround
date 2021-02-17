package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import game.Program;
import game.Program.STATE;

public class Reticle extends GameObject {
	private static Player player;
	private double xPlayer, yPlayer;
	private boolean wantToLimitRange; //functionality exists
	private static Color COLOR_TRUE_COORDINATES = new Color(255,255,255,11);
	private static BufferedImage[] spriteSheet = new BufferedImage[8];
	private int ticks, spriteIndex;
	
	public Reticle(double x, double y) {
		super(x, y);
		player = Program.player;
		xPlayer = player.getX();
		yPlayer = player.getY();
	}

	public void tick() {
		x = Program.clamp(x, 0, Program.WIDTH);
		y = Program.clamp(y, 0, Program.HEIGHT);
		
		if (Program.gameState == STATE.InGame) {
			if (!Program.isOnEdgeX()) xPlayer = player.getX();
			if (!Program.isOnEdgeY()) yPlayer = player.getY();
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
		x = Program.clamp(x, xP - xW, xP + xW);
		y = Program.clamp(y, yP - yW, yP + yW);
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
