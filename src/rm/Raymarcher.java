package rm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

public class Raymarcher {
	
	private RaymarchingJFrame 		rmFrame;
	private RaymarchingJPanel  		rmPanel;
	private RaymarchingJTextArea 	rmText;
	private RaymarchingJButton		rmButton;
	
	private final int WIDTH 	= 160;
	private final int HEIGHT 	= 50;
	
	public Raymarcher() {
		rmPanel = new RaymarchingJPanel();
		
		rmText = new RaymarchingJTextArea(WIDTH, HEIGHT);
		rmPanel.add(rmText, BorderLayout.CENTER);
		
		rmButton = new RaymarchingJButton(rmText);
		rmPanel.add(rmButton, BorderLayout.SOUTH);
		
		rmFrame = new RaymarchingJFrame(rmPanel);
		rmFrame.setVisible(true);
	}
	
	public void launchExecutorServiceAndDraw() {
		RaymarchingUpdater rmUpdater = new RaymarchingUpdater(rmText, WIDTH, HEIGHT);
		rmUpdater.start();
	}

	public static void main(String[] args) {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Raymarcher rm = new Raymarcher();
				rm.launchExecutorServiceAndDraw();
			}
		};
		
		EventQueue.invokeLater(runnable);
		
	}
}
