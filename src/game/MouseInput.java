package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.Program.STATE;
import game.Weapons.Gun;

public class MouseInput extends MouseAdapter{
	private static Player player;
	
	public MouseInput() {
		player = Program.player;
	}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (Program.gameState == STATE.InGame) {
			if (button == MouseEvent.BUTTON1) {
				Gun gun = player.getGunWielded();
				if (gun.isFullAuto()) {
					gun.resetTickDivier();
					gun.setShooting(true);
				} else gun.shoot();
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
}
