package game;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FastZombie extends Zombie {

	public FastZombie(double x, double y, double speed, double health) {
		super(x, y, speed, health);
		this.moneyValue = 34;
		this.zombieSprites = zombieSprites3;
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
		
		if (tickDivider % 4 == 0) {
			detectCollision();
			spriteNum++;
		}
		
		speed *= 1.001;
		
		tickDivider++;
	}
	
	@Override
	public void damageMe(double damage, double angle, double knock) {
		knock *= 3;
		super.damageMe(damage, angle, knock);
	}

}
