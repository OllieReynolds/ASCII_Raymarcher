package rm;

import java.awt.Color;
import java.awt.Font;
import java.nio.CharBuffer;

import javax.swing.JTextArea;

public class RaymarchingJTextArea extends JTextArea {

	private static final long serialVersionUID = 1L;

	private String lineFiller;
			
	public RaymarchingJTextArea(int characterWidth, int characterHeight) {
		super(characterHeight, characterWidth);
		
		setEditable(false);
		
		setBackground(Color.BLACK);
		
		setForeground(Color.GREEN);
		
		setFont(new Font("monospaced", Font.PLAIN, 12));
		
		setLineWrap(true);
		
		lineFiller = CharBuffer.allocate(getColumns()).toString().replace('\0', 'W');
	}
	
	public void fillLine() {
		
		
		append(lineFiller);
	}
}
