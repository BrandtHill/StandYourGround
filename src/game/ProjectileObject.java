package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class ProjectileObject extends GameObject{

	private double velMag, velAng, xScale, yScale, xPrev, yPrev, damage, knockBack;
	private boolean old;
	
	public ProjectileObject(double x, double y, double magnitude, double angle, double damage, double knock, Handler handler) {
		super(x, y, handler);
		velMag = magnitude;
		velAng = angle;
		xScale = sin(velAng);
		yScale = cos(velAng);
		xPrev = x;
		yPrev = y;
		this.damage = damage;
		knockBack = knock;
	}
	
	public Line2D.Double getBounds() {
		return new Line2D.Double(x, y, xPrev, yPrev);
	}

	public void tick() {
		velX = xScale * velMag;
		velY = yScale * velMag;
		xPrev = x;
		yPrev = y;
		x += velX;
		y += velY;
		if (x < 0 || x > Program.WIDTH || y < 0 || y > Program.HEIGHT)
			old = true;
		if (old)
			handler.removeObject(this);
		detectCollision();
	}
	
	public void detectCollision() {
		if (!old) {
			for (int i = 1; i < handler.getObjList().size(); i++) {
				GameObject obj = handler.getObjectAt(i);
				if (obj instanceof ZombieObject) {
					ZombieObject zomb = (ZombieObject) obj;
					if (zomb.getBounds().intersectsLine(this.getBounds())) {
						zomb.damageMe(damage, velAng, knockBack);
						old = true;
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.YELLOW);
		g2d.draw(getBounds());
	}
	
	public void setOld() {old = true;}
	public boolean getOld() {return old;}
	
}
