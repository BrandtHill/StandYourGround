package game.Weapons;

import java.awt.Color;

import game.Projectile;
import game.Audio.AudioPlayer;

public class M77 extends Gun {

	public M77() {
		super();
		gunId = GUN.M77;
		reloadSound = AudioPlayer.getSound("ReloadM77");
		reloadTime = 3000;
		chamberTime = 1250;
		gunName = "M77";
		ammoLoaded = magSize = 3;
		ammoExtra = ammoCapacity = 15;
		damage = 110;
		spread = 0;
		xOffset = -3;
		yOffset = 21;
		velocity = 42;
		knock = 25;
		hits = 5;
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObject(new Projectile(this));
			AudioPlayer.getSound("Sniper").play(1f, 0.4f);
			if(ammoLoaded > 1) AudioPlayer.getSound("CycleM77").play();
			onShotFired();
		}
		reloadIfNeeded();
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angleMulti = 0.1;
		p.color = new Color(243, 144, 0);
		p.hits += 3;
		p.damage *= 1.25;
		p.magnitude *= 1.15;
		p.knockBack *= 1.2;
	}
}
