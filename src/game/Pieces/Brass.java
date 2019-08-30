package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class Brass extends GameObject {
	private int w, l;
	private Color c;
	private static Random r = new Random();
	private int ticks = 0;
	private int alpha = 255;
	
	public Brass(double x, double y, int w, int l, double v, double t) {
		this(x, y, w, l, v, t, new Color(181, 137, 66));
	}
	
	public Brass(double x, double y, int w, int l, double v, double t, Color c) {
		super(x, y);
		this.w = w;
		this.l = l;
		this.velX = v * Math.sin(t + 0.7 * (r.nextDouble() - 0.5));
		this.velY = v * Math.cos(t + 0.7 * (r.nextDouble() - 0.5));
		this.c = c;
	}
	
	@Override
	public void tick() {
		x += velX;
		y += velY;
		velX *= 0.85;
		velY *= 0.85;
		if (ticks++ > 600) {
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), --alpha);
			if (alpha <= 0) handler.addDeadObject(this);
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(c);
		g2d.fillRect((int)x, (int)y, w, l);
	}

}
