package game.Pieces.Enemies;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FastZombie extends Zombie {

	public FastZombie(double x, double y) {
		super(x, y);
		this.health = 40 + 4 * currLevelOrMax(20);
		this.speed = 1.9 + currLevelOrMax(20) * 0.015;
		this.moneyValue = 27;
		this.zombieSprites = Zombie.spriteSheets[2];
		this.maxAngleChangeDegrees = 3;
	}
	
	@Override
	public void tick() {
		angle = determineAngle();
		velX = r.nextGaussian() * 1.5 + speed*sin(angle);
		velY = r.nextGaussian() * 1.5 + speed*cos(angle);
		move();
		detectCollision();
		
		if (ticks++ % 4 == 0) spriteNum++;
		
		speed *= 1.0005;
		speed = Math.max(speed, 2.375);
		if (ticks % 30 == 0) findPathIfNeeded();
	}
	
	@Override
	public void damageMe(double damage, double angle, double knock) {
		knock *= 1.8;
		speed *= 0.90;
		super.damageMe(damage, angle, knock);
	}

}
