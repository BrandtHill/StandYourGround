package game.Pieces.Enemies;

import game.Main;

public class DodgingZombie extends Zombie {
	private int dodgeTicks = 0;
	private boolean isDodging;
	private boolean strafeDir;
	private int ticksInSight, maxTicksInSight, ticksSinceDodge, ticksSinceStrafe;
	private double theta;
	
	public DodgingZombie(double x, double y) {
		super(x, y);
		this.health = 36 + 3 * Main.spawnSys.getLevel();
		this.speed = 1.5 + Main.spawnSys.getLevel() * 0.015;
		this.zombieSprites = Zombie.spriteSheets[1];
		this.maxTicksInSight = r.nextInt(60) + 20;
		this.ticksInSight = 0;
		this.moneyValue = 23;
		this.maxAngleChangeDegrees = 10;
	}

	@Override
	public void tick() {
		if (ticksSinceDodge++ > 360) {
			ticksInSight = isLineOfSight() ? ticksInSight + 1 : 0;
			if (ticksInSight >= maxTicksInSight) initDodge();
		}
		
		if (isDodging) dodgeTick();
		else {
			super.tick();
			if (isLineOfSight()) {
				double phi = angle + (strafeDir ? 1 : -1) * Math.PI / 2;
				velX = 1.2 * Math.sin(phi);
				velY = 1.2 * Math.cos(phi);
				move();
			}
		}
		
		if (ticksSinceStrafe++ > 300) {
			strafeDir = r.nextBoolean();
			ticksSinceStrafe = 0;
		}
	}
	
	private boolean isLineOfSight() {
		return player.getSightBounds().intersects(super.getBounds());
	}
	
	private void initDodge() {
		isDodging = true;
		ticksSinceDodge = 0;
		dodgeTicks = r.nextInt(21) + 20;
		ticksInSight = 0;
		theta = angle + (r.nextBoolean() ? 1 : -1) * (Math.PI / 3f);
		speed *= 1.1;
	}
	
	private void dodgeTick() {
		double tempSpeed = 0.75 + dodgeTicks / 25f;
		theta += r.nextGaussian() * 0.1f;
		angle = getAngleToPlayer();
		velX = tempSpeed * speed * Math.sin(theta);
		velY = tempSpeed * speed * Math.cos(theta);
		move();
		if (--dodgeTicks <= 0) isDodging = false;
		if (r.nextBoolean()) spriteNum++;
	}
}
