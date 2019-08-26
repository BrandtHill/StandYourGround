package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import game.Audio.AudioPlayer;
import game.GamePieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;

public class Store extends MouseAdapter{

	private static Player player;
	private int buttonX [] = {100, 260, 420, 580, 100, 260, 420, 580, 100, 260, 420, 580};
	private int buttonY [] = {100, 100, 100, 100, 200, 200, 200, 200, 300, 300, 300, 300};
	public Button[] buttons = new Button[12];
	public static Menu menu = Menu.Other;
	
	public static enum Menu{
		Other,
		BuyGuns,
		BuyUpgrades,
		SelectSidearm,
		SelectPrimary,
		SelectSecondary,
		Final
	}
	
	public Store() {
		player = Program.player;
		Button.setPlayer(player);
	}
	
	public void mousePressed(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {	
			Button b = Arrays.stream(buttons).filter(x -> x != null && x.inBounds(e.getPoint()) && x.isClickable()).findAny().orElse(null);
			
			if (b == null) return;
			
			switch (menu) {
			case BuyGuns:
				buyGun(b.getGun(), b.getPrice());
				break;
			case BuyUpgrades:
				if (b.getFirstLine().contains("Ammo")) 		upgradeCapacity(b.getGun(), b.getAmount(), b.getPrice());
				if (b.getFirstLine().contains("Mag")) 		upgradeMagSize(b.getGun(), b.getAmount(), b.getPrice());
				if (b.getFirstLine().contains("Hollow"))	upgradeRounds(b.getGun(), b.getPrice());
				if (b.getFirstLine().contains("Loads")) 	upgradeRounds(b.getGun(), b.getPrice());
				if (b.getFirstLine().contains("Auto")) 		upgradeFireMode(b.getGun(), b.getPrice());
				break;
			case SelectSidearm:
				player.equipSidearm(b.getGun());
				break;
			case SelectPrimary:
				player.equipPrimary(b.getGun());
				break;
			case SelectSecondary:
				player.equipSecondary(b.getGun());
				break;
			default:
				break;
			}
			
			Arrays.stream(buttons).filter(x -> x != null).forEach(x -> x.updateColor());
			
			player.setMoneyAtRoundStart(player.getMoney());
		}
	}

	public void nextMenu() {
		switch (menu) {
		case BuyGuns: 
			menu = Menu.BuyUpgrades;
			player.autoEquip();
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
		onMenuUpdate();
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
		onMenuUpdate();
	}
	
	public void onMenuUpdate() {
		Arrays.fill(buttons, null);
		
		switch (menu) {
		case BuyGuns:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "Buy", "AR-15", "$875");
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "Buy", "M77", "$650");
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "Buy", "Over-Under", "$525");
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "Buy", "Model 57", "$475");
			buttons[4] = new Button(buttonX[4], buttonY[4], true, "Buy", "PX4 Compact", "$350");
			break;
		case BuyUpgrades:
			buttons[0] = new Button(buttonX[0], buttonY[0], true, "Increase Ammo", "AR-15", "$300", 30);
			buttons[1] = new Button(buttonX[1], buttonY[1], true, "Increase Ammo", "M77", "$300", 9);
			buttons[2] = new Button(buttonX[2], buttonY[2], true, "Increase Ammo", "Over-Under", "$250", 8);
			buttons[3] = new Button(buttonX[3], buttonY[3], true, "Increase Ammo", "Model 57", "$200", 12);
			buttons[4] = new Button(buttonX[4], buttonY[4], true, "Increase Ammo", "PX4 Compact", "$175", 15);
			buttons[5] = new Button(buttonX[5], buttonY[5], true, "Increase Ammo", "Titan", "$150", 14);
			buttons[6] = new Button(buttonX[6], buttonY[6], true, "Increase Mag Size", "AR-15", "$500", 10);
			buttons[7] = new Button(buttonX[7], buttonY[7], true, "Drop-In Auto Sear", "AR-15", "$1100");
			buttons[8] = new Button(buttonX[8], buttonY[8], true, "Hollow Points", "PX4 Compact", "$350");
			buttons[9] = new Button(buttonX[9], buttonY[9], true, "Hand Loads", "M77", "$500");
			break;
		case SelectPrimary:
			for (int i = 0; i < player.getArsenal().size(); i++) {
				buttons[i] = new Button(buttonX[i], buttonY[i], true, "", player.getGunAt(i).getName(), "");
			}
			break;
		case SelectSecondary:
			for (int i = 0; i < player.getArsenal().size(); i++) {
				buttons[i] = new Button(buttonX[i], buttonY[i], true, "", player.getGunAt(i).getName(), "");
			}
			break;
		case SelectSidearm:
			for (int i = 0; i < player.getArsenal().size(); i++) {
				buttons[i] = new Button(buttonX[i], buttonY[i], player.getGunAt(i).isSidearm(), "", player.getGunAt(i).getName(), "");
			}
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		Arrays.stream(buttons).filter(x -> x != null).forEach(x -> x.render(g));
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
			player.renderPreview(g, player.getGunPrimary());
			drawEquipped(g);
			break;
		case SelectSidearm:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SIDEARM", 280, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			player.renderPreview(g, player.getGunSidearm());
			drawEquipped(g);
			break;
		case SelectSecondary:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("SELECT SECONDARY", 280, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			player.renderPreview(g, player.getGunSecondary());
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
	
	private void upgradeCapacity(Gun gun, int ammo, int money) {
		if (gun.isOwned()) {
			int cap = gun.getAmmoCapacity();
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setAmmoCapacity(ammo + cap);
				player.setMoney(player.getMoney() - money);
			} 
		}
	}
	
	private void upgradeMagSize(Gun gun, int ammo, int money) {
		if (gun.isOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				int mag = gun.getMagSize();
				int cap = gun.getAmmoCapacity();
				int m = player.getMoney();
				gun.setMagSize(mag + ammo);
				gun.setAmmoCapacity(cap + ammo);
				gun.setMagIncreased(true);
				player.setMoney(m - money);
			} 
		}		
	}
	
	private void upgradeRounds(Gun gun, int money) {
		if (gun.isOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setSpecialRounds(true);
				player.setMoney(player.getMoney() - money);
			}
		}
	}
	
	private void upgradeFireMode(Gun gun, int money) {
		if (gun.isOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setFullAuto(true);
				player.setMoney(player.getMoney() - money);
			}
		}
	}
	
	private void buyGun(Gun gun, int money) {
		if (!gun.isOwned()) {
			if (player.getMoney() >= money) {
				AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
				gun.setOwned(true);
				player.setMoney(player.getMoney() - money);
			} 
		}
	}
}
