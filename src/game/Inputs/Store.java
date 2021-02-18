package game.Inputs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.imageio.ImageIO;

import game.Button;
import game.MenuHelpers;
import game.Program;
import game.Audio.AudioPlayer;
import game.Pieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;

public class Store extends MouseAdapter {

	private static Player player;
	private int bX [] = {100, 260, 420, 580, 100, 260, 420, 580, 100, 260, 420, 580, 100, 260, 420, 580};
	private int bY [] = {75, 75, 75, 75, 165, 165, 165, 165, 255, 255, 255, 255, 345, 345, 345, 345};
	public Button[] buttons = new Button[16];
	public Button hover;
	public static BufferedImage pegboard;
	public static Menu menu = Menu.Other;
	
	public static enum Menu{
		Other,
		BuyGuns,
		BuyAmmo,
		BuyUpgrades,
		SelectSidearm,
		SelectPrimary,
		SelectSecondary,
		Final
	}
	
	public Store() {
		player = Program.player;
	}
	
	public void mousePressed(MouseEvent e) {
		if (Program.gameState == STATE.StoreMenu) {	
			Button b = Arrays.stream(buttons).filter(x -> x != null && x.inBounds(e.getPoint()) && x.isClickable()).findAny().orElse(null);
			
			if (b == null) return;
			
			switch (menu) {
			case BuyGuns:
				buyGun(b.getGun(), b.getPrice());
				break;
			case BuyAmmo:
				upgradeCapacity(b.getGun(), b.getAmount(), b.getPrice());
				break;
			case BuyUpgrades:
				if (b.getFirstLine().contains("Mags")) upgradeMagSize(b.getGun(), b.getAmount(), b.getPrice());
				if (b.getFirstLine().contains("Auto")) upgradeFireMode(b.getGun(), b.getPrice());
				if (b.getFirstLine().contains("Speed")) upgradeReload(b.getGun(), b.getPrice());
				if (b.getFirstLine().matches(".*(Hollow|Loads|Buck).*")) upgradeRounds(b.getGun(), b.getPrice());
				break;
			case SelectSidearm:
				player.equipSidearm(b.getGun());
				player.switchToSidearm();
				break;
			case SelectPrimary:
				player.equipPrimary(b.getGun());
				player.switchToPrimary();
				break;
			case SelectSecondary:
				player.equipSecondary(b.getGun());
				player.switchToSecondary();
				break;
			default:
				break;
			}
			
			Arrays.stream(buttons).filter(x -> x != null).forEach(x -> x.update());
			
			player.setMoneyAtRoundStart(player.getMoney());
			
			mouseMoved(e);
		}
	}

	public void nextMenu() {
		switch (menu) {
		case BuyGuns: 
			menu = Menu.BuyAmmo;
			player.autoEquip();
			break;
		case BuyAmmo: 
			menu = Menu.BuyUpgrades;
			break;
		case BuyUpgrades: 
			menu = Menu.SelectSidearm;
			player.switchToSidearm();
			break;
		case SelectSidearm: 
			menu = Menu.SelectPrimary;
			if (player.getGunSidearm() != null) player.getGunSidearm().lockIn();
			player.switchToPrimary();
			break;
		case SelectPrimary: 
			menu = Menu.SelectSecondary;
			if (player.getGunPrimary() != null) player.getGunPrimary().lockIn();
			player.switchToSecondary();
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
		case BuyAmmo: 
			menu = Menu.BuyGuns;
			break;
		case BuyUpgrades: 
			menu = Menu.BuyAmmo;
			break;
		case SelectSidearm: 
			menu = Menu.BuyUpgrades;
			break;
		case SelectPrimary: 
			menu = Menu.SelectSidearm;
			if (player.getGunSidearm() != null) player.getGunSidearm().unLock();
			player.switchToSidearm();
			break;
		case SelectSecondary: 
			menu = Menu.SelectPrimary;
			if (player.getGunPrimary() != null) player.getGunPrimary().unLock();
			player.switchToPrimary();
			break;
		case Final:
			menu = Menu.SelectSecondary;
			if (player.getGunSecondary() != null) player.getGunSecondary().unLock();
			player.switchToSecondary();
		default:
			break;		
		}
		onMenuUpdate();
	}
	
	public void onMenuUpdate() {
		Arrays.fill(buttons, null);
		int i = 0;
		
		switch (menu) {
		case BuyGuns:
			buttonHelper(i++, true, "Buy", "AKM", "$1200", "The klassic Kalashnikov_with a nice face lift_7.62x39mm, 30rd");
			buttonHelper(i++, true, "Buy", "AR-15", "$1100", "The classic 'Black Rifle'_with modern furniture_5.56x45mm, 30rd");
			buttonHelper(i++, true, "Buy", "M77", "$800", "Powerful Bolt Action Rifle_7mm Rem. Mag., 3rd");
			buttonHelper(i++, true, "Buy", "Model 12", "$750", "Classic Winchester Pump_Shotgun - Can slam fire_12 Gauge, 6rd");
			buttonHelper(i++, true, "Buy", "Over-Under", "$575", "Double-Barreled Shotgun_12 Gauge, 2rd");
			buttonHelper(i++, true, "Buy", "Model 57", "$525", "S&W Magnum Revolver_.41 Magnum, 6rd");			
			buttonHelper(i++, true, "Buy", "PX4 Compact", "$450", "Modern Handgun -_Compact PX4 Storm_9x19mm, 15rd");
			buttonHelper(i++, true, "Buy", "Judge", "$350", "Revolver that can shoot_.410 shot shells_.410 Bore, 5rd");
			buttonHelper(i++, true, "Buy", "Titan", "", "Pocket Pistol -_Better than nothing_.25 ACP, 7rd");
			break;
		case BuyAmmo:
			buttonHelper(i++, true, "Increase Ammo", "AKM", "$500", "30 more rounds", 30);
			buttonHelper(i++, true, "Increase Ammo", "AR-15", "$475", "30 more rounds", 30);
			buttonHelper(i++, true, "Increase Ammo", "M77", "$300", "9 more rounds", 9);
			buttonHelper(i++, true, "Increase Ammo", "Model 12", "$200", "6 more shells", 6);
			buttonHelper(i++, true, "Increase Ammo", "Over-Under", "$200", "6 more shells", 6);
			buttonHelper(i++, true, "Increase Ammo", "Model 57", "$250", "12 more rounds", 12);
			buttonHelper(i++, true, "Increase Ammo", "PX4 Compact", "$175", "15 more rounds", 15);
			buttonHelper(i++, true, "Increase Ammo", "Judge", "$200", "10 more shells", 10);
			buttonHelper(i++, true, "Increase Ammo", "Titan", "$75", "21 more rounds", 21);
			break;
		case BuyUpgrades:
			buttonHelper(i++, true, "RPK Mags", "AKM", "$600", "40-round RPK_magazines", 10);
			buttonHelper(i++, true, "Full Auto Sear", "AKM", "$1300", "Restore the AK to its_former glory");
			buttonHelper(i++, true, "Extended Mags", "AR-15", "$500", "40-round AR-15_magazines", 10);
			buttonHelper(i++, true, "Drop-In Auto Sear", "AR-15", "$1100", "Give AR-15 Select Fire_Capability");
			buttonHelper(i++, true, "Extended Mags", "PX4 Compact", "$500", "Use full-size PX4_Storm 20-round_extended magazsines", 5);
			buttonHelper(i++, true, "Hollow Points", "PX4 Compact", "$350", "Anti-Zombie Hollow_Point rounds");
			buttonHelper(i++, true, "Bear Loads", "Model 57", "$450", "Powerful rounds suitable_for stopping bears");
			buttonHelper(i++, true, "Speed Loaders", "Model 57", "$400", "Load cylinder with six_rounds at once");
			buttonHelper(i++, true, "Hand Loads", "M77", "$500", "High pressure, High_penetration, hand-loaded_ammunition");
			buttonHelper(i++, true, "000 Buckshot", "Over-Under", "$425", "Triple-Ought Buckshot -_Six heavy shot per shell");
			buttonHelper(i++, true, "000 Buckshot", "Model 12", "$425", "Triple-Ought Buckshot -_Six heavy shot per shell");
			break;
		case SelectPrimary:
			for (i = 0; i < Player.NUMGUNS; i++) buttonHelper(i, true, "", player.getGunAt(i).getName(), "", "");
			break;
		case SelectSecondary:
			for (i = 0; i < Player.NUMGUNS; i++) buttonHelper(i, true, "", player.getGunAt(i).getName(), "", "");
			break;
		case SelectSidearm:
			for (i = 0; i < Player.NUMGUNS; i++) buttonHelper(i, player.getGunAt(i).isSidearm(), "", player.getGunAt(i).getName(), "", "");
			break;
		default:
			break;
		}
		
		Arrays.stream(buttons).filter(x -> x != null).forEach(x -> x.update());
	}
	
	private void buttonHelper(int i, boolean active, String l1, String l2, String l3, String tooltip, int amt) {
		buttons[i] = new Button(bX[i], bY[i], active, l1, l2, l3, tooltip, amt);
	}
	
	private void buttonHelper(int i, boolean active, String l1, String l2, String l3, String tooltip) {
		buttons[i] = new Button(bX[i], bY[i], active, l1, l2, l3, tooltip);
	}
	
	public void render(Graphics g) {
		Arrays.stream(buttons).filter(x -> x != null).forEach(x -> x.render(g));
		Button b;
		switch (menu) {
		case BuyGuns:
			drawComponents(g, true, false, true, null);
			if ((b = hover) != null) b.renderTooltip(g);
			g.drawImage(pegboard, 220, 340, 384, 192, null);
			if ((b = hover) != null) {
				g.drawImage(b.getGun().getSprite(), 220, 340, 384, 192, null);
				b.renderTooltip(g);
			}
			break;
		case BuyAmmo:
			drawComponents(g, true, true, true, null);
			if ((b = hover) != null) {
				b.renderTooltip(g);
				g.setFont(new Font("Arial", 1, 24));
				g.drawString(MessageFormat.format("{0} : {1} (+ {2})", b.getGun().getName(), b.getGun().getMagSize(), b.getGun().getAmmoCapacity()), 300, 450);
			}
			break;
		case BuyUpgrades:
			drawComponents(g, true, true, true, null);
			if ((b = hover) != null) b.renderTooltip(g);
			break;
		case SelectPrimary:
			drawComponents(g, false, true, true, "SELECT PRIMARY");
			player.renderPreview(g, player.getGunPrimary());
			drawEquipped(g);
			break;
		case SelectSidearm:
			drawComponents(g, false, true, true, "SELECT SIDEARM");
			player.renderPreview(g, player.getGunSidearm());
			drawEquipped(g);
			break;
		case SelectSecondary:
			drawComponents(g, false, true, true, "SELECT SECONDARY");
			player.renderPreview(g, player.getGunSecondary());
			drawEquipped(g);
			break;
		case Final:
			drawEquippedFinal(g);
			MenuHelpers.renderCommand(g, 225, 425, "Space", "Commence Next Level");
			MenuHelpers.renderCommand(g, 225, 450, "1", "Save Current State to Save 1");
			MenuHelpers.renderCommand(g, 225, 475, "2", "Save Current State to Save 2");
			MenuHelpers.renderCommand(g, 225, 500, "3", "Save Current State to Save 3");
		default:
			break;
		}	
	}
	
	private void drawComponents(Graphics g, boolean doMoney, boolean doBack, boolean doForward, String header) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 24));
		if (doMoney) g.drawString("MONEY: $" + player.getMoney(), 340, 50);
		if (header != null) g.drawString(header, 280, 50);
		g.setFont(new Font("Arial", 1, 12));
		if (doBack) g.drawString("BACKSPACE", 100, 543);
		if (doForward) g.drawString("SPACE", 663, 543);
		g.setFont(new Font("Arial", 1, 48));
		if (doBack) g.drawString("←", 40, 550);
		if (doForward) g.drawString("→", 715, 550);
	}
	
	private void drawEquipped(Graphics g) {
		g.setColor(Color.GRAY);
		g.setFont(new Font("Monospaced", 0, 12));
		g.drawString("Primary   :  " + (player.getGunPrimary()   != null ? player.getGunPrimary().getName()   : "-"), 60, 390);
		g.drawString("Secondary :  " + (player.getGunSecondary() != null ? player.getGunSecondary().getName() : "-"), 60, 405);
		g.drawString("Sidearm   :  " + (player.getGunSidearm()   != null ? player.getGunSidearm().getName()   : "-"), 60, 420);
	}
	
	private void drawEquippedFinal(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Monospaced", 0, 30));
		g.drawString("Primary   :  " + (player.getGunPrimary()   != null ? player.getGunPrimary().getName()   : "-"), 60, 150);
		g.drawString("Secondary :  " + (player.getGunSecondary() != null ? player.getGunSecondary().getName() : "-"), 60, 250);
		g.drawString("Sidearm   :  " + (player.getGunSidearm()   != null ? player.getGunSidearm().getName()   : "-"), 60, 350);
		g.drawImage(pegboard, 550, 100, 160, 80, null);
		g.drawImage(pegboard, 550, 200, 160, 80, null);
		g.drawImage(pegboard, 550, 300, 160, 80, null);
		if (player.getGunPrimary()   != null) g.drawImage(player.getGunPrimary().getSprite(),   550, 100, 160, 80, null);
		if (player.getGunSecondary() != null) g.drawImage(player.getGunSecondary().getSprite(), 550, 200, 160, 80, null);
		if (player.getGunSidearm()   != null) g.drawImage(player.getGunSidearm().getSprite(),   550, 300, 160, 80, null);
	}
	
	private void upgradeCapacity(Gun gun, int ammo, int money) {
		if (gun.isOwned() && player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setAmmoCapacity(ammo + gun.getAmmoCapacity());
			player.setMoney(player.getMoney() - money);
		}
	}
	
	private void upgradeMagSize(Gun gun, int ammo, int money) {
		if (gun.isOwned() && player.getMoney() >= money) {
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
	
	private void upgradeReload(Gun gun, int money) {
		if (gun.isOwned() && player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setReloadImproved(true);
			player.setMoney(player.getMoney() - money);
		}
	}
	
	private void upgradeRounds(Gun gun, int money) {
		if (gun.isOwned() && player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setSpecialRounds(true);
			player.setMoney(player.getMoney() - money);
		}
	}
	
	private void upgradeFireMode(Gun gun, int money) {
		if (gun.isOwned() && player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setFullAuto(true);
			player.setMoney(player.getMoney() - money);
		}
	}
	
	private void buyGun(Gun gun, int money) {
		if (!gun.isOwned() && player.getMoney() >= money) {
			AudioPlayer.getSound("BlipMajor").play(1f, 0.7f);
			gun.setOwned(true);
			player.setMoney(player.getMoney() - money);
		}
	}
	
	public void mouseMoved(MouseEvent e) {	
		if (Program.gameState == STATE.StoreMenu) {
			hover = Arrays.stream(buttons)
			.filter(b -> b != null)
			.filter(b -> b.inBounds(e.getPoint()))
			.findFirst()
			.orElse(null);
			
			Arrays.stream(buttons)
			.filter(b -> b != null)
			.filter(b -> b != hover)
			.filter(b -> b.isActive())
			.forEach(b -> b.updateDisplay());
			
			if (hover == null || !hover.isActive()) return;
			
			if (hover.isClickable() && hover.isMainColor()) AudioPlayer.getSound("BlipMinor").play(1f, 0.7f);
			hover.updateHoverDisplay();
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	public static void loadAssets() {
		try (FileInputStream fis = new FileInputStream("./res/Pegboard.png")) {
			pegboard = ImageIO.read(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
