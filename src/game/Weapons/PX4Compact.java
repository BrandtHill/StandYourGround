package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class PX4Compact extends Gun {

	public PX4Compact() {
		super(GUN.PX4Compact);
		reloadSound = "ReloadPX4";
		reloadTime = 1600;
		chamberTime = 50;
		gunName = "PX4 Compact";
		ammoLoaded = magSize = 15;
		ammoExtra = ammoCapacity = 15;
		damage = 31;
		spread = 6;
		xOffset = -2;
		yOffset = 11;
		velocity = 23;
		knock = 9;
		isSidearm = true;
	}

	public void shoot() {
		if (canShoot()) {
			Main.handler.addObjectAsync(new Projectile(this));
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 2),
				offsetPointY(xOffset, yOffset - 2),
				2,
				2,
				4 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 2));
			onShotFired();
			AudioPlayer.playSound("Pistol", 0.95f, 0.35f);
		}
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.85;
		p.knockBack *= 1.15;
	}
}
