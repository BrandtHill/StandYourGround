package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import game.Program.STATE;

public class Store extends MouseAdapter{

	private PlayerObject player;
	private int buttonX [] = {100, 260, 420, 580, 100, 260, 420, 580};
	private int buttonY [] = {100, 100, 100, 100, 200, 200, 200, 200};
	public Button[] buttons = new Button[8];
	public static Menu menu;
	
	public static enum Menu{
		Other,
		BuyGuns,
		BuyUpgrades,
		SelectSidearm,
		SelectPrimary,
		SelectSecondary,
		Final
	}
	
	public Store(Handler handler) {
		try {
			player = (PlayerObject)handler.getObjectAt(0);
			Button.setPlayer(player);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {
			switch (menu) {
			case BuyGuns:
				if (buttons[0].inBounds(e.getPoint()))
					buyGun("AR-15", 750);
				else if (buttons[1].inBounds(e.getPoint()))
					buyGun("Over-Under", 450);
				else if (buttons[2].inBounds(e.getPoint()))
					buyGun("M77", 600);
				else if (buttons[3].inBounds(e.getPoint()))
					buyGun("PX4 Compact", 375);
				break;
			case BuyUpgrades:
				if (buttons[0].inBounds(e.getPoint()))
					upgradeCapacity("AR-15", 30, 300);
				else if (buttons[1].inBounds(e.getPoint()))
					upgradeCapacity("Over-Under", 8, 250);
				else if (buttons[2].inBounds(e.getPoint()))
					upgradeCapacity("Titan", 14, 150);
				else if (buttons[3].inBounds(e.getPoint()))
					upgradeMagSize("AR-15", 10, 500);
				break;
			case SelectSidearm:
				if (buttons[3].inBounds(e.getPoint()))
					player.equipSidearm("PX4 Compact");
				else if (buttons[4].inBounds(e.getPoint()))
					player.equipSidearm("Titan");
				break;
			case SelectPrimary:
				if (buttons[0].inBounds(e.getPoint()))
					player.equipPrimary("AR-15");
				else if (buttons[1].inBounds(e.getPoint()))
					player.equipPrimary("Over-Under");
				else if (buttons[2].inBounds(e.getPoint()))
					player.equipPrimary("M77");
				else if (buttons[3].inBounds(e.getPoint()))
					player.equipPrimary("PX4 Compact");
				else if (buttons[4].inBounds(e.getPoint()))
					player.equipPrimary("Titan");
				break;
			case SelectSecondary:
				if (buttons[0].inBounds(e.getPoint()))
					player.equipSecondary("AR-15");
				else if (buttons[1].inBounds(e.getPoint()))
					player.equipSecondary("Over-Under");
				else if (buttons[2].inBounds(e.getPoint()))
					player.equipSecondary("M77");
				else if (buttons[3].inBounds(e.getPoint()))
					player.equipSecondary("PX4 Compact");
				else if (buttons[4].inBounds(e.getPoint()))
					player.equipSecondary("Titan");
				break;
			default:
				break;
			}
			
			for (Button b : buttons) {
				if (b != null) b.updateColor();
			}
			
			player.setMoneyAtRoundStart(player.getMoney());
		}
	}
	
	public void nextMenu() {
		switch (menu) {
		case BuyGuns: 
			menu = Menu.BuyUpgrades;
			break;
		case BuyUpgrades: 
			menu = Menu.SelectSidearm;
			break;
		case SelectSidearm: 
			menu = Menu.SelectPrimary;
			if (player.getGunSidearm() != null) player.getGunSidearm().lockIn();
			break;
		case SelectPrimary: 
			menu = Menu.SelectSecondary;
			if (player.getGunPrimary() != null) player.getGunPrimary().lockIn();
			break;
		case SelectSecondary: 
			menu = Menu.Final;
			if (player.getGunSecondary() != null) player.getGunSecondary().lockIn();
			break;
		case Final:
			menu = Menu.Other;
			player.unselectAll();
			Program.gameState = STATE.InGame;
		default:
			break;		
		}
		onMenuChange();
	}
	
	public void prevMenu() {
		switch (menu) {
		case BuyUpgrades: 
			menu = Menu.BuyGuns;
			break;
		case SelectSidearm: 
			menu = Menu.BuyUpgrades;
			break;
		case SelectPrimary: 
			menu = Menu.SelectSidearm;
			if (player.getGunSidearm() != null) player.getGunSidearm().unLock();
			break;
		case SelectSecondary: 
			menu = Menu.SelectPrimary;
			if (player.getGunPrimary() != null) player.getGunPrimary().unLock();
			break;
		case Final:
			menu = Menu.SelectSecondary;
			if (player.getGunSecondary() != null) player.getGunSecondary().unLock();
		default:
			break;		
		}
		onMenuChange();
	}
	
	public void onMenuChange() {
		Arrays.fill(buttons, null);
		
		switch (menu) {
		case BuyGuns:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "Buy", "AR-15", "$750");
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "Buy", "Over-Under", "$450");
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "Buy", "M77", "$600");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "Buy", "PX4 Compact", "$375");
			break;
		case BuyUpgrades:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "Increase Ammo", "AR-15", "$300");
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "Increase Ammo", "Over-Under", "$250");
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "Increase Ammo", "Titan", "$150");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "Increase Mag Size", "AR-15", "$500");
			break;
		case SelectPrimary:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "", "AR-15", "");
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "", "Over-Under", "");
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "", "M77", "");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "", "PX4 Compact", "");
			buttons[4] = new Button(buttonX[4], buttonY[4], true, "", "Titan", "");
			break;
		case SelectSecondary:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "", "AR-15", "");
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "", "Over-Under", "");
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "", "M77", "");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "", "PX4 Compact", "");
			buttons[4] = new Button(buttonX[4], buttonY[4], true, "", "Titan", "");
			break;
		case SelectSidearm:
			buttons[0] = new Button(buttonX[0], buttonY[0], false, "", "AR-15", "");
			buttons[1] = new Button(buttonX[1], buttonY[1], false, "", "Over-Under", "");
			buttons[2] = new Button(buttonX[2], buttonY[2], false, "", "M77", "");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "", "PX4 Compact", "");
			buttons[4] = new Button(buttonX[4], buttonY[4], true, "", "Titan", "");
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		
		for (Button b : buttons) {
			if (b != null) b.render(g);
		}
		switch (menu) {
		case BuyGuns:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			break;
		case BuyUpgrades:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			break;
		case SelectPrimary:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT PRIMARY", 280, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			drawEquipped(g);
			break;
		case SelectSidearm:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SIDEARM", 280, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			drawEquipped(g);
			break;
		case SelectSecondary:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SECONDARY", 280, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			drawEquipped(g);
			break;
		case Final:
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("Arial", 1, 48));
			g.drawString("Primary      :   " + (player.getGunPrimary() != null ? player.getGunPrimary().getName() : " -"), 100, 200);
			g.drawString("Secondary :   " + (player.getGunSecondary() != null ? player.getGunSecondary().getName() : " -"), 100, 280);
			g.drawString("Sidearm     :   " + (player.getGunSidearm() != null ? player.getGunSidearm().getName() : " -"), 100, 360);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("PRESS SPACE TO COMMENCE NEXT LEVEL", 150, 550);
		default:
			break;
		}	
	}
	
	private void drawEquipped(Graphics g) {
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial", 1, 10));
		g.drawString("Primary      :   " + (player.getGunPrimary() != null ? player.getGunPrimary().getName() : " -"), 70, 390);
		g.drawString("Secondary :   " + (player.getGunSecondary() != null ? player.getGunSecondary().getName() : " -"), 70, 405);
		g.drawString("Sidearm     :   " + (player.getGunSidearm() != null ? player.getGunSidearm().getName() : " -"), 70, 420);
	}
	
	public boolean inBounds(Point p, int i) {
		return buttons[i].inBounds(p);
	}
	
	public void upgradeCapacity(String gunName, int ammo, int money) {
		
		Gun gun = player.searchGun(gunName);
		if (gun.isOwned()) {
			int cap = gun.getAmmoCapacity();
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setAmmoCapacity(ammo + cap);
				player.setMoney(player.getMoney() - money);
			} 
		}
	}
	
	public void upgradeMagSize(String gunName, int ammo, int money) {

		Gun gun = player.searchGun(gunName);
		if (gun.isOwned()) {
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
		
		Gun gun = player.searchGun(gunName);
		if (!gun.isOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setOwned(true);
				player.setMoney(player.getMoney() - money);
			} 
		}
	}
}
