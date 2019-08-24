package game.Weapons;

import static java.lang.Math.sin;

import java.util.Random;

import static java.lang.Math.cos;

import org.newdawn.slick.Sound;

import game.Handler;
import game.Player;
import game.Projectile;

public abstract class Gun {

	protected double damage, spread;
	protected double velocity, knock;
	protected int xOffset, yOffset; //From center of player
	protected int ammoLoaded, ammoCapacity, ammoExtra, magSize, hits;
	protected boolean owned, lockedIn, isSidearm, specialRounds, isMagIncreased;
	protected boolean currentlyReloading, isFullAuto, shooting, chambered;
	protected long reloadTime, chamberTime;
	protected long timerReload, timerChamber;
	protected String gunName;
	protected static Player player;
	protected static Handler handler;
	protected transient Sound reloadSound;
	protected long tickDivider;
	protected Random r;
	protected GUN gunId;
	public static enum GUN {
		AR15,
		OverUnder,
		M77,
		Titan,
		PX4Compact, 
		Model57
	}
	
	public Gun() {
		r = new Random();
		hits = 1;
	}
	
	public abstract void shoot();
	
	public abstract void makeRoundSpecial(Projectile p);
	
	public void reload() {
		if (!currentlyReloading && ammoExtra > 0 && ammoLoaded < magSize) {
			reloadSound.play(1f, 1f);
			currentlyReloading = true;
			timerReload = System.currentTimeMillis();
		}
	}
	private void reloadFinish() {
		int reloadAmount = magSize - ammoLoaded;

		if (ammoExtra >= reloadAmount) {
			ammoLoaded += reloadAmount;
			ammoExtra -= reloadAmount;
		} else {
			ammoLoaded += ammoExtra;
			ammoExtra = 0;
		}
		
		currentlyReloading = false;
	}
	
	public void tick() {
		reloadIfNeeded();
		
		if (currentlyReloading && (System.currentTimeMillis() - timerReload) > reloadTime) {			
			reloadFinish();
		}
		
		if(!chambered && (System.currentTimeMillis() - timerChamber) > chamberTime) {
			chambered = true;
		}
	}
	
	public void resetAmmo() {
		ammoLoaded = magSize;
		ammoExtra = ammoCapacity;
		shooting = false;
		if (reloadSound.playing()) reloadSound.stop();
	}
	
	public void swapGun() {
		if (reloadSound.playing()) reloadSound.stop();
		currentlyReloading = false;
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	public double muzzlePointX() {
		return player.getX() + 10 + xOffset*cos(player.getAngle()) + yOffset*sin(player.getAngle());
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	public double muzzlePointY() {
		return player.getY() + 10 + yOffset*cos(player.getAngle()) - xOffset*sin(player.getAngle());
	}
	
	public double getDamage() {return damage;}
	public double getSpread() {return spread;}
	public double getVelocity() {return velocity;}
	public double getKnock() {return knock;}
	public int getMagSize() {return magSize;}
	public int getAmmoLoaded() {return ammoLoaded;}
	public int getAmmoExtra() {return ammoExtra;}
	public int getAmmoCapacity() {return ammoCapacity;}
	public int getHits() {return hits;}
	public boolean isShooting() {return shooting;}
	public boolean isFullAuto() {return isFullAuto;}
	public boolean isOwned() {return owned;}
	public boolean isLockedIn() {return lockedIn;}
	public boolean isSidearm() {return isSidearm;}
	public boolean isSpecialRounds() {return specialRounds;}
	public boolean isMagIncreased() {return isMagIncreased;}
	public String getName() {return gunName;}
	public GUN getId() {return gunId;}

	public void setDamage(double damage) {this.damage = damage;}
	public void setSpread(double spread) {this.spread = spread;}
	public void setMagSize(int magSize) {this.magSize = magSize;}
	public void setAmmoLoaded(int ammoLoaded) {this.ammoLoaded = ammoLoaded;}
	public void setAmmoExtra(int ammoExtra) {this.ammoExtra = ammoExtra;}
	public void setAmmoCapacity(int ammoCapacity) {this.ammoCapacity = ammoCapacity;}
	public void setShooting(boolean shooting) {this.shooting = shooting;}
	public void setFullAuto(boolean fullAuto) {this.isFullAuto = fullAuto;}
	public void setOwned(boolean owned) {this.owned = owned;}
	public void setSpecialRounds(boolean specialRounds) {this.specialRounds = specialRounds;}
	public void setMagIncreased(boolean isMagIncreased) {this.isMagIncreased = isMagIncreased;}
	public static void setPlayer(Player player) {Gun.player = player;}
	public static void setHandler(Handler handler) {Gun.handler = handler;}
	public void resetTickDivier() {tickDivider = 0;}
	public void lockIn() {lockedIn = true;}
	public void unLock() {lockedIn = false;}
	
	// These are wrapper functions that interact with this gun in relation to the player
	public int getIndexOf() {return player.getIndexOfGun(this);}
	public boolean isEquipped() {return player.isEquipped(this);}
	public void unequip() {player.unequip(this);}
	
	protected boolean canShoot() {
		return ammoLoaded > 0 && !currentlyReloading && chambered;
	}
	
	protected void reloadIfNeeded() {
		if (ammoExtra > 0 && ammoLoaded <= 0) reload();
	}
	
	protected void onShotFired() {
		ammoLoaded--;
		chambered = false;
		timerChamber = System.currentTimeMillis();
	}
	
}
