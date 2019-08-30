package game.Weapons;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

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
			handler.addObjectAsync(new Projectile(this));
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 2),
				offsetPointY(xOffset, yOffset - 2),
				2,
				2,
				3 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 2));
			onShotFired();
			AudioPlayer.getSound("Pistol").play(1.2f, 0.25f);
		} 
		reloadIfNeeded();
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
	}

}
