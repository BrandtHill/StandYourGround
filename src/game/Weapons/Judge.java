package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Judge extends Gun {

	public Judge() {
		super(GUN.Judge);
		reloadSound = "ReloadJudge";
		reloadTime = 3500;
		chamberTime = 250;
		gunName = "Judge";
		ammoLoaded = magSize = 5;
		ammoExtra = ammoCapacity = 10;
		damage = 24;
		spread = 9;
		xOffset = -2;
		yOffset = 15;
		velocity = 15;
		knock = 4;
		isSidearm = true;
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < 4; i++) {
				Main.handler.addObjectAsync(new Projectile(this));
			}
			if (ammoLoaded > 1) AudioPlayer.playSound("CockJudge");
			onShotFired();
			AudioPlayer.playSound("Pistol2", 0.75f, 0.85f);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (reloadTicks == 30) {
			for (int i = 0; i < magSize; i++) {
				Main.handler.addObjectAsync(new Brass(
					offsetPointX(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					offsetPointY(xOffset, yOffset - 2) + 4 * r.nextDouble(),
					3,
					1,
					3 * r.nextDouble(),
					Main.player.getAngle() + Math.PI / 3,
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
