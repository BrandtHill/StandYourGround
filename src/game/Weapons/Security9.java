package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Security9 extends Gun {

	public Security9() {
		super(GUN.Security9);
		reloadSound = "ReloadPX4";
		reloadTime = 1600;
		chamberTime = 85;
		gunName = "Security 9";
		ammoLoaded = magSize = 15;
		ammoExtra = ammoCapacity = 15;
		damage = 31;
		spread = 6.5;
		xOffset = -2;
		yOffset = 12;
		velocity = 24;
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
			AudioPlayer.playSound("Pistol", 1f, 0.3f);
		}
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.20;
		p.magnitude *= 1.20;
		p.hits += r.nextDouble() > 0.3 ? 2 : 1;
		p.knockBack *= 1.1;
	}
}
