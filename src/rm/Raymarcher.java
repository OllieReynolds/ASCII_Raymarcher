package rm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import utils.Vec3;

public class Raymarcher extends Thread {

	private RaymarchingJFrame rmFrame;
	private RaymarchingJPanel rmPanel;
	private RaymarchingJTextArea rmText;

	private Vec3 position;

	private boolean isRunning;

	private static final int WIDTH = 160;
	private static final int HEIGHT = 50;

	private static final Logger LOGGER = Logger.getLogger(Raymarcher.class.getName());

	private final ExecutorService executor;
	private final List<RaymarchingCallable> tasks;

	public Raymarcher() {
		this.position = new Vec3(0.0, 0.0, 0.0);

		JPanel asciiPanel = new JPanel(new FlowLayout());
		rmText = new RaymarchingJTextArea(WIDTH, HEIGHT);
		asciiPanel.add(rmText);

		rmPanel = new RaymarchingJPanel();
		rmPanel.add(asciiPanel, BorderLayout.CENTER);
		
		rmFrame = new RaymarchingJFrame(rmPanel);
		rmFrame.setVisible(true);

		executor = Executors.newCachedThreadPool();

		tasks = new ArrayList<>(rmText.getRows());
		for (int i = 0; i < rmText.getRows(); i++) {
			tasks.add(new RaymarchingCallable(WIDTH, HEIGHT, i));
		}

	}

	private void retrieveFutureResult(StringBuilder sb, Future<String> future) {
		try {
			sb.append(future.get());
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to get Future", e);
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
			tasks.forEach(e -> e.setPosition(position));

			try {
				List<Future<String>> futures = executor.invokeAll(tasks);
				for (Future<String> future : futures) {
					retrieveFutureResult(sb, future);
				}

			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Unable to invoke exeuctor tasks", e);
				Thread.currentThread().interrupt();
			} finally {
				rmText.setText(sb.toString());
			}
		}

		try {
			LOGGER.log(Level.INFO, "Terminating Running Tasks");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.log(Level.WARNING, "Unable to terminate running tasks", e);
			Thread.currentThread().interrupt();
		} finally {
			if (!executor.isTerminated()) {
				LOGGER.log(Level.WARNING, "Exector did not terminate properly");
			}
			executor.shutdownNow();
			LOGGER.log(Level.INFO, "Shutdown Finished");
		}
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void setRunning(boolean state) {
		this.isRunning = state;
	}

	public static void main(String[] args) {

		Runnable r = () -> {
			Raymarcher rm = new Raymarcher();
			rm.start();
		};

		EventQueue.invokeLater(r);

	}
}
