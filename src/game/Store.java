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
	public Color colors [] = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
	private int buttonX [] = {100, 260, 420, 580, 100, 260, 420, 580};
	private int buttonY [] = {100, 100, 100, 100, 200, 200, 200, 200};
	private int buttonW;
	private int buttonH;
	private int numButtons = colors.length;
	public static Menu menu;
	
	public static enum Menu{
		BuyGuns,
		BuyUpgrades,
		SelectSidearm,
		SelectPrimary,
		SelectSecondary;
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
			
			switch (menu) {
			case BuyGuns:
				if (inBounds(buttonX[0], buttonY[0]))
					buyGun("AR-15", 750);
				else if (inBounds(buttonX[1], buttonY[1]))
					buyGun("Over-Under", 450);
				else if (inBounds(buttonX[2], buttonY[2]))
					buyGun("M77", 600);
				else if (inBounds(buttonX[3], buttonY[3]))
					buyGun("PX4 Compact", 375);
				break;
			case BuyUpgrades:
				if (inBounds(buttonX[0], buttonY[0]))
					upgradeCapacity("AR-15", 30, 300);
				else if (inBounds(buttonX[1], buttonY[1]))
					upgradeCapacity("Over-Under", 8, 250);
				else if (inBounds(buttonX[2], buttonY[2]))
					upgradeCapacity("Titan", 14, 150);
				else if (inBounds(buttonX[3], buttonY[3]))
					upgradeMagSize("AR-15", 10, 500);
				break;
			case SelectPrimary:
				if (inBounds(buttonX[0], buttonY[0]))
					equipGun("AR-15", 1);
				else if (inBounds(buttonX[1], buttonY[1]))
					equipGun("Over-Under", 1);
				else if (inBounds(buttonX[2], buttonY[2]))
					equipGun("M77", 1);
				else if (inBounds(buttonX[3], buttonY[3]))
					equipGun("PX4 Compact", 1);
				else if (inBounds(buttonX[4], buttonY[4]))
					equipGun("Titan", 1);
				break;
			case SelectSecondary:
				if (inBounds(buttonX[0], buttonY[0]))
					equipGun("AR-15", 2);
				else if (inBounds(buttonX[1], buttonY[1]))
					equipGun("Over-Under", 2);
				else if (inBounds(buttonX[2], buttonY[2]))
					equipGun("M77", 2);
				else if (inBounds(buttonX[3], buttonY[3]))
					equipGun("PX4 Compact", 2);
				else if (inBounds(buttonX[4], buttonY[4]))
					equipGun("Titan", 2);
				break;
			case SelectSidearm:
				if (inBounds(buttonX[3], buttonY[3]))
					equipGun("PX4 Compact", 1);
				else if (inBounds(buttonX[4], buttonY[4]))
					equipGun("Titan", 1);
				break;
			default:
				break;
			
			}
			
			player.setMoneyAtRoundStart(player.getMoney());
		}
	}
	
	public void render(Graphics g) {
		
		switch (menu) {
		case BuyGuns:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("Buy", buttonX[0] + 10, buttonY[0] + 20);
			g.drawString("AR-15", buttonX[0] + 20, buttonY[0] + 40);
			g.drawString("$750", buttonX[0] + 20, buttonY[0] + 60);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Buy", buttonX[1] + 10, buttonY[1] + 20);
			g.drawString("Over-Under", buttonX[1] + 20, buttonY[1] + 40);
			g.drawString("$450", buttonX[1] + 20, buttonY[1] + 60);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("Buy", buttonX[2] + 10, buttonY[2] + 20);
			g.drawString("M77", buttonX[2] + 20, buttonY[2] + 40);
			g.drawString("$600", buttonX[2] + 20, buttonY[0] + 60);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("Buy", buttonX[3] + 10, buttonY[3] + 20);
			g.drawString("PX4 Compact", buttonX[3] + 20, buttonY[3] + 40);
			g.drawString("$375", buttonX[3] + 20, buttonY[3] + 60);
			break;
			
		case BuyUpgrades:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("Increase Capacity", buttonX[0] + 10, buttonY[0] + 20);
			g.drawString("AR-15", buttonX[0] + 20, buttonY[0] + 40);
			g.drawString("$300", buttonX[0] + 20, buttonY[0] + 60);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Increase Capacity", buttonX[1] + 10, buttonY[1] + 20);
			g.drawString("Over-Under", buttonX[1] + 20, buttonY[1] + 40);
			g.drawString("$250", buttonX[1] + 20, buttonY[1] + 60);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("Increase Capacity", buttonX[2] + 10, buttonY[2] + 20);
			g.drawString("Titan", buttonX[2] + 20, buttonY[2] + 40);
			g.drawString("$150", buttonX[2] + 20, buttonY[2] + 60);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("Increase Mag Size", buttonX[3] + 10, buttonY[3] + 20);
			g.drawString("AR-15", buttonX[3] + 20, buttonY[3] + 40);
			g.drawString("$500", buttonX[3] + 20, buttonY[3] + 60);
			break;
			
		case SelectPrimary:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT PRIMARY", 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("AR-15", buttonX[0] + 20, buttonY[0] + 40);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Over-Under", buttonX[1] + 20, buttonY[1] + 40);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("M77", buttonX[2] + 20, buttonY[2] + 40);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("PX4 Compact", buttonX[3] + 20, buttonY[3] + 40);
			
			g.setColor(colors[4]);
			g.draw3DRect(buttonX[4], buttonY[4], buttonW, buttonH, true);
			g.drawString("Titan", buttonX[4] + 20, buttonY[4] + 40);
			break;
			
		case SelectSecondary:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SECONDARY", 340, 50);
			g.drawString("PRESS SPACE TO COMMENCE NEXT LEVEL", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("AR-15", buttonX[0] + 20, buttonY[0] + 40);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Over-Under", buttonX[1] + 20, buttonY[1] + 40);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("M77", buttonX[2] + 20, buttonY[2] + 40);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("PX4 Compact", buttonX[3] + 20, buttonY[3] + 40);
			
			g.setColor(colors[4]);
			g.draw3DRect(buttonX[4], buttonY[4], buttonW, buttonH, true);
			g.drawString("Titan", buttonX[4] + 20, buttonY[4] + 40);
			break;
			
		case SelectSidearm:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SIDEARM", 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 120, 450);
			g.setFont(new Font("Arial", 1, 12));
			
			g.setColor(colors[0]);
			g.draw3DRect(buttonX[0], buttonY[0], buttonW, buttonH, true);
			g.drawString("AR-15", buttonX[0] + 20, buttonY[0] + 40);
			
			g.setColor(colors[1]);
			g.draw3DRect(buttonX[1], buttonY[1], buttonW, buttonH, true);
			g.drawString("Over-Under", buttonX[1] + 20, buttonY[1] + 40);
			
			g.setColor(colors[2]);
			g.draw3DRect(buttonX[2], buttonY[2], buttonW, buttonH, true);
			g.drawString("M77", buttonX[2] + 20, buttonY[2] + 40);
			
			g.setColor(colors[3]);
			g.draw3DRect(buttonX[3], buttonY[3], buttonW, buttonH, true);
			g.drawString("PX4 Compact", buttonX[3] + 20, buttonY[3] + 40);
			
			g.setColor(colors[4]);
			g.draw3DRect(buttonX[4], buttonY[4], buttonW, buttonH, true);
			g.drawString("Titan", buttonX[4] + 20, buttonY[4] + 40);
			break;
			
		default:
			break;
		
		}	
	}
	
	public boolean inBounds(int x, int y) {
		if(mX > x && mX < x+buttonW) {
			if(mY > y && mY < y+buttonH) {
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

		if (gun.getOwned()) {
			int cap = gun.getAmmoCapacity();
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setAmmoCapacity(ammo + cap);
				player.setMoney(player.getMoney() - money);
			} 
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
		
		if (gun.getOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				int mag = gun.getMagSize();
				int cap = gun.getAmmoCapacity();
				int m = player.getMoney();
				gun.setMagSize(mag + ammo);
				gun.setAmmoCapacity(cap + ammo);
				player.setMoney(m - money);
			} 
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

		if (!gun.getOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setOwned(true);
				player.setMoney(player.getMoney() - money);
			} 
		}
	}
	
	public void equipGun(String gunName, int index) {
		
		Gun gun = player.searchGun(gunName);
		if(gun.getOwned()) {
			if(!gun.getEquipped()) {
				gun.setEquipped(true);
				if(index == 0)
					player.setSidearm(gun);
				else if(index == 1)
					player.setPrimary(gun);
				else if(index == 2)
					player.setSecondary(gun);
			}
		}
	}

	public int getNumButtons() {
		return numButtons;
	}

	public void setNumButtons(int numButtons) {
		this.numButtons = numButtons;
	}

}
