package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import game.Gun.GUN;
import game.Program.STATE;

public class PlayerObject extends GameObject{// implements Serializable{
	
	private static final long serialVersionUID = 2544512233426538755L;
	private Gun gunWeilded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private LinkedList<Gun> arsenal;
	private double angle;
	private double speed;
	private byte tickDivider;
	private static transient BufferedImage spriteSheet;
	private static transient BufferedImage[][] playerSprites = new BufferedImage [8][3];
	private int money, moneyAtRoundStart;
	private int spriteNum, gunNum;
	private int level;
	
	public PlayerObject(double xPos, double yPos, Handler h) {
		super(xPos, yPos, ObjectType.Player, h);
		gunSidearm = new Gun("Titan", GUN.Pistol, 7, 42, 22, false, this, h);
		gunPrimary = new Gun("AR-15", GUN.Rifle, 30, 30, 35, false, this, h); 
		gunSecondary = new Gun("Over-Under", GUN.Shotgun, 2, 10, 40, false, this, h);
		arsenal = new LinkedList<Gun>();
		arsenal.add(gunPrimary);
		arsenal.add(gunSecondary);
		arsenal.add(gunSidearm);
		gunWeilded = gunSidearm;
		tickDivider = 0;
		spriteNum = 0;
		money = 0;
		moneyAtRoundStart = 0;
		speed = 2;
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
					//handler.removeObject(this);
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
	
	public void setAngle(double a) {angle = a;}
	public void setArsenal(LinkedList<Gun> l) {arsenal = l;}
	public void setSpeed(double s) {speed = s;}
	public void setMoney(int m) {money = m;}
	public void setMoneyAtRoundStart(int m) {moneyAtRoundStart = m;}
	public void setLevel(int l) {level = l;}
	
	public void setGuns() {
		gunPrimary = arsenal.get(0);
		gunSecondary = arsenal.get(1);
		gunSidearm = arsenal.get(2);
		gunWeilded = gunSidearm;
	}
	
	public void switchToPrimary() {
		if(gunWeilded != gunPrimary) 
			gunWeilded.swapGun();
			gunWeilded = gunPrimary;
	}
	public void switchToSecondary() {
		if(gunWeilded != gunSecondary)
			gunWeilded.swapGun();
			gunWeilded = gunSecondary;
	}
	public void switchToSidearm() {
		if(gunWeilded != gunSidearm)
			gunWeilded.swapGun();
			gunWeilded = gunSidearm;
	}
	public void resetAllAmmo() {
		for(int i = 0; i < arsenal.size(); i++) {
			arsenal.get(i).resetAmmo();
		}
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
}
