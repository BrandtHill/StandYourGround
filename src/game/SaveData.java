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
import java.util.List;

import game.Pieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;
import game.Weapons.Gun.GUN;

public class SaveData implements Serializable {

	private static final long serialVersionUID = 6194456681958310352L;
	
	private int money;
	private int level;
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
	
	public static void saveToFile(String filename) {
		SaveData s = new SaveData();
		s.saveInstanceToFile(filename);
	}
	
	private void saveInstanceToFile(String filename) {
		try {
			Player player = Program.player;
			SpawnSystem spawnSys = Program.spawnSys;
	        FileOutputStream file = new FileOutputStream(filename);
	        ObjectOutputStream out = new ObjectOutputStream(file);
			List<Gun> arsenal = player.getArsenal();
			
			money = player.getMoneyAtRoundStart();
			level = spawnSys.getLevel();
			gunBeans.clear();
			for (int i = 0; i < arsenal.size(); i++) {
				Gun g = arsenal.get(i);
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
	        
	        out.writeObject(this);
	        out.close();
	        file.close();
	        System.out.println(MessageFormat.format("Level: {0}, Money: {1}, GunsOwned: {2}", level, money, player.getArsenal().stream().filter(g -> g.isOwned()).count()));
	        System.out.println("Saved current game state: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFromFile(String filename) {
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			SaveData s = (SaveData)in.readObject();
			in.close();
			file.close();
			System.out.println("Loaded game state from: " + filename);
			s.setAfterLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setAfterLoad() {
		Player player = Program.player;
		SpawnSystem spawnSys = Program.spawnSys;
		
		player.setMoney(money);
		player.setMoneyAtRoundStart(money);
		spawnSys.setLevel(level);
		for (GunBean b : gunBeans) {
			Gun g = player.getGun(b.Id);
			g.setMagSize(b.MagSize);
			g.setAmmoCapacity(b.AmmoCap);
			g.setOwned(b.GunOwned);
			g.setFullAuto(b.FireMode);
			g.setSpecialRounds(b.SpecialRound);
			g.setMagIncreased(b.MagIncreased);
			g.setReloadImproved(b.ReloadImproved);
			g.resetAmmo();
		}
		player.autoEquip();
		
		System.out.println(MessageFormat.format("Level: {0}, Money: {1}, GunsOwned: {2}", level, money, player.getArsenal().stream().filter(g -> g.isOwned()).count()));
		Program.gameState = STATE.StoreMenu;
	}
}
