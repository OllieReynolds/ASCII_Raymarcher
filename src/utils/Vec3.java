package utils;

public class Vec3 {
	public double x,y,z;
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double length() {
		return Math.sqrt((x*x) + (y*y) + (z*z));
	}
	
	public double max() {
		return Math.max(Math.max(x, y), z);
	}
	
	public double min() {
		return Math.min(Math.min(x, y), z);
	}
	
	public Vec3 abs() {
		return new Vec3(
			Math.abs(x),
			Math.abs(y),
			Math.abs(z)
		);
	}
	
	
	public static Vec3 add(Vec3 a, Vec3 b) {
		return new Vec3(
			a.x + b.x,	
			a.y + b.y,
			a.z + b.z
		);
				
	}
	
	public static Vec3 minus(Vec3 a, Vec3 b) {
		return new Vec3(
			a.x - b.x,	
			a.y - b.y,
			a.z - b.z
		);
	}
	
	public static Vec3 multiply(Vec3 a, double factor) {
		return new Vec3(
			a.x * factor,	
			a.y * factor,
			a.z * factor
		);
	}
}
