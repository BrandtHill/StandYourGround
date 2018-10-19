package game.Weapons;

import game.AudioPlayer;
import game.Projectile;

public class AR15 extends Gun {

	public AR15() {
		super();
		gunId = GUN.AR15;
		reloadSound = AudioPlayer.getSound("ReloadAR15");
		reloadTime = 2750;
		chamberTime = 60;
		gunName = "AR-15";
		ammoLoaded = magSize = 30;
		ammoExtra = ammoCapacity = 30;
		damage = 35;
		spread = 2;
		xOffset = -3;
		yOffset = 19;
		velocity = 30;
		knock = 17.5;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObject(new Projectile(this));
			onShotFired();
			AudioPlayer.getSound("Rifle").play(1.0f, 0.3f);
		} 
		reloadIfNeeded();	
	}
	
	@Override
	public void tick() {
		super.tick();
		if ((tickDivider % 4) == 0 && isFullAuto && shooting) {
			shoot();
		}
		tickDivider++;
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
	}
}
