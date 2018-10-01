package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import game.Store.Menu;

public class Button {

	private int x, y, w, h;
	private String line1, line2, line3;
	public Color displayColor, mainColor;
	private Font font;
	private boolean active;
	private static PlayerObject player;
	private Gun gun;
	
	public Button(int x, int y, boolean active, String line1, String line2, String line3) {
		this.x = x;
		this.y = y;
		this.w = 120;
		this.h = 80;
		this.active = active;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.gun = player.searchGun(line2);
		if (Store.menu == Menu.BuyGuns) {
			this.mainColor = gun.isOwned() ? Color.GRAY : Color.WHITE;
		}
		else {	
			this.mainColor = gun.isOwned() ?
						(gun.isSelected() ? 
								new Color(131, 53, 214) 
								: Color.WHITE) 
								: Color.GRAY;
		}
		this.displayColor = active ? this.mainColor : Color.DARK_GRAY;
		this.font = new Font("Arial", 1, 12);
	}
	
	public void render(Graphics g) {
		g.setColor(displayColor);
		g.setFont(font);
		g.draw3DRect(x, y, w, h, true);
		g.drawString(line1, x + 10, y + 20);
		g.drawString(line2, x + 20, y + 40);
		g.drawString(line3, x + 20, y + 60);
	}

	public boolean isActive() {return active;}
	public void setActive(boolean active) {this.active = active;}

	public static PlayerObject getPlayer() {return player;}
	public static void setPlayer(PlayerObject player) {Button.player = player;}

	public boolean inBounds(Point p) {
		if(p.getX() > x && p.getX() < (x + w)) {
			if(p.getY() > y && p.getY() < (y + h)) {
				return true;
			}
		}
		return false;
	}
}
