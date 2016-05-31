package mattjohns.common.math;

public class RectangleI {
	private Vector2I topLeft;
	private Vector2I bottomRight;

	public RectangleI() {
		topLeft = Vector2I.zero();
		bottomRight = Vector2I.zero();
	}

	public RectangleI(Vector2I topLeft, Vector2I bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	public RectangleI(RectangleI item) {
		topLeft = new Vector2I(item.topLeftGet());
		bottomRight = new Vector2I(item.bottomRightGet());
	}
	
	public static RectangleI convertFrom(RectangleF rectangleF) {
		Vector2I topLeft = new Vector2I((int)rectangleF.topLeftGet().xGet(), (int)rectangleF.topLeftGet().yGet()); 
		Vector2I bottomRight = new Vector2I((int)rectangleF.bottomRightGet().xGet(), (int)rectangleF.bottomRightGet().yGet());
		
		return new RectangleI(topLeft, bottomRight);
	}
	
	public static RectangleI createFromPositionAndSize(Vector2I position, Vector2I size) {
		return new RectangleI(position, position.add(size));
	}

	public Vector2I topLeftGet() {
		return topLeft;
	}

	public void topLeftSet(Vector2I item) {
		topLeft = new Vector2I(item);
	}

	public Vector2I bottomRightGet() {
		return bottomRight;
	}

	public void bottomRightSet(Vector2I item) {
		bottomRight = new Vector2I(item);
	}

	public Vector2I sizeGet() {
		return bottomRight.subtract(topLeft);
	}

	public String toString() {
		return "(" + topLeft.toString() + ", " + bottomRight.toString() + ")";
	}

	public Vector2I centerGet() {
		return sizeGet()
				.divide(new Vector2I(2, 2))
				.add(topLeft);
	}
}