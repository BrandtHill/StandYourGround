package game.Pieces.Enemies;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import game.Handler;
import game.Main;
import game.Pieces.Blood;
import game.Pieces.DeadZed;
import game.Pieces.GameObject;
import game.Pieces.Player;
import game.SpawnSystem.SpawnSystem;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class Zombie extends GameObject {

	public final static int NUMZOMBIETYPES = SpawnSystem.ZOMBIE.values().length;
	protected double health, angle, speed;
	protected Random r;
	protected Player player;
	protected static BufferedImage[][] spriteSheets = new BufferedImage[NUMZOMBIETYPES][8];
	protected BufferedImage[] zombieSprites;
	protected int ticks;
	protected int spriteNum;
	protected int moneyValue;
	protected int maxAngleChangeDegrees;
	protected List<Rectangle> path = new LinkedList<>();
	
	public Zombie(double x, double y) {
		super(x, y);
		this.player = Main.player;
		this.zombieSprites = spriteSheets[0];
		this.r = new Random();
		
		this.health = 40 + 4 * Main.spawnSys.getLevel();
		this.speed = 1.3 + Main.spawnSys.getLevel() * 0.015;
		this.moneyValue = 19;
		this.maxAngleChangeDegrees = 5;
	}

	public void tick() {
		angle = determineAngle();
		velX = r.nextGaussian() + speed*sin(angle);
		velY = r.nextGaussian() + speed*cos(angle);
		
		move();
		detectCollision();
		
		if (ticks++ % 8 == 0) spriteNum++;
		if (health < 20) speed *= 1.0015;
		if (ticks % 60 == 0) findPathIfNeeded();
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
	
	protected double getAdjustedAngle(double toAngle, double maxChangeDeg) {
		double diff = getAngleDiff(getAngleToPlayer());
		double diffSign = diff >= 0 ? 1 : -1;
		double diffAbs = Math.abs(diff);
		double maxChange = maxChangeDeg * (Math.PI / 180);
		return (angle + diffSign * Math.min(diffAbs, maxChange)) % (2 * Math.PI);
	}
	
	protected double determineAngle() {
		if (!handler.hitsObstacle(getSightToPlayer()) || path.isEmpty()) return getAdjustedAngle(getAngleToPlayer(), maxAngleChangeDegrees);
		Rectangle r = path.get(0);
		if (r == getGridNode() && path.size() > 1) {
			path.remove(r);
			r = path.get(0);
		}
		return atan2(r.getCenterX() - (x + 10), r.getCenterY() - (y + 10));
	}
	
	//Path correcting function unrelated to A* path
	protected double correctForObstacles() {
		if (!handler.hitsObstacle(getSightToPlayer())) return 0;
		for (int i = 0; i < 180; i += 10) {
			double theta = i * Math.PI / 180;
			if (!handler.hitsObstacle(getSightBounds(theta))) return theta;
			if (!handler.hitsObstacle(getSightBounds(-theta))) return -theta;
		}
		return 0;
	}
	
	protected void findPathIfNeeded() {
		if (handler.hitsObstacle(getSightToPlayer()) && Handler.validGridIndex(Handler.nodeX((int)x + 10), Handler.nodeY((int)y + 10))) path = handler.aStar(new Point((int)x + 10, (int)y + 10), new Point((int)player.getX() + 10, (int)player.getY() + 10));
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
		 if (!handler.hitsObstacle((getBounds(velX, 0)))) x += velX;
		 if (!handler.hitsObstacle((getBounds(0, velY)))) y += velY;
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
	
	protected Line2D.Double getSightToPlayer() {
		return new Line2D.Double(x + 10, y + 10, player.getX() + 10, player.getY() + 10);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(-angle, x+10, y+10);
		g2d.drawImage(zombieSprites[spriteNum % 8], (int)x, (int)y, null);
		g2d.rotate(angle, x+10, y+10);
		//g2d.setColor(new Color(0, 255, 0, 63));
		//g2d.draw(getSightToPlayer());
		//g2d.draw(getSightBounds());
		//path.forEach(p -> g2d.draw(p));
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
			Main.spawnSys.decrementRemaining();
			handler.removeObjectAsync(this);
			handler.addObjectAsync(new DeadZed(this.x, this.y, this.angle, this.getClass()));
		}
	}
	
	public double getHealth() {return health;}
	
	//Related to path-correcting unrelated to A* path
	protected Polygon getSightBounds(double theta) {
		double phi = angle + theta;
		return new Polygon(
				new int[]{(int)(x + 10 + 10*cos(phi) - 10*sin(phi)), (int)(x + 10 - 10*cos(phi) - 10*sin(phi)), (int)(x + 10 + 60*sin(phi))},
				new int[]{(int)(y + 10 - 10*cos(phi) - 10*sin(phi)), (int)(y + 10 - 10*cos(phi) + 10*sin(phi)), (int)(y + 10 + 60*cos(phi))},
				3);
	}
	
	public Rectangle getGridNode() {
		return handler.getGridNode(new Point((int)x + 10, (int)y + 10));
	}

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
