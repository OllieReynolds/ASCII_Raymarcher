package rm;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		double theta = elapsedTime * 0.001;
		
		Vec2 yz = new Vec2(0.0, 0.0);
		yz.x = (p.y * Math.cos(theta)) - (p.z * Math.sin(theta));
		yz.y = (p.z * Math.cos(theta)) + (p.y * Math.sin(theta));
		p.y = yz.x;
		p.z = yz.y;
		
		Vec2 xy = new Vec2(0.0, 0.0);
		xy.x = (p.x * Math.cos(theta)) - (p.y * Math.sin(theta));
		xy.y = (p.y * Math.cos(theta)) + (p.x * Math.sin(theta));
		p.x = xy.x;
		p.y = xy.y;
		
		double sz = 0.5;
		double sz2 = sz - 0.1;
		double a = fBoxCheap(p, new Vec3(sz, sz, sz));
		
		double b = fBoxCheap(p, new Vec3(1.0, sz2, sz2));
		double c = fBoxCheap(p, new Vec3(sz2, 1.0, sz2));
		double d = fBoxCheap(p, new Vec3(sz2, sz2, 1.0));
		
		
		return Math.max(Math.max(Math.max(a, -b), -c), -d);
	}
	
	private double raymarch(Vec3 ro, Vec3 rd) {
		double sceneDist = 0.0;
		double rayDepth = 0.0;
		double epsilon = 0.0001;
		double max = 10.0;
		int iter = 50;
		
		for (int i = 0; i < iter; i++) {
			sceneDist = map(Vec3.add(ro, Vec3.multiply(rd, rayDepth)));
			
			if (sceneDist < epsilon || rayDepth >= max) {
				break;
			}
			
			rayDepth += sceneDist;
		}
		
		if (sceneDist >= epsilon) {
			rayDepth = max;
		} else {
			rayDepth += sceneDist;
		}
		
		return rayDepth;
	}


	@Override
	public String call() throws Exception {

		char[] myArray 	= new char[width];
		
		for (int i = 0; i < width; i++) {
			
			Vec2 uv = Vec2.minus(Vec2.multiply(new Vec2((double)i / (double)(width-1), (double)rowIndex / (double)(height-1)), 2.0), 1.0);
			
			Vec3 ro = new Vec3(uv.x, uv.y, -1.0);
			Vec3 rd = new Vec3(0.0, 0.0, 1.0);
			
			double dist = Math.abs(raymarch(ro, rd));
			
			char replaceChar = '.';
			if (dist != 10.0)
				replaceChar = '#';
			
			//System.out.println(dist);
			
			myArray[i] = replaceChar;
		}
		
		return String.valueOf(myArray);
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
