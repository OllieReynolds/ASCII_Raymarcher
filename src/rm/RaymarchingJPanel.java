package rm;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RaymarchingJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public RaymarchingJPanel() {
		setLayout(new BorderLayout(0, 0));	
		
		setBorder(BorderFactory.createTitledBorder("ASCII Raymarcher"));
	}

}
