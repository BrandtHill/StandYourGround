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

	private double health, xPlayer, yPlayer, xBias, yBias, angle;
	private PlayerObject player;
	private Random r;
	
	public ZombieObject(double xPos, double yPos, Handler h) {
		super(xPos, yPos, ObjectType.Zombie, h);
		
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		r = new Random();
		health = r.nextInt(51) + 50;
		
		xPlayer = yPlayer = xBias = yBias = angle = 0;
	}

	public void tick() {
		xPlayer = handler.getObjectAt(0).getX()+10;
		yPlayer = handler.getObjectAt(0).getY()+10;
		angle = atan2(xPlayer-x, yPlayer-y);
		xBias = sin(angle);
		yBias = cos(angle);
		r = new Random();
		velX = r.nextInt(61)/10.0 - 3 + xBias;
		velY = r.nextInt(61)/10.0 - 3 + yBias;
		
		x += velX;
		y += velY;
		
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-48);
		
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
			handler.removeObject(this);
		}
	}

}
