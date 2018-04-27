package game;

import java.awt.Graphics;
import java.io.Serializable;

import static java.lang.Math.sqrt;

public abstract class GameObject implements Serializable{

	private static final long serialVersionUID = 3699691857953926475L;
	protected double x, y, velX, velY;
	protected ObjectType type;
	public static final double HALFSQRT2 = sqrt(2)/2;
	protected static Handler handler;
	
	public GameObject(double xPos, double yPos, ObjectType objType, Handler h) {
		x = xPos;
		y = yPos;
		type = objType;
		velX = velY = 0;
		handler = h;
	}
	
	public GameObject() {
		
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	
	//Getters and setters
	public void setX(double val) 		{x = val;}
	public void setY(double val) 		{y = val;}
	public void setVelX(double val) 	{velX = val;}
	public void setVelY(double val) 	{velY = val;}
	public double getX() 				{return x;}
	public double getY() 				{return y;}
	public double getVelX() 			{return velX;}
	public double getVelY() 			{return velY;}
	public ObjectType getType() 	{return type;}
}
