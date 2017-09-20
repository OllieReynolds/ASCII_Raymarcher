package rm;

import java.util.concurrent.Callable;
import utils.Vec2;
import utils.Vec3;

public class RaymarchingCallable implements Callable<String> {

	private int rowIndex;
	private long elapsedTime;
	private int width;
	private int height;
	private double minDistanceEncountered;
	private double maxDistanceEncountered;
	private Vec3 position;
	
	public RaymarchingCallable(int width, int height, int rowIndex) {
		this.rowIndex = rowIndex;
		this.elapsedTime = 0L;
		this.width = width;
		this.height = height;
		this.minDistanceEncountered = Double.MAX_VALUE;
		this.maxDistanceEncountered = Double.MIN_VALUE;
		this.position = new Vec3(0.0, 0.0, 0.0);
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
		
		final double sz1 = 0.5;
		final double sz2 = sz1 - 0.1;
		final double sz3 = sz1 * 2.0;
		
		final double a = fBoxCheap(p, new Vec3(sz1, sz1, sz1));
		final double b = fBoxCheap(p, new Vec3(sz3, sz2, sz2));
		final double c = fBoxCheap(p, new Vec3(sz2, sz3, sz2));
		final double d = fBoxCheap(p, new Vec3(sz2, sz2, sz3));
		
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
			
			Vec3 ro = new Vec3(uv.x + position.x, uv.y + position.y, -1.0 + position.z);
			Vec3 rd = new Vec3(0.0, 0.0, 1.0);
			
			double dist = Math.abs(raymarch(ro, rd));
			
			char replaceChar = ' ';
			
			if (dist != 10.0) {
				
				if (dist < minDistanceEncountered) {
					minDistanceEncountered = dist;
					//System.out.println("new min: " + minDistanceEncountered);
				}
				else if (dist > maxDistanceEncountered) {
					maxDistanceEncountered = dist;
					//System.out.println("new max: " + maxDistanceEncountered);
				}
				
				double range = maxDistanceEncountered - minDistanceEncountered;
				double offsetFromMin = maxDistanceEncountered - dist;
				double result = offsetFromMin / range;
				
				String palette = " `-_:/~|(%zr*uwJ$khOZ8W@B#M";
				int col = (int) (result * palette.length());
				
				if (col == 27)
					col--;
				replaceChar = palette.charAt(col);	
			}
			
			
			myArray[i] = replaceChar;
		}
		
		return String.valueOf(myArray);
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public void setPosition(Vec3 p) {
		this.position = p;
	}
}
