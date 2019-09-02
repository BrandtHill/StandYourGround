package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

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
		spread = 6;
		xOffset = -2;
		yOffset = 11;
		velocity = 23;
		knock = 7;
		isSidearm = true;
	}

	public void shoot() {
		if (canShoot()) {
			handler.addObjectAsync(new Projectile(this));
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 2),
				offsetPointY(xOffset, yOffset - 2),
				2,
				2,
				4 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 2));
			onShotFired();
			AudioPlayer.getSound("Pistol").play(0.95f, 0.3f);
		}
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.85;
		p.knockBack *= 1.15;
	}
}
