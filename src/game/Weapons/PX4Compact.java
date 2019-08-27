package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.GamePieces.Projectile;

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
			AudioPlayer.getSound("Pistol").setGain(0.3f);
			AudioPlayer.getSound("Pistol").play(); //pitch 0.95f
		} 
		reloadIfNeeded();
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.75;
	}
}
