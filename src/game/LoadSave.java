package game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import game.Program.STATE;

public class LoadSave implements Serializable {

	private static final long serialVersionUID = 6194456681958310352L;
	
	public LoadSave() {
		
	}
	
	public void saveToFile(String filename) {
		try {
	         FileOutputStream file = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(file);
	         out.writeObject(Program.player);
	         out.close();
	         file.close();
	         System.out.println("Level: "+Program.player.getLevel() + "   Money: " + Program.player.getMoney() + "   MoneyAtStart: " + Program.player.getMoneyAtRoundStart());
	         System.out.println("Gun: "+Program.player.getGun().getMagSize()+"    "+Program.player.getGun().getAmmoCapacity());
	         System.out.println("Saved current game state: " + filename);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void loadFromFile(String filename) {
		PlayerObject temp = null;
		PlayerObject hPlayer = (PlayerObject) Program.handler.getObjectAt(0);
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			temp = (PlayerObject)in.readObject();
			in.close();
			file.close();
			System.out.println("Loaded game state from: " + filename);
			
			
			System.out.println("TEMP PLAYER BEFORE - Level: "+temp.getLevel() + "   Money: " + temp.getMoney() + "   MoneyAtStart: " + temp.getMoneyAtRoundStart());
			System.out.println("STATIC PLAYER BEFORE - Level: "+Program.player.getLevel() + "   Money: " + Program.player.getMoney() + "   MoneyAtStart: " + Program.player.getMoneyAtRoundStart());
			System.out.println("HANDLER PLAYER BEFORE- Level: "+hPlayer.getLevel() + "   Money: " + hPlayer.getMoney() + "   MoneyAtStart: " + hPlayer.getMoneyAtRoundStart());
			
			Program.player = temp;
			
			
			
			Program.handler.loadInPlayer(Program.player);
			hPlayer = (PlayerObject) Program.handler.getObjectAt(0);
			//setupPlayerDataAfterLoad(hPlayer);
			
			
			System.out.println("TEMP PLAYER AFTER - Level: "+temp.getLevel() + "   Money: " + temp.getMoney() + "   MoneyAtStart: " + temp.getMoneyAtRoundStart());
			System.out.println("STATIC PLAYER AFTER - Level: "+Program.player.getLevel() + "   Money: " + Program.player.getMoney() + "   MoneyAtStart: " + Program.player.getMoneyAtRoundStart());
			System.out.println("HANDLER PLAYER AFTER- Level: "+hPlayer.getLevel() + "   Money: " + hPlayer.getMoney() + "   MoneyAtStart: " + hPlayer.getMoneyAtRoundStart());
			
			Program.spawnSys.setLevel(Program.player.getLevel());
			Program.player.setMoney(Program.player.getMoneyAtRoundStart());
			//Program.gameState = STATE.StoreMenu;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*public void setupPlayerDataAfterLoad(PlayerObject temp) {
		PlayerObject p = Program.player;
		p.setArsenal(temp.getArsenal());
		p.setGuns();
		p.setMoney(temp.getMoneyAtRoundStart());
		p.setLevel(temp.getLevel());
		
	}*/

}
