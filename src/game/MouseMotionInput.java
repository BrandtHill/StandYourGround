package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static java.lang.Math.atan2;

public class MouseMotionInput extends MouseMotionAdapter {

	private Handler handler;
	private PlayerObject player; 
	private GameObject reticle; 
	private double angle;
	
	public MouseMotionInput(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
			reticle = (ReticleObject)handler.getObjectAt(1);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void mouseMoved(MouseEvent e) {
		reticle.setX(e.getX());
		reticle.setY(e.getY());
		player.setAngle(atan2(e.getX() - (player.getX() + 10), e.getY() - (player.getY() + 10)));
	}
	public void mouseDragged(MouseEvent e) {
		reticle.setX(e.getX());
		reticle.setY(e.getY());
		player.setAngle(atan2(e.getX() - (player.getX() + 10), e.getY() - (player.getY() + 10)));
	}

}
