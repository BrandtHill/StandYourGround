package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.round;

public class ProjectileObject extends GameObject {

	private int velMag;
	private double velAng, xScale, yScale;
	public boolean old;
	
	public ProjectileObject(int xPos, int yPos, int magnitude, double angle, Handler h) {
		super(xPos, yPos, ObjectType.Projectile, h);
		velMag = magnitude;
		velAng = angle;
		xScale = sin(velAng);
		yScale = cos(velAng);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, 4, 4);
	}

	public void tick() {
		velX = (int)round(xScale*velMag);
		velY = (int)round(yScale*velMag);
		
		x += velX;
		y += velY;
		
		if(x<0 || x>Program.WIDTH || y<0 || y>Program.HEIGHT)
			old = true;
		detectCollision();
	}
	
	public void detectCollision()
	{
		for(int i = 1; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj.getType() == ObjectType.Zombie) {
				ZombieObject zomb = (ZombieObject)obj;
				if(zomb.getBounds().intersects(this.getBounds())) {
					handler.removeObject(zomb);
					old = true;
				}
			}
		}
	}

	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.GREEN);
		//g2d.draw(getBounds());
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, 4, 4);
	}

	
}
