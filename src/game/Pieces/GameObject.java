package game.Pieces;

import java.awt.Graphics;

import game.Handler;
import game.Program;

import static java.lang.Math.sqrt;

public abstract class GameObject {

	protected double x, y, velX, velY;
	public static final double HALFSQRT2 = sqrt(2)/2;
	public static final double SQRT2 = sqrt(2);
	protected Handler handler;
	
	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
		this.handler = Program.handler;
		velX = velY = 0;
	}
	
	public GameObject() {}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	//Getters and setters
	public void setX(double x) 			{this.x = x;}
	public void setY(double y) 			{this.y = y;}
	public void setVelX(double velX) 	{this.velX = velX;}
	public void setVelY(double velY) 	{this.velY = velY;}
	public double getX() 				{return x;}
	public double getY() 				{return y;}
	public double getVelX() 			{return velX;}
	public double getVelY() 			{return velY;}
}
