package rm;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

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
	
//	public Predicate<Integer> doesIntersectX(double speed) {
//		
//		double sT 		= Math.abs(Math.sin(elapsedTime * speed));
//		double wst 		= sT * width;
//		
//		return p -> 
//	}


	@Override
	public String call() throws Exception {

		char[] myArray 	= new char[width];
		double speed	= 0.001;
		double sT 		= Math.abs(Math.sin(elapsedTime * speed));
		double wst 		= sT * width;
		double hst 		= sT * height;

		
		for (int i = 0; i < width; i++) {
			int x = i;
			int y = rowIndex;
			char replaceChar = (x > wst || x < width - wst || y > hst || y < height - hst) ? 'R' : ' ';
			myArray[i] = replaceChar;
		}
		
		return String.valueOf(myArray);
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
