package mattjohns.common.math;

public class Vector3F {
	private float x;
	private float y;
	private float z;

	public Vector3F(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3F(Vector3F item) {
		this.x = item.xGet();
		this.y = item.yGet();
		this.z = item.zGet();
	}

	public static Vector3F convertFrom(Vector2F item) {
		return new Vector3F(item.xGet(), item.yGet(), 0f);
	}

	public float xGet() {
		return x;
	}

	public float yGet() {
		return y;
	}

	public float zGet() {
		return z;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public Vector3F add(Vector3F item) {
		return new Vector3F(x + item.xGet(), y + item.yGet(), z + item.zGet());
	}

	public Vector3F add(float x, float y, float z) {
		return add(new Vector3F(x, y, z));
	}

	public Vector3F subtract(Vector3F item) {
		return new Vector3F(x - item.x, y - item.y, z - item.z);
	}

	public Vector3F multiply(Vector3F item) {
		return new Vector3F(x * item.x, y * item.y, z * item.z);
	}

	public Vector3F divide(Vector3F item) {
		return new Vector3F(x / item.x, y / item.y, z / item.z);
	}

	public static Vector3F zero() {
		return new Vector3F(0f, 0f, 0f);
	}

	public Vector3F scale(float item) {
		return new Vector3F(x * item, y * item, z * item);
	}
}