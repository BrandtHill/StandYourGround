package game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMotionInput extends MouseMotionAdapter {

	private Handler handler;
	private Point point;
	private GameObject player; 
	
	public MouseMotionInput(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void mouseMoved(MouseEvent e) {
		point = e.getPoint();
		//System.out.println("M X: " + (int) point.getX() + " Y: " + (int) point.getY());
	}

}
