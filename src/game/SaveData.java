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

import game.Pieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;

public class SaveData implements Serializable {

	private static final long serialVersionUID = 6194456681958310352L;
	
	private int money;
	private int moneyAtRoundStart;
	private int level;
	private ArrayList<GunBean> gunBeans;

	class GunBean implements Serializable {
		int MagSize;
		int AmmoCap;
		boolean GunOwned;
		boolean FireMode;
		boolean SpecialRound;
		boolean MagIncreased;
	}
	
	public SaveData() {
		File directory = new File("./res/saves");
		if (!directory.exists()) directory.mkdirs();
		gunBeans = new ArrayList<>();
	}
	
	public void saveToFile(String filename, Player player) {
		try {
	        FileOutputStream file = new FileOutputStream(filename);
	        ObjectOutputStream out = new ObjectOutputStream(file);
			ArrayList<Gun> arsenal = player.getArsenal();
			
			money = player.getMoney();
			moneyAtRoundStart = player.getMoneyAtRoundStart();
			level = player.getLevel();
			for (int i = 0; i < arsenal.size(); i++) {
				Gun g = arsenal.get(i);
				GunBean b = new GunBean();
				b.MagSize = g.getMagSize();
				b.AmmoCap = g.getAmmoCapacity();
				b.GunOwned = g.isOwned();
				b.SpecialRound = g.isSpecialRounds();
				b.FireMode = g.isFullAuto();
				b.MagIncreased = g.isMagIncreased();
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
	
	public static SaveData loadFromFile(String filename) {
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			SaveData temp = (SaveData)in.readObject();
			in.close();
			file.close();
			System.out.println("Loaded game state from: " + filename);
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPlayerAfterLoad(Player player) {
		ArrayList<Gun> arsenal = player.getArsenal();
		player.setMoney(moneyAtRoundStart);
		player.setMoneyAtRoundStart(moneyAtRoundStart);
		player.setLevel(level);
		Program.spawnSys.setLevel(level);
		for (int i = 0; i < arsenal.size(); i++) {
			Gun g = arsenal.get(i);
			GunBean b = gunBeans.get(i);
			g.setMagSize(b.MagSize);
			g.setAmmoCapacity(b.AmmoCap);
			g.setOwned(b.GunOwned);
			g.setFullAuto(b.FireMode);
			g.setSpecialRounds(b.SpecialRound);
			g.setMagIncreased(b.MagIncreased);
			g.resetAmmo();
		}
		player.autoEquip();
		
		System.out.println(MessageFormat.format("Level: {0}, Money: {1}, GunsOwned: {2}", level, money, player.getArsenal().stream().filter(g -> g.isOwned()).count()));
		Program.gameState = STATE.StoreMenu;
	}
}
