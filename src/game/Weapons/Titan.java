package game.Weapons;

import static java.lang.Math.PI;

import java.util.Random;

import game.AudioPlayer;
import game.Projectile;

public class Titan extends Gun {

	public Titan() {
		super();
		gunId = GUN.Titan;
		reloadSound = AudioPlayer.getSound("ReloadTitan");
		reloadTime = 2000;
		chamberTime = 50;
		gunName = "Titan";
		ammoLoaded = magSize = 7;
		ammoExtra = ammoCapacity = 56;
		damage = 22;
		isSidearm = true;
	}
	
	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			double spread = (r.nextDouble() - 0.5) * 7 * PI / 180;
			handler.addObject(
					new Projectile(muzzlePointX(-2, 10), muzzlePointY(-2, 10), 20, angle + spread, damage, 5, handler));
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
			AudioPlayer.getSound("Pistol").play(1.2f, 0.25f);
			
		} 
		else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();	
	}

}
