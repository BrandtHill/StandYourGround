package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.Program.STATE;

public class KeyInput extends KeyAdapter{
	
	private static Handler handler;
	private static PlayerObject player;
	private boolean w, a, s, d;
	private double speed = 2;
	
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
		
		if (Program.gameState == STATE.InGame) {
			if (key == KeyEvent.VK_W) w = true;
			if (key == KeyEvent.VK_A) a = true;
			if (key == KeyEvent.VK_S) s = true;
			if (key == KeyEvent.VK_D) d = true;
			if (key == KeyEvent.VK_1) player.switchToPrimary();
			if (key == KeyEvent.VK_2) player.switchToSecondary();
			if (key == KeyEvent.VK_3) player.switchToSidearm();
			
			changeVelocity();

			if (key == KeyEvent.VK_ESCAPE) {
				Program.gameState = STATE.PauseMenu;
				w = a = s = d = false;
			}
			if (key == KeyEvent.VK_R) {
				player.getGun().reload();
			}
			
		}
		else if (Program.gameState == STATE.GameOver) {
			
		}
		else if (Program.gameState == STATE.StartMenu) {
			if (key == KeyEvent.VK_SPACE) {
				Program.gameState = STATE.InGame;
				Program.commenceLevel();
			}
			if (key == KeyEvent.VK_1) {
				Program.loadsave.loadFromFile("res/saves/save1.syg");
			}
			if (key == KeyEvent.VK_2) {
				Program.loadsave.loadFromFile("res/saves/save2.syg");
			}
			if (key == KeyEvent.VK_3) {
				Program.loadsave.loadFromFile("res/saves/save3.syg");
			}
	
		}
		else if (Program.gameState == STATE.PauseMenu) {
			if (key == KeyEvent.VK_ESCAPE) {
				Program.gameState = STATE.InGame;
			}
		}
		else if (Program.gameState == STATE.StoreMenu) {
			
			if (key == KeyEvent.VK_SPACE) {
				Program.gameState = STATE.InGame;
				Program.commenceLevel();
			}
			if (key == KeyEvent.VK_1) {
				Program.loadsave.saveToFile("res/saves/save1.syg");
				Program.loadsave.loadFromFile("res/saves/save1.syg");
			}
			if (key == KeyEvent.VK_2) Program.loadsave.saveToFile("res/saves/save2.syg");
			if (key == KeyEvent.VK_3) Program.loadsave.saveToFile("res/saves/save3.syg");
		}
	}
	
	/*
	 * All this does is set bools false then calls changeVelocity.
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
			
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

		speed = player.getSpeed();
		
		if((w && s) || !(w || s))	player.setVelY(0);
		if(w && !s) 				player.setVelY(-1*speed);
		if(!w && s)					player.setVelY(speed);
		if((a && d) || !(a || d))	player.setVelX(0);
		if(a && !d)					player.setVelX(-1*speed);
		if(!a && d)					player.setVelX(speed);

	}
}
