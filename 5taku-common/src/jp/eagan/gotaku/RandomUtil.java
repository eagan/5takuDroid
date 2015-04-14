package jp.eagan.gotaku;

import java.util.Random;

public class RandomUtil {
	public static void shuffle(Object[] target, Random rand) {
		for (int i = 0; i < target.length; i++) {
			int j = (int) (rand.nextFloat() * target.length);
			Object tmp = target[j];
			target[j] = target[i];
			target[i] = tmp;
		}
	}
	
	public static void shuffle(int[] target, Random rand) {
		for (int i = 0; i < target.length; i++) {
			int j = (int) (rand.nextFloat() * target.length);
			int tmp = target[j];
			target[j] = target[i];
			target[i] = tmp;
		}
	}
}
