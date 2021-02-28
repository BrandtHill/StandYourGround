package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import game.Main;
import game.Audio.AudioPlayer;

public class Bomb extends GameObject {

	private int ticks;
	private double velocity, damage, knock, angle;
	private static BufferedImage sprite;
	
	public Bomb(double x, double y, double angle, double speed) {
		super(x, y);
		this.angle = angle;
		this.velX = speed * Math.sin(angle);
		this.velY = speed * Math.cos(angle);
		this.knock = 15;
		this.damage = 45;
		this.velocity = 22;
	}
	
	@Override
	public void tick() {
		x += velX;
		y += velY;
		if (handler.hitsObstacle(getBounds())) {
			velX *= -1;
			velY *= -1;
		}
		velX *= 0.9;
		velY *= 0.9;
		if (ticks == 90) detonate();
		if (ticks > 100) Main.handler.removeObjectAsync(this);
		ticks++;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (ticks < 90) {
			g2d.rotate(-angle, x, y);
			g2d.drawImage(sprite, (int)x, (int)y, null);
			g2d.rotate(angle, x, y);
		} else {
			g2d.setColor(new Color(200, 150, 30, 255 - (ticks - 85) * 15));
			int diameter = ticks;
			int radius = diameter / 2;
			g2d.fillOval((int)x - radius, (int)y - radius, diameter, diameter);
		}
	}
	
	private void detonate() {
		AudioPlayer.playSound("Pistol2", 0.5f, 1.2f);
		float num = 50;
		for (int i = 0; i < num; i++) {
			Main.handler.addObjectAsync(new Projectile(this, 2 * Math.PI * (i / num)));
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 10, 10);
	}

	public double getVelocity() {return velocity;}
	public double getDamage() {return damage;}
	public double getKnock() {return knock;}
	public static BufferedImage getSprite() {return sprite;}
	
	public static void loadAssets() {
		try (FileInputStream fis = new FileInputStream("./res/Bomb.png")) {
			sprite = ImageIO.read(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
