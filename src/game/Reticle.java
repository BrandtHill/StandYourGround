package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.Program.STATE;

public class Reticle extends GameObject {

	private final int[] xZeroed = {-10, 10, 0, 0, 0};
	private final int[] yZeroed = {0, 0, 0, 10, -10};
	private int[] xLine = new int[5];
	private int[] yLine = new int[5];
	private static Player player;
	private double xDisplay, yDisplay;
	
	public Reticle(double x, double y) {
		super(x, y);
		player = Program.player;
	}

	public void tick() {
		
		xDisplay = x = Program.clamp(x, 0, Program.WIDTH);
		yDisplay = y = Program.clamp(y, 0, Program.HEIGHT);
				
		if (Program.gameState == STATE.InGame) {
			/*double angle = player.getAngle();
			double xW = Math.abs(200 * Math.sin(angle));
			double yW = Math.abs(200 * Math.cos(angle));
			double xP = player.getX() + 10;
			double yP = player.getY() + 10;
			xDisplay = Program.clamp(x, xP - xW, xP + xW);
			yDisplay = Program.clamp(y, yP - yW, yP + yW);*/
			xDisplay = Program.clamp(x + player.getX() + 10 - Program.WIDTH/2, 0, Program.WIDTH);
			yDisplay = Program.clamp(y + player.getY() + 30 - Program.HEIGHT/2, 0, Program.HEIGHT);
		}
		
		for(int i = 0; i<xLine.length; i++)
		{
			xLine[i] = (int)xDisplay + xZeroed[i];
			yLine[i] = (int)yDisplay + yZeroed[i];
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawPolyline(xLine, yLine, 5);
		g2d.setColor(new Color(255,255,255,63));
		g2d.drawOval((int)x-2, (int)y-2, 4, 4);
	}
	
	public double getXDisplay() {return xDisplay;}
	public double getYDisplay() {return yDisplay;}

}
