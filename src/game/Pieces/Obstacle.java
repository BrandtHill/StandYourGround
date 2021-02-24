package game.Pieces;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle extends GameObject {
	
	private Rectangle bounds;
	
	public Obstacle(double x, double y, double w, double h) {
		super(x, y);
		this.bounds = new Rectangle((int)x, (int)y, (int)w, (int)h);
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}
	
	@Override
	public void tick() {}

	@Override
	public void render(Graphics g) {
		//g.setColor(Color.GREEN);
		//g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

}
