package game;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;


public class MouseInput extends MouseAdapter{
	
	private Handler handler;
	private GameObject player;
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
		if(button == MouseEvent.BUTTON1) {
			double spread = (r.nextDouble()-0.5)*7*PI/180;
			point = e.getPoint();
			angle = atan2(point.getX()-(player.getX()+10), point.getY()-(player.getY()+10));
			//System.out.println("Angle: " + angle*180/PI + " Spread: " + spread*180/PI);
			handler.addObject(new ProjectileObject(player.getX() + 10, player.getY() + 10, 20, angle+spread, handler));
			AudioPlayer.getSound("Pistol").play(1.0f, 0.25f);
		}
	}
}
