package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.Program.STATE;

public class MouseMotionInput extends MouseMotionAdapter {
	
	private static Reticle reticle;
	private static Player player;
	public MouseMotionInput() {
		reticle = Program.reticle;
		player = Program.player;
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if (Program.gameState == STATE.InGame) {
			//To test out different types of reticles
		}
		
		reticle.setX(x);
		reticle.setY(y);
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}
