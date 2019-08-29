package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

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
			if (ammoLoaded > 1) {
				AudioPlayer.getSound("CycleM77").play();
				ticks = 30;
			}
			onShotFired();
		}
		reloadIfNeeded();
	}
	
	@Override
	public void tick() {
		super.tick();
		ticks = Math.max(0, ticks - 1);
		if (ticks == 1) {
			handler.addBrass(new Brass(offsetPointX(xOffset, yOffset - 6), offsetPointY(xOffset, yOffset - 6), 4, 2, 7 + 2 * r.nextDouble(), player.getAngle() - Math.PI / 1.8));
		}
	}

	@Override
	public void reload() {
		if (!currentlyReloading && ammoExtra > 0 && ammoLoaded < magSize) {
			reloadSound.play(1f, 1f);
			currentlyReloading = true;
			ticks = 30;
		}
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
