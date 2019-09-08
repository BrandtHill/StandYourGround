package game.Pieces.Enemies;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import game.Program;
import game.Pieces.Blood;
import game.Pieces.GameObject;
import game.Pieces.Player;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class Zombie extends GameObject {

	public final static int NUMZOMBIETYPES = 4; 
	protected double health, angle, speed;
	protected Random r;
	protected Player player;
	protected static BufferedImage[][] spriteSheets = new BufferedImage[NUMZOMBIETYPES][8];
	protected BufferedImage[] zombieSprites;
	protected int ticks;
	protected int spriteNum;
	protected int moneyValue;
	protected int maxAngleChangeDegrees;
	
	public Zombie(double x, double y, double speed, double health) {
		super(x, y);
		this.player = Program.player;
		this.health = health;	
		this.speed = speed;
		this.zombieSprites = spriteSheets[0];
		this.r = new Random();
		
		moneyValue = 19;
		maxAngleChangeDegrees = 5;
	}

	public void tick() {
		angle = getAdjustedAngleToPlayer(5);
		angle += correctForObstacles();
		velX = r.nextGaussian() + speed*sin(angle);
		velY = r.nextGaussian() + speed*cos(angle);
		
		move();
		detectCollision();
		
		if (ticks++ % 8 == 0) spriteNum++;
		if (health < 20) speed *= 1.0015;
	}
	
	protected double getAngleToPlayer() {
		return atan2(player.getX() - x, player.getY() - y);
	}
	
	protected double getAngleDiff(double toAngle) {
		double diff = toAngle - this.angle;
		if (diff < -Math.PI) diff += 2 * Math.PI;
		if (diff > Math.PI) diff -= 2 * Math.PI;
		return diff;
	}
	
	protected double getAdjustedAngleToPlayer(double maxChangeDegrees) {
		double diff = getAngleDiff(getAngleToPlayer());
		double diffSign = diff >= 0 ? 1 : -1;
		double diffAbs = Math.abs(diff);
		double maxChange = maxChangeDegrees * (Math.PI / 180);
		return (angle + diffSign * Math.min(diffAbs, maxChange)) % (2 * Math.PI);
	}
	
	protected double correctForObstacles() {
		for (int i = 0; i < 180; i += 10) {
			double theta = i * Math.PI / 180;
			if (!handler.getObstacles().anyMatch(o -> getSightBounds(theta).intersects(o.getBounds()))) return theta;
			if (!handler.getObstacles().anyMatch(o -> getSightBounds(-theta).intersects(o.getBounds()))) return -theta;
		}
		return 0;
	}
	
	public void detectCollision() {
		handler.getZombies()
		.filter(z -> z != this)
		.filter(z -> z.getBounds().intersects(this.getBounds()))
		.forEach(z -> {
			double theta = atan2(x - z.getX(), y - z.getY());
			velX = 2.0 * cos(theta);
			velY = 2.0 * sin(theta);
			move();
		});
	}
	
	protected void move() {
		 if (!handler.getObstacles().anyMatch(o -> o.getBounds().intersects(getBounds(velX, 0)))) x += velX;
		 if (!handler.getObstacles().anyMatch(o -> o.getBounds().intersects(getBounds(0, velY)))) y += velY;
	}
	
	public Rectangle getBounds() {
		return getBounds(0, 0);
	}
	
	protected Rectangle getBounds(double xDiff, double yDiff) {
		return new Rectangle((int)(x + xDiff), (int)(y + yDiff), 20, 20);
	}
	
	protected Polygon getSightBounds() {
		return getSightBounds(0);
	}
	
	protected Polygon getSightBounds(double theta) {
		double phi = angle + theta;
		return new Polygon(
				new int[]{(int)(x + 10 + 10*cos(phi) - 10*sin(phi)), (int)(x + 10 - 10*cos(phi) - 10*sin(phi)), (int)(x + 10 + 60 * Math.sin(phi))},
				new int[]{(int)(y + 10 - 10*cos(phi) - 10*sin(phi)), (int)(y + 10 - 10*cos(phi) + 10*sin(phi)), (int)(y + 10 + 60 * Math.cos(phi))},
				3);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(-angle, x+10, y+10);
		g2d.drawImage(zombieSprites[spriteNum % 8], (int)x, (int)y, null);
		g2d.rotate(angle, x+10, y+10);
		//g2d.draw(getSightBounds());
	}
	
	public void damageMe(double damage, double angle, double knock) {
		health -= damage;
		handler.addObjectAsync(new Blood(x+10, y+10));
		velX = sin(angle) * knock;
		velY = cos(angle) * knock;
		move();
		handler.bloodSplat(x+10, y+10, knock, angle, 2 + (int)(damage / 30));
		if (health <= 0) {
			player.setMoney(moneyValue + player.getMoney());
			player.zombiesLeft--;
			handler.removeObjectAsync(this);
		}
	}
	
	public double getHealth() {return health;}
	
	public static void loadAssets() {
		for (int i = 1; i <= NUMZOMBIETYPES; i++) {
			try (FileInputStream fis = new FileInputStream("./res/ZombieSprite_" + i + ".png")) {
				BufferedImage img = ImageIO.read(fis);
				for (int j = 0; j < 8; j++) spriteSheets[i-1][j] = img.getSubimage(20 * j, 0, 20, 24);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
