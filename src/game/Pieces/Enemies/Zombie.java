package game.Pieces.Enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	protected double health, xPlayer, yPlayer, xBias, yBias, angle, speed;
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
		
		xPlayer = yPlayer = xBias = yBias = angle = 0;
		ticks = 0;
		moneyValue = 19;
		maxAngleChangeDegrees = 5;
	}

	public void tick() {
		angle = getAdjustedAngle(5);
		xBias = speed*sin(angle);
		yBias = speed*cos(angle);
		velX = r.nextInt(31)/10.0 - 1.5 + xBias;
		velY = r.nextInt(31)/10.0 - 1.5 + yBias;
		
		x += velX;
		y += velY;
		
		if (ticks % 8 == 0) {
			detectCollision();
			spriteNum++;
		}
		
		if (health < 20) speed *= 1.001;
		
		ticks++;
	}
	
	protected double getAngleToPlayer() {
		return atan2(player.getX() + 10 - x, player.getY() + 10 - y);
	}
	
	protected double getAngleToPlayerDiff() {
		double angleToPlayer = getAngleToPlayer();
		double diff = angleToPlayer - angle;
		if (diff < -Math.PI) diff += 2 * Math.PI;
		if (diff > Math.PI) diff -= 2 * Math.PI;
		return diff;
	}
	
	protected double getAdjustedAngle(double maxChangeDegrees) {
		double diff = getAngleToPlayerDiff();
		double diffSign = diff >= 0 ? 1 : -1;
		double diffAbs = Math.abs(diff);
		double maxChange = maxChangeDegrees * (Math.PI / 180);
		return (angle + diffSign * Math.min(diffAbs, maxChange)) % (2 * Math.PI);
	}
	
	public void detectCollision() {
		handler.getObjectStream()
				.filter(o -> o instanceof Zombie)
				.map(o -> (Zombie)o)
				.filter(z -> z.getBounds().intersects(this.getBounds()))
				.forEach(z -> {
					double theta = atan2(x - z.getX(), y - z.getY());
					x += 3*sin(theta);
					y += 3*cos(theta);
				});
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 20, 20);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.RED);
		g2d.rotate(-angle, x+10, y+10);
		g2d.drawImage(zombieSprites[spriteNum % 8], (int)x, (int)y, null);
		g2d.rotate(angle, x+10, y+10);
	}
	
	public void damageMe(double damage, double angle, double knock) {
		health -= damage;
		handler.addObjectAsync(new Blood(x+10, y+10));
		x += sin(angle)*knock;
		y += cos(angle)*knock;
		handler.bloodSplat(x+10, y+10, knock, angle, 2 + (int)(damage / 30));
		if (health <= 0) {
			player.setMoney(moneyValue + player.getMoney());
			player.zombiesLeft--;
			handler.removeObjectAsync(this);
		}
	}
	
	public double getHealth() {return health;}
	
	static { 
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
