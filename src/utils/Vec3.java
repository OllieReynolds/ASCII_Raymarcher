package utils;

public class Vec3 {
	private double x;
	private double y;
	private double z;
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setX(double newX) {
		x = newX;
	}
	
	public void setY(double newY) {
		y = newY;
	}
	
	public void setZ(double newZ) {
		z = newZ;
	}
	
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
