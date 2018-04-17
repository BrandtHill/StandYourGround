package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	private boolean w, a, s, d;
	private GameObject player;
	
	public KeyInput(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * All this does is set bools true then calls changeVelocity.
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		//System.out.println("Key Pressed: " + key);
		if(key == KeyEvent.VK_W) w = true;
		if(key == KeyEvent.VK_A) a = true;
		if(key == KeyEvent.VK_S) s = true;
		if(key == KeyEvent.VK_D) d = true;
		changeVelocity();
	}
	
	/*
	 * All this does is set bools false then calls changeVelocity.
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		//System.out.println("Key Released: " + key);		
		if(key == KeyEvent.VK_W) w = false;
		if(key == KeyEvent.VK_A) a = false;
		if(key == KeyEvent.VK_S) s = false;
		if(key == KeyEvent.VK_D) d = false;
		changeVelocity();

	}
	
	/*
	 * This handles all the cases so that there's fluid
	 * movement when opposing keys are pressed and released.
	 */
	private void changeVelocity() {

				if((w && s) || !(w || s))	player.setVelY(0);
				if(w && !s) 				player.setVelY(-3);
				if(!w && s)					player.setVelY(3);
				if((a && d) || !(a || d))	player.setVelX(0);
				if(a && !d)					player.setVelX(-3);
				if(!a && d)					player.setVelX(3);

	}
}
