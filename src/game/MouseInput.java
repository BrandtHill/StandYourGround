package game;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import game.Program.STATE;

import static java.lang.Math.atan2;
import static java.lang.Math.PI;


public class MouseInput extends MouseAdapter{
	
	private Handler handler;
	private PlayerObject player;
	private Point point;
	private double angle;
	private Random r = new Random();
	
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
				angle = atan2(point.getX() - (player.getX() + 10), point.getY() - (player.getY() + 10));
				player.getGun().shoot(angle);
				//System.out.println("Angle: " + angle*180/PI + " Spread: " + spread*180/PI);
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
