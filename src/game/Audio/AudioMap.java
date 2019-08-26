package game.Audio;

import java.util.HashMap;
import java.util.Map;

public class AudioMap {
	private static Map<String, ISound> soundMap = new HashMap<>();

	public static void init() {
		soundMap.put("P1", new SoundEffect("./res/PistolSound.wav").init());
		soundMap.put("P2", new MultiSoundEffect("./res/PistolSound.wav").init());
		soundMap.put("C1", new SoundEffect("./res/CycleM77Sound.ogg").init());
		soundMap.put("C2", new MultiSoundEffect("./res/CycleM77Sound.ogg").init());
	}
	
	public static ISound get(String key) {
		return soundMap.get(key);
	}
}
