package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.lwjgl.openal.AL;

public class Program extends Canvas implements Runnable{

	private static final long serialVersionUID = -1499886446881465910L;
	private boolean running;
	private Thread gameThread;
	private HUD hud;
	private Store store;
	private StoreMotion storeMotion;
	private BufferedImage background;
	public static SpawnSystem spawnSys;
	public static SaveData saveData;
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
		AudioPlayer.init();
		handler = new Handler();
		player = new Player(WIDTH/2-10, HEIGHT/2-30);
		reticle = new Reticle(WIDTH/2-10, HEIGHT/2-30);
		handler.addObject(player);
		handler.addObject(reticle);
		player.addReticle(reticle);
		saveData = new SaveData();
		spawnSys = new SpawnSystem();
		store = new Store();
		storeMotion = new StoreMotion(store);
		hud = new HUD();
		
		addKeyListener(new KeyInput(store));
		addMouseListener(new MouseInput());
		addMouseListener(store);
		addMouseMotionListener(new MouseMotionInput());
		
		try {
			background = ImageIO.read(new File("res/GrassBackground.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		saveToFile("res/saves/newgame.syg/");
		saveToFile("res/saves/autosave.syg/");
		
		new Window(WIDTH,HEIGHT,"Stand Your Ground", this);
		
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
			AL.destroy();
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		System.out.println("Launching...");
		new Program();
	}

	/**
	 * This method is the main game loop. It 'ticks' 60 times
	 * per second. It renders the game as fast as possible and
	 * prints the frame rate to console every second. 
	 */
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		long currTime = System.nanoTime();
        double ticksPerSec = 60.0;
        double nsPerTick = 1000000000 / ticksPerSec;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
	        
        	currTime = System.nanoTime();
	        delta += (currTime - lastTime) / nsPerTick;
	        lastTime = currTime;
	        
	        while (delta >=1) {
				tick();
				delta--;
	        }
	        
	        render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer = System.currentTimeMillis();
				System.out.println("FPS: " + (frames > 1000 ? "1000+" : frames));
				frames = 0;
			}
	        
	        if (delta < 0.9) delay(Duration.ofMillis(1));
        }
        stop();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;					
		}
		
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		switch (gameState) {
		case GameOver:
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(30,60,30));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 42));
			g.drawString("YOU DIED", 250, 200);
			g.setFont(new Font("Arial", 1, 28));
			g.drawString("R: RETRY FROM AUTOSAVE", 150, 400);
			g.drawString("N: NEW GAME", 150, 450);
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
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(115,48,168));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 48));
			g.drawString("GAME PAUSED", 200, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS 'ESC' TO RESUME", 160, 400);
			reticle.render(g);
			break;
			
		case StartMenu:
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(150,48,30));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 42));
			g.drawString("STAND YOUR GROUND", 160, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS SPACE TO BEGIN", 180, 400);
			reticle.render(g);
			break;
			
		case StoreMenu:
			g.fillRect(0, 0, WIDTH, HEIGHT);
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
		case GameOver:
			break;	
		case InGame:
			handler.tick();
			hud.tick();
			spawnSys.tick();
			break;
		case PauseMenu:
		case StartMenu:
		case StoreMenu:
		default:
			reticle.tick();
			break;
		}
		
		if(prevState != gameState) {
			stateChange();
		}
		prevState = gameState;
	}
	
	private void stateChange() {
		hud.tick();
		
		switch (gameState) {
		case InGame:
			if (prevState != STATE.PauseMenu) commenceLevel();
			removeMouseMotionListener(storeMotion);
			break;
		case StoreMenu:
			addMouseMotionListener(storeMotion);
			saveToFile("res/saves/autosave.syg/");
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
	
	public static void commenceLevel() {
		spawnSys.commenceLevel();
	}
	
	public static void saveToFile(String filename) {
		saveData.saveToFile(filename, player);
	}
	
	public static void loadFromFile(String filename) {
		saveData = saveData.loadFromFile(filename);
		if (saveData!= null) saveData.setPlayerAfterLoad(player);
		else System.out.println("Player load was not successful.");
	}
	
	public static void delay(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
