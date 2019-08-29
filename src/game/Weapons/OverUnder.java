package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Projectile;

public class OverUnder extends Gun {

	public OverUnder() {
		super();
		gunId = GUN.OverUnder;
		reloadSound = AudioPlayer.getSound("ReloadOverUnder");
		reloadTime = 2750;
		chamberTime = 250;
		gunName = "Over-Under";
		ammoLoaded = magSize = 2;
		ammoExtra = ammoCapacity = 10;
		damage = 29;
		spread = 9;
		xOffset = -3;
		yOffset = 21;
		velocity = 15;
		knock = 6;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < (isSpecialRounds() ? 1 : 9); i++) {
				handler.addObject(new Projectile(this));
			}
			onShotFired();
			AudioPlayer.getSound("Shotgun").play(1.0f, 0.30f);
		}
		reloadIfNeeded();
	}
	
	@Override
	public int getHits() {
		return r.nextInt(3) + 1;
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angle = player.getAngle();
		p.hits = 7;
		p.magnitude *= 1.30;
		p.damage *= 3;
		p.knockBack *= 1.4;
		p.angleMulti = 0;
		p.color = Color.WHITE;
	}
}
