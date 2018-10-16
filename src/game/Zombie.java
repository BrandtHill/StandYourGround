package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class Zombie extends GameObject{

	protected double health, xPlayer, yPlayer, xBias, yBias, angle, speed;
	protected Random r;
	protected Player player;
	private static BufferedImage spriteSheet1;
	private static BufferedImage spriteSheet2;
	protected BufferedImage[] zombieSprites;
	protected static BufferedImage[] zombieSprites1 = new BufferedImage[8];
	protected static BufferedImage[] zombieSprites2 = new BufferedImage[8];
	protected int tickDivider;
	protected int spriteNum;
	
	public Zombie(double x, double y, double speed, double health) {
		super(x, y);
		this.player = Program.player;
		this.health = health;	
		this.speed = speed;
		this.zombieSprites = zombieSprites1;

		this.r = new Random();
		
		xPlayer = yPlayer = xBias = yBias = angle = 0;
		tickDivider = 0;
	}

	public void tick() {
		angle = getAngleToPlayer();
		xBias = speed*sin(angle);
		yBias = speed*cos(angle);
		velX = r.nextInt(31)/10.0 - 1.5 + xBias;
		velY = r.nextInt(31)/10.0 - 1.5 + yBias;
		
		x += velX;
		y += velY;
		
		if (tickDivider % 8 == 0) {
			detectCollision();
			spriteNum++;
		}
		
		if (health<20) speed *= 1.001;
		
		tickDivider++;
	}
	
	protected double getAngleToPlayer() {
		xPlayer = player.getX()+10;
		yPlayer = player.getY()+10;
		return atan2(xPlayer-x, yPlayer-y);
	}
	
	public void detectCollision() {
		for(int i = 2; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj instanceof Zombie) {
				Zombie zomb = (Zombie)obj;
				if(zomb.getBounds().intersects(this.getBounds())) {
					double theta = atan2(x-zomb.getX(), y-zomb.getY());
					x += 3*sin(theta);
					y += 3*cos(theta);
				}
			}
		}
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
		handler.addBlood(new Blood(x+10, y+10));
		x += sin(angle)*knock;
		y += cos(angle)*knock;
		handler.bloodSplat(x+10, y+10, knock, angle, 2 + (int)(damage / 30));
		if(health<= 0) {
			int money = player.getMoney();
			player.setMoney(25 + money);
			player.zombiesLeft--;
			handler.removeObject(this);
		}
	}
	
	public static void loadSprites() {
		try {
			FileInputStream file = new FileInputStream("res/ZombieSprite_1.png");
			spriteSheet1 = ImageIO.read(file);
			file.close();
			file = new FileInputStream("res/ZombieSprite_2.png");
			spriteSheet2 = ImageIO.read(file);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < 8; i++) {
			zombieSprites1[i] = spriteSheet1.getSubimage(20 * i, 0, 20, 24);
		}
		for(int i = 0; i < 8; i++) {
			zombieSprites2[i] = spriteSheet2.getSubimage(20 * i, 0, 20, 24);
		}
	}
}
