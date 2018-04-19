package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.Gun.GUN;

public class PlayerObject extends GameObject{
	
	private Gun gunWeilded;
	private Gun gunPrimary;
	private Gun gunSecondary;
	private Gun gunSidearm;
	private double angle;
	
	public PlayerObject(double xPos, double yPos, Handler h) {
		super(xPos, yPos, ObjectType.Player, h);
		gunSidearm = new Gun("Titan", GUN.Pistol, 7, 105, 22, this, h);
		gunPrimary = new Gun("AR-15", GUN.Rifle, 30, 90, 35, this, h); 
		gunSecondary = new Gun("Over-Under", GUN.Shotgun, 2, 18, 40, this, h);
		gunWeilded = gunPrimary;
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
		detectCollision();
		gunWeilded.tick();
	}
	
	public void detectCollision()
	{
		for(int i = 1; i < handler.getObjList().size(); i++) {
			GameObject obj = handler.getObjectAt(i);
			if(obj.getType() == ObjectType.Zombie) {
				ZombieObject zomb = (ZombieObject)obj;
				if(zomb.getBounds().intersects(this.getBounds())) {
					handler.removeObject(this);
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.rotate(-angle, x+10, y+10);
		g2d.fillRect((int)x, (int)y, 20, 20);
		g2d.rotate(angle, x+10, y+10);
	}

	public Gun getGun() {
		return gunWeilded;
	}
	public void switchToPrimary() {
		if(gunWeilded != gunPrimary) 
			gunWeilded = gunPrimary;
	}
	public void switchToSecondary() {
		if(gunWeilded != gunSecondary)
			gunWeilded = gunSecondary;
	}
	public void switchToSidearm() {
		if(gunWeilded != gunSidearm)
			gunWeilded = gunSidearm;
	}

	
	public double getAngle() {return angle;}

	public void setAngle(double a) {angle = a;}
}
