package game.Weapons;

import game.AudioPlayer;
import game.Projectile;

public class Model57 extends Gun {

	public Model57() {
		super();
		gunId = GUN.Model57;
		reloadSound = AudioPlayer.getSound("ReloadPX4");
		reloadTime = 3000;
		chamberTime = 200;
		gunName = "Model 57";
		ammoLoaded = magSize = 6;
		ammoExtra = ammoCapacity = 18;
		damage = 65;
		spread = 2;
		xOffset = -2;
		yOffset = 16;
		velocity = 37;
		knock = 20;
		hits = 3;
		isSidearm = true;
	}
	
	@Override
	public void shoot() {
		if (canShoot()) {
			handler.addObject(new Projectile(this));
			chambered = false;
			timerChamber = System.currentTimeMillis();
			ammoLoaded--;
			AudioPlayer.getSound("Pistol").play(0.75f,0.45f);
		}
		reloadIfNeeded();
	}

}
