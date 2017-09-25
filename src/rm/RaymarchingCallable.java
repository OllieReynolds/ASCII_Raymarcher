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

	private double fTorus(Vec3 p, Vec2 t) {
		Vec2 q = new Vec2(new Vec2(p.getX(), p.getZ()).length() - t.getX(), p.getY());
		return q.length() - t.getY();
	}

	private double map(Vec3 p) {

		double theta = elapsedTime * 0.001;

		Vec2 yz = new Vec2(0.0, 0.0);
		yz.setX((p.getY() * Math.cos(theta)) - (p.getZ() * Math.sin(theta)));
		yz.setY((p.getZ() * Math.cos(theta)) + (p.getY() * Math.sin(theta)));
		p.setY(yz.getX());
		p.setZ(yz.getY());

		Vec2 xy = new Vec2(0.0, 0.0);
		xy.setX((p.getX() * Math.cos(theta)) - (p.getY() * Math.sin(theta)));
		xy.setY((p.getY() * Math.cos(theta)) + (p.getX() * Math.sin(theta)));
		p.setX(xy.getX());
		p.setY(xy.getY());

		final double sz1 = 0.5;
		final double sz2 = sz1 - 0.1;
		final double sz3 = sz1 * 2.0;

		final double a = fBoxCheap(p, new Vec3(sz3, sz2, sz2));

		return Math.min(fTorus(p, new Vec2(1.0, 0.5)), a);

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

		char[] myArray = new char[width];

		for (int i = 0; i < width; i++) {

			Vec2 uv = Vec2.minus(Vec2.multiply(
					new Vec2((double) i / (double) (width - 1), (double) rowIndex / (double) (height - 1)), 2.0), 1.0);

			Vec3 ro = new Vec3(uv.getX() + position.getX(), uv.getY() + position.getY(), -1.0 + position.getZ());
			Vec3 rd = new Vec3(0.0, 0.0, 1.0);

			double dist = Math.abs(raymarch(ro, rd));

			char replaceChar = ' ';

			if (dist != 10.0) {

				if (dist < minDistanceEncountered) {
					minDistanceEncountered = dist;
				} else if (dist > maxDistanceEncountered) {
					maxDistanceEncountered = dist;
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
