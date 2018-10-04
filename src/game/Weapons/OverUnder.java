package game.Weapons;

import static java.lang.Math.PI;

import java.util.Random;

import game.AudioPlayer;
import game.Projectile;

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
		damage = 29;
	}
	
	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			double spread;
			for (int i = 0; i < 9; i++) {
				spread = (r.nextDouble() - 0.5) * 9 * PI / 180;
				handler.addObject(new Projectile(muzzlePointX(-3, 21), muzzlePointY(-3, 21), 15, angle + spread, damage,
						6, handler, r.nextInt(3) + 1));
			}
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
			AudioPlayer.getSound("Shotgun").play(1.0f, 0.30f);
		}
		else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();
	}

}
