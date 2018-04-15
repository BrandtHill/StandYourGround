package game;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerObject extends GameObject{

	public PlayerObject(int xPos, int yPos) {
		super(xPos, yPos, ObjectType.Player);
		//velX=4;
	}

	public void tick() {
		x+=velX;
		y+=velY;
	}

	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x, y, 20, 20);
	}

}
