package game.Inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import game.Program.STATE;
import game.Program;
import game.Pieces.Player;
import game.Pieces.Reticle;
import game.Weapons.Gun;

public class MouseInput extends MouseAdapter {
	private static Player player;
	private static Reticle reticle;
	
	public MouseInput() {
		player = Program.player;
		reticle = Program.reticle;
	}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (Program.gameState == STATE.InGame) {
			if (button == MouseEvent.BUTTON1) {
				Gun gun = player.getGunWielded();
				gun.setShooting(true);
				if (gun.isFullAuto()) gun.resetTickDivier();
				else gun.shoot();
			}
		}
		else if(Program.gameState == STATE.GameOver);
		else if(Program.gameState == STATE.PauseMenu);
		else if(Program.gameState == STATE.StartMenu);
		else if(Program.gameState == STATE.StoreMenu);
	}
	
	public void mouseReleased(MouseEvent e) {
		if (Program.gameState == STATE.InGame) {
			if (e.getButton() == MouseEvent.BUTTON1) player.getGunWielded().setShooting(false);
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (Program.gameState == STATE.InGame) {
			if (e.getWheelRotation() == -1) {player.switchToPrevious();}
			if (e.getWheelRotation() == 1) {player.switchToNext();}
		}
	}

	public void mouseMoved(MouseEvent e) {
		reticle.setX(e.getX());
		reticle.setY(e.getY());
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}
