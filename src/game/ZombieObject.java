package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class ZombieObject extends GameObject{

	private double health, xPlayer, yPlayer, xBias, yBias, angle, speed;
	private PlayerObject player;
	private Random r;
	private byte tickDivider;
	
	public ZombieObject(double x, double y, Handler handler, double speed, double health) {
		super(x, y, handler);
		
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		this.health = health;	
		this.speed = speed;
		
		xPlayer = yPlayer = xBias = yBias = angle = 0;
		tickDivider = 0;
	}

	public void tick() {
		xPlayer = player.getX()+10;
		yPlayer = player.getY()+10;
		angle = atan2(xPlayer-x, yPlayer-y);
		xBias = speed*sin(angle);
		yBias = speed*cos(angle);
		r = new Random();
		velX = r.nextInt(61)/10.0 - 3 + xBias;
		velY = r.nextInt(61)/10.0 - 3 + yBias;
		
		x += velX;
		y += velY;
		
		if(tickDivider%8 == 0) detectCollision();
		
		if(health<20) speed *= 1.001;
		
		tickDivider++;
	}
	
	public void detectCollision()
	{
		for(int i = 2; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj instanceof ZombieObject) {
				ZombieObject zomb = (ZombieObject)obj;
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
		g2d.fillRect((int)x, (int)y, 20, 20);
		g2d.rotate(angle, x+10, y+10);
	}
	
	public void damageMe(double damage, double angle, double knock) {
		health -= damage;
		x += sin(angle)*knock;
		y += cos(angle)*knock;
		if(health<= 0) {
			int money = player.getMoney();
			player.setMoney(20+r.nextInt(11) + money);
			player.zombiesLeft--;
			handler.removeObject(this);
		}
	}

}
