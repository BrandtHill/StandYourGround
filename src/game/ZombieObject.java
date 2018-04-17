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

	
	public ZombieObject(double xPos, double yPos, Handler h) {
		super(xPos, yPos, ObjectType.Zombie, h);
		
	}

	public void tick() {
		double xPlayer = handler.getObjectAt(0).getX()+10;
		double yPlayer = handler.getObjectAt(0).getY()+10;
		double angle = atan2(xPlayer-x, yPlayer-y);
		double xBias = sin(angle);
		double yBias = cos(angle);
		Random r = new Random();
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
		
		g2d.setColor(Color.GREEN);
		//g2d.draw(getBounds());
		g.setColor(Color.RED);
		g.fillRect((int)x, (int)y, 20, 20);
	}

}
