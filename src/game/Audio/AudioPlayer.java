package game.Audio;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import static game.Main.delay;

import game.Main;
import game.Main.STATE;

public class AudioPlayer {

	private static Map<String, Sound> soundMap = new HashMap<String, Sound>();
	private static Music music;
	
	private static void loadSounds() {
		try {
			soundMap.put("Pistol", 			new Sound("./res/PistolSound.wav"));
			soundMap.put("Pistol2", 		new Sound("./res/PistolSound2.ogg"));
			soundMap.put("Pistol3", 		new Sound("./res/PistolSound3.ogg"));
			soundMap.put("Pistol4", 		new Sound("./res/PistolSound4.ogg"));
			soundMap.put("Pistol5", 		new Sound("./res/PistolSound5.ogg"));
			soundMap.put("Rifle", 			new Sound("./res/RifleSound.wav"));
			soundMap.put("Rifle2", 			new Sound("./res/RifleSound2.ogg"));
			soundMap.put("AKM", 			new Sound("./res/AKMSound.ogg"));
			soundMap.put("Shotgun", 		new Sound("./res/ShotgunSound.wav"));
			soundMap.put("Shotgun2", 		new Sound("./res/ShotgunSound2.ogg"));
			soundMap.put("Sniper", 			new Sound("./res/SniperSound.wav"));
			soundMap.put("ReloadTitan", 	new Sound("./res/ReloadTitanSound.ogg"));
			soundMap.put("ReloadPX4", 		new Sound("./res/ReloadPX4Sound.ogg"));
			soundMap.put("ReloadAR15", 		new Sound("./res/ReloadAR15Sound.ogg"));
			soundMap.put("ReloadAKM", 		new Sound("./res/ReloadAKMSound.ogg"));
			soundMap.put("ReloadOverUnder", new Sound("./res/ReloadOverUnderSound.ogg"));
			soundMap.put("ReloadM77", 		new Sound("./res/ReloadM77Sound.ogg"));
			soundMap.put("ReloadJudge", 	new Sound("./res/ReloadJudgeSound.ogg"));
			soundMap.put("ReloadModel57", 	new Sound("./res/ReloadModel57Sound.ogg"));
			soundMap.put("ReloadModel12", 	new Sound("./res/ReloadModel12Sound.ogg"));
			soundMap.put("SpeedReloadModel57", 	new Sound("./res/SpeedReloadModel57Sound.ogg"));
			soundMap.put("ReloadEmptyModel12", 	new Sound("./res/ReloadEmptyModel12Sound.ogg"));
			soundMap.put("CycleM77", 		new Sound("./res/CycleM77Sound.ogg"));
			soundMap.put("CockModel57", 	new Sound("./res/CockModel57Sound.ogg"));
			soundMap.put("CockJudge", 		new Sound("./res/CockJudgeSound.ogg"));
			soundMap.put("CycleModel12", 	new Sound("./res/CycleModel12Sound.ogg"));
			soundMap.put("LevelEnd", 		new Sound("./res/LevelEnd.ogg"));
			soundMap.put("BlipMinor", 		new Sound("./res/BlipMinor.wav"));
			soundMap.put("BlipMajor", 		new Sound("./res/BlipMajor.wav"));
		} catch (SlickException e) {
			e.printStackTrace();
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
		try {
			String fn = Arrays.stream(new File("./").list())
			.filter(s -> s.matches("([^\\.]+(\\.(?i)(wav|ogg))$)"))
			.findAny()
			.orElse("./res/Dying.ogg");
			
			music = new Music(fn);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private static void runMusicLoop() {
		music.loop(1f, 0.25f);
		STATE prev = Main.gameState;
		STATE curr = Main.gameState;
		while (true) {
			curr = Main.gameState;
			if (curr != prev) stateChange(curr);
			prev = curr;
			delay(Duration.ofMillis(50));
		}
	}
	
	private static void stateChange(STATE curr) {
		float pos = music.getPosition();
		
		switch (curr) {
		case GameOver:	music.loop(0.65f, 0.25f);
			break;
		case InGame:	music.loop(1f, 0.25f);
			break;
		case PauseMenu: music.loop(0.65f, 0.25f);
			break;
		case StartMenu: music.loop(1f, 0.25f);
			break;
		case StoreMenu: music.loop(1.35f, 0.25f);
			break;
		default:		music.loop(1f, 0.25f);
			break;
		}
		
		music.setPosition(pos);
	}
	
	public static Sound getSound(String key) {
		return soundMap.get(key);
	}
}
