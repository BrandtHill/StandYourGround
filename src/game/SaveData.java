package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;

import game.Main.STATE;
import game.Weapons.Gun;
import game.Weapons.Gun.GUN;

public class SaveData implements Serializable {

	private static final long serialVersionUID = 6194456681958310352L;
	
	private int money;
	private int level;
	private int bombs;
	private ArrayList<GunBean> gunBeans;

	class GunBean implements Serializable {
		private static final long serialVersionUID = -7906334672407292428L;
		GUN Id;
		int MagSize;
		int AmmoCap;
		boolean GunOwned;
		boolean FireMode;
		boolean SpecialRound;
		boolean MagIncreased;
		boolean ReloadImproved;
	}
	
	public SaveData() {
		File directory = new File("./res/saves");
		if (!directory.exists()) directory.mkdirs();
		gunBeans = new ArrayList<>();
	}
	
	public void syncToGameState() {
		money = Main.player.getMoneyAtRoundStart();
		level = Main.spawnSys.getLevel();
		bombs = Main.player.getBombsAtRoundStart();
		gunBeans.clear();
		for (int i = 0; i < Main.player.getArsenal().size(); i++) {
			Gun g = Main.player.getArsenal().get(i);
			GunBean b = new GunBean();
			b.Id = g.getId();
			b.MagSize = g.getMagSize();
			b.AmmoCap = g.getAmmoCapacity();
			b.GunOwned = g.isOwned();
			b.SpecialRound = g.isSpecialRounds();
			b.FireMode = g.isFullAuto();
			b.MagIncreased = g.isMagIncreased();
			b.ReloadImproved = g.isReloadImproved();
			gunBeans.add(i, b);
		}
		System.out.println(MessageFormat.format("Syncing to state: Level: {0}, Money: {1}, GunsOwned: {2}", level, money, gunBeans.stream().filter(g -> g.GunOwned).count()));
	}
	
	public void writeToFile(String filename) {
		try {
	        FileOutputStream file = new FileOutputStream(filename);
	        ObjectOutputStream out = new ObjectOutputStream(file);			
	        out.writeObject(this);
	        out.close();
	        file.close();
	        System.out.println(MessageFormat.format("Writing to {3}: Level: {0}, Money: {1}, GunsOwned: {2}", level, money, gunBeans.stream().filter(g -> g.GunOwned).count(), filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SaveData readFromFile(String filename) {
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			SaveData s = (SaveData)in.readObject();
			in.close();
			file.close();
			System.out.println("Loaded game state from: " + filename);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setGameState() {
		Main.player.setMoney(money);
		Main.player.setMoneyAtRoundStart(money);
		Main.player.setBombs(bombs);
		Main.player.setBombsAtRoundStart(bombs);
		Main.spawnSys.setLevel(level);
		for (GunBean b : gunBeans) {
			Gun g = Main.player.getGun(b.Id);
			g.setMagSize(b.MagSize);
			g.setAmmoCapacity(b.AmmoCap);
			g.setOwned(b.GunOwned);
			g.setFullAuto(b.FireMode);
			g.setSpecialRounds(b.SpecialRound);
			g.setMagIncreased(b.MagIncreased);
			g.setReloadImproved(b.ReloadImproved);
			g.resetAmmo();
		}
		Main.player.autoEquip(Main.spawnSys.sidearmsOnly());
		
		System.out.println(MessageFormat.format("Setting state: Level: {0}, Money: {1}, GunsOwned: {2}", level, money, gunBeans.stream().filter(g -> g.GunOwned).count()));
		Main.gameState = STATE.StoreMenu;
	}
}
