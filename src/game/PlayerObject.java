package game;

import static java.lang.Math.atan2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import game.Gun.GUN;
import game.Program.STATE;

public class PlayerObject extends GameObject{
	
	private Gun gunWielded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private ArrayList<Gun> arsenal;
	private ReticleObject reticle;
	private double angle;
	private double speed;
	private byte tickDivider;
	private static transient BufferedImage spriteSheet;
	private static transient BufferedImage[][] playerSprites = new BufferedImage [8][3];
	private int money, moneyAtRoundStart;
	private int spriteNum, gunNum;
	private int level;
	public int zombiesLeft;
	
	public PlayerObject(double x, double y, Handler handler) {
		super(x, y, handler);
		Gun.setPlayer(this);
		Gun.setHandler(handler);
		arsenal = new ArrayList<Gun>();
		arsenal.add(new Gun(GUN.AR15));
		arsenal.add(new Gun(GUN.M77));
		arsenal.add(new Gun(GUN.OverUnder));
		arsenal.add(new Gun(GUN.PX4Compact));;
		arsenal.add(new Gun(GUN.Titan));
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
		y = Program.clamp(y, 0, Program.HEIGHT-48);
		
		angle = atan2(reticle.getX() - (x + 10), reticle.getY() - (y + 10));
		
		gunWielded.tick();
		
		if (tickDivider%8 == 0) {
			detectCollision();
			
			switch(gunWielded.getId()) {
			case Titan:
			case PX4Compact:
				gunNum = 0; 		
				break;
			case AR15: 
				gunNum = 1;
				break;
			case OverUnder:
			case M77:
				gunNum = 2;
				break;
			default: 
				gunNum = 0;
				break;
			}
			
			if(velX != 0 || velY != 0)
				spriteNum++;
			
			spriteNum = spriteNum % 8;
		}
		
		tickDivider++;	
	}
	
	public void detectCollision()
	{
		for(int i = 2; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj instanceof ZombieObject) {
				ZombieObject zomb = (ZombieObject)obj;
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
		for(int i = 0; i < arsenal.size(); i++) {
			arsenal.get(i).resetAmmo();
		}
		if (gunPrimary != null) {
			if (gunPrimary.isOwned()) gunWielded = gunPrimary;
			else gunPrimary = null;
		}
		else if (gunSecondary != null) {
			if (gunSecondary.isOwned()) gunWielded = gunSecondary;	
			else gunSecondary = null;
		}
		else if (gunSidearm != null) {
			if (gunSidearm.isOwned()) gunWielded = gunSidearm;
			else gunSidearm = null;
		}
	}
	
	public void equipPrimary(String name) {
		Gun g = searchGun(name);
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunPrimary = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public void equipSecondary(String name) {
		Gun g = searchGun(name);
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunSecondary = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public void equipSidearm(String name) {
		Gun g = searchGun(name);
		if (g.isOwned() && !g.isLockedIn()) {
			if (g.isEquipped()) g.unequip();
			gunSidearm = g;
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
		}
	}
	
	public boolean isEquipped(String name) {
		Gun g = searchGun(name);
		return g == gunPrimary
			|| g == gunSecondary
			|| g == gunSidearm;
	}
	
	public void unselectAll() {
		for (Gun g : arsenal) {
			g.unLock();
		}
	}
	
	public void unequip(String name) {
		switch(getIndexOfGun(name)) {
		case 0: gunPrimary = null;
			break;
		case 1: gunSecondary = null;
			break;
		case 2: gunSidearm = null;
			break;
		default:
			break;
		}
		searchGun(name).unLock();
	}
	
	public int getIndexOfGun(String name) {
		Gun g = searchGun(name);
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
			System.out.println(e.getMessage());
		}
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 3; j++) {
				playerSprites[i][j] = spriteSheet.getSubimage(20 * i, 32 * j, 20, 32);
			}
		}
	}
	
	public void addReticle(ReticleObject r) {
		reticle = r;
	}
}
