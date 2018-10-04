package game.Weapons;

import static java.lang.Math.PI;

import java.awt.Color;
import java.util.Random;

import game.AudioPlayer;
import game.Projectile;

public class PX4Compact extends Gun {

	public PX4Compact() {
		super();
		gunId = GUN.PX4Compact;
		reloadSound = AudioPlayer.getSound("ReloadPX4");
		reloadTime = 2000;
		chamberTime = 50;
		gunName = "PX4 Compact";
		ammoLoaded = magSize = 15;
		ammoExtra = ammoCapacity = 30;
		damage = 31;
		isSidearm = true;
	}

	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			double spread = (r.nextDouble() - 0.5) * 5 * PI / 180;;
			double newDmg = damage * (isSpecialRounds() ? 1.75 : 1.0);
			Projectile p = new Projectile(muzzlePointX(-2, 10), muzzlePointY(-2, 10), 23, angle + spread, newDmg, 7, handler);
			if (isSpecialRounds()) p.color = new Color(243, 144, 0);
			handler.addObject(p);
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
			AudioPlayer.getSound("Pistol").play(0.95f, 0.3f);
		} 
		else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();	
	}
}
