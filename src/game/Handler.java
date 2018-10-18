package game;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import game.SpawnSystem.REGION;
import game.SpawnSystem.ZOMBIE;

import static java.lang.Math.abs;

/*
 * This class contains the list of game objects and ticks
 * and renders each them.
 */
public class Handler {
	
	private LinkedList<GameObject> 	gameObjs;
	private LinkedList<Blood>	   	bloodList;
	private Random 					r;
	
	public Handler() {
		gameObjs = new LinkedList<GameObject>();
		bloodList = new LinkedList<Blood>();
		Zombie.loadSprites();
		r = new Random();
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
	
	public void addRandomZombie() {
		double x, y;
		double xPlayer = Program.player.getX();
		double yPlayer = Program.player.getY();
		do {
			x = r.nextInt(Program.WIDTH);
			y = r.nextInt(Program.HEIGHT);
		}
		while (abs(x-xPlayer) < 300 && abs(y-yPlayer) < 300);
		
		addNormalZombie(x, y);
		
	}
	
	public void addZombie(ZOMBIE zombie, REGION region) {
		double x, y;
		
		switch (region) {
		case DOWN:
			x = r.nextInt(Program.WIDTH);
			y = Program.HEIGHT + 100;
			break;
		case LEFT:
			x = -100;
			y = r.nextInt(Program.HEIGHT);
			break;
		case RIGHT:
			x = Program.WIDTH + 100;
			y = r.nextInt(Program.HEIGHT);
			break;
		case UP:
			x = r.nextInt(Program.WIDTH);
			y = -100;
			break;
		default:
			x = y = 0;
			break;
		}
		
		switch(zombie) {
		case DODGING:
			addDodgingZombie(x, y);
			break;
		case FAST:
			addFastZombie(x, y); 
			break;
		case NORMAL:
			addNormalZombie(x, y); 
			break;
		default:
			break;
		}
	}
	
	public void addNormalZombie(double x, double y) {
		double hp = 40 + 4 * Program.player.getLevel();
		double speed = 1.2 + Program.player.getLevel() * 0.02;
		addObject(new Zombie(x, y, speed, hp));
	}
	
	public void addDodgingZombie(double x, double y) {
		double hp = 36 + 3 * Program.player.getLevel();
		double speed = 1.4 + Program.player.getLevel() * 0.02;
		addObject(new DodgingZombie(x, y, speed, hp));
	}
	
	public void addFastZombie(double x, double y) {
		double hp = 40 + 4 * Program.player.getLevel();
		double speed = 1.8 + Program.player.getLevel() * 0.02;
		addObject(new FastZombie(x, y, speed, hp));
	}
	
	public void bloodSplat(double x, double y, double knock, double angle, int num) {
		addBlood(new Blood(x, y, knock, angle));
		for (int i = 1; i < num; i++) {
			addBlood(new Blood(x, y, knock * (r.nextDouble() * 0.5 + 0.5 ), angle + (r.nextDouble() - 0.5)*1.55));
		}
	}
	
	public void addObject(GameObject obj)		{gameObjs.add(obj);}
	public void addBlood(Blood blood)			{bloodList.add(blood);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public void removeBlood(Blood blood)		{bloodList.remove(blood);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public LinkedList<GameObject> getObjList()	{return gameObjs;}
}