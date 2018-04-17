package game;

import java.awt.Graphics;
import static java.lang.Math.sqrt;

public abstract class GameObject {
	protected int x, y, velX, velY;
	protected ObjectType type;
	public static final double HALFSQRT2 = sqrt(2)/2;
	protected Handler handler;
	
	public GameObject(int xPos, int yPos, ObjectType objType, Handler h) {
		x = xPos;
		y = yPos;
		type = objType;
		velX = velY = 0;
		handler = h;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	
	//Getters and setters
	public void setX(int val) 		{x = val;}
	public void setY(int val) 		{y = val;}
	public void setVelX(int val) 	{velX = val;}
	public void setVelY(int val) 	{velY = val;}
	public int getX() 				{return x;}
	public int getY() 				{return y;}
	public int getVelX() 			{return velX;}
	public int getVelY() 			{return velY;}
	public ObjectType getType() 	{return type;}
}
