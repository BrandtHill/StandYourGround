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

import game.GamePieces.Player;
import game.Program.STATE;
import game.Weapons.Gun;

public class SaveData implements Serializable {

	private static final long serialVersionUID = 6194456681958310352L;
	
	private int money;
	private int moneyAtRoundStart;
	private int level;
	private ArrayList<Integer> magSizes;
	private ArrayList<Integer> ammoCaps;
	private ArrayList<Boolean> gunsOwned;
	private ArrayList<Boolean> fireModes;
	private ArrayList<Boolean> specialRounds;
	private ArrayList<Boolean> magIncreased;
	
	public SaveData() {
		File directory = new File("./res/saves");
		if (!directory.exists()) directory.mkdirs();
	}
	
	public void saveToFile(String filename, Player player) {
		try {
	        FileOutputStream file = new FileOutputStream(filename);
	        ObjectOutputStream out = new ObjectOutputStream(file);
	         
			ArrayList<Gun> arsenal;
			magSizes = new ArrayList<Integer>();
			ammoCaps = new ArrayList<Integer>();
			gunsOwned = new ArrayList<Boolean>();
			fireModes = new ArrayList<Boolean>();
			specialRounds = new ArrayList<Boolean>();
			magIncreased = new ArrayList<Boolean>();
			money = player.getMoney();
			moneyAtRoundStart = player.getMoneyAtRoundStart();
			level = player.getLevel();
			arsenal = player.getArsenal();
			for(int i = 0; i < arsenal.size(); i++) {
				Gun g = arsenal.get(i);
				magSizes.add(i, g.getMagSize());
				ammoCaps.add(i, g.getAmmoCapacity());
				gunsOwned.add(i, g.isOwned());
				specialRounds.add(i, g.isSpecialRounds());
				fireModes.add(i, g.isFullAuto());
				magIncreased.add(i, g.isMagIncreased());
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
	
	public SaveData loadFromFile(String filename) {
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
			g.setMagSize(magSizes.get(i));
			g.setAmmoCapacity(ammoCaps.get(i));
			g.setOwned(gunsOwned.get(i));
			g.setFullAuto(fireModes.get(i));
			g.setSpecialRounds(specialRounds.get(i));
			g.setMagIncreased(magIncreased.get(i));
			g.resetAmmo();
		}
		player.autoEquip();
		
		System.out.println(MessageFormat.format("Level: {0}, Money: {1}, GunsOwned: {2}", level, money, player.getArsenal().stream().filter(g -> g.isOwned()).count()));
		Program.gameState = STATE.StoreMenu;
	}
}
