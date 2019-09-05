package game.Pieces.Enemies;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FastZombie extends Zombie {

	public FastZombie(double x, double y, double speed, double health) {
		super(x, y, speed, health);
		this.moneyValue = 30;
		this.zombieSprites = Zombie.spriteSheets[2];
		this.maxAngleChangeDegrees = 3;
	}
	
	@Override
	public void tick() {
		angle = getAngleToPlayer();
		xBias = speed*sin(angle);
		yBias = speed*cos(angle);
		velX = r.nextInt(31)/10.0 - 1.5 + xBias;
		velY = r.nextInt(31)/10.0 - 1.5 + yBias;
		
		x += velX;
		y += velY;
		
		if (ticks % 4 == 0) {
			detectCollision();
			spriteNum++;
		}
		
		speed *= 1.0005;
		speed = Math.max(speed, 2.5);
		
		ticks++;
	}
	
	@Override
	public void damageMe(double damage, double angle, double knock) {
		knock *= 1.8;
		speed *= 0.90;
		super.damageMe(damage, angle, knock);
	}

}
