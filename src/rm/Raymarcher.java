package rm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Raymarcher extends Thread {
	
	private RaymarchingJFrame 		rmFrame;
	private RaymarchingJPanel  		rmPanel;
	private RaymarchingJTextArea 	rmText;
	private RaymarchingJButton		rmButton;
	
	private boolean isRunning;
	
	private final int WIDTH 	= 160;
	private final int HEIGHT 	= 50;
	
	private final ExecutorService executor;
	private final List<RaymarchingCallable> tasks;
	
	public Raymarcher() {
		rmPanel = new RaymarchingJPanel();
		
		rmText = new RaymarchingJTextArea(WIDTH, HEIGHT);
		rmPanel.add(rmText, BorderLayout.CENTER);
		
		rmButton = new RaymarchingJButton(rmText, this);
		rmPanel.add(rmButton, BorderLayout.SOUTH);
		
		rmFrame = new RaymarchingJFrame(rmPanel);
		rmFrame.setVisible(true);
		
		executor = Executors.newCachedThreadPool();
		
		tasks = new ArrayList<RaymarchingCallable>(rmText.getRows());
		for (int i = 0; i < rmText.getRows(); i++) {
			tasks.add(new RaymarchingCallable(WIDTH, HEIGHT, i));
		}
	}
	
	@Override
	public void run() {
		
		isRunning = true;
		long startTime = System.currentTimeMillis();
		
		while (isRunning) {
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
	
	public boolean isRunning() {
		return this.isRunning;
	}
	
	public void setRunning(boolean state) {
		this.isRunning = state;
	}

	public static void main(String[] args) {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Raymarcher rm = new Raymarcher();
				rm.start();
			}
		};
		
		EventQueue.invokeLater(runnable);
		
	}
}
