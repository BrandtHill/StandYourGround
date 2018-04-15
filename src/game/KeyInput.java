package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	
	public KeyInput(Handler h) {
		handler = h;
	}
	
	public void KeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		//System.out.println("Key Pressed: " + key);
		for(GameObject obj : handler.gameObjs) {
			if (obj.getType() == ObjectType.Player) {
				if(key == KeyEvent.VK_W) obj.setVelY(-2);
				if(key == KeyEvent.VK_A) obj.setVelX(-2);
				if(key == KeyEvent.VK_S) obj.setVelX(2);
				if(key == KeyEvent.VK_D) obj.setVelY(2);
			} 
		}
	}
	
	public void KeyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		//System.out.println("Key Released: " + key);
		for(GameObject obj : handler.gameObjs) {
			if (obj.getType() == ObjectType.Player) {
				if(key == KeyEvent.VK_W) obj.setVelY(0);
				if(key == KeyEvent.VK_A) obj.setVelX(0);
				if(key == KeyEvent.VK_S) obj.setVelX(0);
				if(key == KeyEvent.VK_D) obj.setVelY(0);
			} 
		}
	}
}
