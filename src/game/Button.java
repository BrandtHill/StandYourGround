package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class Button {

	private int x, y, w, h;
	private String line1, line2, line3;
	public Color color;
	private Font font;
	private boolean active;
	
	public Button(int x, int y, boolean active, String line1, String line2, String line3) {
		this.x = x;
		this.y = y;
		this.w = 120;
		this.h = 80;
		this.active = active;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.color = Color.WHITE;
		this.font = new Font("Arial", 1, 12);
	}
	
	public void render(Graphics g) {
		g.setColor(active ? color : Color.GRAY);
		g.setFont(font);
		g.draw3DRect(x, y, w, h, true);
		g.drawString(line1, x + 10, y + 20);
		g.drawString(line2, x + 20, y + 40);
		g.drawString(line3, x + 20, y + 60);
	}

	public boolean isActive() {return active;}
	public void setActive(boolean active) {this.active = active;}

	public boolean inBounds(Point p) {
		if(p.getX() > x && p.getX() < (x + w)) {
			if(p.getY() > y && p.getY() < (y + h)) {
				return true;
			}
		}
		return false;
	}
}
