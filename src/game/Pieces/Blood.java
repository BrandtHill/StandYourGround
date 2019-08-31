package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;

public class Blood extends GameObject {
	private int ticks;
	private int alpha;
	
	public Blood(double x, double y) {
		super(x, y);
		ticks = 0;
		alpha = 255;
	}
	
	public Blood(double x, double y, double knock, double angle) {
		this(x, y);
		velX = Math.sin(angle) * knock;
		velY = Math.cos(angle) * knock;
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;
		velX *= 0.75;
		velY *= 0.75;
		alpha -= ticks / 120;
		if (alpha <= 0) handler.removeObjectAsync(this);
		ticks++;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(160, 0, 0, alpha));
		g.fillRect((int)x - 2, (int)y - 2, 4, 4);
	}
}
