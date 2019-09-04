package game.Inputs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import game.Button;
import game.Program;
import game.Audio.AudioPlayer;
import game.Pieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;

public class Store extends MouseAdapter {

	private static Player player;
	private int bX [] = {100, 260, 420, 580, 100, 260, 420, 580, 100, 260, 420, 580, 100, 260, 420, 580};
	private int bY [] = {100, 100, 100, 100, 200, 200, 200, 200, 300, 300, 300, 300, 400, 400, 400, 400};
	public Button[] buttons = new Button[16];
	public Button hover;
	public static BufferedImage pegboard;
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
				if (b.getFirstLine().contains("Buck"))		upgradeRounds(b.getGun(), b.getPrice());
				if (b.getFirstLine().contains("Auto")) 		upgradeFireMode(b.getGun(), b.getPrice());
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
			menu = Menu.BuyUpgrades;
			player.autoEquip();
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
		case BuyUpgrades: 
			menu = Menu.BuyGuns;
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
		
		switch (menu) {
		case BuyGuns:
			buttonHelper(0, true, "Buy", "AR-15", "$925", "The classic 'Black Rifle'_with modern furniture_5.56x45mm, 30rd");
			buttonHelper(1, true, "Buy", "M77", "$750", "Powerful Bolt Action Rifle_7mm Rem. Mag., 3rd");
			buttonHelper(2, true, "Buy", "Model 12", "$625", "Classic Winchester Pump_Shotgun - Can slam fire_12 Gauge, 6rd");
			buttonHelper(3, true, "Buy", "Over-Under", "$525", "Double-Barreled Shotgun_12 Gauge, 2rd");
			buttonHelper(4, true, "Buy", "Model 57", "$475", "S&W Magnum Revolver_.41 Magnum, 6rd");
			buttonHelper(5, true, "Buy", "Judge", "$475", "Revolver that can shoot_.410 shot shells_.410 Bore, 5rd");
			buttonHelper(6, true, "Buy", "PX4 Compact", "$350", "Modern Handgun -_Compact PX4 Storm_9x19mm, 15rd");
			buttonHelper(7, true, "Buy", "Titan", "", "Pocket Pistol -_Better than nothing_.25 ACP, 7rd");
			break;
		case BuyUpgrades:
			buttonHelper(0, true, "Increase Ammo", "AR-15", "$375", "30 more rounds", 30);
			buttonHelper(1, true, "Increase Ammo", "M77", "$300", "9 more rounds", 9);
			buttonHelper(2, true, "Increase Ammo", "Model 12", "$200", "6 more shells", 6);
			buttonHelper(3, true, "Increase Ammo", "Over-Under", "$250", "8 more shells", 8);
			buttonHelper(4, true, "Increase Ammo", "Model 57", "$200", "12 more rounds", 12);
			buttonHelper(5, true, "Increase Ammo", "Judge", "$200", "12 more rounds", 12);
			buttonHelper(6, true, "Increase Ammo", "PX4 Compact", "$175", "15 more rounds", 15);
			buttonHelper(7, true, "Increase Ammo", "Titan", "$75", "14 more rounds", 14);
			buttonHelper(8, true, "Increase Mag Size", "AR-15", "$500", "40-round AR-15_magazines", 10);
			buttonHelper(9, true, "Drop-In Auto Sear", "AR-15", "$1100", "Give AR-15 Select Fire_Capability");
			buttonHelper(10,true, "Hollow Points", "PX4 Compact", "$350", "Anti-Zombie Hollow_Point rounds");
			buttonHelper(11,true, "Hand Loads", "M77", "$500", "High pressure, High_penetration, hand-loaded_ammunition");
			buttonHelper(12,true, "000 Buckshot", "Over-Under", "$525", "Triple-Ought Buckshot -_Six heavy shot per shell");
			buttonHelper(13,true, "000 Buckshot", "Model 12", "$525", "Triple-Ought Buckshot -_Six heavy shot per shell");
			break;
		case SelectPrimary:
			for (int i = 0; i < Player.NUMGUNS; i++) {
				buttonHelper(i, true, "", player.getGunAt(i).getName(), "", "");
			}
			break;
		case SelectSecondary:
			for (int i = 0; i < Player.NUMGUNS; i++) {
				buttonHelper(i, true, "", player.getGunAt(i).getName(), "", "");
			}
			break;
		case SelectSidearm:
			for (int i = 0; i < Player.NUMGUNS; i++) {
				buttonHelper(i, player.getGunAt(i).isSidearm(), "", player.getGunAt(i).getName(), "", "");
			}
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
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			g.drawImage(pegboard, 220, 320, 384, 192, null);
			if ((b = hover) != null) {
				g.drawImage(b.getGun().getSprite(), 220, 320, 384, 192, null);
				b.renderTooltip(g);
			}
			break;
		case BuyUpgrades:
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 24));
			g.drawString("MONEY: $" + player.getMoney(), 340, 50);
			g.drawString("PRESS SPACE TO CONTINUE", 210, 550);
			if ((b = hover) != null) b.renderTooltip(g);
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
	
	static {
		try (FileInputStream fis = new FileInputStream("./res/Pegboard.png")) {
			pegboard = ImageIO.read(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
