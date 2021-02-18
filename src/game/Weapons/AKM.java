package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class AKM extends Gun {

	public AKM() {
		super();
		gunId = GUN.AKM;
		reloadSound = AudioPlayer.getSound("ReloadAKM");
		reloadTime = 3000;
		chamberTime = 95;
		gunName = "AKM";
		ammoLoaded = magSize = 30;
		ammoExtra = ammoCapacity = 30;
		damage = 49;
		spread = 4;
		xOffset = -3;
		yOffset = 19;
		velocity = 24;
		knock = 18;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObjectAsync(new Projectile(this));
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				3,
				2,
				9 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 1.8,
				new Color(94, 85, 58)));
			onShotFired();
			AudioPlayer.getSound("AKM").play(1.0f, 0.8f);
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
		return r.nextDouble() > 0.4 ? 2 : 1;
	}
	
	@Override
	public void makeRoundSpecial(Projectile p) {
		// TODO Auto-generated method stub

	}

}
