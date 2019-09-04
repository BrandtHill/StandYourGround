package game.Weapons;

import java.awt.Color;

import org.newdawn.slick.Sound;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Model12 extends Gun {

	private Sound cycleSound;
	private Sound reloadEmptySound;
	private int ejTicks;
	private int reloadTimeBase;
	private int reloadTimeEmpty;
	
	public Model12() {
		super();
		gunId = GUN.Model12;
		reloadSound = AudioPlayer.getSound("ReloadModel12");
		reloadEmptySound = AudioPlayer.getSound("ReloadEmptyModel12");
		cycleSound = AudioPlayer.getSound("CycleModel12");
		reloadTimeBase = 750;
		reloadTimeEmpty = 1250;
		reloadTime = reloadTimeBase;
		chamberTime = 600;
		gunName = "Model 12";
		ammoLoaded = magSize = 6;
		ammoExtra = ammoCapacity = 12;
		damage = 27;
		spread = 8.5;
		xOffset = -3;
		yOffset = 21;
		velocity = 17;
		knock = 5.5;
		isFullAuto = true; //Pump-action with slam fire
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			for (int i = 0; i < (specialRounds ? 6 : 8); i++) {
				handler.addObjectAsync(new Projectile(this));
			}
			if (ammoLoaded > 1) cycleSound.play(1f, 0.80f);
			ejTicks = 0;
			onShotFired();
			AudioPlayer.getSound("Shotgun").play(0.925f, 0.275f);
		}
	}

	@Override
	public void reload() {
		if (!reloading && ammoExtra > 0 && ammoLoaded < magSize) {
			if (ammoLoaded > 0) {
				reloadSound.play(1f, 1f);
				reloadTime = reloadTimeBase;
			}
			else {
				reloadEmptySound.play(1f, 1f);
				reloadTime = reloadTimeEmpty;
			}
			reloading = true;
		}
	}
	
	@Override
	protected void reloadFinish() {
		ammoExtra--;
		ammoLoaded++;
		
		reloading = false;
		reloadTicks = 0;
		if (!shooting) reload();
	}
	
	@Override
	public void tick() {
		super.tick();
		ticks++;
		ejTicks++;
		if (ejTicks == 20 && !chambered) {
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				4,
				2,
				3 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 1.8,
				Color.RED));
		}
		if (shooting) {
			if (reloading && ammoLoaded > 0) {
				if (reloadSound.playing()) reloadSound.stop();
				if (reloadEmptySound.playing()) reloadEmptySound.stop();
				reloading = false;
				chamberTicks = reloadTicks = 0;
			}
			shoot();
		}
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
