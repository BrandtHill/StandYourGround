package game.Weapons;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Model57 extends Gun {

	public Model57() {
		super();
		gunId = GUN.Model57;
		reloadSound = AudioPlayer.getSound("ReloadModel57");
		reloadTime = 3000;
		chamberTime = 200;
		gunName = "Model 57";
		ammoLoaded = magSize = 6;
		ammoExtra = ammoCapacity = 18;
		damage = 65;
		spread = 3;
		xOffset = -2;
		yOffset = 16;
		velocity = 37;
		knock = 20;
		hits = 3;
		isSidearm = true;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObjectAsync(new Projectile(this));
			AudioPlayer.getSound("Pistol").play(0.50f,0.45f);
			if (ammoLoaded > 1) AudioPlayer.getSound("CockModel57").play();
			onShotFired();
		}
		reloadIfNeeded();
	}
	
	@Override
	public void tick() {
		super.tick();
		if (reloadTicks == 30) {
			for (int i = 0; i < magSize; i++) {
				handler.addObjectAsync(new Brass(
					offsetPointX(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					offsetPointY(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					2,
					2,
					3 * r.nextDouble(),
					player.getAngle() + Math.PI / 3));
			}
		}
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
	}

}
