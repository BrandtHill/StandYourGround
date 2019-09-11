package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import game.Pieces.Blood;
import game.Pieces.Brass;
import game.Pieces.GameObject;
import game.Pieces.Obstacle;
import game.Pieces.Player;
import game.Pieces.Projectile;
import game.Pieces.Reticle;
import game.Pieces.Enemies.DodgingZombie;
import game.Pieces.Enemies.FastZombie;
import game.Pieces.Enemies.ThiccZombie;
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
	private List<GameObject>	deadQueue;
	private List<GameObject>	asyncQueue;
	private Random 				r;
	public Rectangle[][]		grid;
	public static final int 	REC_SIZE = 40;
	public static final int		GRID_WIDTH = Program.WIDTH / REC_SIZE;
	public static final int		GRID_HEIGHT = Program.HEIGHT / REC_SIZE;
	
	public Handler() {
		gameObjs = new LinkedList<>();
		deadQueue = new LinkedList<>();
		asyncQueue = Collections.synchronizedList(new LinkedList<>());
		r = new Random();
		grid = new Rectangle[GRID_WIDTH][GRID_HEIGHT];
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int y = 0; y < GRID_HEIGHT; y++) {
				grid[x][y] = new Rectangle(x * REC_SIZE, y * REC_SIZE, REC_SIZE, REC_SIZE);
			}
		}
	}
	
	public void tick() {
		addQueued();
		gameObjs.stream().forEach(o -> o.tick());
		cullDead();
	}
	
	private void addQueued() {
		asyncQueue.stream().forEach(o -> addObject(o));
		asyncQueue.clear();
	}
	
	private void cullDead() {
		gameObjs.removeAll(deadQueue);
		deadQueue.clear();
	}
	
	public void render(Graphics g) {
		gameObjs.stream().filter(o -> o instanceof Blood).forEach(b -> b.render(g));
		gameObjs.stream().filter(o -> o instanceof Brass).forEach(b -> b.render(g));
		gameObjs.stream().filter(o -> o instanceof Obstacle).forEach(b -> b.render(g));
		gameObjs.stream().filter(o -> o instanceof Projectile).forEach(p -> p.render(g));
		gameObjs.stream().filter(o -> o instanceof Zombie).forEach(z -> z.render(g));
		gameObjs.stream().filter(o -> o instanceof Reticle).findFirst().get().render(g);
		gameObjs.stream().filter(o -> o instanceof Player).findFirst().get().render(g);
		//for (Rectangle[] arr : grid) for (Rectangle r : arr) ((Graphics2D)g).draw(r);
	}

	public void removeBlood() {
		gameObjs.removeIf(b -> b instanceof Blood);
		asyncQueue.removeIf(b -> b instanceof Blood);
	}
	
	public void removeBrass() {
		gameObjs.removeIf(b -> b instanceof Brass);
		asyncQueue.removeIf(b -> b instanceof Brass);
	}
	
	public void removeProjectiles() {
		gameObjs.removeIf(p -> p instanceof Projectile);
	}
	
	public void removeZombies() {
		gameObjs.removeIf(z -> z instanceof Zombie);
	}
	
	public void removeObstacles() {
		gameObjs.removeIf(o -> o instanceof Obstacle);
	}
	
	public void addRandomZombie() {
		double x, y;
		double xPlayer = Program.player.getX();
		double yPlayer = Program.player.getY();
		do {
			x = r.nextInt(2 * Program.WIDTH) - Program.WIDTH;
			y = r.nextInt(Program.HEIGHT);
		} while (abs(x-xPlayer) < 300 && abs(y-yPlayer) < 300);
		
		double zombieDist = r.nextGaussian();
		if 		(zombieDist < -1) 	addDodgingZombie(x, y);
		else if (zombieDist > 1.5) 	addFastZombie(x, y);
		else						addNormalZombie(x, y);
	}
	
	public void addZombie(REGION region, ZOMBIE zombie) {
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
		case DODGING:	addDodgingZombie(x, y);
			break;
		case FAST:		addFastZombie(x, y); 
			break;
		case NORMAL:	addNormalZombie(x, y); 
			break;
		case THICC:		addThiccZombie(x, y);
			break;
		default:
			break;
		}
	}
	
	public void addNormalZombie(double x, double y) {
		double hp = 40 + 4 * Program.spawnSys.getLevel();
		double speed = 1.3 + Program.spawnSys.getLevel() * 0.015;
		addObject(new Zombie(x, y, speed, hp));
	}
	
	public void addDodgingZombie(double x, double y) {
		double hp = 36 + 3 * Program.spawnSys.getLevel();
		double speed = 1.5 + Program.spawnSys.getLevel() * 0.015;
		addObject(new DodgingZombie(x, y, speed, hp));
	}
	
	public void addFastZombie(double x, double y) {
		double hp = 40 + 4 * Program.spawnSys.getLevel();
		double speed = 1.9 + Program.spawnSys.getLevel() * 0.015;
		addObject(new FastZombie(x, y, speed, hp));
	}
	
	public void addThiccZombie(double x, double y) {
		double hp = 120 + 4 * Program.spawnSys.getLevel();
		double speed = 1.1 + Program.spawnSys.getLevel() * 0.015;
		addObject(new ThiccZombie(x, y, speed, hp));
	}
	
	public void bloodSplat(double x, double y, double knock, double angle, int num) {
		addObjectAsync(new Blood(x, y, knock, angle));
		for (int i = 1; i < num; i++) {
			addObjectAsync(new Blood(x, y, knock * (r.nextDouble() * 0.5 + 0.5 ), angle + (r.nextDouble() - 0.5)*1.55));
		}
	}
	
	public Rectangle getGridNode(Point position) {
		int x = position.x / REC_SIZE;
		int y = position.y / REC_SIZE;
		return validGridIndex(x, y) ? grid[x][y] : null;
	}
	
	public List<Node> aStar(Point start, Point dest) {
		List<Node> path = new LinkedList<>();
		Set<Node> open = new HashSet<>();
		Set<Node> closed = new HashSet<>();
		Node sNode = new Node(start);
		Node dNode = new Node(dest);
		sNode.f = sNode.g = sNode.h = 0;
		open.add(sNode);
		Node curr;
		while (open.size() > 0) {
			curr = open.stream().min(compF).get();
			open.remove(curr);
			closed.add(curr);
		}
		return path;
	}
	
	private List <Node> generateChildren(Node antecedent) {
		List<Node> list = new LinkedList<>();
		int x = antecedent.getX();
		int y = antecedent.getY();
		Node n;
		int[][] offsets = new int[][] {{x-1, y-1}, {x+1, y-1}, {x-1, y+1}, {x+1, y+1},  //0-3 diagonal children 
									   {x  , y-1}, {x  , y+1}, {x-1, y  }, {x+1, y  }}; //4-7 normal children
		
		for (int i = 0; i < offsets.length; i++) {
			int xo = offsets[i][0];
			int yo = offsets[i][1];
			boolean diagonal = i < 4; //First 4 offsets are diagonal, Last 4 are normal
			if (validGridIndex(xo, yo) && !getObstacles().anyMatch(o -> o.getBounds().intersects(grid[xo][yo]))) {
				n = new Node(xo, yo);
				n.calcH();
				n.g = REC_SIZE * (diagonal ? GameObject.SQRT2 : 1.0); //Diagonal children are farther away
				n.calcF();
				list.add(n);
			}
		}
		
		return list;
	}
	
	private boolean validGridIndex(int x, int y) {
		return x >= 0
			&& x < GRID_WIDTH
			&& y >= 0
			&& y < GRID_HEIGHT;
	}
	
	private static Comparator<Node> compF = Comparator.comparing(Node::getF);
	private static Comparator<Node> compG = Comparator.comparing(Node::getG);
	private static Comparator<Node> compH = Comparator.comparing(Node::getH);
	
	class Node {
		public Node(int x, int y) {
			this(grid[x][y]);
		}
		public Node(Point p) {
			this(p.x / REC_SIZE, p.y / REC_SIZE);
		}
		public Node(Rectangle r) {
			this.rectangle = r;
		}
		public void calcH() {h = Point.distance(rectangle.getCenterX(), rectangle.getCenterY(), Program.player.getX(), Program.player.getY());}
		public void calcG() {}
		public void calcF() {f = g + h;}
		public double getF() {return f;}
		public double getG() {return g;}
		public double getH() {return h;}
		public int getX() {return rectangle.x / REC_SIZE;}
		public int getY() {return rectangle.y / REC_SIZE;}
		
		Node parent;
		Rectangle rectangle;
		double f = Double.POSITIVE_INFINITY, g = Double.POSITIVE_INFINITY, h = Double.POSITIVE_INFINITY;
	}
	
	public void addObjectAsync(GameObject nu) 		{asyncQueue.add(nu);}
	public void removeObjectAsync(GameObject dead) 	{deadQueue.add(dead);}
	public void addObject(GameObject obj) 			{gameObjs.add(obj);}
	public void removeObject(GameObject obj) 		{gameObjs.remove(obj);}
	public Stream<GameObject> getObjectStream() 	{return gameObjs.stream();}
	public Stream<Zombie> getZombies()				{return gameObjs.stream().filter(z -> z instanceof Zombie).map(z -> (Zombie)z);}
	public Stream<Obstacle> getObstacles()			{return gameObjs.stream().filter(o -> o instanceof Obstacle).map(o -> (Obstacle)o);}
}