package game;

import java.awt.Graphics;

public abstract class GameObject {
	protected int x, y, velX, velY;
	protected ObjectType type;
	
	public GameObject(int xPos, int yPos, ObjectType objType) {
		x = xPos;
		y = yPos;
		type = objType;
		velX = velY = 0;
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
