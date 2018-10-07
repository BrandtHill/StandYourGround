package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.Program.STATE;

public class MouseMotionInput extends MouseMotionAdapter {
	
	private static Reticle reticle;
	
	public MouseMotionInput() {
		reticle = Program.reticle;
	}

	public void mouseMoved(MouseEvent e) {
		reticle.setX(e.getX());
		reticle.setY(e.getY());
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}
