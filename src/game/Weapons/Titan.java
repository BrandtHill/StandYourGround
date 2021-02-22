package game.Weapons;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Titan extends Gun {

	public Titan() {
		super(GUN.Titan);
		reloadSound = AudioPlayer.getSound("ReloadTitan");
		reloadTime = 2000;
		chamberTime = 100;
		gunName = "Titan";
		ammoLoaded = magSize = 7;
		ammoExtra = ammoCapacity = 42;
		damage = 22;
		spread = 7.5;
		xOffset = -2;
		yOffset = 10;
		velocity = 20;
		knock = 5;
		isSidearm = true;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			Main.handler.addObjectAsync(new Projectile(this));
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 2),
				offsetPointY(xOffset, yOffset - 2),
				2,
				2,
				3 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 2));
			onShotFired();
			AudioPlayer.getSound("Pistol4").play(1.0f, 0.75f);
		}
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
	}

}
