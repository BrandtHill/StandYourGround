package game.Weapons;

import java.awt.Color;

import org.newdawn.slick.Sound;

import game.Audio.AudioPlayer;
import game.Pieces.Brass;
import game.Pieces.Projectile;

public class M77 extends Gun {

	private Sound cycleSound;
	private int ejTicks;
	
	public M77() {
		super();
		gunId = GUN.M77;
		reloadSound = AudioPlayer.getSound("ReloadM77");
		cycleSound = AudioPlayer.getSound("CycleM77");
		reloadTime = 2500;
		chamberTime = 1050;
		gunName = "M77";
		ammoLoaded = magSize = 3;
		ammoExtra = ammoCapacity = 15;
		damage = 110;
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
			handler.addObjectAsync(new Projectile(this));
			AudioPlayer.getSound("Sniper").play(1f, 0.4f);
			if (ammoLoaded > 1) cycleSound.play();
			onShotFired();
			ejTicks = 0;
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		ejTicks++;
		if (ejTicks == 20 && !chambered) {
			handler.addObjectAsync(new Brass(
				offsetPointX(xOffset, yOffset - 9),
				offsetPointY(xOffset, yOffset - 9),
				4,
				2,
				3 + 2 * r.nextDouble(),
				player.getAngle() - Math.PI / 1.8));
		}
	}
	
	@Override
	public void onSwapFrom() {
		super.onSwapFrom();
		if (cycleSound.playing()) cycleSound.stop();
	}
	
	@Override
	public void onSwapTo() {
		super.onSwapTo();
		if (!chambered && ammoLoaded > 0) cycleSound.play();
	}

	@Override
	public void reload() {
		if (!reloading && ammoExtra > 0 && ammoLoaded < magSize) {
			reloadSound.play(1f, 1f);
			reloading = true;
			ticks = 20;
		}
	}
	
	@Override
	public void makeRoundSpecial(Projectile p) {
		p.angleMulti = 0.1;
		p.color = new Color(243, 144, 0);
		p.hits += 3;
		p.damage *= 1.65;
		p.magnitude *= 1.15;
		p.knockBack *= 1.2;
	}
}
