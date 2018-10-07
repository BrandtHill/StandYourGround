package game;


import static java.lang.Math.atan2;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.Program.STATE;
import game.Weapons.Gun;


public class MouseInput extends MouseAdapter{
	
	private static Player player;
	private static Reticle reticle;
	
	public MouseInput() {
		player = Program.player;
		reticle = Program.reticle;
	}
	
	public void mousePressed(MouseEvent e) {
		
		int button = e.getButton();
		
		if(Program.gameState == STATE.InGame){
			if (button == MouseEvent.BUTTON1) {
				Gun gun = player.getGunWielded();
				player.setAngle(atan2(reticle.getXDisplay() - (player.getX() + 10), reticle.getYDisplay() - (player.getY() + 10)));
				
				if(gun.isFullAuto()) {
					gun.resetTickDivier();
					gun.setShooting(true);
				}
				else	
					gun.shoot();
			}
		}
		else if(Program.gameState == STATE.GameOver){
			if (button == MouseEvent.BUTTON1) {
			}
		}
		else if(Program.gameState == STATE.PauseMenu){
			if (button == MouseEvent.BUTTON1) {
			}
		}
		else if(Program.gameState == STATE.StartMenu){
			if (button == MouseEvent.BUTTON1) {
			}
		}
		else if(Program.gameState == STATE.StoreMenu){
			if (button == MouseEvent.BUTTON1) {
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
		int button = e.getButton();
		
		if(Program.gameState == STATE.InGame) {
			if (button == MouseEvent.BUTTON1) {
				player.getGunWielded().setShooting(false);
			}
		}
		
	}
}
