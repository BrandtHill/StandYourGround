package game.Weapons;

import java.awt.Color;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class OverUnder extends Gun {

	public OverUnder() {
		super();
		gunId = GUN.OverUnder;
		reloadSound = AudioPlayer.getSound("ReloadOverUnder");
		reloadTime = 2750;
		chamberTime = 250;
		gunName = "Over-Under";
		ammoLoaded = magSize = 2;
		ammoExtra = ammoCapacity = 10;
		damage = 27;
		spread = 9;
		xOffset = -3;
		yOffset = 21;
		velocity = 15;
		knock = 6;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < (specialRounds ? 6 : 9); i++) {
				handler.addObjectAsync(new Projectile(this));
			}
			onShotFired();
			AudioPlayer.getSound("Shotgun").play(1.0f, 0.30f);
		}
		reloadIfNeeded();
	}
	
	@Override
	public void tick() {
		super.tick();
		if (reloadTicks == 30) {
			for (int i = 0; i < magSize; i++) {
				handler.addObjectAsync(new Brass(
					offsetPointX(xOffset, yOffset - 14) + 4 * r.nextDouble(),
					offsetPointY(xOffset, yOffset - 14) + 4 * r.nextDouble(),
					4,
					2,
					1 + 2 * r.nextDouble(),
					player.getAngle() - Math.PI / 3,
					Color.RED));
			}
		}
	}
	
	@Override
	public int getHits() {
		return r.nextInt(2) + 1;
	}

	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angle = player.getAngle();
		p.hits += r.nextInt(2) + 2;
		p.magnitude *= 1.10;
		p.knockBack *= 1.15;
		p.damage *= 1.35;
		p.color = new Color(243, 144, 0);;
	}
}
