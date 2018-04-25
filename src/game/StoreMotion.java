package game;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.Program.STATE;


public class StoreMotion extends MouseMotionAdapter{

	private Store store;

	
	public StoreMotion(Store s) {
		store = s;
	}
	
	public void mouseMoved(MouseEvent e) {
		
		if (Program.gameState == STATE.StoreMenu) {
			for (int i = 0; i < 4; i++) {
				if (store.inBounds(e.getPoint(), i)) {
					store.colors[i] = Color.GREEN;
				}
				else store.colors[i] = Color.WHITE;
			} 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			for (int i = 0; i < 4; i++) {
				if (store.inBounds(e.getPoint(), i)) {
					store.colors[i] = Color.CYAN;
				}
				else store.colors[i] = Color.WHITE;
			} 
		}
	}

}
