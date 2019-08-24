package game;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
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
		frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),new Point(),null ));
		frame.setIconImage(new ImageIcon("res/Oscilloshape1.JPG").getImage());
		frame.add(p);
		frame.setVisible(true);
		p.start();
	}
}
