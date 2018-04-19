package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import org.newdawn.slick.Music;

public class Program extends Canvas implements Runnable{

	private static final long serialVersionUID = -1499886446881465910L;
	private Thread thread;
	private boolean running;
	private Handler handler;
	//private PlayerObject player;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public Music song;
	
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
		
		handler = new Handler();
		handler.addObject(new PlayerObject(WIDTH/2-10, HEIGHT/2-30, handler));
		handler.addObject(new ReticleObject(WIDTH/2-10, HEIGHT/2-30, handler));
		
		
		addKeyListener(new KeyInput(handler));
		addMouseListener(new MouseInput(handler));
		addMouseMotionListener(new MouseMotionInput(handler));
		
		gameState = STATE.StartMenu;
		AudioPlayer.load();
		song = AudioPlayer.getMusic("Husk");
		song.loop(1f, 0.25f);
		
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
        int sec = 0;
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
	        
	        if (gameState == STATE.InGame) {
	        	
	        	currMilli = System.currentTimeMillis();
				if (currMilli - timer > 1000) {
					//timer += 1000;
					timer = System.currentTimeMillis();
					//System.out.println("FPS: " + frames);
					frames = 0;

					sec++;
					if (sec > 0) {
						handler.addZombie();
						sec = 0;
					}
				}
			}
	        else if (gameState == STATE.PauseMenu) {
	        	
	        }
	        else if (gameState == STATE.StartMenu) {
	        	
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
			handler.render(g);
		}
		else if (gameState == STATE.PauseMenu) {
			g.setColor(new Color(115,48,168));
			g.fill3DRect(100, 100, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 48));
			g.drawString("GAME PAUSED", 180, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS 'ESC' TO RESUME", 150, 400);
		}
		else if (gameState == STATE.StartMenu) {
			g.setColor(new Color(175,48,79));
			g.fill3DRect(100, 100, WIDTH-200, HEIGHT-200, true);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 48));
			g.drawString("Stand Your Ground", 180, 200);
			g.setFont(new Font("Arial", 1, 36));
			g.drawString("PRESS ANY KEY TO BEGIN", 150, 400);
        }
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		if (gameState == STATE.InGame) {
			handler.tick();
		}
		else if (gameState == STATE.PauseMenu)
		{
			
		}
		
		if(prevState != gameState) {
			stateChange();
		}
		prevState = gameState;
	}
	
	private void stateChange() {
		musicChange();
		if (gameState == STATE.InGame) {
			
		}
		else {
			
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
	}
	
	/*
	 * This method makes sure the input 'val' is within
	 * the bounds on 'min' and 'max'. I think it's a rather
	 * clever one-line implementation.
	 */
	public static double clamp(double val, double min, double max) {
		return (val > min) ? ((val < max) ? val : max) : min;
	}

}
