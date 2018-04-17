package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.round;

public class ZombieObject extends GameObject{

	
	public ZombieObject(int xPos, int yPos, Handler h) {
		super(xPos, yPos, ObjectType.Zombie, h);
		
	}

	public void tick() {
		int xPlayer = handler.getObjectAt(0).getX()+10;
		int yPlayer = handler.getObjectAt(0).getY()+10;
		double angle = atan2(xPlayer-x, yPlayer-y);
		int xBias = (int)round(sin(angle));
		int yBias = (int)round(cos(angle));
		Random r = new Random();
		velX = r.nextInt(7) - 3 + xBias;
		velY = r.nextInt(7) - 3 + yBias;
		
		x += velX;
		y += velY;
		
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-48);
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, 20, 20);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.GREEN);
		//g2d.draw(getBounds());
		g.setColor(Color.RED);
		g.fillRect(x, y, 20, 20);
	}

}
