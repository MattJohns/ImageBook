package mattjohns.common.math;

public class Vector2I {
	private int x;
	private int y;

	public Vector2I(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2I(Vector2I item) {
		this.x = item.xGet();
		this.y = item.yGet();
	}

	public int xGet()
	{
		return x;
	}

	public int yGet()
	{
		return y;
	}

	public String toString() {
		return "(" + x + "," + y+ ")";
	}

	public Vector2I add(Vector2I item)
	{
		return new Vector2I(x + item.xGet(), y + item.yGet());
	}

	public Vector2I add(int x, int y)
	{
		return add(new Vector2I(x, y));
	}

	public Vector2I subtract(Vector2I item) {
		return new Vector2I(x - item.x, y - item.y);
	}

	public Vector2I multiply(Vector2I item) {
		return new Vector2I(x * item.x, y * item.y);
	}

	public Vector2I divide(Vector2I item) {
		return new Vector2I(x / item.x, y / item.y);
	}

	public static Vector2I zero()
	{
		return new Vector2I(0, 0);
	}

	public float aspectRatioGet() {
		if (y == 0)
			return 0f;

		return (float)x / (float)y;
	}
}