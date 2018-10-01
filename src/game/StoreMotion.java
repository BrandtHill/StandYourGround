package game;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.Program.STATE;


public class StoreMotion extends MouseMotionAdapter{

	private Store store;

	
	public StoreMotion(Store store) {
		this.store = store;
	}
	
	public void mouseMoved(MouseEvent e) {	
		if (Program.gameState == STATE.StoreMenu) {
			for (Button b : store.buttons) {
				if (b != null && b.isActive()) {
					if (b.inBounds(e.getPoint())) {
						if (b.color == Color.WHITE)
							AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
						b.color = Color.GREEN;
					} else b.color = Color.WHITE;
				}
			} 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			for (Button b : store.buttons) {
				if (b != null && b.isActive()) {
					if (b.inBounds(e.getPoint())) {
						if (b.color == Color.WHITE)
							AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
						b.color = Color.CYAN;
					} else b.color = Color.WHITE;
				}
			} 
		}
	}
}
