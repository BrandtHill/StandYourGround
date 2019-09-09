package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle extends GameObject {
	
	private Rectangle bounds;
	private double w, h;
	
	public Obstacle(double x, double y, double w, double h) {
		super(x, y);
		this.w = w;
		this.h = h;
		this.bounds = new Rectangle((int)x, (int)y, (int)w, (int)h);
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(0, 255, 0));
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}

}
