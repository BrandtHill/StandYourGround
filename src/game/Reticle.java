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
	private double xPlayer, yPlayer;
	private boolean wantToLimitRange; //functionality exists
	
	public Reticle(double x, double y) {
		super(x, y);
		player = Program.player;
		xPlayer = player.getX();
		yPlayer = player.getY();
	}

	public void tick() {	
		xDisplay = x = Program.clamp(x, 0, Program.WIDTH);
		yDisplay = y = Program.clamp(y, 0, Program.HEIGHT);
		
		if (Program.gameState == STATE.InGame) {
			
			if (!Program.isOnEdgeX()) xPlayer = player.getX();
			if (!Program.isOnEdgeY()) yPlayer = player.getY();
			
			xDisplay = Program.clamp(x + xPlayer + 10 - Program.WIDTH / 2, 0, Program.WIDTH);
			yDisplay = Program.clamp(y + yPlayer + 30 - Program.HEIGHT / 2, 0, Program.HEIGHT);
			
			if (wantToLimitRange) limitRange(200);
		}
		
		for (int i = 0; i < xLine.length; i++)
		{
			xLine[i] = (int)xDisplay + xZeroed[i];
			yLine[i] = (int)yDisplay + yZeroed[i];
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.drawPolyline(xLine, yLine, 5);
		g2d.setColor(new Color(255,255,255,47));
		g2d.drawOval((int)x-3, (int)y-3, 6, 6);
	}
	
	private void limitRange(double range) {
		double angle = Math.atan2(xDisplay - (player.getX() + 10), yDisplay - (player.getY() + 10));
		double xW = Math.abs(range * Math.sin(angle));
		double yW = Math.abs(range * Math.cos(angle));
		double xP = player.getX() + 10;
		double yP = player.getY() + 10;
		xDisplay = Program.clamp(xDisplay, xP - xW, xP + xW);
		yDisplay = Program.clamp(yDisplay, yP - yW, yP + yW);
	}
	
	public double getXDisplay() {return xDisplay;}
	public double getYDisplay() {return yDisplay;}

}
