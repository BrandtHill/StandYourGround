package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Reticle extends GameObject {

	private final int[] xZeroed = {-10, 10, 0, 0, 0};
	private final int[] yZeroed = {0, 0, 0, 10, -10};
	private int[] xLine = new int[5];
	private int[] yLine = new int[5];
	
	public Reticle(double x, double y) {
		super(x, y);
	}

	public void tick() {
		x = Program.clamp(x, 0, Program.WIDTH);
		y = Program.clamp(y, 0, Program.HEIGHT);
		for(int i = 0; i<xLine.length; i++)
		{
			xLine[i] = (int)x + xZeroed[i];
			yLine[i] = (int)y + yZeroed[i];
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawPolyline(xLine, yLine, 5);
	}

}
