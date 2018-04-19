package game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static java.lang.Math.atan2;

public class MouseMotionInput extends MouseMotionAdapter {

	private Handler handler;
	private Point point;
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
		point = e.getPoint();
		reticle.setX(point.getX());
		reticle.setY(point.getY());
		player.setAngle(atan2(point.getX() - (player.getX() + 10), point.getY() - (player.getY() + 10)));
	}
	public void mouseDragged(MouseEvent e) {
		point = e.getPoint();
		reticle.setX(point.getX());
		reticle.setY(point.getY());
		player.setAngle(atan2(point.getX() - (player.getX() + 10), point.getY() - (player.getY() + 10)));
	}

}
