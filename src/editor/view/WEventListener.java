package editor.view;

import java.awt.event.WindowEvent;

import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class WEventListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getComponent();
		int code = JOptionPane.showConfirmDialog(frame,
				"Are you sure you want to exit?", "Exit",
				JOptionPane.YES_NO_OPTION);

		if (code != JOptionPane.YES_OPTION)
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		else
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
