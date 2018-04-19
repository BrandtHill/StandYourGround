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
		if (ammoLoaded>0 && !waitingOnReload) {
			Random r = new Random();
			double spread = (r.nextDouble() - 0.5) * 7 * PI / 180;
			handler.addObject(
					new ProjectileObject(player.getX() + 10, player.getY() + 10, 20, angle + spread, handler));
			AudioPlayer.getSound("Pistol").play(1.0f, 0.25f);
			ammoLoaded--;
		}
		else reload();
	}
	
	public void reload() {
		if (ammoExtra > 0 && ammoLoaded < magSize) {
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
	}
	
	public void tick() {
		if(waitingOnReload) {
			if((System.currentTimeMillis() - timer) > 0) {				
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
