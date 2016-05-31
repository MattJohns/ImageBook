package mattjohns.common.math;

public class Vector2F {
	private float x;
	private float y;

	public Vector2F(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2F(Vector2F item) {
		this.x = item.xGet();
		this.y = item.yGet();
	}

	public static Vector2F ConvertFrom(Vector2I item) {
		return new Vector2F((float)item.xGet(), (float)item.yGet());
	}

	public float xGet() {
		return x;
	}

	public float yGet() {
		return y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public Vector2F add(Vector2F item) {
		return new Vector2F(x + item.xGet(), y + item.yGet());
	}

	public Vector2F add(float x, float y) {
		return add(new Vector2F(x, y));
	}

	public Vector2F subtract(Vector2F item) {
		return new Vector2F(x - item.x, y - item.y);
	}

	public Vector2F multiply(Vector2F item) {
		return new Vector2F(x * item.x, y * item.y);
	}

	public Vector2F divide(Vector2F item) {
		return new Vector2F(x / item.x, y / item.y);
	}

	public static Vector2F zero() {
		return new Vector2F(0f, 0f);
	}

	public Vector2F scale(float item) {
		return new Vector2F(x * item, y * item);
	}

	public float aspectRatioGet() {
		if (General.isNearlyEqual(y, 0f))
			return 0f;

		return (float)x / (float)y;
	}
}