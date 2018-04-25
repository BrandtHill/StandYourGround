package game;


import static java.lang.Math.atan2;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.Program.STATE;


public class MouseInput extends MouseAdapter{
	
	private Handler handler;
	private PlayerObject player;
	private Point point;
	private double angle;
	
	public MouseInput(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void mousePressed(MouseEvent e) {
		
		int button = e.getButton();
		
		if(Program.gameState == STATE.InGame){
			if (button == MouseEvent.BUTTON1) {
				
				point = e.getPoint();
				Gun gun = player.getGun();
				player.setAngle(atan2(point.getX() - (player.getX() + 10), point.getY() - (player.getY() + 10)));
				
				gun.setTickDivider(0);
				
				if(gun.getFullAuto())
					gun.setShooting(true);
				else	
					gun.shoot(player.getAngle());
			}
		}
		else if(Program.gameState == STATE.GameOver){
			if (button == MouseEvent.BUTTON1) {
				point = e.getPoint();
			}
		}
		else if(Program.gameState == STATE.PauseMenu){
			if (button == MouseEvent.BUTTON1) {
				point = e.getPoint();
			}
		}
		else if(Program.gameState == STATE.StartMenu){
			if (button == MouseEvent.BUTTON1) {
				point = e.getPoint();
			}
		}
		else if(Program.gameState == STATE.StoreMenu){
			if (button == MouseEvent.BUTTON1) {
				point = e.getPoint();
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
		int button = e.getButton();
		
		if(Program.gameState == STATE.InGame) {
			player.getGun().setShooting(false);
		}
		
	}
}
