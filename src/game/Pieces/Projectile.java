package game.Pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

import game.Program;
import game.Pieces.Enemies.Zombie;
import game.Weapons.Gun;

import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.lang.Math.cos;

public class Projectile extends GameObject {
	
	// public variables because I'm not about to make all these getters and setters
	public double magnitude, angle, xScale, yScale, xPrev, yPrev, damage, knockBack, angleMulti;
	public int hits;
	public Color color;
	
	public Projectile(Gun g) {
		super(g.muzzlePointX(), g.muzzlePointY());
		this.angle = Program.player.getAngle() + (new Random().nextDouble() - 0.5) * g.getSpread() * PI / 180;
		this.xScale = sin(angle);
		this.yScale = cos(angle);
		this.xPrev = x;
		this.yPrev = y;
		this.damage = g.getDamage();
		this.knockBack = g.getKnock();
		this.magnitude = g.getVelocity();
		this.hits = g.getHits();
		this.color = Color.YELLOW;
		this.angleMulti = 0.5;
		
		if (g.isSpecialRounds()) g.makeRoundSpecial(this);
	}
	
	public Line2D.Double getBounds() {
		return new Line2D.Double(x, y, xPrev, yPrev);
	}

	public void tick() {
		velX = xScale * magnitude;
		velY = yScale * magnitude;
		xPrev = x;
		yPrev = y;
		x += velX;
		y += velY;
		
		if (hits <= 0 || x < 0 || x > Program.WIDTH || y < 0 || y > Program.HEIGHT) handler.removeObject(this);
		else detectCollision();
	}
	
	public void detectCollision() {
		for (int i = 1; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if (obj instanceof Zombie) {
				Zombie zomb = (Zombie) obj;
				if (zomb.getBounds().intersectsLine(this.getBounds())) {
					zomb.damageMe(damage, angle, knockBack);
					hits--;
					damage /= 1.4;
					magnitude /= 1.2;
					angle += (new Random().nextDouble() - 0.5) * angleMulti;
					xScale = sin(angle);
					yScale = cos(angle);
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.draw(getBounds());
	}
}
