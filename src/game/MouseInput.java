package game;


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
				//angle = atan2(point.getX() - (player.getX() + 10), point.getY() - (player.getY() + 10));
				angle = player.getAngle();
				player.getGun().shoot(angle);
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
}
