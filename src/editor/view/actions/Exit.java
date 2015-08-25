package editor.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;

public class Exit extends AbstractAction {

	JFrame frameToClose;

	public Exit(JFrame frameToClose, ResourceManager rm) {
		this.frameToClose = frameToClose;
		
		String shortDesc = "Exit";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.EXIT));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		frameToClose.dispatchEvent(new WindowEvent(frameToClose,
				WindowEvent.WINDOW_CLOSING));
	}
}
