package rm;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import utils.Vec3;

public class RaymarchingJButton extends JButton {

	private static final long serialVersionUID = 1L;

	
	public RaymarchingJButton(String label, RaymarchingJTextArea jtextarea, Raymarcher rm, Vec3 position, ActionListener l) {
		super(label);		
		addActionListener(l);
	}
	
}
