package game;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Math.atan2;


public class MouseInput extends MouseAdapter{
	
	private Handler handler;
	private GameObject player;
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
		if(button == MouseEvent.BUTTON1) {
			point = e.getPoint();
			angle = atan2(point.getX()-player.getX(), point.getY()-player.getY());
			handler.addObject(new ProjectileObject(player.getX() + 10, player.getY() + 10, 20, angle));
		}
	}
}
