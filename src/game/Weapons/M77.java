package game.Weapons;

import java.awt.Color;

import game.Main;
import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class M77 extends Gun {

	private String cycleSound;
	private int ejTicks;
	
	public M77() {
		super(GUN.M77);
		reloadSound = "ReloadM77";
		cycleSound = "CycleM77";
		reloadTime = 2500;
		chamberTime = 1050;
		gunName = "M77";
		ammoLoaded = magSize = 3;
		ammoExtra = ammoCapacity = 9;
		damage = 115;
		spread = 0;
		xOffset = -3;
		yOffset = 21;
		velocity = 42;
		knock = 25;
		hits = 5;
	}

	@Override
	public void shoot() {
		if (canShoot()) {
			Main.handler.addObjectAsync(new Projectile(this));
			AudioPlayer.playSound("Sniper", 1f, 0.4f);
			if (ammoLoaded > 1) AudioPlayer.playSound(cycleSound);
			onShotFired();
			ejTicks = 0;
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		ejTicks++;
		if (ejTicks == 20 && !chambered) {
			Main.handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				4,
				2,
				3 + 2 * r.nextDouble(),
				Main.player.getAngle() - Math.PI / 1.8));
		}
	}
	
	@Override
	public void onSwapFrom() {
		super.onSwapFrom();
		AudioPlayer.stopSound(cycleSound);
	}
	
	@Override
	public void onSwapTo() {
		super.onSwapTo();
		if (!chambered && ammoLoaded > 0) AudioPlayer.playSound(cycleSound);
	}

	@Override
	public void reload() {
		if (!reloading && ammoExtra > 0 && ammoLoaded < magSize) {
			AudioPlayer.playSound(reloadSound);
			reloading = true;
			ticks = 20;
		}
	}
	
	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angleMulti = 0.1;
		p.color = new Color(243, 144, 0);
		p.hits += 3;
		p.damage *= 1.72;
		p.magnitude *= 1.15;
		p.knockBack *= 1.2;
	}
}
