package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Program extends Canvas implements Runnable{

	private static final long serialVersionUID = -1499886446881465910L;
	private Thread thread;
	private boolean running;
	private Handler handler;
	
	public final int HEIGHT = 600;
	public final int WIDTH = 800;
	
	
	public Program() {
		handler = new Handler();
		
		this.addKeyListener(new KeyInput(handler));
		this.setFocusable(true);
		
		
		new Window(WIDTH,HEIGHT,"Stand Your Ground", this);
		
		handler.addObject(new PlayerObject(WIDTH/2-10, HEIGHT/2-30));
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
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
		long lastTime = System.nanoTime();
		long currTime = System.nanoTime();
        double ticksPerSec = 60.0;
        double nsPerTick = 1000000000 / ticksPerSec;
        double delta = 0;
        long timer = System.currentTimeMillis();
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
	        
	        if(System.currentTimeMillis() - timer > 1000)
	        {
	            timer += 1000;
	            System.out.println("FPS: "+ frames);
	            frames = 0;
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
		g.fillRect(0, 0, 800, 600);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		handler.tick();
	}
	

}
