package rm;

import javax.swing.JButton;

public class RaymarchingJButton extends JButton {

	private static final long serialVersionUID = 1L;

	public RaymarchingJButton(RaymarchingJTextArea jtextarea) {
		super("CLICK ME");		
		addActionListener(e -> jtextarea.fillLine());
	}
	
}
