package game;

import java.awt.Graphics;
import java.util.LinkedList;

/*
 * This class contains the list of game objects and ticks
 * and renders each them.
 */
public class Handler{
	LinkedList<GameObject> gameObjs = new LinkedList<GameObject>();
	
	public void tick() {
		for(int i = 0; i < gameObjs.size(); i++) {
			GameObject obj = gameObjs.get(i);
			obj.tick();
			if (obj.getType() == ObjectType.Projectile) {
				ProjectileObject projectile = (ProjectileObject) obj;
				if (projectile.old) removeObject(projectile);
			} 
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < gameObjs.size(); i++) {
			GameObject obj = gameObjs.get(i);
			obj.render(g);
		}
	}
	
	public void addObject(GameObject obj) 		{gameObjs.add(obj);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public LinkedList<GameObject> getObjList()	{return gameObjs;}
}