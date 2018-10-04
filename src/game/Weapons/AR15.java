package game.Weapons;

import static java.lang.Math.PI;

import java.util.Random;

import game.AudioPlayer;
import game.Projectile;

public class AR15 extends Gun {

	public AR15() {
		super();
		gunId = GUN.AR15;
		reloadSound = AudioPlayer.getSound("ReloadAR15");
		reloadTime = 2750;
		chamberTime = 67;
		gunName = "AR-15";
		ammoLoaded = magSize = 30;
		ammoExtra = ammoCapacity = 30;
		damage = 35;
	}
	
	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			double spread = (r.nextDouble() - 0.5) * 3 * PI / 180;
			handler.addObject(
					new Projectile(muzzlePointX(-3, 19), muzzlePointY(-3, 19), 30, angle + spread, damage, 17.5, handler));
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
			AudioPlayer.getSound("Rifle").play(1.0f, 0.3f);
			
		} 
		else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();	
	}
}
