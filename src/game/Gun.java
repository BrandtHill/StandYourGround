package game;

import static java.lang.Math.PI;

import java.util.Random;

public class Gun {

	private double damage;
	private int ammoLoaded;
	private int ammoExtra;
	private int magSize;
	private boolean waitingOnReload;
	private boolean hasClickedOnEmpty;
	private long reloadTime;
	private String name;
	private PlayerObject player;
	private Handler handler;
	private long timer;
	
	public Gun(String gunName, int mag, int extra, double dmg, PlayerObject p, Handler h) {
		
		name = gunName;
		waitingOnReload = false;
		hasClickedOnEmpty = false;
		damage = dmg;
		magSize = mag;
		ammoLoaded = mag;
		ammoExtra = extra;
		player = p;
		handler = h;
		reloadTime = 1500;
	}
	
	public void shoot(double angle) {
		if (!waitingOnReload) {
			if (ammoLoaded>0) {
				Random r = new Random();
				double spread = (r.nextDouble() - 0.5) * 7 * PI / 180;
				handler.addObject(
						new ProjectileObject(player.getX() + 10, player.getY() + 10, 20, angle + spread, handler));
				AudioPlayer.getSound("Pistol").play(1.0f, 0.25f);
				ammoLoaded--;
			} else if (ammoExtra > 0)
				reload();
		}
	}
	
	public void reload() {
		if (ammoExtra > 0 && ammoLoaded < magSize) {
			System.out.println("Reloading!");
			waitingOnReload = true;
			timer = System.currentTimeMillis();
		}
	}
	private void reloadFinish() {
		int reloadAmount = magSize - ammoLoaded;

		if (ammoExtra >= reloadAmount) {
			ammoLoaded += reloadAmount;
			ammoExtra -= reloadAmount;
		}
		else {
			ammoLoaded += ammoExtra;
			ammoExtra -= ammoExtra;
		}
		
		waitingOnReload = false;

		System.out.println("Reloaded!");
	}
	
	public void tick() {
		if(!(ammoLoaded > 0) && !(waitingOnReload))
			reload();
		
		if(waitingOnReload) {
			long timeElapsed = System.currentTimeMillis() - timer;
			if((timeElapsed) > reloadTime) {				
				reloadFinish();
			}
		}
	}
	
	public double getDamage() {
		return damage;
	}

	public void setDamage(double dmg) {
		damage = dmg;
	}
	

}
