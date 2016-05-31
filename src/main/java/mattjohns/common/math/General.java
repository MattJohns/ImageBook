package mattjohns.common.math;

public class General {
	private static float CLOSE_ENOUGH_F = 0.0000000001f;

	public static boolean isNearlyEqual(float item1, float item2) {
		float delta = Math.abs(item1 - item2);
		return (delta <= CLOSE_ENOUGH_F);
	}
}