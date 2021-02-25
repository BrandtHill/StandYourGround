package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import game.Audio.AudioPlayer;
import game.Inputs.KeyInput;
import game.Inputs.MouseInput;
import game.Inputs.Store;
import game.Pieces.DeadZed;
import game.Pieces.GameObject;
import game.Pieces.Player;
import game.Pieces.Reticle;
import game.Pieces.Enemies.Zombie;
import game.SpawnSystem.SpawnSystem;
import game.Weapons.Gun;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = -1499886446881465910L;
	private boolean running;
	private Thread gameThread;
	public static HUD hud;
	public static Store store;
	public static BufferedImage background;
	public static BufferedImage backgroundSlice;
	private static BufferedImage background1;
	private static BufferedImage background2;
	private static BufferedImage background3;
	private static BufferedImage background4;
	private static BufferedImage background5;
	private static JFrame frame;
	public static SpawnSystem spawnSys;
	public static Reticle reticle;
	public static Player player;
	public static Handler handler;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public static final int XTBOUND = -160;
	public static final int YTBOUND = -140;
	public static final double SCALE = 1.25;
	
	public static enum STATE{
		InGame,
		StoreMenu,
		StartMenu,
		GameOver,
		GameOverWin,
		PauseMenu;
	}
	
	public static STATE gameState = STATE.StartMenu;
	private static STATE prevState;
	
	public Main() {
		loadAssets();
		handler = new Handler();
		player = new Player(WIDTH/2-10, HEIGHT/2-30);
		reticle = new Reticle(WIDTH/2-10, HEIGHT/2-30);
		handler.addObject(player);
		handler.addObject(reticle);
		spawnSys = new SpawnSystem();
		store = new Store();
		hud = new HUD();
		
		MouseInput mi = new MouseInput();
		addKeyListener(new KeyInput());
		addMouseListener(mi);
		addMouseWheelListener(mi);
		addMouseMotionListener(mi);
		addMouseListener(store);
		addMouseMotionListener(store);
		
		SaveData.saveToFile("./res/saves/newgame.syg");
		SaveData.saveToFile("./res/saves/autosave.syg");
		
		setupJFrame();
		start();
	}
	
	private void setupJFrame() {
		Dimension dim = new Dimension(WIDTH, HEIGHT);
		frame = new JFrame("Stand Your Ground");
		frame.setPreferredSize(dim);
		frame.setMinimumSize(dim);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),new Point(),null ));
		frame.setIconImage(new ImageIcon("./res/Oscilloshape1.JPG").getImage());
		frame.add(this);
		frame.setVisible(true);
	}
	
	private void loadAssets() {
		AudioPlayer.init();
		Player.loadAssets();
		Gun.loadAssets();
		Zombie.loadAssets();
		DeadZed.loadAssets();
		Store.loadAssets();
		Reticle.loadAssets();
		try {
			background1 = ImageIO.read(new File("./res/GrassBackground.png"));
			background2 = ImageIO.read(new File("./res/StreetBackground.png"));
			background3 = ImageIO.read(new File("./res/UrbanBackground.png"));
			background4 = ImageIO.read(new File("./res/DeadendBackground.png"));
			background5 = ImageIO.read(new File("./res/TunnelBackground.png"));
			background = background1;
			backgroundSlice = background.getSubimage(0, 20, 120, 40);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		running = true;
		gameThread = new Thread(this, "Main Game Thread");
		gameThread.start();
	}
	
	public synchronized void stop() {
		try {
			gameThread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Launching...");
		new Main();
	}

	/** This method is the main game loop. Tick rate is 60/second. */
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		long currTime = System.nanoTime();
        double ticksPerSec = 60.0;
        double nsPerTick = 1e9 / ticksPerSec;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
        	currTime = System.nanoTime();
	        delta += (currTime - lastTime) / nsPerTick;
	        lastTime = currTime;
	        
	        while (delta >=1) {
	        	tick();
				delta--;

		        render();
				frames++;
	        }
	        
	        
			if (System.currentTimeMillis() - timer > 1000) {
				timer = System.currentTimeMillis();
				System.out.println("FPS: " + frames);
				frames = 0;
			}
	        
			delay(Duration.ofMillis(1));
        }
        stop();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		double xScale = getXScale();
		double yScale = getYScale();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) (WIDTH * xScale), (int) (HEIGHT * yScale));
		
		switch (gameState) {
		case GameOver:
			g.scale(xScale, yScale);
			MenuHelpers.renderGameOverMenu(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		case GameOverWin:
			g.scale(xScale, yScale);
			MenuHelpers.renderGameOverWinMenu(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		case InGame:
			g.scale(SCALE * xScale, SCALE * yScale);
			g.translate(player.getXOffset(), player.getYOffset());
			g.drawImage(background, 0, 0, null);
			handler.render(g);
			g.translate(-player.getXOffset(), -player.getYOffset());
			g.scale(1/(SCALE * xScale), 1/(SCALE * yScale));
			g.scale(xScale, yScale);
			hud.render(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		case PauseMenu:
			g.scale(xScale, yScale);
			MenuHelpers.renderPauseMenu(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		case StartMenu:
			g.scale(xScale, yScale);
			MenuHelpers.renderStartMenu(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		case StoreMenu:
			g.scale(xScale, yScale);
			store.render(g);
			g.scale(1/xScale, 1/yScale);
			reticle.render(g);
			break;
			
		default:
			break;
		}
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		adjustSize();
		switch (gameState) {
		case InGame:
			handler.tick();
			hud.tick();
			spawnSys.tick();
			break;
		case GameOver:
		case PauseMenu:
		case StartMenu:
		case StoreMenu:
		default:
			reticle.tick();
			break;
		}
		
		if (prevState != gameState) stateChange();
		prevState = gameState;
	}
	
	private void stateChange() {
		hud.tick();
		
		switch (gameState) {
		case InGame:
			if (spawnSys.getLevel() <= 5) background = background1;
			else if (spawnSys.getLevel() <= 10) background = background2;
			else if (spawnSys.getLevel() <= 15) background = background3;
			else if (spawnSys.getLevel() <= 20) background = background4;
			else if (spawnSys.getLevel() <= 25) background = background5;
			
			if (prevState != STATE.PauseMenu) spawnSys.commence();
			if (prevState == STATE.StoreMenu) SaveData.saveToFile("./res/saves/autosave.syg");
			removeMouseMotionListener(store);
			break;
		case StoreMenu:
			addMouseMotionListener(store);
			SaveData.saveToFile("./res/saves/autosave.syg");
			backgroundSlice = background.getSubimage(0, 20, 120, 40);
			Store.menu = Store.Menu.BuyGuns;
			store.onMenuUpdate();
			break;
		case GameOver:
		case PauseMenu:
		case StartMenu:
		default:
			break;
		}
	}
	
	public static boolean isOnEdgeX() {
		double x = -player.getX() + (WIDTH + YTBOUND)/2;
		return x > 0 || x < XTBOUND;
	}
	
	public static boolean isOnEdgeY() {
		double y = -player.getY() + (HEIGHT + XTBOUND)/2;
		return y > 0 || y < YTBOUND;
	}
	
	public static double clamp(double val, double min, double max) {
		return Math.min(Math.max(val, min), max);
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.min(Math.max(val, min), max);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public static double distance(GameObject g1, GameObject g2) {
		return distance(g1.getX(), g1.getY(), g2.getX(), g2.getY());
	}
	
	public static void delay(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static double getXScale() {
		Dimension d = frame.getSize();
		return d.getWidth() / WIDTH;
	}
	
	public static double getYScale() {
		Dimension d = frame.getSize();
		return d.getHeight() / HEIGHT;
	}
	
	public static double getXOffset() {
		return (getXScale() - 1) * WIDTH / 2;
	}
	
	private static void adjustSize() {
		double scale = Math.min(getXScale(), getYScale());
		if (getXScale() != getYScale()) {
			System.out.println("Adjusting size to maintain aspect ratio - scale " + scale);
			frame.setSize((int)(scale * WIDTH), (int)(scale * HEIGHT));
		}
	}
}
