package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class ProjectileObject extends GameObject {

	private double velMag, velAng, xScale, yScale, xPrev, yPrev;
	private boolean old;
	
	public ProjectileObject(double xPos, double yPos, double magnitude, double angle, Handler h) {
		super(xPos, yPos, ObjectType.Projectile, h);
		velMag = magnitude;
		velAng = angle;
		xScale = sin(velAng);
		yScale = cos(velAng);
		xPrev = x;
		yPrev = y;
	}
	
	public Line2D.Double getBounds() {
		//return new Rectangle((int)x-2, (int)y-2, 4, 4);
		return new Line2D.Double(x, y, xPrev, yPrev);
	}

	public void tick() {
		velX = xScale*velMag;
		velY = yScale*velMag;
		
		xPrev = x;
		yPrev = y;
		
		x += velX;
		y += velY;
		
		if(x<0 || x>Program.WIDTH || y<0 || y>Program.HEIGHT)
			old = true;
		if(old)
			handler.removeObject(this);
		detectCollision();
	}
	
	public void detectCollision()
	{
		for(int i = 1; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj.getType() == ObjectType.Zombie) {
				ZombieObject zomb = (ZombieObject)obj;
				if(zomb.getBounds().intersectsLine(this.getBounds())) {
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
		g.fillOval((int)x-1, (int)y-1, 2, 2);
		g.drawLine((int)x, (int)y, (int)xPrev, (int)yPrev);
	}

	
}
