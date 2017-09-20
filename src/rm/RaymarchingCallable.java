package rm;

import java.util.concurrent.Callable;

import utils.Vec2;
import utils.Vec3;

public class RaymarchingCallable implements Callable<String> {

	private int rowIndex;
	private long elapsedTime;
	private int width;
	private int height;
	
	public RaymarchingCallable(int width, int height, int rowIndex) {
		this.rowIndex = rowIndex;
		this.elapsedTime = 0L;
		this.width = width;
		this.height = height;
	}
	
	private double fSphere(Vec3 p, double r) {
		return p.length() - r;
	}
	
	private double fBoxCheap(Vec3 p, Vec3 b) {
		return Vec3.minus(p.abs(), b).max();
	}
	
	private double map(Vec3 p) {
		double pulse = Math.abs(Math.sin(elapsedTime * 0.001));
		
		return fBoxCheap(p, new Vec3(0.5, 0.5, 0.5));
		//return fSphere(p, pulse);
	}
	
	private double raymarch(Vec3 ro, Vec3 rd) {
		double sceneDist = 0.0;
		double rayDepth = 0.0;
		
		for (int i = 0; i < 50; i++) {
			sceneDist = map(Vec3.add(ro, Vec3.multiply(rd, rayDepth)));
			
			if (sceneDist < 0.001 || rayDepth >= 100.0) {
				break;
			}
			
			rayDepth += sceneDist * 0.75;
		}
		
		if (sceneDist >= 0.001) {
			rayDepth = 100.0;
		} else {
			rayDepth += sceneDist;
		}
		
		return rayDepth;
	}


	@Override
	public String call() throws Exception {

		char[] myArray 	= new char[width];
		double speed	= 0.001;
		double sT 		= Math.sin(elapsedTime * speed);
		double cT		= Math.cos(elapsedTime * speed);
		
		for (int i = 0; i < width; i++) {
			
			Vec2 uv = Vec2.minus(Vec2.multiply(new Vec2((double)i / (double)(width-1), (double)rowIndex / (double)(height-1)), 2.0), 1.0);
			
			Vec3 ro = new Vec3(uv.x, uv.y, 0.0);
			Vec3 rd = new Vec3(0.0, 0.0, 1.0);
			
			double dist = raymarch(ro, rd);
			
			char replaceChar = '.';
			if (dist < 0.5)
				replaceChar = '#';
			
			myArray[i] = replaceChar;
		}
		
		return String.valueOf(myArray);
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
