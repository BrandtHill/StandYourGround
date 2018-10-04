package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class Projectile extends GameObject{

	private double magnitude, angle, xScale, yScale, xPrev, yPrev, damage, knockBack;
	private boolean old;
	private int passes;
	public Color color = Color.YELLOW;
	
	public Projectile(double x, double y, double magnitude, double angle, double damage, double knock, Handler handler) {
		super(x, y, handler);
		this.magnitude = magnitude;
		this.angle = angle;
		xScale = sin(angle);
		yScale = cos(angle);
		xPrev = x;
		yPrev = y;
		this.damage = damage;
		knockBack = knock;
		passes = 1;
	}
	
	public Projectile(double x, double y, double magnitude, double angle, double damage, double knock, Handler handler, int passes) {
		this(x, y, magnitude, angle, damage, knock, handler);
		this.passes = passes;
	}
	
	public Line2D.Double getBounds() {
		return new Line2D.Double(x, y, xPrev, yPrev);
	}

	public void tick() {
		velX = xScale * magnitude;
		velY = yScale * magnitude;
		xPrev = x;
		yPrev = y;
		x += velX;
		y += velY;
		if (passes <= 0 || x < 0 || x > Program.WIDTH || y < 0 || y > Program.HEIGHT)
			old = true;
		if (old)
			handler.removeObject(this);
		detectCollision();
	}
	
	public void detectCollision() {
		if (!old) {
			for (int i = 1; i < handler.getObjList().size(); i++) {
				GameObject obj = handler.getObjectAt(i);
				if (obj instanceof Zombie) {
					Zombie zomb = (Zombie) obj;
					if (zomb.getBounds().intersectsLine(this.getBounds())) {
						zomb.damageMe(damage, angle, knockBack);
						passes--;
						damage /= 1.4;
						magnitude /= 1.2;
						angle += (new Random().nextDouble() - 0.5) / 2;
						xScale = sin(angle);
						yScale = cos(angle);
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.draw(getBounds());
	}
	
	public void setOld() {old = true;}
	public boolean getOld() {return old;}
	
}
