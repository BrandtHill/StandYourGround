package game.Weapons;

import game.Audio.AudioPlayer;
import game.GamePieces.Projectile;

public class Titan extends Gun {

	public Titan() {
		super();
		gunId = GUN.Titan;
		reloadSound = AudioPlayer.getSound("ReloadTitan");
		reloadTime = 2000;
		chamberTime = 50;
		gunName = "Titan";
		ammoLoaded = magSize = 7;
		ammoExtra = ammoCapacity = 56;
		damage = 22;
		spread = 7;
		xOffset = -2;
		yOffset = 10;
		velocity = 20;
		knock = 5;
		isSidearm = true;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObject(new Projectile(this));
			onShotFired();
			AudioPlayer.getSound("Pistol").setGain(0.25f);
			AudioPlayer.getSound("Pistol").play(); //pitch 1.2
		} 
		reloadIfNeeded();
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
	}

}
