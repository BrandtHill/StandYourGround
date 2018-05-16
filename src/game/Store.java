package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import game.Program.STATE;

public class Store extends MouseAdapter{

	private PlayerObject player;
	private Handler handler;
	private int mX, mY;
	public Color colors [] = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
	private int buttonX [] = {100, 260, 420, 580};
	private int buttonY [] = {100, 100, 100, 100};
	private int buttonW;
	private int buttonH;
	public static Menu menu;
	
	public static enum Menu{
		BuyGuns,
		BuyUpgrades,
		SelectGuns
	}
	
	public Store(Handler h) {
		handler = h;
		try {
			player = (PlayerObject)handler.getObjectAt(0);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		buttonW = 120;
		buttonH = 80;
	}
	
	public void mousePressed(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			mX = e.getX();
			mY = e.getY();
			if (menu == Menu.BuyUpgrades) {
				if (inBounds(buttonX[0], buttonY[0], buttonW, buttonH))
					upgradeCapacity("AR-15", 30, 300);
				else if (inBounds(buttonX[1], buttonY[1], buttonW, buttonH))
					upgradeCapacity("Over-Under", 8, 250);
				else if (inBounds(buttonX[2], buttonY[2], buttonW, buttonH))
					upgradeCapacity("Titan", 14, 200);
				else if (inBounds(buttonX[3], buttonY[3], buttonW, buttonH))
					upgradeMagSize("AR-15", 10, 500);
			}
			if (menu == Menu.BuyGuns) {
				if (inBounds(buttonX[0], buttonY[0], buttonW, buttonH))
					buyGun("AR-15", 750);
				else if (inBounds(buttonX[1], buttonY[1], buttonW, buttonH))
					buyGun("Over-Under", 550);
			}
			player.setMoneyAtRoundStart(player.getMoney());
		}
	}
	
	public void render(Graphics g) {
		if (menu == Menu.BuyGuns) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("Buy", 110, 120);
			g.drawString("AR-15", 120, 140);
			g.drawString("$750", 120, 160);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Buy", 270, 120);
			g.drawString("Over-Under", 280, 140);
			g.drawString("$550", 280, 160);

		}
		if (menu == Menu.BuyUpgrades) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO COMMENCE NEXT LEVEL", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("Increase Capacity", 110, 120);
			g.drawString("AR-15", 120, 140);
			g.drawString("$300", 120, 160);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Increase Capacity", 270, 120);
			g.drawString("Over-Under", 280, 140);
			g.drawString("$250", 280, 160);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("Increase Capacity", 430, 120);
			g.drawString("Titan", 440, 140);
			g.drawString("$200", 440, 160);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("Increase Mag Size", 590, 120);
			g.drawString("AR-15", 600, 140);
			g.drawString("$500", 600, 160);
		}
		
	}
	
	public boolean inBounds(int x, int y, int w, int h) {
		if(mX > x && mX < x+w) {
			if(mY > y && mY < y+h) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inBounds(Point p, int i) {
		if(p.getX() > buttonX[i] && p.getX() < buttonX[i] + buttonW) {
			if(p.getY() > buttonY[i] && p.getY() < buttonY[i] + buttonH) {
				return true;
			}
		}
		return false;
	}
	
	public void upgradeCapacity(String gunName, int ammo, int money) {
		
		LinkedList<Gun> arsenal = player.getArsenal();
		Gun gun = null;
		for(int i = 0; i < arsenal.size(); i++) {
			if(arsenal.get(i).getName().equals(gunName)) {
				gun = arsenal.get(i);
				break;
			}
		}

		int cap = gun.getAmmoCapacity();
		if(player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setAmmoCapacity(ammo+cap);
			player.setMoney(player.getMoney() - money);
		}
	}
	
	public void upgradeMagSize(String gunName, int ammo, int money) {
		
		LinkedList<Gun> arsenal = player.getArsenal();
		Gun gun = null;
		for(int i = 0; i < arsenal.size(); i++) {
			if(arsenal.get(i).getName().equals(gunName)) {
				gun = arsenal.get(i);
				break;
			}
		}
		
		if(player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			int mag = gun.getMagSize();
			int cap = gun.getAmmoCapacity();
			int m = player.getMoney();
			gun.setMagSize(mag + ammo);
			gun.setAmmoCapacity(cap + ammo);
			player.setMoney(m - money);
		}		
	}
	
	public void buyGun(String gunName, int money) {
		
		LinkedList<Gun> arsenal = player.getArsenal();
		Gun gun = null;
		for(int i = 0; i < arsenal.size(); i++) {
			if(arsenal.get(i).getName().equals(gunName)) {
				gun = arsenal.get(i);
				break;
			}
		}

		if(player.getMoney() >= money && !gun.getOwned()) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setOwned(true);
			player.setMoney(player.getMoney() - money);
		}
	}

}
