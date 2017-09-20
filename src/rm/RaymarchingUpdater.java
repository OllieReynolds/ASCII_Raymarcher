package rm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RaymarchingUpdater extends Thread {

	private long startTime;
	private RaymarchingJTextArea rmText;
	private int width;
	private int height;
	
	
	public RaymarchingUpdater(RaymarchingJTextArea rmText, int width, int height) {
		this.rmText = rmText;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void run() {
		ExecutorService executors[] = {
				Executors.newSingleThreadExecutor(),
				Executors.newCachedThreadPool(),
				Executors.newSingleThreadScheduledExecutor(),
				Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() + 1)
		};
		
		ExecutorService executor = executors[3];
		List<RaymarchingCallable> tasks = new ArrayList<RaymarchingCallable>(rmText.getRows());
		
		for (int i = 0; i < rmText.getRows(); i++) {
			tasks.add(new RaymarchingCallable(width, height, i));
		}
		
		startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 100000000; i++) {
			StringBuilder sb = new StringBuilder();
			
			long elapsedTime = System.currentTimeMillis() - startTime;
			tasks.forEach(e -> e.setElapsedTime(elapsedTime));
			
			try {
				List<Future<String>> futures = executor.invokeAll(tasks);
				for (Future<String> future: futures) {
					try {
						sb.append(future.get());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				rmText.setText(sb.toString());
			}
		}
			
		try {
			System.out.println("Terminating Running Tasks");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} 
		catch (InterruptedException e) {
			System.err.println("Tasks Interrupted");
		}
		finally {
			if (!executor.isTerminated()) {
				System.err.println("Cancel Non-Finished Tasks");
			}
			executor.shutdownNow();
			System.out.println("Shutdown Finished");
		}
	}
}
