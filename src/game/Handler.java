package game;

import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import game.Pieces.Blood;
import game.Pieces.Brass;
import game.Pieces.GameObject;
import game.Pieces.Projectile;
import game.Pieces.Enemies.DodgingZombie;
import game.Pieces.Enemies.FastZombie;
import game.Pieces.Enemies.Zombie;
import game.SpawnSystem.REGION;
import game.SpawnSystem.ZOMBIE;

import static java.lang.Math.abs;

/*
 * This class contains the list of game objects and ticks
 * and renders each them.
 */
public class Handler {
	
	private List<GameObject>	gameObjs;
	private List<Blood>	   		bloodList;
	private List<Brass>			brassList;
	private List<GameObject>	deadQueue;
	private List<GameObject>	asyncQueue;
	private Random 				r;
	
	public Handler() {
		gameObjs = new LinkedList<>();
		bloodList = new LinkedList<>();
		brassList = new LinkedList<>();
		deadQueue = new LinkedList<>();
		asyncQueue = Collections.synchronizedList(new LinkedList<>());
		r = new Random();
	}
	
	public void tick() {
		addQueued();
		bloodList.stream().forEach(b -> b.tick());
		brassList.stream().forEach(b -> b.tick());
		gameObjs.stream().forEach(o -> o.tick());
		cullDead();
	}
	
	private void addQueued() {
		for (GameObject o : asyncQueue) {
			if (o instanceof Brass) addBrass((Brass)o);
			else if (o instanceof Blood) addBlood((Blood)o);
			else addObject(o);
		}
		asyncQueue.clear();
	}
	
	private void cullDead() {
		bloodList.removeAll(deadQueue);
		brassList.removeAll(deadQueue);
		gameObjs.removeAll(deadQueue);
		deadQueue.clear();
	}
	
	public void render(Graphics g) {
		bloodList.stream().forEach(b -> b.render(g));
		brassList.stream().forEach(b -> b.render(g));
		
		for (int i = gameObjs.size() - 1; i >= 0; i--) {
			getObjectAt(i).render(g);
		}
	}

	public void removeBlood() {
		bloodList.clear();
	}
	
	public void removeBrass() {
		brassList.clear();
	}
	
	public void removeProjectiles() {
		gameObjs.removeIf(p -> p instanceof Projectile);
	}
	
	public void removeZombies() {
		gameObjs.removeIf(z -> z instanceof Zombie);
	}
	
	public void addRandomZombie() {
		double x, y;
		double xPlayer = Program.player.getX();
		double yPlayer = Program.player.getY();
		do {
			x = r.nextInt(2 * Program.WIDTH) - Program.WIDTH;
			y = r.nextInt(Program.HEIGHT);
		}
		while (abs(x-xPlayer) < 300 && abs(y-yPlayer) < 300);
		
		double zombieDist = r.nextGaussian();
		if 		(zombieDist < -1) 	addDodgingZombie(x, y);
		else if (zombieDist > 1.5) 	addFastZombie(x, y);
		else						addNormalZombie(x, y);
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
	
	public void addObjectAsync(GameObject nu)	{asyncQueue.add(nu);}
	public void addDeadObject(GameObject dead) 	{deadQueue.add(dead);}
	public void addObject(GameObject obj)		{gameObjs.add(obj);}
	public void addBlood(Blood blood)			{bloodList.add(blood);}
	public void addBrass(Brass brass)			{brassList.add(brass);}
	public void removeObject(GameObject obj) 	{gameObjs.remove(obj);}
	public void removeBlood(Blood blood)		{bloodList.remove(blood);}
	public void removeBrass(Brass brass)		{brassList.remove(brass);}
	public GameObject getObjectAt(int i)		{return gameObjs.get(i);}
	public List<GameObject> getObjList()		{return gameObjs;}
}