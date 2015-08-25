package editor.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;

public class About extends AbstractAction {

	JFrame frame;
	Icon pictureOfMe; 
	public About(JFrame frameToClose, ResourceManager rm) {
		pictureOfMe = rm.getIcon(ICONS.MY_PICTURE);
		this.frame = frameToClose;
		
		String shortDesc = "About";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.ABOUT));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {

		JOptionPane
				.showMessageDialog(
						frame,
						"Programmed by Vladimir Makarić \nicons by: http://www.defaulticon.com/ \njanuary 2015",
						"About", JOptionPane.INFORMATION_MESSAGE,
						pictureOfMe);
	}

}
