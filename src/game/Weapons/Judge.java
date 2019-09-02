package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Judge extends Gun {

	public Judge() {
		super();
		gunId = GUN.Judge;
		reloadSound = AudioPlayer.getSound("ReloadModel57");
		reloadTime = 4000;
		chamberTime = 250;
		gunName = "Judge";
		ammoLoaded = magSize = 5;
		ammoExtra = ammoCapacity = 15;
		damage = 20;
		spread = 12;
		xOffset = -2;
		yOffset = 15;
		velocity = 14;
		knock = 4;
		isSidearm = true;
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < 5; i++) {
				handler.addObjectAsync(new Projectile(this));
			}
			onShotFired();
			AudioPlayer.getSound("Shotgun").play(1.3f, 0.25f);
			if (ammoLoaded > 1) AudioPlayer.getSound("CockModel57").play();
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (reloadTicks == 30) {
			for (int i = 0; i < magSize; i++) {
				handler.addObjectAsync(new Brass(
					offsetPointX(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					offsetPointY(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					3,
					1,
					3 * r.nextDouble(),
					player.getAngle() + Math.PI / 3,
					Color.RED));
			}
		}
	}
	
	@Override
	public int getHits() {
		return r.nextDouble() > 0.75 ? 2 : 1;
	}

	@Override
	public void makeRoundSpecial(Projectile p) {}

}
