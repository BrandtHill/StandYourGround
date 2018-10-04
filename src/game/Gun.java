package game;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import java.awt.Color;

import static java.lang.Math.cos;

import java.util.Random;

import org.newdawn.slick.Sound;

public class Gun {

	private double damage;
	private int ammoLoaded, ammoCapacity, ammoExtra, magSize;
	private boolean owned, lockedIn, isSidearm, specialRounds;
	private boolean waitingOnReload, isFullAuto, shooting, chambered;
	private long reloadTime, chamberTime;
	private long timerReload, timerChamber;
	private String gunName;
	private static Player player;
	private static Handler handler;
	private transient Sound reloadSound;
	private long tickDivider;
	private GUN gunId;
	public static enum GUN {
		AR15,
		OverUnder,
		M77,
		Titan,
		PX4Compact
	}
	
	public Gun(GUN id) {
		
		gunId = id;
		
		switch (gunId) {
		case AR15:
			reloadSound = AudioPlayer.getSound("ReloadAR15");
			reloadTime = 2750;
			chamberTime = 67;
			gunName = "AR-15";
			ammoLoaded = magSize = 30;
			ammoExtra = ammoCapacity = 30;
			damage = 35;
			break;
		case M77:
			reloadSound = AudioPlayer.getSound("ReloadM77");
			reloadTime = 3000;
			chamberTime = 1250;
			gunName = "M77";
			ammoLoaded = magSize = 3;
			ammoExtra = ammoCapacity = 15;
			damage = 110;
			break;
		case OverUnder:
			reloadSound = AudioPlayer.getSound("ReloadOverUnder");
			reloadTime = 2750;
			chamberTime = 250;
			gunName = "Over-Under";
			ammoLoaded = magSize = 2;
			ammoExtra = ammoCapacity = 10;
			damage = 29;
			break;
		case PX4Compact:
			reloadSound = AudioPlayer.getSound("ReloadPX4");
			reloadTime = 2000;
			chamberTime = 50;
			gunName = "PX4 Compact";
			ammoLoaded = magSize = 15;
			ammoExtra = ammoCapacity = 30;
			damage = 31;
			isSidearm = true;
			break;
		case Titan:
			reloadSound = AudioPlayer.getSound("ReloadTitan");
			reloadTime = 2000;
			chamberTime = 50;
			gunName = "Titan";
			ammoLoaded = magSize = 7;
			ammoExtra = ammoCapacity = 56;
			damage = 22;
			isSidearm = true;
			break;
		default:
			break;
		
		}
	}
	
	public void shoot(double angle) {
		if (ammoLoaded > 0 && !waitingOnReload && chambered) {
			Random r = new Random();
			double spread;
			
			switch(gunId) {
				
			case Titan:
				spread = (r.nextDouble() - 0.5) * 7 * PI / 180;
				handler.addObject(
						new Projectile(muzzlePointX(-2, 10), muzzlePointY(-2, 10), 20, angle + spread, damage, 5, handler));
				AudioPlayer.getSound("Pistol").play(1.2f, 0.25f);
				break;
				
			case PX4Compact:
				spread = (r.nextDouble() - 0.5) * 5 * PI / 180;
				double newDmg = damage * (isSpecialRounds() ? 1.5 : 1.0);
				Projectile p = new Projectile(muzzlePointX(-2, 10), muzzlePointY(-2, 10), 23, angle + spread, newDmg, 7, handler);
				if (isSpecialRounds()) p.color = new Color(243, 144, 0);
				handler.addObject(p);
				AudioPlayer.getSound("Pistol").play(0.95f, 0.3f); 
				break;
				
			case AR15:
				spread = (r.nextDouble() - 0.5) * 3 * PI / 180;
				handler.addObject(
						new Projectile(muzzlePointX(-3, 19), muzzlePointY(-3, 19), 30, angle + spread, damage, 17.5, handler));
				AudioPlayer.getSound("Rifle").play(1.0f, 0.3f);
				break;
			
			case OverUnder: 
				for (int i = 0; i < 9; i++) {
					spread = (r.nextDouble() - 0.5) * 9 * PI / 180;
					handler.addObject(
							new Projectile(muzzlePointX(-3, 21), muzzlePointY(-3, 21), 15, angle + spread, damage, 6, handler, r.nextInt(3) + 1));
				}
				AudioPlayer.getSound("Shotgun").play(1.0f, 0.30f);
				break;
				
			case M77:
				handler.addObject(
						new Projectile(muzzlePointX(-3, 19), muzzlePointY(-3, 19), 42, angle, damage, 25, handler, 5));
				AudioPlayer.getSound("Sniper").play(1f, 0.4f);
				if(ammoLoaded > 1) 
					AudioPlayer.getSound("CycleM77").play();
				break;
				
			default:
				break;
			
			}
			
			chambered = false;
			timerChamber = System.currentTimeMillis();
			
			ammoLoaded--;
		} else if (ammoExtra > 0 && !(ammoLoaded > 0)) reload();
	}
	
	public void reload() {
		if (!waitingOnReload) {
			if (ammoExtra > 0 && ammoLoaded < magSize) {
				reloadSound.play(1f, 1f);
				waitingOnReload = true;
				timerReload = System.currentTimeMillis();
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
			long timeElapsed = System.currentTimeMillis() - timerReload;
			if((timeElapsed) > reloadTime) {				
				reloadFinish();
			}
		}
		
		if(!chambered) {
			long timeElapsed = System.currentTimeMillis() - timerChamber;
			if((timeElapsed) > chamberTime) {				
				chambered = true;
			}
		}
		
		if (tickDivider%4 == 0) {
			if (shooting && isFullAuto) {
				shoot(player.getAngle());
			} 
		}
		
	}
	
	public void resetAmmo() {
		ammoLoaded = magSize;
		ammoExtra = ammoCapacity;
		shooting = false;
		if (reloadSound != null) {
			if (reloadSound.playing())
				reloadSound.stop();
		}
	}
	
	public void swapGun() {
		if(reloadSound.playing())
			reloadSound.stop();
		waitingOnReload = false;
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	private double muzzlePointX(int relX, int relY) {
		return player.getX() + 10 + relX*cos(player.getAngle()) + relY*sin(player.getAngle());
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	private double muzzlePointY(int relX, int relY) {
		return player.getY() + 10 + relY*cos(player.getAngle()) - relX*sin(player.getAngle());
	}
	
	public double getDamage() {return damage;}
	public int getMagSize() {return magSize;}
	public int getAmmoLoaded() {return ammoLoaded;}
	public int getAmmoExtra() {return ammoExtra;}
	public int getAmmoCapacity() {return ammoCapacity;}
	public boolean isShooting() {return shooting;}
	public boolean isFullAuto() {return isFullAuto;}
	public boolean isOwned() {return owned;}
	public boolean isLockedIn() {return lockedIn;}
	public boolean isSidearm() {return isSidearm;}
	public boolean isSpecialRounds() {return specialRounds;}
	public String getName() {return gunName;}
	public GUN getId() {return gunId;}

	public void setDamage(double damage) {this.damage = damage;}
	public void setMagSize(int magSize) {this.magSize = magSize;}
	public void setAmmoLoaded(int ammoLoaded) {this.ammoLoaded = ammoLoaded;}
	public void setAmmoExtra(int ammoExtra) {this.ammoExtra = ammoExtra;}
	public void setAmmoCapacity(int ammoCapacity) {this.ammoCapacity = ammoCapacity;}
	public void setShooting(boolean shooting) {this.shooting = shooting;}
	public void setFullAuto(boolean fullAuto) {this.isFullAuto = fullAuto;}
	public void setOwned(boolean owned) {this.owned = owned;}
	public void setSpecialRounds(boolean specialRounds) {this.specialRounds = specialRounds;}
	public static void setPlayer(Player player) {Gun.player = player;}
	public static void setHandler(Handler handler) {Gun.handler = handler;}
	public void lockIn() {lockedIn = true;}
	public void unLock() {lockedIn = false;}
	
	// These are wrapper functions that interact with this gun in relation to the player
	public int getIndexOf() {return player.getIndexOfGun(gunName);}
	public boolean isEquipped() {return player.isEquipped(gunName);}
	public void unequip() {player.unequip(gunName);}
	
}
