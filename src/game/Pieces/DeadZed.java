package game.Pieces;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import game.Pieces.Enemies.ChargingZombie;
import game.Pieces.Enemies.DodgingZombie;
import game.Pieces.Enemies.FastZombie;
import game.Pieces.Enemies.ThiccZombie;
import game.Pieces.Enemies.Zombie;

public class DeadZed extends GameObject {

	private static BufferedImage[] spriteSheet = new BufferedImage[Zombie.NUMZOMBIETYPES];
	private BufferedImage sprite;
	private int ticks;
	private double angle;
	
	
	public DeadZed(double x, double y, double angle, Class<?> c) {
		super(x, y);
		this.ticks = 0;
		this.angle = angle;
		this.velX = 3*Math.sin(angle - Math.PI);
		this.velY = 3*Math.cos(angle - Math.PI);
		this.sprite = getImageFromClass(c);
	}
	
	@Override
	public void tick() {
		x += velX;
		y += velY;
		velX *= 0.80;
		velY *= 0.80;
		if (ticks >= 300) handler.removeObjectAsync(this);
		ticks++;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(-angle, x+10, y+20);
		g2d.drawImage(sprite, (int)x, (int)y, null);
		g2d.rotate(angle, x+10, y+20);
	}

	private static BufferedImage getImageFromClass(Class<?> c) {
		int i = 0;
		if (c == DodgingZombie.class) i = 1;
		else if (c == FastZombie.class) i = 2;
		else if (c == ThiccZombie.class) i = 3;
		else if (c == ChargingZombie.class) i = 4;
		return spriteSheet[i];
	}
	
	public static void loadAssets() {
		try (FileInputStream fis = new FileInputStream("./res/DeadZeds.png")) {
			BufferedImage img = ImageIO.read(fis);
			for (int i = 0; i < Zombie.NUMZOMBIETYPES; i++) spriteSheet[i] = img.getSubimage(20 * i, 0, 20, 46);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
