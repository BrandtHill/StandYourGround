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
			for (int i = 0; i < store.getNumButtons(); i++) {
				if (store.inBounds(e.getPoint(), i)) {
					if (store.colors[i] == Color.WHITE)
						AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
					store.colors[i] = Color.GREEN;
				}
				else store.colors[i] = Color.WHITE;
			} 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			for (int i = 0; i < store.getNumButtons(); i++) {
				if (store.inBounds(e.getPoint(), i)) {
					if (store.colors[i] == Color.WHITE)
						AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);	
					store.colors[i] = Color.CYAN;
				}
				else store.colors[i] = Color.WHITE;
			} 
		}
	}

}
