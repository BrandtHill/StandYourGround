package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class OverUnder extends Gun {

	public OverUnder() {
		super(GUN.OverUnder);
		reloadSound = "ReloadOverUnder";
		reloadTime = 2750;
		chamberTime = 250;
		gunName = "Over-Under";
		ammoLoaded = magSize = 2;
		ammoExtra = ammoCapacity = 6;
		damage = 28;
		spread = 5.5;
		xOffset = -3;
		yOffset = 21;
		velocity = 17;
		knock = 6;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < (specialRounds ? 6 : 8); i++) {
				Main.handler.addObjectAsync(new Projectile(this));
			}
			onShotFired();
			AudioPlayer.playSound("Shotgun", 1.0f, 0.30f);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (reloadTicks == 30) {
			for (int i = 0; i < magSize; i++) {
				Main.handler.addObjectAsync(new Brass(
					offsetPointX(xOffset, yOffset - 14) + 4 * r.nextDouble(),
					offsetPointY(xOffset, yOffset - 14) + 4 * r.nextDouble(),
					4,
					2,
					1 + 2 * r.nextDouble(),
					Main.player.getAngle() - Math.PI / 3,
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
		p.angle = Main.player.getAngle();
		p.hits += r.nextInt(2) + 2;
		p.magnitude *= 1.10;
		p.knockBack *= 1.20;
		p.damage *= 1.375;
		p.color = new Color(243, 144, 0);
	}
}
