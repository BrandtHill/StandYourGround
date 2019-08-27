package game.Audio;

import java.util.HashMap;
import java.util.Map;

public class AudioMap {
	private static Map<String, ISound> soundMap = new HashMap<>();

	public static void init() {
		soundMap.put("P1", new SoundEffect("./res/PistolSound.wav").init());
		soundMap.put("P2", new MultiSoundEffect("./res/PistolSound.wav").init());
		soundMap.put("Dying", new Music("./res/Dying.wav").init());
		get("P2").setGain(-10f);
		//soundMap.put("C1", new SoundEffect("./res/CycleM77Sound.ogg").init());
		//soundMap.put("C2", new MultiSoundEffect("./res/CycleM77Sound.ogg").init());
	}
	
	public static ISound get(String key) {
		return soundMap.get(key);
	}
}
