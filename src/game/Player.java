package game;

import static java.lang.Math.atan2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Program.STATE;
import game.Weapons.AR15;
import game.Weapons.Gun;
import game.Weapons.Gun.GUN;
import game.Weapons.M77;
import game.Weapons.Model57;
import game.Weapons.OverUnder;
import game.Weapons.PX4Compact;
import game.Weapons.Titan;

public class Player extends GameObject{
	
	private final static int NUMSPRITECYCLES = 8;
	private final static int NUMGUNS = 6;
	private Gun gunWielded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private ArrayList<Gun> arsenal;
	private Reticle reticle;
	private double angle;
	private double speed;
	private byte tickDivider;
	private static transient BufferedImage spriteSheet;
	private static transient BufferedImage[][] playerSprites = new BufferedImage [NUMSPRITECYCLES][NUMGUNS];
	private int money, moneyAtRoundStart;
	private int spriteNum, gunNum;
	private int level;
	public int zombiesLeft;
	
	public Player(double x, double y) {
		super(x, y);
		Gun.setPlayer(this);
		Gun.setHandler(Program.handler);
		arsenal = new ArrayList<Gun>();
		arsenal.add(new AR15());
		arsenal.add(new M77());
		arsenal.add(new OverUnder());
		arsenal.add(new Model57());
		arsenal.add(new PX4Compact());
		arsenal.add(new Titan());
		gunSidearm = searchGun(GUN.Titan);
		gunSidearm.setOwned(true);
		gunWielded = gunSidearm;
		tickDivider = 0;
		spriteNum = 0;
		money = 0;
		moneyAtRoundStart = 0;
		speed = 2;
		reticle = null;
		level = 1;
		loadSprites();
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 20, 20);
	}
	
	public Polygon getSightBounds() {
		return new Polygon(
				new int[]{(int)x + 10, (int)(x + 10 + 220 * Math.sin(angle - 0.075)), (int)(x + 10 + 220 * Math.sin(angle + 0.075))}, 
				new int[]{(int)y + 10, (int)(y + 10 + 220 * Math.cos(angle - 0.075)), (int)(y + 10 + 220 * Math.cos(angle + 0.075))},
				3);
	}

	public void tick() {
		
		if (velX != 0 && velY != 0) {
			x += (HALFSQRT2*velX);
			y += (HALFSQRT2*velY);
		}
		
		else {
			x += velX;
			y += velY;
		}
		
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-26);
		
		angle = atan2(reticle.getXDisplay() - (x + 10), reticle.getYDisplay() - (y + 10));
		
		gunWielded.tick();
		
		if (tickDivider%4 == 0) {
			detectCollision();
			
			switch(gunWielded.getId()) {
			case Titan:			gunNum = 0;
				break;
			case PX4Compact:	gunNum = 1;
				break;
			case Model57:		gunNum = 2;
				break;
			case AR15: 			gunNum = 3;
				break;
			case OverUnder:		gunNum = 4;
				break;
			case M77:			gunNum = 5;
				break;
			default: 			gunNum = 0;
				break;
			}
			
			if (velX != 0 || velY != 0) spriteNum++;
			
			spriteNum = spriteNum % 8;
		}
		
		tickDivider++;	
	}
	
	public void detectCollision() {
		for(int i = 2; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj instanceof Zombie) {
				Zombie zomb = (Zombie)obj;
				if(zomb.getBounds().intersects(this.getBounds())) {
					Program.gameState = STATE.GameOver;
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.rotate(-angle, x + 10, y + 10);
		g2d.drawImage(playerSprites[spriteNum][gunNum], (int)x, (int)y, null);
		g2d.rotate(angle, x + 10, y + 10);
	}

	public Gun getGunWielded() {return gunWielded;}
	public Gun getGunPrimary() {return gunPrimary;}
	public Gun getGunSecondary() {return gunSecondary;}
	public Gun getGunSidearm() {return gunSidearm;}
	public ArrayList<Gun> getArsenal() {return arsenal;}
	public double getAngle() {return angle;}
	public double getSpeed() {return speed;}
	public int getMoney() {return money;}
	public int getMoneyAtRoundStart() {return moneyAtRoundStart;}
	public int getLevel() {return level;}
	public Gun getGunAt(int i) {return arsenal.get(i);}
	public int getGunWeildedIndex() {
		if(gunWielded == gunPrimary) return 0;
		if(gunWielded == gunSecondary) return 1;
		if(gunWielded == gunSidearm) return 2;
		return 0;
	}
	
	public void setGunPrimary(Gun g) {this.gunPrimary = g;}
	public void setGunSecondary(Gun g) {this.gunSecondary = g;}
	public void setGunSidearm(Gun g) {this.gunSidearm = g;}
	public void setAngle(double angle) {this.angle = angle;}
	public void setArsenal(ArrayList<Gun> arsenal) {this.arsenal = arsenal;}
	public void setSpeed(double speed) {this.speed = speed;}
	public void setMoney(int money) {this.money = money;}
	public void setMoneyAtRoundStart(int money) {this.moneyAtRoundStart = money;}
	public void setLevel(int level) {this.level = level;}
	
	public Gun searchGun(String name) {
		for(Gun g : arsenal) {
			if(g.getName().equals(name)) return g;
		}
		return null;
	}
	public Gun searchGun(GUN id) {
		for(Gun g : arsenal) {
			if(g.getId() == id) return g;
		}
		return null;
	}
	public void switchToPrimary() {
		if (gunPrimary != null && gunWielded != gunPrimary) {
			gunWielded.swapGun();
			gunWielded = gunPrimary;
		}
	}
	public void switchToSecondary() {
		if (gunSecondary != null && gunWielded != gunSecondary) {
			gunWielded.swapGun();
			gunWielded = gunSecondary;
		}
	}
	public void switchToSidearm() {
		if (gunSidearm != null && gunWielded != gunSidearm) {
			gunWielded.swapGun();
			gunWielded = gunSidearm;
		}
	}
	public void resetAllAmmo() {
		for(Gun g : arsenal) {
			g.resetAmmo();
		}
	}
	
	public void autoEquip() {
		gunPrimary = gunSecondary = gunSidearm = gunWielded = null;
		for (Gun g : arsenal) {
			if (g.isOwned() && g.isSidearm() && !g.isEquipped()) {
				gunSidearm = g;
				break;
			}
		}
		for (Gun g : arsenal) {
			if (g.isOwned() && !g.isEquipped()) {
				gunPrimary = g;
				break;
			}
		}
		for (Gun g : arsenal) {
			if (g.isOwned() && !g.isEquipped()) {
				gunSecondary = g;
				break;
			}
		}
		autoWield();
	}
	
	public void autoWield() {
		if (gunPrimary != null) gunWielded = gunPrimary;
		else if (gunSecondary != null) gunWielded = gunSecondary;
		else if (gunSidearm != null) gunWielded = gunSidearm;
	}
	
	public void equipPrimary(Gun g) {
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunPrimary = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public void equipSecondary(Gun g) {
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunSecondary = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public void equipSidearm(Gun g) {
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunSidearm = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public boolean isEquipped(Gun g) {
		return g == gunPrimary
			|| g == gunSecondary
			|| g == gunSidearm;
	}
	
	public void unselectAll() {
		for (Gun g : arsenal) {
			g.unLock();
		}
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
	
	public void loadSprites() {
		try {
			FileInputStream file = new FileInputStream("res/PlayerSprite.png");
			spriteSheet = ImageIO.read(file);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < NUMSPRITECYCLES; i++) {
			for(int j = 0; j < NUMGUNS; j++) {
				playerSprites[i][j] = spriteSheet.getSubimage(20 * i, 32 * j, 20, 32);
			}
		}
	}
	
	public void addReticle(Reticle r) {
		reticle = r;
	}
}
