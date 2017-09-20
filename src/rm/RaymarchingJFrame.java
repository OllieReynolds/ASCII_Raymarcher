package rm;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RaymarchingJFrame extends JFrame {


	private static final long serialVersionUID = 1L;
	
	public RaymarchingJFrame(JPanel jpanel) {
		super("ASCII Raymarcher");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setContentPane(jpanel);
		
		setResizable(false);
		
		setLocationByPlatform(true);
		
		pack();
	}

}
