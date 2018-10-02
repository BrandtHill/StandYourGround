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
				if (b != null && b.isClickable()) {
					if (b.inBounds(e.getPoint())) {
						if (b.displayColor == b.mainColor)
							AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
						b.displayColor = Color.GREEN;
					} else b.displayColor = b.mainColor;
				}
			} 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			for (Button b : store.buttons) {
				if (b != null && b.isClickable()) {
					if (b.inBounds(e.getPoint())) {
						if (b.displayColor == b.mainColor)
							AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
						b.displayColor = Color.GREEN;
					} else b.displayColor = b.mainColor;
				}
			} 
		}
	}
}
