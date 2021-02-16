package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.imageio.ImageIO;

import game.Audio.AudioPlayer;
import game.Inputs.KeyInput;
import game.Inputs.MouseInput;
import game.Inputs.Store;
import game.Pieces.Player;
import game.Pieces.Reticle;
import game.Pieces.Enemies.Zombie;
import game.Weapons.Gun;

public class Program extends Canvas implements Runnable {

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
	public static SpawnSystem spawnSys;
	public static Reticle reticle;
	public static Player player;
	public static Handler handler;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public static final int XTBOUND = -160;
	public static final int YTBOUND = -140;
	
	public static enum STATE{
		InGame,
		StoreMenu,
		StartMenu,
		GameOver,
		PauseMenu;
	}
	
	public static STATE gameState = STATE.StartMenu;
	private static STATE prevState;
	
	public Program() {
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
		
		new Window(WIDTH,HEIGHT,"Stand Your Ground", this);
	}
	
	private void loadAssets() {
		AudioPlayer.init();
		Player.loadAssets();
		Gun.loadAssets();
		Zombie.loadAssets();
		Store.loadAssets();
		Reticle.loadAssets();
		try {
			background1 = ImageIO.read(new File("./res/GrassBackground.png"));
			background2 = ImageIO.read(new File("./res/StreetBackground.png"));
			background3 = ImageIO.read(new File("./res/UrbanBackground.png"));
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
		new Program();
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
	        
			delay(Duration.ofNanos(Math.max((long) (nsPerTick - (System.nanoTime() - currTime)), 0)));
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
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		switch (gameState) {
		case GameOver:
			MenuHelpers.renderGameOverMenu(g);
			reticle.render(g);
			break;
			
		case InGame:
			double xT = clamp(-player.getX() + (WIDTH/2 - 70), XTBOUND, 0);
			double yT = clamp(-player.getY() + (HEIGHT/2 - 80), YTBOUND, 0);
			g.scale(1.25, 1.25);
			g.translate(xT, yT);
			g.drawImage(background, 0, 0, null);
			handler.render(g);
			g.translate(-xT, -yT);
			g.scale(0.8, 0.8);
			hud.render(g);
			break;
			
		case PauseMenu:
			MenuHelpers.renderPauseMenu(g);
			reticle.render(g);
			break;
			
		case StartMenu:
			MenuHelpers.renderStartMenu(g);
			reticle.render(g);
			break;
			
		case StoreMenu:
			store.render(g);
			reticle.render(g);
			break;
			
		default:
			break;
		}
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		
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
			else background = background3;
			
			if (prevState != STATE.PauseMenu) spawnSys.commenceLevel();
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
		double x = -player.getX() + (WIDTH/2 - 70);
		return x > 0 || x < XTBOUND;
	}
	
	public static boolean isOnEdgeY() {
		double y = -player.getY() + (HEIGHT/2 - 80);
		return y > 0 || y < YTBOUND;
	}
	
	public static double clamp(double val, double min, double max) {
		return Math.min(Math.max(val, min), max);
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.min(Math.max(val, min), max);
	}
	
	public static void delay(Duration duration) {
		try {
			Thread.sleep(duration.toMillis(), (int) (duration.toNanosPart() % 1e6));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
