package editor.view.actions.project;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import editor.controller.Controller;
import editor.view.ResourceManager;
import editor.view.Window;

public class RemoveActiveFrame extends AbstractAction {

	Window window;
	Controller controller;

	public RemoveActiveFrame(Controller controller, Window w) {
		this.controller = controller;
		window = w;
		String shortDesc = "Remove active Frame";
		
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		controller.removeActiveFrame();
	}

}
