package game;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler{
	LinkedList<GameObject> gameObjs = new LinkedList<GameObject>();
	
	public void tick() {
		for(GameObject obj : gameObjs) {
			obj.tick();
		}
	}
	
	public void render(Graphics g) {
		for(GameObject obj : gameObjs) {
			obj.render(g);
		}
	}
	
	public void addObject(GameObject obj) 		{gameObjs.add(obj);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
}