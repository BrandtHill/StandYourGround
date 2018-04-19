package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD {
	
	private Handler handler;
	private PlayerObject player;
	private String info;
	
	public HUD(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			e.getMessage();
		}
		info = new String();
	}
	

	public void tick() {
		Gun gun = player.getGun();
		info = gun.getName() + "    " + gun.getAmmoLoaded() + "/" + gun.getMagSize() + "   " + gun.getAmmoExtra();
	}
	
	public void render(Graphics g) {
		//Graphics2D g2d = (Graphics2D)g;
		g.setColor(new Color(170,170,170));
		g.draw3DRect(20, 20, 250, 30, true);
		g.setFont(new Font("Arial", 1, 16));
		g.drawString(info, 40, 42);
	}
}
