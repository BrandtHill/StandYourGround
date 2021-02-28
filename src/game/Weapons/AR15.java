package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class AR15 extends Gun {

	public AR15() {
		super(GUN.AR15);
		reloadSound = "ReloadAR15";
		reloadTime = 2750;
		chamberTime = 66;
		gunName = "AR-15";
		ammoLoaded = magSize = 30;
		ammoExtra = ammoCapacity = 30;
		damage = 38;
		spread = 2;
		xOffset = -3;
		yOffset = 19;
		velocity = 30;
		knock = 12;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			Main.handler.addObjectAsync(new Projectile(this));
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				3,
				2,
				7 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 1.8));
			onShotFired();
			AudioPlayer.playSound("Rifle", 1.0f, 0.3f);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (shooting && isFullAuto && canShoot()) {
			shoot();
		}
		ticks++;
	}
	
	@Override
	public int getHits() {
		return r.nextDouble() > 0.833 ? 2 : 1;
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.325;
		p.magnitude *= 1.25;
		p.angleMulti *= 0.5;
		p.hits += r.nextDouble() > 0.4 ? 3 : 2;
	}
}
