package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class Model12 extends Gun {

	private String cycleSound;
	private String reloadEmptySound;
	private int ejTicks;
	private int reloadTimeBase;
	private int reloadTimeEmpty;
	
	public Model12() {
		super(GUN.Model12);
		reloadSound = "ReloadModel12";
		reloadEmptySound = "ReloadEmptyModel12";
		cycleSound = "CycleModel12";
		reloadTimeBase = 900;
		reloadTimeEmpty = 1250;
		reloadTime = reloadTimeBase;
		chamberTime = 550;
		gunName = "Model 12";
		ammoLoaded = magSize = 6;
		ammoExtra = ammoCapacity = 6;
		damage = 25.5;
		spread = 7;
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
				Main.handler.addObjectAsync(new Projectile(this));
			}
			if (ammoLoaded > 1) AudioPlayer.playSound(cycleSound, 1f, 0.60f);
			ejTicks = 0;
			onShotFired();
			AudioPlayer.playSound("Shotgun2", 1f, 0.275f);
		}
	}

	@Override
	public void reload() {
		if (!reloading && ammoExtra > 0 && ammoLoaded < magSize) {
			if (ammoLoaded > 0) {
				AudioPlayer.playSound(reloadSound);
				reloadTime = reloadTimeBase;
			} else {
				AudioPlayer.playSound(reloadEmptySound);
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
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				4,
				2,
				3 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 1.8,
				Color.RED));
		}
		if (shooting) {
			if (reloading && ammoLoaded > 0) {
				AudioPlayer.stopSound(reloadSound);
				AudioPlayer.stopSound(reloadEmptySound);
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
		p.angle = Main.player.getAngle();
		p.hits += r.nextInt(2) + 2;
		p.magnitude *= 1.10;
		p.knockBack *= 1.20;
		p.damage *= 1.375;
		p.color = new Color(243, 144, 0);
	}

}
