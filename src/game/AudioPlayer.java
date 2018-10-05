package game;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import static game.Program.delay;
import game.Program.STATE;

public class AudioPlayer {

	private static Map<String, Sound> soundMap = new HashMap<String, Sound>();
	private static Map<String, Music> musicMap = new HashMap<String, Music>();	
	private static ExecutorService executor;
	private static Music dying;
	
	private static void loadSounds() {
		try {
			soundMap.put("Pistol", new Sound("/res/PistolSound.wav"));
			soundMap.put("Rifle", new Sound("/res/RifleSound.wav"));
			soundMap.put("Shotgun", new Sound("/res/ShotgunSound.wav"));
			soundMap.put("Sniper", new Sound("/res/SniperSound.wav"));
			soundMap.put("ReloadTitan", new Sound("/res/ReloadTitanSound.ogg"));
			soundMap.put("ReloadPX4", new Sound("/res/ReloadPX4Sound.wav"));
			soundMap.put("ReloadAR15", new Sound("/res/ReloadAR15Sound.ogg"));
			soundMap.put("ReloadOverUnder", new Sound("/res/ReloadOverUnderSound.ogg"));
			soundMap.put("ReloadM77", new Sound("/res/ReloadM77Sound.ogg"));
			soundMap.put("ReloadModel57", new Sound("/res/ReloadModel57.wav"));
			soundMap.put("CycleM77", new Sound("/res/CycleM77Sound.ogg"));
			soundMap.put("CockModel57", new Sound("/res/CockModel57.wav"));
			soundMap.put("BlipMinor", new Sound("/res/BlipMinor.wav"));
			soundMap.put("BlipMajor", new Sound("/res/BlipMajor.wav"));
		} catch (SlickException e) {
			e.getMessage();
		}
	}
	
	public static void init() {
		loadSounds();
		executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName("Music Thread");
				loadMusic();
				runMusicLoop();
			}
		});
	}
	
	private static void loadMusic() {
		try {
			musicMap.put("Dying", new Music("/res/Dying.ogg"));
			//musicMap.put("Husk", new Music("res/Husk.ogg"));
			//musicMap.put("Mystic", new Music("res/Mystic Beat.ogg"));
			//musicMap.put("Fat", new Music("res/Fat Beat.ogg"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private static void runMusicLoop() {
		dying = getMusic("Dying");
		dying.loop(1f, 0.25f);
		STATE prev = Program.gameState;
		STATE curr = Program.gameState;
		while (true) {
			curr = Program.gameState;
			if (curr != prev) {
				stateChange(curr);
			}
			prev = curr;
			delay(Duration.ofMillis(50));
		}
	}
	
	private static void stateChange(STATE curr) {
		float pos = dying.getPosition();
		dying.stop();
		
		switch (curr) {
		case GameOver:	dying.loop(0.65f, 0.25f);
			break;
		case InGame:	dying.loop(1f, 0.25f);
			break;
		case PauseMenu: dying.loop(0.65f, 0.25f);
			break;
		case StartMenu: dying.loop(1f, 0.25f);
			break;
		case StoreMenu: dying.loop(1.35f, 0.25f);
			break;
		default:		dying.loop(1f, 0.25f);
			break;
		}
		
		dying.setPosition(pos);
	}
	
	public static Music getMusic(String key) {
		return musicMap.get(key);
	}
	
	public static Sound getSound(String key) {
		return soundMap.get(key);
	}
}
