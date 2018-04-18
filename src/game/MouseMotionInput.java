package game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMotionInput extends MouseMotionAdapter {

	private Handler handler;
	private Point point;
	private GameObject player; 
	private GameObject reticle; 
	
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
		//System.out.println("M X: " + (int) reticle.getX() + " Y: " + (int) reticle.getY());
	}
	public void mouseDragged(MouseEvent e) {
		point = e.getPoint();
		reticle.setX(point.getX());
		reticle.setY(point.getY());
		//System.out.println("M X: " + (int) reticle.getX() + " Y: " + (int) reticle.getY());
	}

}
