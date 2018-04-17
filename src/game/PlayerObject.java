package game;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.round;

public class PlayerObject extends GameObject{

	public PlayerObject(int xPos, int yPos) {
		super(xPos, yPos, ObjectType.Player);
		//velX=4;
	}

	public void tick() {
		
		if(velX != 0 && velY != 0) {
			x+=(int)round(HALFSQRT2*velX);
			y+=(int)round(HALFSQRT2*velY);
		}
		else {
			x+=velX;
			y+=velY;
		}
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-48);
	}

	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x, y, 20, 20);
	}

}
