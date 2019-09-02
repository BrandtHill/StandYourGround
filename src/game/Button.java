package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import game.Inputs.Store;
import game.Inputs.Store.Menu;
import game.Pieces.Player;
import game.Pieces.Reticle;
import game.Weapons.Gun;

public class Button {

	private static Color COLOR_NOT_CLICKABLE = new Color(160, 160, 240);
	private static Color COLOR_EQUIPPED = new Color(131, 53, 214);
	private int x, y, w, h;
	private int amount;
	private String line1, line2, line3, tooltip;
	public Color displayColor, mainColor;
	private Font font;
	private boolean clickable, active;
	private static Player player = Program.player;
	private static Reticle reticle = Program.reticle;
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
		this.gun = player.getGun(line2);
		this.updateColor();
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

	public void updateColor() {
		if (Store.menu == Menu.BuyGuns) {
			this.clickable = active && !gun.isOwned();
			this.mainColor = gun.isOwned() ? Color.GRAY : Color.WHITE;
		} else if(Store.menu == Menu.BuyUpgrades) {
			this.clickable = active && gun.isOwned() 
					&&(line1.contains("Increase Ammo")
					|| line1.contains("Hollow") && !gun.isSpecialRounds()
					|| line1.contains("Loads") && !gun.isSpecialRounds()
					|| line1.contains("Buck") && !gun.isSpecialRounds()
					|| line1.contains("Auto") && !gun.isFullAuto()
					|| line1.contains("Mag Size") && !gun.isMagIncreased());
			this.mainColor = gun.isOwned() ?
							(isClickable() ?
								  Color.WHITE
								: COLOR_NOT_CLICKABLE)
								: Color.GRAY;
		} else {
			this.clickable = active && gun.isOwned() && !gun.isLockedIn();
			this.mainColor = gun.isOwned() ?
							(gun.isEquipped() ? 
							(gun.isLockedIn() ?
								  COLOR_EQUIPPED
								: COLOR_NOT_CLICKABLE)
								: Color.WHITE) 
								: Color.GRAY;
		}
	}
	
	public Gun getGun() {return this.gun;}
	public int getAmount() {return this.amount;}
	public int getPrice() {return (!line3.isEmpty()) ? Integer.parseInt(line3.substring(1)) : 0;}
	
	public boolean inBounds(Point p) {
		return p.getX() > x && p.getX() < (x + w) && p.getY() > y && p.getY() < (y + h);
	}
	
	public String getFirstLine() {
		return line1;
	}

	public void renderTooltip(Graphics g) {
		if (tooltip == null || tooltip.isEmpty()) return;
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.fillRect((int)reticle.getX(), (int)reticle.getY(), 160, 72);
		g.setColor(Color.WHITE);
		g.drawRect((int)reticle.getX(), (int)reticle.getY(), 160, 72);
		String[] strs = tooltip.split("_");
		for (int i = 0; i < strs.length; i++) {
			g.drawString(strs[i], (int)reticle.getX() + 10, (int)reticle.getY() + 20 + i * 20);
		}
	}
}
