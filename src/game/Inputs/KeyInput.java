package game.Inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.Main;
import game.Pieces.Player;
import game.Main.STATE;
import game.SaveData;

public class KeyInput extends KeyAdapter {
	
	private Store store;
	private Player player;
	private boolean w, a, s, d;
	
	public KeyInput() {
		this.store = Main.store;
		this.player = Main.player;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (Main.gameState) {
		
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
				Main.gameState = STATE.PauseMenu;
				w = a = s = d = false;
				break;
			default:
				break;
			}
			changeVelocity();
			break;
		case GameOverWin:
		case GameOver:
			switch (key) {
			case KeyEvent.VK_R:
				SaveData.loadFromFile("./res/saves/autosave.syg");
				Main.gameState = STATE.StoreMenu;
				break;
			case KeyEvent.VK_N:
				SaveData.loadFromFile("./res/saves/newgame.syg");
				Main.gameState = STATE.StartMenu;
				break;
			case KeyEvent.VK_1:
				SaveData.saveToFile("./res/saves/save1.syg");
				break;
			case KeyEvent.VK_2:
				SaveData.saveToFile("./res/saves/save2.syg");
				break;
			case KeyEvent.VK_3:
				SaveData.saveToFile("./res/saves/save3.syg");
				break;	
			default:
				break;
			}
			break;
		
		case PauseMenu:
			switch (key) {
			case KeyEvent.VK_ESCAPE:
				Main.gameState = STATE.InGame;
				break;
			default:
				break;
			}
			break;
		
		case StartMenu:
			switch (key) {
			case KeyEvent.VK_SPACE:
				Main.gameState = STATE.InGame;
				break;
			case KeyEvent.VK_1:
				SaveData.loadFromFile("./res/saves/save1.syg");
				break;
			case KeyEvent.VK_2:
				SaveData.loadFromFile("./res/saves/save2.syg");
				break;
			case KeyEvent.VK_3:
				SaveData.loadFromFile("./res/saves/save3.syg");
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
				SaveData.saveToFile("./res/saves/save1.syg");
				break;
			case KeyEvent.VK_2:
				SaveData.saveToFile("./res/saves/save2.syg");
				break;
			case KeyEvent.VK_3:
				SaveData.saveToFile("./res/saves/save3.syg");
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
		double speed = player.getSpeed();
		
		if	((w && s) || !(w || s))		player.setVelY(0);
		if	(w && !s) 					player.setVelY(-speed);
		if	(!w && s)					player.setVelY(speed);
		if	((a && d) || !(a || d))		player.setVelX(0);
		if	(a && !d)					player.setVelX(-speed);
		if	(!a && d)					player.setVelX(speed);
	}
}
