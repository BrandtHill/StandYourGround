package game.Pieces.Enemies;

public class ThiccZombie extends Zombie {

	public ThiccZombie(double x, double y) {
		super(x, y);
		this.health = 120 + 4 * currLevelOrMax(20);
		this.speed = 1.1 + currLevelOrMax(15) * 0.015;
		this.moneyValue = 31;
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
