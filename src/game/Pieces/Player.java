package game.Pieces;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import game.Handler;
import game.Program;
import game.Audio.AudioPlayer;
import game.Program.STATE;
import game.Weapons.AR15;
import game.Weapons.Gun;
import game.Weapons.Gun.GUN;
import game.Weapons.Judge;
import game.Weapons.M77;
import game.Weapons.Model12;
import game.Weapons.Model57;
import game.Weapons.OverUnder;
import game.Weapons.PX4Compact;
import game.Weapons.Titan;

public class Player extends GameObject{
	public final static int NUMSPRITECYCLES = 8;
	public final static int NUMGUNS = 8;
	private Gun gunWielded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private List<Gun> arsenal;
	private Map<String, Gun> arsenalStringMap;
	private Map<GUN, Gun> arsenalEnumMap;
	private double angle;
	private double speed;
	private int ticks;
	private static BufferedImage spriteSheet;
	private static BufferedImage[][] playerSprites = new BufferedImage [NUMSPRITECYCLES][NUMGUNS];
	private int money, moneyAtRoundStart;
	private int spriteNum, gunNum;
	public int zombiesLeft;
	
	public Player(double x, double y) {
		super(x, y);
		Gun.setPlayer(this);
		Gun.setHandler(Program.handler);
		arsenalStringMap = new HashMap<>();
		arsenalEnumMap = new HashMap<>();
		arsenal = new ArrayList<>();
		arsenal.add(new AR15());
		arsenal.add(new M77());
		arsenal.add(new Model12());
		arsenal.add(new OverUnder());
		arsenal.add(new Model57());
		arsenal.add(new PX4Compact());
		arsenal.add(new Judge());
		arsenal.add(new Titan());
		arsenal.stream().forEach(g -> {
			arsenalStringMap.put(g.getName(), g);
			arsenalEnumMap.put(g.getId(), g);
		});
		gunSidearm = getGun(GUN.Titan);
		gunSidearm.setOwned(true);
		gunWielded = gunSidearm;
		//money = moneyAtRoundStart = 10000; //For debugging
		speed = 2;
	}
	
	public Rectangle getBounds() {
		return getBounds(0, 0);
	}
	
	private Rectangle getBounds(double xDiff, double yDiff) {
		return new Rectangle((int)(x + xDiff), (int)(y + yDiff), 20, 20);
	}
	
	public Polygon getSightBounds() {
		return new Polygon(
				new int[]{(int)x + 10, (int)(x + 10 + 220 * sin(angle - 0.075)), (int)(x + 10 + 220 * sin(angle + 0.075))}, 
				new int[]{(int)y + 10, (int)(y + 10 + 220 * cos(angle - 0.075)), (int)(y + 10 + 220 * cos(angle + 0.075))},
				3);
	}

	public void tick() {
		double xDiff = (velX == 0 || velY == 0) ? velX : velX * HALFSQRT2;
		double yDiff = (velX == 0 || velY == 0) ? velY : velY * HALFSQRT2;
		
		if (!handler.getObstacles().anyMatch(o -> o.getBounds().intersects(getBounds(xDiff, 0)))) x += xDiff;
		if (!handler.getObstacles().anyMatch(o -> o.getBounds().intersects(getBounds(0, yDiff)))) y += yDiff;
		
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-26);
		
		angle = atan2(Program.reticle.getXDisplay() - (x + 10), Program.reticle.getYDisplay() - (y + 10));
		
		gunWielded.tick();
		
		if (ticks++ % 4 == 0) {
			detectCollision();
			gunNum = getGunSpriteNum(gunWielded);
			if (velX != 0 || velY != 0) spriteNum++;
			spriteNum %= 8;
		}
	}
	
	public static int getGunSpriteNum(Gun g) {
		if (g == null) 					return 0;
		if (g instanceof Titan) 		return 0;
		if (g instanceof PX4Compact)	return 1;
		if (g instanceof Judge) 		return 2;
		if (g instanceof Model57) 		return 3;
		if (g instanceof OverUnder) 	return 4;
		if (g instanceof Model12) 		return 5;
		if (g instanceof M77) 			return 6;
		if (g instanceof AR15) 			return 7;
		return 0;
	}
	
	public void detectCollision() {
		if (handler.getZombies().anyMatch(z -> z.getBounds().intersects(getBounds()))) Program.gameState = STATE.GameOver;
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.rotate(-angle, x + 10, y + 10);
		g2d.drawImage(playerSprites[spriteNum][gunNum], (int)x, (int)y, null);
		g2d.rotate(angle, x + 10, y + 10);
		//g2d.draw(getGridNode());
	}
	
	public void renderPreview(Graphics g, Gun gun) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(Program.backgroundSlice, 260, 360, 450, 150, null);
		g2d.drawImage(playerSprites[1][getGunSpriteNum(gun)], 600, 380, 80, 128, null);
		if (gun != null) g2d.drawImage(gun.getSprite(), 260, 360, 300, 150, null);
	}

	public Gun getGunWielded() {return gunWielded;}
	public Gun getGunPrimary() {return gunPrimary;}
	public Gun getGunSecondary() {return gunSecondary;}
	public Gun getGunSidearm() {return gunSidearm;}
	public Gun getGun(String name) {return arsenalStringMap.get(name);}
	public Gun getGun(GUN id) {return arsenalEnumMap.get(id);}
	public Gun getGunAt(int i) {return arsenal.get(i);}
	public int getGunWeildedIndex() {return getIndexOfGun(gunWielded);}
	public List<Gun> getArsenal() {return arsenal;}
	public double getAngle() {return angle;}
	public double getSpeed() {return speed;}
	public int getMoney() {return money;}
	public int getMoneyAtRoundStart() {return moneyAtRoundStart;}
	public boolean isReloading() {return gunWielded != null && gunWielded.isReloading();}
	
	public void setGunPrimary(Gun g) {this.gunPrimary = g;}
	public void setGunSecondary(Gun g) {this.gunSecondary = g;}
	public void setGunSidearm(Gun g) {this.gunSidearm = g;}
	public void setAngle(double angle) {this.angle = angle;}
	public void setArsenal(List<Gun> arsenal) {this.arsenal = arsenal;}
	public void setSpeed(double speed) {this.speed = speed;}
	public void setMoney(int money) {this.money = money;}
	public void setMoneyAtRoundStart(int money) {this.moneyAtRoundStart = money;}
	
	public void switchToPrimary() {switchToGun(gunPrimary);}
	public void switchToSecondary() {switchToGun(gunSecondary);}
	public void switchToSidearm() {switchToGun(gunSidearm);}
	
	private void switchToGun(Gun g) {
		if (g != null && gunWielded != g) {
			gunWielded.onSwapFrom();
			gunWielded = g;
			gunWielded.onSwapTo();
		}
	}
	
	public void switchToNext() {
		if (gunWielded == gunSidearm && gunSecondary != null) {
			switchToSecondary();
		} else switchToPrimary();
	}
	
	public void switchToPrevious() {
		if (gunWielded == gunPrimary && gunSecondary != null) {
			switchToSecondary();
		} else switchToSidearm();
	}
	
	public void resetAllAmmo() {
		arsenal.forEach(g -> g.resetAmmo());
	}
	
	public void autoEquip() {
		gunPrimary = gunSecondary = gunSidearm = gunWielded = null;
		
		if (gunPrimary == null && (numPrimariesAvailable() > 0 || numSidearmsAvailable() > 1)) {
			gunPrimary = arsenal.stream().filter(g -> g.isOwned() && !g.isEquipped()).findFirst().get();
		}
		if (gunSecondary == null && (numPrimariesAvailable() > 0 || numSidearmsAvailable() > 1)) {
			gunSecondary = arsenal.stream().filter(g -> g.isOwned() && !g.isEquipped()).findFirst().get();
		}
		if (gunSidearm == null && numSidearmsAvailable() > 0) {
			gunSidearm = arsenal.stream().filter(g -> g.isOwned() && !g.isEquipped() && g.isSidearm()).findFirst().get();
		}
		autoWield();
	}
	
	private int numPrimariesAvailable() {
		return (int) arsenal.stream().filter(g -> g.isOwned() && !g.isEquipped() && !g.isSidearm()).count();
	}
	
	private int numSidearmsAvailable() {
		return (int) arsenal.stream().filter(g -> g.isOwned() && !g.isEquipped() && g.isSidearm()).count();
	}
	
	public void autoWield() {
		if (gunSidearm != null) gunWielded = gunSidearm;
		if (gunSecondary != null) gunWielded = gunSecondary;
		if (gunPrimary != null) gunWielded = gunPrimary;
	}
	
	public void equipPrimary(Gun g) {equipGun(g, 0);}
	public void equipSecondary(Gun g) {equipGun(g, 1);}
	public void equipSidearm(Gun g) {equipGun(g, 2);}
	
	private void equipGun(Gun g, int ref) {
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			if (ref == 0) gunPrimary = g;
			if (ref == 1) gunSecondary = g;
			if (ref == 2) gunSidearm = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public boolean isEquipped(Gun g) {
		return g == gunPrimary
			|| g == gunSecondary
			|| g == gunSidearm;
	}
	
	public void unselectAll() {
		arsenal.forEach(g -> g.unLock());
	}
	
	public void unequip(Gun g) {
		switch(getIndexOfGun(g)) {
		case 0: gunPrimary = null;
			break;
		case 1: gunSecondary = null;
			break;
		case 2: gunSidearm = null;
			break;
		default:
			break;
		}
		g.unLock();
	}
	
	public int getIndexOfGun(Gun g) {
		if (g == gunPrimary) return 0;
		if (g == gunSecondary) return 1;
		if (g == gunSidearm) return 2;
		return -1;
	}
	
	public Rectangle getGridNode() {
		return handler.getGridNode(new Point((int)x + 10, (int)y + 10));
	}
	
	public static void loadAssets() {
		try (FileInputStream fis = new FileInputStream("./res/PlayerSprite.png")) {
			spriteSheet = ImageIO.read(fis);
			fis.close();
			for (int i = 0; i < NUMSPRITECYCLES; i++) {
				for (int j = 0; j < NUMGUNS; j++) {
					playerSprites[i][j] = spriteSheet.getSubimage(20 * i, 32 * j, 20, 32);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
