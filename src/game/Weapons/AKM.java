package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class AKM extends Gun {

	public AKM() {
		super(GUN.AKM);
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
			Main.handler.addObjectAsync(new Projectile(this));
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				3,
				2,
				9 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 1.8,
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
		int h = r.nextDouble() > 0.4 ? 2 : 1;
		if (specialRounds) h += 1 + r.nextDouble() > 0.3 ? 1 : 0;
		return h;
	}
	
	@Override
	public void makeRoundSpecial(Projectile p) {
		p.color = new Color(243, 144, 0);
		p.damage *= 1.4;
		p.knockBack *= 1.10;
	}

}
