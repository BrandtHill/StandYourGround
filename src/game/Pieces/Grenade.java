package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import game.Main;
import game.Audio.AudioPlayer;

public class Grenade extends GameObject {

	private int tick;
	private Random r;
	private double velocity, damage, knock;
	
	public Grenade(double x, double y) {
		super(x, y);
		r = new Random();
		this.knock = 10;
		this.damage = 9;
		this.velocity = 22;
	}
	
	@Override
	public void tick() {
		if (tick > 0) detonate();
		
		tick++;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.CYAN);
		g.drawRect((int)x, (int)y, 3, 3);
	}
	
	private void detonate() {
		Main.handler.removeObjectAsync(this);
		AudioPlayer.getSound("Pistol2").play(0.5f, 1.2f);
		for (int i = 0; i < 200; i++) {
			Main.handler.addObjectAsync(new Projectile(this, 2 * Math.PI * (i / 200f)));
		}
	}

	public double getVelocity() {return velocity;}
	public double getDamage() {return damage;}
	public double getKnock() {return knock;}
}
