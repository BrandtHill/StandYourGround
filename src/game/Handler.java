package game;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;
import static java.lang.Math.abs;

/*
 * This class contains the list of game objects and ticks
 * and renders each them.
 */
public class Handler{
	LinkedList<GameObject> gameObjs = new LinkedList<GameObject>();
	
	
	
	public void tick() {
		for(int i = 0; i < gameObjs.size(); i++) {
			GameObject obj = getObjectAt(i);
			obj.tick();
			if (obj.getType() == ObjectType.Projectile) {
				ProjectileObject projectile = (ProjectileObject) obj;
				if (projectile.old) removeObject(projectile);
			} 
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < gameObjs.size(); i++) {
			GameObject obj = getObjectAt(i);
			obj.render(g);
		}
	}
	
	public void addZombie() {
		Random r = new Random();
		int x,y = 0;
		int xPlayer = gameObjs.get(0).getX();
		int yPlayer = gameObjs.get(0).getY();
		do {
			x = r.nextInt(750);
			y = r.nextInt(550);
		}
		while(abs(x-xPlayer)<200 && abs(y-yPlayer)<200);
		
		addObject(new ZombieObject(x,y,this));
		
	}
	
	public void addObject(GameObject obj) 		{gameObjs.add(obj);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public LinkedList<GameObject> getObjList()	{return gameObjs;}

}