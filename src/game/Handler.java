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
	private LinkedList<Blood>	   bloodList;
	
	public Handler() {
		gameObjs = new LinkedList<GameObject>();
		bloodList = new LinkedList<Blood>();
		Zombie.loadSprites();
	}
	
	public void tick() {
		for (int i = 0; i < bloodList.size(); i++) {
			bloodList.get(i).tick();
		}
		for (int i = 0; i < gameObjs.size(); i++) {
			getObjectAt(i).tick();
		}
	}
	
	public void render(Graphics g) {
		for (Blood b : bloodList) {
			b.render(g);
		}
		for (int i = gameObjs.size() - 1; i >= 0; i--) {
			getObjectAt(i).render(g);
		}
	}

	public void removeBlood() {
		bloodList.clear();
	}
	
	public void removeProjectiles() {
		for (int i = gameObjs.size() - 1; i >= 0; i--) {
			GameObject obj = getObjectAt(i);
			if(obj instanceof Projectile) {
				removeObject(obj);
			}
		}
	}
	
	public void removeZombies() {
		for (int i = gameObjs.size() - 1; i >= 0; i--) {
			GameObject obj = getObjectAt(i);
			if(obj instanceof Zombie) {
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
		
		addObject(new Zombie(x, y, r.nextDouble()/5+1, 60));
		
	}
	
	public void addZombieLeft(double speed, int lvl) {
		Random r = new Random();	
		double hp = 40 + 5*lvl + r.nextInt(lvl);
		addObject(new DodgingZombie(-100, r.nextInt(Program.HEIGHT), speed, hp));
	}
	
	public void addZombieRight(double speed, int lvl) {
		Random r = new Random();
		double hp = 40 + 5*lvl + r.nextInt(lvl);
		addObject(new Zombie(Program.WIDTH + 100, r.nextInt(Program.HEIGHT), speed, hp));
	}
	
	public void addZombieUp(double speed, int lvl) {
		Random r = new Random();
		double hp = 40 + 5*lvl + r.nextInt(lvl);
		addObject(new Zombie(r.nextInt(Program.WIDTH), -100, speed, hp));
	}
	
	public void addZombieDown(double speed, int lvl) {
		Random r = new Random();
		double hp = 40 + 5*lvl + r.nextInt(lvl);
		addObject(new Zombie(r.nextInt(Program.WIDTH), Program.HEIGHT + 100, speed, hp));
	}
	
	public void addZombie(double x, double y, int lvl) {
		Random r = new Random();
		double hp = 40 + 5*lvl + r.nextInt(lvl);
		addObject(new Zombie(x, y, r.nextDouble()/5+1.2, hp));
	}
	
	public void bloodSplat(double x, double y, double knock, double angle, int num) {
		Random r = new Random();
		addBlood(new Blood(x, y, knock, angle));
		for (int i = 1; i < num; i++) {
			addBlood(new Blood(x, y, knock * (r.nextDouble() * 0.5 + 0.5 ), angle + (r.nextDouble() - 0.5)*1.55));
		}
	}
	
	public void addObject(GameObject obj) 		{gameObjs.add(obj);}
	public void addBlood(Blood blood)			{bloodList.add(blood);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public void removeBlood(Blood blood)		{bloodList.remove(blood);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public LinkedList<GameObject> getObjList()	{return gameObjs;}

}