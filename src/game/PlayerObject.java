package game;

import static java.lang.Math.atan2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import game.Gun.GUN;
import game.Program.STATE;

public class PlayerObject extends GameObject{
	
	private Gun gunWeilded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private LinkedList<Gun> arsenal;
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
	
	public PlayerObject(double xPos, double yPos, Handler h) {
		super(xPos, yPos, ObjectType.Player, h);
		arsenal = new LinkedList<Gun>();
		arsenal.add(new Gun("AR-15", GUN.AR15, 30, 30, 35, true, this, h));
		arsenal.add(new Gun("M77", GUN.M77, 3, 15, 70, false, this, h));
		arsenal.add(new Gun("Over-Under", GUN.OverUnder, 2, 10, 40, false, this, h));
		arsenal.add(new Gun("PX4 Compact", GUN.PX4Compact, 15, 30, 31, false, this, h));;
		arsenal.add(new Gun("Titan", GUN.Titan, 7, 56, 22, false, this, h));
		gunSidearm = searchGun("Titan");
		gunPrimary = searchGun("AR-15"); 
		gunSecondary = searchGun("Over-Under");
		gunSidearm.setOwned(true);
		//gunPrimary.setOwned(true);
		//gunSecondary.setOwned(true);
		gunWeilded = gunSidearm;
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
		
		if(velX != 0 && velY != 0) {
			x += (HALFSQRT2*velX);
			y += (HALFSQRT2*velY);
		}
		
		else{
			x += velX;
			y += velY;
		}
		
		x = Program.clamp(x, 0, Program.WIDTH-26);
		y = Program.clamp(y, 0, Program.HEIGHT-48);
		
		angle = atan2(reticle.getX() - (x + 10), reticle.getY() - (y + 10));
		
		gunWeilded.tick();
		
		if (tickDivider%8 == 0) {
			detectCollision();
			
			switch(gunWeilded.getName()) {
			case "Titan": gunNum = 0; 		break;
			case "AR-15": gunNum = 1; 		break;
			case "Over-Under": gunNum = 2;	break;
			default: gunNum = 0; 			break;
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
			if(obj.getType() == ObjectType.Zombie) {
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
			//g2d.fillRect((int)x, (int)y, 20, 20);
			g2d.drawImage(playerSprites[spriteNum][gunNum], (int)x, (int)y, null);
			//g2d.drawImage(img, (int)x, (int)y, null);
			g2d.rotate(angle, x + 10, y + 10);
	}

	public Gun getGun() {return gunWeilded;}
	public LinkedList<Gun> getArsenal() {return arsenal;}
	public double getAngle() {return angle;}
	public double getSpeed() {return speed;}
	public int getMoney() {return money;}
	public int getMoneyAtRoundStart() {return moneyAtRoundStart;}
	public int getLevel() {return level;}
	public int getGunIndex() {
		if(gunWeilded == gunPrimary) return 0;
		else if(gunWeilded == gunSecondary) return 1;
		else if(gunWeilded == gunSidearm) return 2;
		else return 0;
	}
	
	public void setAngle(double a) {angle = a;}
	public void setArsenal(LinkedList<Gun> l) {arsenal = l;}
	public void setSpeed(double s) {speed = s;}
	public void setMoney(int m) {money = m;}
	public void setMoneyAtRoundStart(int m) {moneyAtRoundStart = m;}
	public void setLevel(int l) {level = l;}
	
	public Gun searchGun(String n) {
		Gun g = null;
		for(int i = 0; i < arsenal.size(); i++) {
			g = arsenal.get(i);
			if(g.getName().equals(n))
				return g;
		}
		return null;
	}
	
	public void setGuns() {
		try {
			gunPrimary = arsenal.get(0);
			gunSecondary = arsenal.get(1);
			gunSidearm = arsenal.get(2);
			gunWeilded = gunSidearm;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void switchToPrimary() {
		if(gunWeilded != gunPrimary && gunPrimary.getOwned()) {
			gunWeilded.swapGun();
			gunWeilded = gunPrimary;
		}

	}
	public void switchToSecondary() {
		if(gunWeilded != gunSecondary && gunSecondary.getOwned()) {
			gunWeilded.swapGun();
			gunWeilded = gunSecondary;
		}
	}
	public void switchToSidearm() {
		if(gunWeilded != gunSidearm && gunSidearm.getOwned()) {
			gunWeilded.swapGun();
			gunWeilded = gunSidearm;
		}
	}
	public void resetAllAmmo() {
		for(int i = 0; i < arsenal.size(); i++) {
			arsenal.get(i).resetAmmo();
		}
		if(gunPrimary.getOwned())
			gunWeilded = gunPrimary;
		else if(gunSecondary.getOwned())
			gunWeilded = gunSecondary;
		else 
			gunWeilded = gunSidearm;
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
