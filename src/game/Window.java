package game;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas{

	private static final long serialVersionUID = -8255319694373975038L;

	public Window(int w, int h, String t, Program p) {
		JFrame frame = new JFrame(t);
		Dimension dim = new Dimension(w, h);
		
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(p);
		p.start();
	}
}
