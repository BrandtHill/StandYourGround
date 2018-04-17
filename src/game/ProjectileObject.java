package game;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.round;

public class ProjectileObject extends GameObject {

	private int velMag;
	private double velAng, xScale, yScale;
	public boolean old;
	
	public ProjectileObject(int xPos, int yPos, int magnitude, double angle) {
		super(xPos, yPos, ObjectType.Projectile);
		velMag = magnitude;
		velAng = angle;
		xScale = sin(velAng);
		yScale = cos(velAng);
	}

	public void tick() {
		velX = (int)round(xScale*velMag);
		velY = (int)round(yScale*velMag);
		
		x += velX;
		y += velY;
		
		if(x<0 || x>Program.WIDTH || y<0 || y>Program.HEIGHT)
			old = true;
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, 4, 4);
	}

	
}
