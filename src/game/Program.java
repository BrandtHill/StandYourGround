package game;

import static java.lang.Math.atan2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.Music;

public class Program extends Canvas implements Runnable{

	private static final long serialVersionUID = -1499886446881465910L;
	private Thread thread;
	private boolean running;
	private Handler handler;
	private HUD hud;
	private SpawnSystem spawnSys;
	private PlayerObject player;
	private ReticleObject reticle;
	private Store store;
	private MouseMotionInput mmi;
	private StoreMotion storeMotion;
	private BufferedImage background;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public Music song;
	private byte tickDivider;
	
	public static enum STATE{
		InGame,
		StoreMenu,
		StartMenu,
		GameOver,
		PauseMenu;
		
	}
	
	public static STATE gameState;
	private STATE prevState;
	
	public Program() {
		
		AudioPlayer.load();
		handler = new Handler(this);
		handler.addObject(new PlayerObject(WIDTH/2-10, HEIGHT/2-30, handler));
		handler.addObject(new ReticleObject(WIDTH/2-10, HEIGHT/2-30, handler));
		spawnSys = new SpawnSystem(handler);
		hud = new HUD(handler, spawnSys);
		player = (PlayerObject)handler.getObjectAt(0);
		reticle = (ReticleObject)handler.getObjectAt(1);
		store = new Store(handler);
		storeMotion = new StoreMotion(store);
		
		addKeyListener(new KeyInput(handler));
		addMouseListener(new MouseInput(handler));
		addMouseListener(store);
		addMouseMotionListener(new MouseMotionInput(handler));
		
		try {
			background = ImageIO.read(new File("res/GrassBackground.png"));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		gameState = STATE.StartMenu;
		song = AudioPlayer.getMusic("Husk");
		song.loop(1f, 0.25f);
		tickDivider = 0;
		
		new Window(WIDTH,HEIGHT,"Stand Your Ground", this);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		//run();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		System.out.println("Launching...");
		new Program();
	}

	/*
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
        long currMilli;
        int frames = 0;
        while(running)
        {
	        currTime = System.nanoTime();
	        delta += (currTime - lastTime) / nsPerTick;
	        lastTime = currTime;
	        while(delta >=1)
	        {
	            tick();
	            delta--;
	        }
	        
	        render();
	        frames++;
	        
        	currMilli = System.currentTimeMillis();
			if (currMilli - timer > 1000) {
				//timer += 1000;
				timer = System.currentTimeMillis();
				System.out.println("FPS: " + frames);
				frames = 0;

			}
	        
	        if (gameState == STATE.InGame) {
	        	
			}
	        else if (gameState == STATE.PauseMenu) {
	        	
	        }
	        else if (gameState == STATE.StartMenu) {
	        	
	        }
	        else if (gameState == STATE.StoreMenu) {
	        	
	        }
        
        }
        stop();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;					
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if (gameState == STATE.InGame) {
			g.drawImage(background, 0, 0, null);
			handler.render(g);
			hud.render(g);
		}
		else if (gameState == STATE.PauseMenu) {
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(115,48,168));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 48));
			g.drawString("GAME PAUSED", 180, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS 'ESC' TO RESUME", 150, 400);
			
			reticle.render(g);
		}
		else if (gameState == STATE.StartMenu) {
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(150,48,30));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 42));
			g.drawString("STAND YOUR GROUND", 160, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS SPACE TO BEGIN", 180, 400);

			reticle.render(g);
        }
		else if (gameState == STATE.StoreMenu) {
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			store.render(g);
			
			reticle.render(g);
        }
		else if (gameState == STATE.GameOver) {
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(new Color(30,60,30));
			g.draw3DRect(100, 90, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 42));
			g.drawString("GAME OVER", 250, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("RELAUNCH TO PLAY AGAIN", 150, 400);
        }
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		if (gameState == STATE.InGame) {
			handler.tick();
			
			if (tickDivider % 8 == 0) {
				player.setAngle(atan2(reticle.getX() - (player.getX() + 10), reticle.getY() - (player.getY() + 10)));
				hud.tick();
				spawnSys.tick();
			}
			tickDivider++;
		}
		else if (gameState == STATE.PauseMenu)
		{
			reticle.tick();
		}
		else if (gameState == STATE.StoreMenu)
		{
			reticle.tick();
		}
		else if (gameState == STATE.StartMenu)
		{
			reticle.tick();
		}
		if(prevState != gameState) {
			stateChange();
		}
		prevState = gameState;
	}
	
	private void stateChange() {
		musicChange();
		hud.tick();
		if (gameState == STATE.InGame) {
			//player.resetAllAmmo();
			player.resetAllAmmo();
			handler.removeProjectiles();
			removeMouseMotionListener(storeMotion);
		}
		else if (gameState == STATE.StoreMenu) {
			addMouseMotionListener(storeMotion);
		}
		else {
			player.resetAllAmmo();
			handler.removeProjectiles();
		}
	}
	
	private void musicChange() {
		if (gameState == STATE.PauseMenu){
			float pos = song.getPosition();
			song.stop();
			song.loop(0.65f, 0.25f);
			song.setPosition(pos);
		}
		else if (gameState == STATE.InGame){
			float pos = song.getPosition();
			song.stop();
			song.loop(1f, 0.25f);
			song.setPosition(pos);
		}
		else if (gameState == STATE.StoreMenu){
			float pos = song.getPosition();
			song.stop();
			song.loop(1.35f, 0.25f);
			song.setPosition(pos);
		}
	}
	
	/*
	 * This method makes sure the input 'val' is within
	 * the bounds on 'min' and 'max'. I think it's a rather
	 * clever one-line implementation.
	 */
	public static double clamp(double val, double min, double max) {
		return (val > min) ? ((val < max) ? val : max) : min;
	}
	
	public void commenceLevel() {
		player.setX(WIDTH/2-10);
		player.setY(HEIGHT/2-30);
		spawnSys.commenceLevel();
	}

}
