package game.Pieces.Enemies;

public class ThiccZombie extends Zombie {

	public ThiccZombie(double x, double y, double speed, double health) {
		super(x, y, speed, health);
		this.moneyValue = 37;
		this.zombieSprites = Zombie.spriteSheets[3];
		this.maxAngleChangeDegrees = 2;
	}
	
	@Override
	public void damageMe(double damage, double angle, double knock) {
		knock *= 0.35;
		speed *= 1.05;
		super.damageMe(damage, angle, knock);
	}
}
