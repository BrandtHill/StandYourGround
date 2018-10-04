package game.Weapons;

import game.AudioPlayer;
import game.Projectile;

public class PX4Compact extends Gun {

	public PX4Compact() {
		super();
		gunId = GUN.PX4Compact;
		reloadSound = AudioPlayer.getSound("ReloadPX4");
		reloadTime = 2000;
		chamberTime = 50;
		gunName = "PX4 Compact";
		ammoLoaded = magSize = 15;
		ammoExtra = ammoCapacity = 30;
		damage = 31;
		spread = 5;
		xOffset = -2;
		yOffset = 11;
		velocity = 23;
		knock = 7;
		isSidearm = true;
	}

	public void shoot() {
		if (canShoot()) {
			handler.addObject(new Projectile(this));
			onShotFired();
			AudioPlayer.getSound("Pistol").play(0.95f, 0.3f);
		} 
		reloadIfNeeded();
	}
}
