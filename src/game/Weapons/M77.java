package game.Weapons;

import static java.lang.Math.PI;

import java.util.Random;

import game.AudioPlayer;
import game.Projectile;

public class M77 extends Gun {

	public M77() {
		super();
		gunId = GUN.M77;
		reloadSound = AudioPlayer.getSound("ReloadM77");
		reloadTime = 3000;
		chamberTime = 1250;
		gunName = "M77";
		ammoLoaded = magSize = 3;
		ammoExtra = ammoCapacity = 15;
		damage = 110;
	}

	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			handler.addObject(
					new Projectile(muzzlePointX(-3, 19), muzzlePointY(-3, 19), 42, angle, damage, 25, handler, 5));
			AudioPlayer.getSound("Sniper").play(1f, 0.4f);
			if(ammoLoaded > 1) AudioPlayer.getSound("CycleM77").play();
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
		}
		else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();
	}
}
