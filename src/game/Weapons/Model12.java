package game.Weapons;

import java.awt.Color;

import org.newdawn.slick.Sound;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Model12 extends Gun {

	private Sound cycleSound;
	
	public Model12() {
		super();
		gunId = GUN.Model12;
		reloadSound = AudioPlayer.getSound("ReloadM77");
		cycleSound = AudioPlayer.getSound("CycleM77");
		reloadTime = 5000;
		chamberTime = 900;
		gunName = "Model 12";
		ammoLoaded = magSize = 6;
		ammoExtra = ammoCapacity = 12;
		damage = 29;
		spread = 8;
		xOffset = -3;
		yOffset = 21;
		velocity = 16;
		knock = 5;
		isFullAuto = true; //Pump-action with slam fire
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < (specialRounds ? 6 : 8); i++) {
				handler.addObjectAsync(new Projectile(this));
			}
			if (ammoLoaded > 1) cycleSound.play();
			ticks = 0;
			onShotFired();
			AudioPlayer.getSound("Shotgun").play(0.925f, 0.275f);
		}
	}

	@Override
	public void tick() {
		super.tick();
		ticks++;
		if (ticks == 20 && (!chambered || currentlyReloading)) {
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				4,
				2,
				3 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 1.8,
				Color.RED));
		}
		if (shooting) shoot();
	}
	
	@Override
	public int getHits() {
		return r.nextDouble() > 0.35 ? 2 : 1;
	}
	
	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angle = player.getAngle();
		p.hits += r.nextInt(2) + 2;
		p.magnitude *= 1.10;
		p.knockBack *= 1.20;
		p.damage *= 1.375;
		p.color = new Color(243, 144, 0);
	}

}
