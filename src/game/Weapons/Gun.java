package game.Weapons;

import static java.lang.Math.sin;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import static java.lang.Math.cos;

import org.newdawn.slick.Sound;

import game.Handler;
import game.Pieces.Player;
import game.Pieces.Projectile;

public abstract class Gun {
	protected double damage, spread;
	protected double velocity, knock;
	protected double reloadFactor;
	protected int xOffset, yOffset; //From center of player
	protected int ammoLoaded, ammoCapacity, ammoExtra, magSize, hits;
	protected boolean owned, lockedIn, isSidearm;
	protected boolean reloading, shooting, chambered;
	protected boolean isFullAuto, specialRounds, isMagIncreased, isReloadImproved;
	protected long reloadTime, chamberTime;
	protected String gunName;
	protected static Player player;
	protected static Handler handler;
	protected Sound reloadSound;
	protected Sound speedReloadSound;
	protected float reloadSoundPosition;
	protected long ticks;
	protected long reloadTicks, chamberTicks;
	protected Random r;
	protected GUN gunId;
	public static enum GUN {
		Titan,
		PX4Compact,
		Judge,
		Model57,
		OverUnder,
		Model12,
		M77,
		AR15,
	}
	protected static BufferedImage gunSheet;
	protected BufferedImage gunSprite;
	
	public Gun() {
		r = new Random();
		hits = 1;
		reloadFactor = 1.0;
		gunSprite = gunSheet.getSubimage(
				0,
				512 * Player.getGunSpriteNum(this),
				1024,
				512
		);
	}
	
	public abstract void shoot();
	
	public abstract void makeRoundSpecial(Projectile p);
	
	public void reload() {
		if (!reloading && ammoExtra > 0 && ammoLoaded < magSize) {
			if (isReloadImproved) speedReloadSound.play(1f, 1f);
			else reloadSound.play(1f, 1f);
			reloading = true;
		}
	}
	
	protected void reloadFinish() {
		int reloadAmount = magSize - ammoLoaded;

		if (ammoExtra >= reloadAmount) {
			ammoLoaded += reloadAmount;
			ammoExtra -= reloadAmount;
		} else {
			ammoLoaded += ammoExtra;
			ammoExtra = 0;
		}
		
		reloading = false;
		reloadTicks = 0;
	}
	
	private void chamberFinish() {
		chambered = true;
		chamberTicks = 0;
	}
	
	public void tick() {
		reloadIfNeeded();
		
		if (!chambered)	chamberTicks++;
		if (reloading) reloadTicks++;
		if (chamberTicks > ticksForChamber()) chamberFinish();
		if (reloadTicks > ticksForReload()) reloadFinish();
	}
	
	public void resetAmmo() {
		ammoLoaded = magSize;
		ammoExtra = ammoCapacity;
		shooting = reloading = false;
		chambered = true;
		chamberTicks = reloadTicks = ticks = 0;
		stopReloadSound();
	}
	
	public void onSwapFrom() {
		stopReloadSound();
		reloading = false;
		chamberTicks = reloadTicks = 0;
	}
	
	public void onSwapTo() {}
	
	
	protected void stopReloadSound() {
		if (reloadSound != null && reloadSound.playing()) reloadSound.stop();
		if (speedReloadSound != null && speedReloadSound.playing()) speedReloadSound.stop();
	}
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	public double muzzlePointX() {
		return offsetPointX(xOffset, yOffset);
	}
	
	public double offsetPointX(double xo, double yo) {
		return player.getX() + 10 + xo*cos(player.getAngle()) + yo*sin(player.getAngle());
	}
	
	/**
	 * This function takes in the location of the muzzle of the gun 
	 * relative to the center of the player sprite and returns the
	 * X value corrected for player rotation.
	 */
	public double muzzlePointY() {
		return offsetPointY(xOffset, yOffset);
	}
	
	public double offsetPointY(double xo, double yo) {
		return player.getY() + 10 + yo*cos(player.getAngle()) - xo*sin(player.getAngle());
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
	public boolean isReloading() {return reloading;}
	public boolean isReloadImproved() {return isReloadImproved;}
	public boolean isWielded() {return this == player.getGunWielded();}
	public String getName() {return gunName;}
	public GUN getId() {return gunId;}
	public BufferedImage getSprite() {return gunSprite;}

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
	public void setReloadImproved(boolean isReloadImproved) {this.isReloadImproved = isReloadImproved;}
	public static void setPlayer(Player player) {Gun.player = player;}
	public static void setHandler(Handler handler) {Gun.handler = handler;}
	public void resetTickDivier() {ticks = 0;}
	public void lockIn() {lockedIn = true;}
	public void unLock() {lockedIn = false;}
	
	// These are wrapper functions that interact with this gun in relation to the player
	public int getIndexOf() {return player.getIndexOfGun(this);}
	public boolean isEquipped() {return player.isEquipped(this);}
	public void unequip() {player.unequip(this);}
	
	protected boolean canShoot() {
		return ammoLoaded > 0 && !reloading && chambered;
	}
	
	protected void reloadIfNeeded() {
		if (ammoExtra > 0 && ammoLoaded <= 0) reload();
	}
	
	protected void onShotFired() {
		ammoLoaded--;
		chambered = false;
		reloadIfNeeded();
	}
	
	protected int ticksForReload() {
		return (int) ((60f / 1000) * reloadTime * (isReloadImproved ? reloadFactor : 1));
	}
	
	protected int ticksForChamber() {
		return (int) ((60f / 1000) * chamberTime);
	}
	
	static {
		try {
			FileInputStream file = new FileInputStream("./res/GunSprite.png");
			gunSheet = ImageIO.read(file);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
