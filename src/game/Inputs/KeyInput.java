package game.Inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.Program;
import game.Store;
import game.Pieces.Player;
import game.Program.STATE;

public class KeyInput extends KeyAdapter{
	
	private Store store;
	private static Player player;
	private boolean w, a, s, d;
	private double speed = 2;
	
	public KeyInput(Store store) {
		this.store = store;
		KeyInput.player = Program.player;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (Program.gameState) {
		
		case InGame:
			switch (key) {
			case KeyEvent.VK_W: w = true;
				break;
			case KeyEvent.VK_A: a = true;
				break;
			case KeyEvent.VK_S:	s = true;
				break;
			case KeyEvent.VK_D:	d = true;
				break;
			case KeyEvent.VK_1: player.switchToPrimary();
				break;
			case KeyEvent.VK_2: player.switchToSecondary();
				break;
			case KeyEvent.VK_3: player.switchToSidearm();
				break;
			case KeyEvent.VK_R: player.getGunWielded().reload();
				break;
			case KeyEvent.VK_ESCAPE:
				Program.gameState = STATE.PauseMenu;
				w = a = s = d = false;
				break;
			default:
				break;
			}
			changeVelocity();
			break;
			
		case GameOver:
			switch (key) {
			case KeyEvent.VK_R:
				Program.loadFromFile("./res/saves/autosave.syg/");
				Program.gameState = STATE.StoreMenu;
				break;
			case KeyEvent.VK_N:
				Program.loadFromFile("./res/saves/newgame.syg/");
				Program.gameState = STATE.StartMenu;
				break;
			case KeyEvent.VK_1:
				Program.saveToFile("./res/saves/save1.syg/");
				break;
			case KeyEvent.VK_2:
				Program.saveToFile("./res/saves/save2.syg/");
				break;
			case KeyEvent.VK_3:
				Program.saveToFile("./res/saves/save3.syg/");
				break;	
			default:
				break;
			}
			break;
		
		case PauseMenu:
			switch (key) {
			case KeyEvent.VK_ESCAPE:
				Program.gameState = STATE.InGame;
				break;
			default:
				break;
			}
			break;
		
		case StartMenu:
			switch (key) {
			case KeyEvent.VK_SPACE:
				Program.gameState = STATE.InGame;
				Program.commenceLevel();
				break;
			case KeyEvent.VK_1:
				Program.loadFromFile("./res/saves/save1.syg/");
				break;
			case KeyEvent.VK_2:
				Program.loadFromFile("./res/saves/save2.syg/");
				break;
			case KeyEvent.VK_3:
				Program.loadFromFile("./res/saves/save3.syg/");
				break;
			default:
				break;
			}
			break;
		
		case StoreMenu:
			switch (key) {
			case KeyEvent.VK_SPACE:
				store.nextMenu();
				break;
			case KeyEvent.VK_BACK_SPACE:
				store.prevMenu();
				break;
			case KeyEvent.VK_1:
				Program.saveToFile("./res/saves/save1.syg/");
				break;
			case KeyEvent.VK_2:
				Program.saveToFile("./res/saves/save2.syg/");
				break;
			case KeyEvent.VK_3:
				Program.saveToFile("./res/saves/save3.syg/");
				break;
			default:
				break;
			}
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * All this does is set bools false then calls changeVelocity.
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (key){
		case KeyEvent.VK_W: w = false;
			break;
		case KeyEvent.VK_A: a = false;
			break;
		case KeyEvent.VK_S: s = false;
			break;
		case KeyEvent.VK_D: d = false;
			break;
		default:
			break;
		}
		changeVelocity();
	}
	
	/**
	 * This handles all the cases so that there's fluid
	 * movement when opposing keys are pressed and released.
	 */
	private void changeVelocity() {
		speed = player.getSpeed();
		
		if	((w && s) || !(w || s))		player.setVelY(0);
		if	(w && !s) 					player.setVelY(-1*speed);
		if	(!w && s)					player.setVelY(speed);
		if	((a && d) || !(a || d))		player.setVelX(0);
		if	(a && !d)					player.setVelX(-1*speed);
		if	(!a && d)					player.setVelX(speed);
	}
}
