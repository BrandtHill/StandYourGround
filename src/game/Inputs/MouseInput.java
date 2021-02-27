package game.Inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import game.Main.STATE;
import game.Main;
import game.Pieces.Grenade;
import game.Pieces.Player;
import game.Pieces.Reticle;
import game.Weapons.Gun;

public class MouseInput extends MouseAdapter {
	private static Player player;
	private static Reticle reticle;
	
	public MouseInput() {
		player = Main.player;
		reticle = Main.reticle;
	}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (Main.gameState == STATE.InGame) {
			if (button == MouseEvent.BUTTON1) {
				Gun gun = player.getGunWielded();
				gun.setShooting(true);
				if (gun.isFullAuto()) gun.resetTickDivier();
				else gun.shoot();
			} else if (button == MouseEvent.BUTTON3) {
				//Main.handler.addObjectAsync(new Grenade(reticle.getX()/(Main.SCALE * Main.getXScale()) - player.getXOffset(), reticle.getY()/(Main.SCALE * Main.getYScale()) - player.getYOffset()));
			}
		}
		else if(Main.gameState == STATE.GameOver);
		else if(Main.gameState == STATE.PauseMenu);
		else if(Main.gameState == STATE.StartMenu);
		else if(Main.gameState == STATE.StoreMenu);
	}
	
	public void mouseReleased(MouseEvent e) {
		if (Main.gameState == STATE.InGame) {
			if (e.getButton() == MouseEvent.BUTTON1) player.getGunWielded().setShooting(false);
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (Main.gameState == STATE.InGame) {
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
