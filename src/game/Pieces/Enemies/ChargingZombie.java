package game.Pieces.Enemies;

import game.Main;

public class ChargingZombie extends Zombie {
	private int chargeTicks = 0;
	private boolean isCharging;
	private int ticksSinceCharge;
	
	public ChargingZombie(double x, double y) {
		super(x, y);
		this.health = 105 + 3.5 * Main.spawnSys.getLevel();
		this.speed = 0.7 + Main.spawnSys.getLevel() * 0.015;
		this.zombieSprites = Zombie.spriteSheets[4];
		this.moneyValue = 30;
		this.maxAngleChangeDegrees = 2;
	}

	@Override
	public void tick() {
		if (ticksSinceCharge++ > 420 
				&& !handler.hitsObstacle(getSightToPlayer())
				&& Main.distance(this, player) < 225) {
			initCharge();
		}
		
		if (isCharging) chargeTick();
		else super.tick();
	}
	
	private void initCharge() {
		isCharging = true;
		ticksSinceCharge = 0;
		angle = getAngleToPlayer();
		chargeTicks = r.nextInt(11) + 100;
		speed *= 1.1;
	}
	
	private void chargeTick() {
		double tempSpeed = 10 * (chargeTicks / 60f);
		if (ticksSinceCharge < 55) tempSpeed = 0;
		else spriteNum++;
		angle = getAdjustedAngle(getAngleToPlayer(), 12);
		velX = tempSpeed * Math.sin(angle);
		velY = tempSpeed * Math.cos(angle);
		move();
		if (--chargeTicks <= 0) isCharging = false;
	}
}
