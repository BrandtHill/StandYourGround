package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import game.Inputs.Store;
import game.Weapons.Gun;

public class Button {

	private static Color COLOR_LIGHT_PURPLE = new Color(160, 160, 240);
	private static Color COLOR_DEEP_PURPLE = new Color(96, 64, 112);
	private static Color COLOR_RED_GRAY = new Color(120, 100, 100);
	private static Color COLOR_FADED_GREEN = new Color(80, 192, 128);
	private int x, y, w, h;
	private int amount;
	private String line1, line2, line3, tooltip;
	public Color displayColor, mainColor;
	private Font font;
	private boolean clickable, active;
	private Gun gun;
	
	public Button(int x, int y, boolean active, String line1, String line2, String line3, String tooltip) {
		this.x = x;
		this.y = y;
		this.w = 120;
		this.h = 80;
		this.active = active;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.tooltip = tooltip;
		this.gun = Main.player.getGun(line2);
		this.update();
		this.displayColor = active ? this.mainColor : Color.DARK_GRAY;
		this.font = new Font("Ariel", 1, 12);
	}
	
	public Button(int x, int y, boolean active, String line1, String line2, String line3, String tooltip, int amount) {
		this(x, y, active, line1, line2, line3, tooltip);
		this.amount = amount;
	}
	
	public void render(Graphics g) {
		g.setColor(displayColor);
		g.setFont(font);
		g.draw3DRect(x, y, w, h, true);
		g.drawString(line1, x + 10, y + 20);
		g.drawString(line2, x + 20, y + 40);
		g.drawString(line3, x + 20, y + 60);
	}

	public boolean isClickable() {return clickable;}

	public void update() {
		switch (Store.menu) {
		case BuyGuns:
			this.clickable = active && !gun.isOwned();
			this.mainColor = gun.isOwned() ? COLOR_RED_GRAY : Color.WHITE;
			break;

		case BuyAmmo:
			if (this.gun == null) {
				this.clickable = active && !Main.player.ownsBomb();
				this.mainColor = Main.player.ownsBomb() ? COLOR_RED_GRAY : Color.WHITE;
				break;
			}
		case BuyUpgrades:
			this.clickable = active && gun.isOwned() 
					&&(line1.contains("Ammo")
					|| line1.matches(".*(Hollow|Loads|Buck|Rounds).*") && !gun.isSpecialRounds()
					|| line1.contains("Auto")  && !gun.isFullAuto()
					|| line1.contains("Mags")  && !gun.isMagIncreased()
					|| line1.contains("Speed") && !gun.isReloadImproved());
			this.mainColor = gun.isOwned() ?
					   		(isClickable() ?
								  Color.WHITE
								: COLOR_RED_GRAY)
								: Color.GRAY;
			break;
			
		default:
			this.clickable = active && gun.isOwned() && !gun.isLockedIn() && !gun.isWielded();							
			this.mainColor = gun.isOwned() ?
							(gun.isLockedIn() ?
								  COLOR_RED_GRAY :
							(gun.isEquipped() ?
							(gun.isWielded() ?
								  COLOR_DEEP_PURPLE 
								: COLOR_LIGHT_PURPLE)
								: Color.WHITE))
								: Color.GRAY;
			break;
		}
	}
	
	public Gun getGun() {return this.gun;}
	public int getAmount() {return this.amount;}
	public int getPrice() {return (!line3.isEmpty()) ? Integer.parseInt(line3.substring(1)) : 0;}
	public boolean isActive() {return active;}
	public boolean isMainColor() {return this.displayColor == this.mainColor;}
	public void updateDisplay() {displayColor = mainColor;}
	public void updateHoverDisplay() {displayColor = clickable ? COLOR_FADED_GREEN : mainColor;}
	
	public boolean inBounds(Point p) {
		return p.getX() > x && p.getX() < (x + w) && p.getY() > y && p.getY() < (y + h);
	}
	
	public String getFirstLine() {
		return line1;
	}

	public void renderTooltip(Graphics g) {
		if (tooltip == null || tooltip.isEmpty()) return;
		double s = Math.min(Main.getXScale(), Main.getYScale());
		double x = Main.reticle.getX() / s;
		double y = Main.reticle.getY() / s;
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.fillRect((int)x, (int)y, 160, 72);
		g.setColor(Color.WHITE);
		g.drawRect((int)x, (int)y, 160, 72);
		String[] strs = tooltip.split("_");
		for (int i = 0; i < strs.length; i++) {
			g.drawString(strs[i], (int)x + 10, (int)y + 20 + i * 20);
		}
	}
}
