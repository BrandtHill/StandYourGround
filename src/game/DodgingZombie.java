package game;

public class DodgingZombie extends Zombie {

	private long lastDodged = 0;
	private int numTicks = 0;
	private boolean isDodging;
	private int ticksInSight, maxTicksInSight;
	private double theta;
	
	public DodgingZombie(double x, double y, double speed, double health) {
		super(x, y, speed, health);
		this.zombieSprites = zombieSprites2;
		this.maxTicksInSight = r.nextInt(60) + 20;
		this.ticksInSight = 0;
	}

	@Override
	public void tick() {
		
		if ((System.currentTimeMillis() - lastDodged > 6000)) {
			if (isLineOfSight()) ticksInSight++;
			else ticksInSight = 0;
			
			if (ticksInSight >= maxTicksInSight) initDodge();
		}
		
		if (isDodging) dodgeTick();
		else super.tick();
	}
	
	private boolean isLineOfSight() {
		return player.getSightBounds().intersects(super.getBounds());
	}
	
	private void initDodge() {
		isDodging = true;
		lastDodged = System.currentTimeMillis();
		numTicks = r.nextInt(21) + 20;
		ticksInSight = 0;
		theta = angle + (r.nextBoolean() ? 1 : -1) * (Math.PI / 3f);
		speed *= 1.1;
	}
	
	private void dodgeTick() {
		double tempSpeed = 0.75 + numTicks / 15f;
		theta += r.nextGaussian() * 0.1f;
		angle = getAngleToPlayer();
		x += tempSpeed * speed * Math.sin(theta);
		y += tempSpeed * speed * Math.cos(theta);
		if (--numTicks <= 0) isDodging = false;
		if (r.nextBoolean()) spriteNum++;
	}
}
