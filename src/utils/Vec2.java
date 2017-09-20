package utils;

public class Vec2 {
	public double x,y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double length() {
		return Math.sqrt((x*x) + (y*y));
	}
	
	public double max() {
		return Math.max(x, y);
	}
	
	public double min() {
		return Math.min(x, y);
	}
	
	public Vec2 abs() {
		return new Vec2(
			Math.abs(x),
			Math.abs(y)
		);
	}
	
	public static double length(Vec2 a, Vec2 b) {
		double dX = a.x - b.x;
		double dY = a.y - b.y;
		return Math.sqrt((dX*dX) + (dY*dY));
	}
	
	public static Vec2 add(Vec2 a, Vec2 b) {
		return new Vec2(
			a.x + b.x,	
			a.y + b.y
		);
				
	}
	
	public static Vec2 minus(Vec2 a, Vec2 b) {
		return new Vec2(
			a.x - b.x,	
			a.y - b.y
		);
	}
	
	public static Vec2 minus(Vec2 a, double b) {
		return new Vec2(
			a.x - b,	
			a.y - b
		);
	}
	
	public static Vec2 multiply(Vec2 a, double factor) {
		return new Vec2(
			a.x * factor,	
			a.y * factor
		);
	}
}
