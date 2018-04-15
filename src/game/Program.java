package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Program extends Canvas implements Runnable{

	private static final long serialVersionUID = -1499886446881465910L;
	private Thread thread;
	private boolean running;
	
	public Program() {
		new Window(600,800,"Stand Your Ground", this);
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
		// TODO Auto-generated method stub
		System.out.println("Launching...");
		new Program();
	}

	public void run() {
		// TODO Auto-generated method stub
		long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;
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
		// TODO Auto-generated method stub
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;					
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		g.dispose();
		bs.show();
	}

	private void tick() {
		// TODO Auto-generated method stub
		
	}
	

}
