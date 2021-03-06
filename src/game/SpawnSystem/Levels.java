package game.SpawnSystem;

import game.SpawnSystem.SpawnSystem.REGION;
import game.SpawnSystem.SpawnSystem.ZOMBIE;
import static game.SpawnSystem.SpawnSystem.REGION.*;
import static game.SpawnSystem.SpawnSystem.ZOMBIE.*;

import java.lang.reflect.Field;

public class Levels {
	
	static Level l1 = l(1000, 
			w(5000,	
					s(LEFT, NORMAL, 3)),
			w(0000, 
					s(RIGHT, NORMAL, 3))
			);
	
	static Level l2 = l(1000, 
			w(3000, 
					s(UP, NORMAL, 5)),
			w(4000, 
					s(DOWN, NORMAL, 2), 
					s(DOWN, DODGING, 1)),
			w(4000, 
					s(LEFT, DODGING, 1)),
			w(0000, 
					s(RIGHT, NORMAL, 5))
			);
	
	static Level l3 = l(2500, 
			w(10000, 
					s(RIGHT, THICC, 1), 
					s(LEFT, DODGING, 2)),
			w(9000, 
					s(RIGHT, NORMAL, 3), 
					s(LEFT, NORMAL, 3)),
			w(0000, 
					s(UP, NORMAL, 5))
			);
	
	static Level l4 = l(500, 
			w(5000, 
					s(RIGHT, NORMAL, 4), 
					s(LEFT, NORMAL, 4)),
			w(5000, 
					s(UP, FAST, 1), 
					s(UP, NORMAL, 2), 
					s(DOWN, DODGING, 1), 
					s(DOWN, NORMAL, 2)),
			w(0000, 
					s(RIGHT, THICC, 3), 
					s(LEFT, NORMAL, 3))
			);
	
	static Level l5 = l(1500, 
			w(9000, 
					s(UP, NORMAL, 10), 
					s(UP, THICC, 5)),
			w(4000, 
					s(DOWN, NORMAL, 10)),
			w(0000, 
					s(LEFT, DODGING, 2), 
					s(LEFT, NORMAL, 3))
			);
	
	static Level l6 = l(500, 
			w(7500, 
					s(RIGHT, FAST, 2), 
					s(LEFT, FAST, 1)),
			w(10000, 
					s(RIGHT, FAST, 1), 
					s(LEFT, FAST, 2),
					s(DOWN, CHARGING, 1)),
			w(2000, 
					s(UP, DODGING, 3), 
					s(UP, THICC, 1)),
			w(0000, 
					s(DOWN, DODGING, 3), 
					s(DOWN, THICC, 2))
			);
	
	static Level l7 = l(500, 
			w(12000, 
					s(UP, NORMAL, 3), 
					s(UP, DODGING, 3), 
					s(LEFT, THICC, 8)),
			w(5000, 
					s(RIGHT, THICC, 8), 
					s(DOWN, NORMAL, 4), 
					s(DOWN, DODGING, 2)),
			w(0000, 
					s(UP, FAST, 1), 
					s(DOWN, FAST, 1))
			);
	
	static Level l8 = l(500, 
			w(5500, s(RIGHT, NORMAL, 2), 
					s(RIGHT, DODGING, 2), 
					s(LEFT, FAST, 1), 
					s(LEFT, THICC, 1)),
			w(5500, 
					s(RIGHT, FAST, 1), 
					s(RIGHT, THICC, 1), 
					s(LEFT, NORMAL, 2), 
					s(LEFT, DODGING, 2)),
			w(3750, 
					s(UP, FAST, 1), 
					s(UP, THICC, 1), 
					s(DOWN, NORMAL, 2), 
					s(DOWN, DODGING, 2),
					s(LEFT, CHARGING, 1)),
			w(6000, 
					s(UP, NORMAL, 2), 
					s(UP, DODGING, 2), 
					s(DOWN, FAST, 1), 
					s(DOWN, THICC, 1),
					s(RIGHT, CHARGING, 1)),
			w(0000, 
					s(RIGHT, THICC, 6), 
					s(LEFT, THICC, 6))
			);
	
	static Level l9 = l(500, 
			w(6500, 
					s(RIGHT, FAST, 1), 
					s(LEFT, FAST, 1), 
					s(UP, FAST, 1), 
					s(DOWN, FAST, 1)),
			w(12000, 
					s(RIGHT, DODGING, 2), 
					s(LEFT, DODGING, 2), 
					s(UP, DODGING, 2), 
					s(DOWN, DODGING, 2)),
			w(6000, 
					s(RIGHT, FAST, 1), 
					s(LEFT, FAST, 1), 
					s(RIGHT, CHARGING, 1), 
					s(LEFT, CHARGING, 1), 
					s(UP, DODGING, 1), 
					s(DOWN, DODGING, 1)),
			w(9000, 
					s(RIGHT, FAST, 2), 
					s(LEFT, FAST, 2), 
					s(UP, FAST, 1), 
					s(DOWN, FAST, 1)),
			w(0000, 
					s(RIGHT, DODGING, 1), 
					s(LEFT, DODGING, 1), 
					s(UP, DODGING, 1), 
					s(DOWN, DODGING, 1),
					s(UP, CHARGING, 1), 
					s(DOWN, CHARGING, 1),
					s(RIGHT, THICC, 1), 
					s(LEFT, THICC, 1), 
					s(UP, THICC, 1), 
					s(DOWN, THICC, 1))
			);
	
	static Level l10 = l(2000, 
			w(5000, 
					s(LEFT, NORMAL, 8), 
					s(DOWN, DODGING, 2)),
			w(6000, 
					s(LEFT, FAST, 4), 
					s(UP, THICC, 2)),
			w(18000, 
					s(LEFT, DODGING, 8), 
					s(DOWN, CHARGING, 2)),
			w(4000, 
					s(RIGHT, FAST, 4), 
					s(UP, THICC, 2)),
			w(4000, 
					s(RIGHT, FAST, 4), 
					s(DOWN, THICC, 4)),
			w(0000, 
					s(RIGHT, THICC, 6), 
					s(UP, FAST, 2), 
					s(DOWN, DODGING, 2))
			);
	
	static Level l11 = l(3000, 
			w(6000, 
					s(DOWN, NORMAL, 2), 
					s(DOWN, DODGING, 4)),
			w(6000, 
					s(UP, FAST, 4), 
					s(DOWN, NORMAL, 2)),
			w(0000, 
					s(LEFT, THICC, 4))
			);
	
	static Level l12 = l(500, 
			w(3000, 
					s(DOWN, NORMAL, 6)),
			w(4000, 
					s(RIGHT, THICC, 6)),
			w(0000, 
					s(UP, NORMAL, 10),
					s(DOWN, NORMAL, 2))
			);
	
	static Level l13 = l(1500, 
			w(6000, 
					s(RIGHT, FAST, 2), 
					s(UP, FAST, 2)),
			w(5000, 
					s(LEFT, FAST, 2), 
					s(DOWN, FAST, 2)),
			w(11000, 
					s(DOWN, THICC, 6), 
					s(UP, THICC, 6)),
			w(5000, 
					s(LEFT, DODGING, 4), 
					s(RIGHT, NORMAL, 4)),
			w(0000, 
					s(DOWN, THICC, 4), 
					s(UP, NORMAL, 4))
			);
	
	static Level l14 = l(1500, 
			w(9000, 
					s(DOWN, THICC, 8), 
					s(UP, DODGING, 3)),
			w(9000, 
					s(UP, THICC, 8), 
					s(DOWN, CHARGING, 1)),
			w(9000, 
					s(DOWN, THICC, 8), 
					s(UP, NORMAL, 3)),
			w(5000, 
					s(UP, CHARGING, 2), 
					s(DOWN, NORMAL, 12)),
			w(0, 
					s(DOWN, NORMAL, 8))
			);
	
	static Level l15 = l(1500, 
			w(9000, 
					s(DOWN, NORMAL, 5), 
					s(UP, NORMAL, 3), 
					s(UP, DODGING, 2)),
			w(9000, 
					s(LEFT, NORMAL, 5), 
					s(RIGHT, NORMAL, 3), 
					s(RIGHT, THICC, 2)),
			w(9000, 
					s(LEFT, NORMAL, 5), 
					s(UP, NORMAL, 3), 
					s(UP, FAST, 2)),
			w(9000, 
					s(RIGHT, NORMAL, 5), 
					s(DOWN, NORMAL, 3), 
					s(DOWN, DODGING, 2)),
			w(9000, 
					s(LEFT, NORMAL, 5), 
					s(DOWN, NORMAL, 3), 
					s(DOWN, THICC, 2)),
			w(9000, 
					s(RIGHT, NORMAL, 5), 
					s(UP, NORMAL, 3), 
					s(UP, FAST, 2)),
			w(9000, 
					s(DOWN, NORMAL, 5), 
					s(UP, NORMAL, 3), 
					s(UP, DODGING, 2)),
			w(0, 
					s(UP, FAST, 5),
					s(LEFT, CHARGING, 3))
			);
	
	static Level l16 = l(2000,
			w(6000,
					s(LEFT, NORMAL, 5),
					s(UP, CHARGING, 1)),
			w(8000,
					s(LEFT, NORMAL, 15),
					s(LEFT, FAST, 3)),
			w(0, 
					s(LEFT, FAST, 2))
			);
	
	static Level l17 = l(500,
			w(5000,
					s(LEFT, NORMAL, 10)),
			w(5000,
					s(LEFT, NORMAL, 12)),
			w(3000,
					s(LEFT, NORMAL, 10)),
			w(0,
					s(UP, NORMAL, 10),
					s(LEFT, NORMAL, 4))
			);
	
	static Level l18 = l(2000,
			w(6000,
					s(LEFT, FAST, 4),
					s(UP, CHARGING, 2)),
			w(6000,
					s(LEFT, FAST, 4),
					s(UP, FAST, 4)),
			w(6000,
					s(LEFT, THICC, 4),
					s(UP, CHARGING, 2)),
			w(0,
					s(LEFT, DODGING, 2),
					s(UP, FAST, 4))
			);
	
	static Level l19 = l(3000,
			w(6000,
					s(LEFT, THICC, 10),
					s(UP, THICC, 10)),
			w(10000,
					s(UP, CHARGING, 5),
					s(LEFT, NORMAL, 30)),
			w(0,
					s(LEFT, FAST, 2),
					s(UP, FAST, 2))
			);
	
	static Level l20 = l(5000,
			w(15000,
					s(LEFT, NORMAL, 30),
					s(UP, DODGING, 8)),
			w(0,
					s(LEFT, CHARGING, 3),
					s(LEFT, NORMAL, 20),
					s(UP, CHARGING, 3),
					s(UP, NORMAL, 20))
			);
	
	static Level l21 = l(3000,
			w(7500,
					s(LEFT, NORMAL, 2),
					s(UP, DODGING, 2)),
			w(10000,
					s(DOWN, FAST, 3),
					s(LEFT, CHARGING, 2),
					s(RIGHT, CHARGING, 1)),
			w(0,
					s(RIGHT, NORMAL, 5))
			);
	
	static Level l22 = l(5000,
			w(14000,
					s(LEFT, FAST, 4),
					s(RIGHT, THICC, 3)),
			w(17000,
					s(DOWN, FAST, 4),
					s(UP, CHARGING, 2),
					s(UP, THICC, 2)),
			w(14000,
					s(RIGHT, NORMAL, 5),
					s(LEFT, NORMAL, 5)),
			w(0,
					s(UP, THICC, 4),
					s(DOWN, CHARGING, 2),
					s(RIGHT, DODGING, 5),
					s(LEFT, NORMAL, 3))
			);
	
	static Level l23 = l(4000,
			w(10000,
					s(LEFT, FAST, 3),
					s(RIGHT, FAST, 2)),
			w(10000,
					s(RIGHT, FAST, 3),
					s(DOWN, FAST, 2)),
			w(10000,
					s(UP, FAST, 3),
					s(LEFT, FAST, 2)),
			w(10000,
					s(DOWN, FAST, 3),
					s(UP, FAST, 2)),
			w(10000,
					s(LEFT, FAST, 3),
					s(DOWN, FAST, 2)),
			w(8000,
					s(RIGHT, FAST, 3),
					s(UP, FAST, 2))
			);
	
	static Level l24 = l(500,
			w(12000,
					s(LEFT, NORMAL, 5),
					s(RIGHT, NORMAL, 5)),
			w(4000,
					s(LEFT, NORMAL, 5),
					s(RIGHT, NORMAL, 5)),
			w(4000,
					s(UP, CHARGING, 1),
					s(DOWN, CHARGING, 1)),
			w(10000,
					s(DOWN, NORMAL, 7),
					s(DOWN, CHARGING, 1),
					s(UP, NORMAL, 7),
					s(UP, CHARGING, 1)),
			w(0,
					s(LEFT, NORMAL, 2),
					s(RIGHT, NORMAL, 2))
			);
	
	static Level l25 = l(2000,
			w(12000,
					s(LEFT, NORMAL, 5),
					s(UP, DODGING, 4)),
			w(4000,
					s(LEFT, NORMAL, 8),
					s(UP, FAST, 3)),
			w(18000,
					s(LEFT, THICC, 10)),
			w(0,
					s(DOWN, NORMAL, 4),
					s(DOWN, CHARGING, 2),
					s(LEFT, NORMAL, 8))
			);
	
	
	
	public static Level getLevel(int i) {
		try {
			Field f = Levels.class.getDeclaredField("l" + i);
			return (Level) f.get(null);
		} catch (Exception e) {
			System.out.println("Couldn't find static level. Returning null.");
			return null;
		}
	}
	
	private static Spawn s(REGION region, ZOMBIE zombie, int num) {
		return new Spawn(region, zombie, num);
	}
	
	private static Wave w(int delay, Spawn...spawns) {
		return new Wave(delay, spawns);
	}
	
	private static Level l(int delay, Wave...waves) {
		return new Level(delay, waves);
	}
}
