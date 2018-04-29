package game;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;
import static java.lang.Math.abs;

/*
 * This class contains the list of game objects and ticks
 * and renders each them.
 */
public class Handler {
	
	private LinkedList<GameObject> gameObjs; 
	
	public Handler() {
		gameObjs = new LinkedList<GameObject>();
	}
	
	public void tick() {
		for(int i = 0; i < gameObjs.size(); i++) {
			GameObject obj = getObjectAt(i);
			obj.tick();
		}
	}
	
	public void render(Graphics g) {
		for(int i = gameObjs.size()-1; i >= 0; i--) {
			GameObject obj = getObjectAt(i);
			obj.render(g);
		}
	}

	public void removeProjectiles() {
		for(int i = gameObjs.size() - 1; i >= 0; i--) {
			GameObject obj = getObjectAt(i);
			if(obj.getType() == ObjectType.Projectile) {
				removeObject(obj);
			}
		}
	}
	
	public void removeZombies() {
		for(int i = gameObjs.size() - 1; i >= 0; i--) {
			GameObject obj = getObjectAt(i);
			if(obj.getType() == ObjectType.Zombie) {
				removeObject(obj);
			}
		}
	}
	
	public void addZombie() {
		Random r = new Random();
		double x,y = 0;
		double xPlayer = gameObjs.get(0).getX();
		double yPlayer = gameObjs.get(0).getY();
		do {
			x = r.nextInt(750);
			y = r.nextInt(550);
		}
		while(abs(x-xPlayer)<200 && abs(y-yPlayer)<200);
		
		addObject(new ZombieObject(x,y,this, r.nextDouble()/5+1));
		
	}
	
	public void addZombie(double x, double y) {
		Random r = new Random();
		addObject(new ZombieObject(x,y,this, r.nextDouble()/5+1.2));
	}
	
	public void addObject(GameObject obj) 		{gameObjs.add(obj);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public LinkedList<GameObject> getObjList()	{return gameObjs;}

}