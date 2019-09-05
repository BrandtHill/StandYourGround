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
	private double xDisplay, yDisplay;
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
		xDisplay = x = Program.clamp(x, 0, Program.WIDTH);
		yDisplay = y = Program.clamp(y, 0, Program.HEIGHT);
		
		if (Program.gameState == STATE.InGame) {
			if (!Program.isOnEdgeX()) xPlayer = player.getX();
			if (!Program.isOnEdgeY()) yPlayer = player.getY();
			xDisplay = Program.clamp(x + xPlayer + 10 - Program.WIDTH / 2, 0, Program.WIDTH);
			yDisplay = Program.clamp(y + yPlayer + 30 - Program.HEIGHT / 2, 0, Program.HEIGHT);
			if (wantToLimitRange) limitRange(200);
		}
		if (ticks++ % 4 == 0) spriteIndex++;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (player.isReloading()) {
			g2d.drawImage(spriteSheet[spriteIndex%8], (int)xDisplay - 10, (int)yDisplay - 10, 21, 21, null);
		} else {
			g2d.drawImage(spriteSheet[0], (int)xDisplay - 10, (int)yDisplay - 10, 21, 21, null);
		}
		g2d.setColor(COLOR_TRUE_COORDINATES);
		g2d.drawOval((int)x-3, (int)y-3, 6, 6);
	}
	
	private void limitRange(double range) {
		double angle = Math.atan2(xDisplay - (player.getX() + 10), yDisplay - (player.getY() + 10));
		double xW = Math.abs(range * Math.sin(angle));
		double yW = Math.abs(range * Math.cos(angle));
		double xP = player.getX() + 10;
		double yP = player.getY() + 10;
		xDisplay = Program.clamp(xDisplay, xP - xW, xP + xW);
		yDisplay = Program.clamp(yDisplay, yP - yW, yP + yW);
	}
	
	public double getXDisplay() {return xDisplay;}
	public double getYDisplay() {return yDisplay;}
	
	static {
		try (FileInputStream fis = new FileInputStream("./res/Reticle.png")) {
			BufferedImage img = ImageIO.read(fis);
			for (int i = 0; i < 8; i++) spriteSheet[i] = img.getSubimage(0, i * 21, 21, 21);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
