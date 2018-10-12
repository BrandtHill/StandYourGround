package game;

import java.util.Random;

public class DodgingZombie extends Zombie {

	private long lastDodged = 0;
	private int numTicks = 0;
	private boolean isDodging;
	
	public DodgingZombie(double x, double y, double speed, double health) {
		super(x, y, speed, health);
		this.zombieSprites = zombieSprites2;
	}

	@Override
	public void tick() {
		
		if ((System.currentTimeMillis() - lastDodged > 6000) && isLineOfSight()) initDodge();
		
		if (isDodging) dodgeTick();
		else super.tick();
	}
	
	private boolean isLineOfSight() {
		return player.getSightBounds().intersects(super.getBounds());
	}
	
	private void initDodge() {
		isDodging = true;
		lastDodged = System.currentTimeMillis();
		numTicks = 30;
		if (new Random().nextBoolean()) angle += (Math.PI / 3f);
		else angle -= (Math.PI / 3f);
	}
	
	private void dodgeTick() {
		double tempSpeed = 0.5 + numTicks / 20f;
		x += tempSpeed * speed * Math.sin(angle);
		y += tempSpeed * speed * Math.cos(angle);
		if (--numTicks <= 0) isDodging = false;
	}
}
