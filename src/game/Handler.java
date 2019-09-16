package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
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
 * The handler maintains a list of game pieces and ticks and renders them in order. It provides methods for accessing streams of game pieces,
 * adding game pieces asynchronously (e.g. triggered by user input), adding zombies based on type and location, detecting collision with 
 * obstacles, and calculating paths using the A* algorithm in the case that a straight-line path intersects with an obstacle.
 */
public class Handler {
	
	private List<GameObject>	gameObjs;
	private List<GameObject>	deadQueue;
	private ConcurrentLinkedQueue<GameObject>	asyncQueue;
	private Random 				r;
	public Rectangle[][]		grid;
	public static final int 	REC_SIZE = 25;
	public static final int		GRID_WIDTH = Program.WIDTH / REC_SIZE;
	public static final int		GRID_HEIGHT = Program.HEIGHT / REC_SIZE;
	
	public Handler() {
		gameObjs = new LinkedList<>();
		deadQueue = new LinkedList<>();
		asyncQueue = new ConcurrentLinkedQueue<GameObject>();
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
		asyncQueue.forEach(___ -> addObject(asyncQueue.remove()));
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
	
	public boolean hitsObstacle(Rectangle r) {
		return getObstacles().anyMatch(o -> o.getBounds().intersects(r));
	}
	
	public boolean hitsObstacle(Line2D.Double l) {
		return getObstacles().anyMatch(o -> o.getBounds().intersectsLine(l));
	}
	
	public boolean hitsObstacle(Polygon p) {
		return getObstacles().anyMatch(o -> p.getBounds().intersects(o.getBounds()));
	}
	
	public Rectangle getGridNode(Point position) {
		int x = nodeX(position.x);
		int y = nodeY(position.y);
		return validGridIndex(x, y) ? grid[x][y] : null;
	}
	
	public List<Rectangle> aStar(Point startPoint, Point destPoint) {
		Node[][] nodes = generateNodes();
		List<Rectangle> path = new LinkedList<>();
		Set<Node> open = new HashSet<>();
		Set<Node> closed = new HashSet<>();
		Node start = nodes[nodeX(startPoint.x)][nodeY(startPoint.y)];
		Node dest = nodes[nodeX(destPoint.x)][nodeY(destPoint.y)];
		path.add(dest.rectangle);
		start.f = start.g = start.h = 0;
		open.add(start);
		Node curr = null;
		while (!open.isEmpty()) {
			if ((curr = open.stream().min(compF).get()) == dest) break;
			open.remove(curr);
			closed.add(curr);
			
			int x = curr.getX();
			int y = curr.getY();
			Node n = null;
			int[][] offsets = new int[][] {{x-1, y-1}, {x+1, y-1}, {x-1, y+1}, {x+1, y+1},  //0-3 diagonal children 
										   {x  , y-1}, {x  , y+1}, {x-1, y  }, {x+1, y  }}; //4-7 normal children
			
			for (int i = 0; i < offsets.length; i++) {
				int xo = offsets[i][0];
				int yo = offsets[i][1];
				boolean diagonal = i < 4; //First 4 offsets are diagonal, Last 4 are normal
				if (validGridIndex(xo, yo) && !closed.contains(n = nodes[xo][yo]) && (!hitsObstacle(n.rectangle) || n == dest) && curr.g + gVal(diagonal) < n.g) {
					n.parent = curr;
					n.calcH(dest);
					n.calcG(diagonal);
					n.calcF();
					open.add(n);
				}
			}
		}
		while (curr != start) {
			path.add(0, curr.rectangle);
			curr = curr.parent;
		}
		return path;
	}
	
	public static int nodeX(int x) {return x / REC_SIZE;}
	public static int nodeY(int y) {return y / REC_SIZE;}
	public static double gVal(boolean diagonal) {return REC_SIZE * (diagonal ? GameObject.SQRT2 : 1.0);}
	
	private Node[][] generateNodes() {
		Node[][] nodes = new Node[GRID_WIDTH][GRID_HEIGHT];
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int y = 0; y < GRID_HEIGHT; y++) {
				nodes[x][y] = new Node(x, y);
			}
		}
		return nodes;
	}
	
	public static boolean validGridIndex(int x, int y) {
		return x >= 0
			&& x < GRID_WIDTH
			&& y >= 0
			&& y < GRID_HEIGHT;
	}
	
	private static Comparator<Node> compF = Comparator.comparing(Node::getF);
	
	class Node {
		public Node(int x, int y) {this.rectangle = grid[x][y];}
		public void calcH(Node dest) {h = Point.distance(rectangle.getCenterX(), rectangle.getCenterY(), dest.rectangle.getCenterX(), dest.rectangle.getCenterY());}
		public void calcG(boolean diagonal) {g = parent.g + gVal(diagonal);}
		public void calcF() {f = g + h;}
		public double getF() {return f;}
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