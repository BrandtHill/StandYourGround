package game.Audio;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.newdawn.easyogg.OggClip;

import static game.Program.delay;

import game.Program;
import game.Program.STATE;

public class AudioPlayer {

	private static Map<String, OggClip> oggMap = new HashMap<String, OggClip>();
	private static OggClip dying;
	
	private static void loadSounds() {
		oggMap.put("Pistol", 			newClip("./res/PistolSound.ogg"));
		oggMap.put("Rifle", 			newClip("./res/RifleSound.ogg"));
		oggMap.put("Shotgun", 			newClip("./res/ShotgunSound.ogg"));
		oggMap.put("Sniper", 			newClip("./res/SniperSound.ogg"));
		oggMap.put("ReloadTitan", 		newClip("./res/ReloadTitanSound.ogg"));
		oggMap.put("ReloadPX4", 		newClip("./res/ReloadPX4Sound.ogg"));
		oggMap.put("ReloadAR15", 		newClip("./res/ReloadAR15Sound.ogg"));
		oggMap.put("ReloadOverUnder", 	newClip("./res/ReloadOverUnderSound.ogg"));
		oggMap.put("ReloadM77", 		newClip("./res/ReloadM77Sound.ogg"));
		oggMap.put("ReloadModel57", 	newClip("./res/ReloadModel57.ogg"));
		oggMap.put("CycleM77", 			newClip("./res/CycleM77Sound.ogg"));
		oggMap.put("CockModel57", 		newClip("./res/CockModel57.ogg"));
		oggMap.put("BlipMinor", 		newClip("./res/BlipMinor.ogg"));
		oggMap.put("BlipMajor", 		newClip("./res/BlipMajor.ogg"));
	}
	
	private static OggClip newClip(String filename) {
		try {
			return new OggClip(new FileInputStream(filename));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void init() {
		loadSounds();
		Executors.newSingleThreadExecutor().execute( () -> {
			Thread.currentThread().setName("Music Thread");
			loadMusic();
			runMusicLoop();
		});
	}
	
	private static void loadMusic() {
		oggMap.put("Dying", newClip("./res/Dying.ogg"));
		//oggMap.put("Husk", new Music("./res/Husk.ogg"));
		//oggMap.put("Mystic", new Music("./res/Mystic Beat.ogg"));
		//oggMap.put("Fat", new Music("./res/Fat Beat.ogg"));
	}
	
	private static void runMusicLoop() {
		dying = getSound("Dying");
		dying.loop();
		dying.setGain(0.25f);
		//dying.loop(1f, 0.25f);
		STATE prev = Program.gameState;
		STATE curr = Program.gameState;
		while (true) {
			curr = Program.gameState;
			if (curr != prev) stateChange(curr);
			prev = curr;
			delay(Duration.ofMillis(50));
		}
	}
	
	private static void stateChange(STATE curr) {
		//dying.stop();
		
		switch (curr) {
		case GameOver:	//dying.loop(0.65f, 0.25f);
			break;
		case InGame:	//dying.loop(1f, 0.25f);
			break;
		case PauseMenu: //dying.loop(0.65f, 0.25f);
			break;
		case StartMenu: //dying.loop(1f, 0.25f);
			break;
		case StoreMenu: //dying.loop(1.35f, 0.25f);
			break;
		default:		//dying.loop(1f, 0.25f);
			break;
		}
	}
	
	public static OggClip getSound(String key) {
		return oggMap.get(key);
	}
}
