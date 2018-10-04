package game.Weapons;

import static java.lang.Math.sin;

import static java.lang.Math.cos;

import org.newdawn.slick.Sound;

import game.Handler;
import game.Player;

public abstract class Gun {

	protected double damage;
	protected int ammoLoaded, ammoCapacity, ammoExtra, magSize;
	protected boolean owned, lockedIn, isSidearm, specialRounds;
	protected boolean waitingOnReload, isFullAuto, shooting, chambered;
	protected long reloadTime, chamberTime;
	protected long timerReload, timerChamber;
	protected String gunName;
	protected static Player player;
	protected static Handler handler;
	protected transient Sound reloadSound;
	protected long tickDivider;
	protected GUN gunId;
	public static enum GUN {
		AR15,
		OverUnder,
		M77,
		Titan,
		PX4Compact
	}
	
	public Gun() {
	}
	
	public abstract void shoot(double angle);
	
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
	protected double muzzlePointX(int relX, int relY) {
		return player.getX() + 10 + relX*cos(player.getAngle()) + relY*sin(player.getAngle());
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	protected double muzzlePointY(int relX, int relY) {
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
