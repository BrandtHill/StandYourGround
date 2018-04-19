package game;

import static java.lang.Math.PI;

import java.util.Random;

public class Gun {

	private double damage;
	private int ammoLoaded;
	private int ammoExtra;
	private int magSize;
	private boolean waitingOnReload;
	//private boolean hasClickedOnEmpty;
	private long reloadTime;
	private String gunName;
	private PlayerObject player;
	private Handler handler;
	private long timer;
	private GUN gunType;
	public static enum GUN{
		Pistol,
		Rifle,
		Shotgun;	
	}
	
	public Gun(String name, GUN type, int mag, int extra, double dmg, PlayerObject p, Handler h) {
		
		gunName = name;
		gunType = type;
		waitingOnReload = false;
		//hasClickedOnEmpty = false;
		damage = dmg;
		magSize = mag;
		ammoLoaded = mag;
		ammoExtra = extra;
		player = p;
		handler = h;
		reloadTime = 2000;
	}
	
	public void shoot(double angle) {
		if (ammoLoaded>0 && !waitingOnReload) {
			Random r = new Random();
			
			if (gunType == GUN.Pistol) {
				double spread = (r.nextDouble() - 0.5) * 7 * PI / 180;
				handler.addObject(
						new ProjectileObject(player.getX() + 10, player.getY() + 10, 20, angle + spread, damage, 5, handler));
				AudioPlayer.getSound("Pistol").play(1.0f, 0.25f);
			}
			else if (gunType == GUN.Rifle) {
				double spread = (r.nextDouble() - 0.5) * 3 * PI / 180;
				handler.addObject(
						new ProjectileObject(player.getX() + 10, player.getY() + 10, 30, angle + spread, damage, 17.5, handler));
				AudioPlayer.getSound("Rifle").play(1.0f, 0.25f);
			}
			else if (gunType == GUN.Shotgun) {
				for (int i = 0; i < 9; i++) {
					double spread = (r.nextDouble() - 0.5) * 9 * PI / 180;
					handler.addObject(
							new ProjectileObject(player.getX() + 10, player.getY() + 10, 15, angle + spread, damage, 6,handler));
				}
				AudioPlayer.getSound("Shotgun").play(1.0f, 0.25f);
			}
			
			ammoLoaded--;
		} else if (ammoExtra > 0) reload();
	}
	
	public void reload() {
		if (!waitingOnReload) {
			if (ammoExtra > 0 && ammoLoaded < magSize) {
				AudioPlayer.getSound("ReloadPistol").play(1f, 0.75f);
				waitingOnReload = true;
				timer = System.currentTimeMillis();
			}
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
		if(!(ammoLoaded > 0))
			reload();
		
		if(waitingOnReload) {
			long timeElapsed = System.currentTimeMillis() - timer;
			if((timeElapsed) > reloadTime) {				
				reloadFinish();
			}
		}
	}
	
	public double getDamage() {return damage;}
	public int getMagSize() {return magSize;}
	public int getAmmoLoaded() {return ammoLoaded;}
	public int getAmmoExtra() {return ammoExtra;}
	public String getName() {return gunName;}

	public void setDamage(double dmg) {damage = dmg;}
	public void setMagSize(int mag) {magSize = mag;}
	public void setAmmoLoaded(int ammo) {ammoLoaded = ammo;}
	public void setAmmoExtra(int ammo) {ammoExtra = ammo;}

}
