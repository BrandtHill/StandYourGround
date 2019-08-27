package game.Inputs;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.Button;
import game.Program;
import game.Store;
import game.Audio.AudioPlayer;
import game.Program.STATE;


public class StoreMotionInput extends MouseMotionAdapter{

	private Store store;
	
	public StoreMotionInput(Store store) {
		this.store = store;
	}
	
	public void mouseMoved(MouseEvent e) {	
		if (Program.gameState == STATE.StoreMenu) {
			for (Button b : store.buttons) {
				if (b != null && (b.isClickable() || b.displayColor == Color.GREEN)) {
					if (b.inBounds(e.getPoint())) {
						if (b.displayColor == b.mainColor) playBlip();
						b.displayColor = Color.GREEN;
					} else b.displayColor = b.mainColor;
				}
			} 
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	private void playBlip() {
		AudioPlayer.getSound("BlipMinor").setGain(0.7f);
		AudioPlayer.getSound("BlipMinor").play();
	}
}
