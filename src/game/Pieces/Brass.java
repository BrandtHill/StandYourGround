package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import game.Program;

public class Brass extends GameObject {
	private int w, l;
	private static Random r = new Random();
	
	public Brass(int w, int l) {
		super(Program.player.getGunWielded().muzzlePointX(), Program.player.getGunWielded().muzzlePointY());
		this.w = w;
		this.l = l;
		this.velX = 5 * Math.sin(Program.player.getAngle() - Math.PI / 2 + 0.5 * (r.nextDouble() - 0.5));
		this.velY = 5 * Math.cos(Program.player.getAngle() - Math.PI / 2 + 0.5 * (r.nextDouble() - 0.5));
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;
		velX *= 0.8;
		velY *= 0.8;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.YELLOW);
		g2d.drawRect((int)x, (int)y, w, l);
	}

}
