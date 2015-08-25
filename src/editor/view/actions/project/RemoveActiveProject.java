package editor.view.actions.project;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import editor.controller.Controller;
import editor.view.Window;

public class RemoveActiveProject extends AbstractAction {

	Window window;
	Controller controller;

	
	
	public RemoveActiveProject(Controller controller, Window w) {
		this.controller = controller;
		window = w;
		
		String shortDesc = "Remove active Project";
		
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		controller.removeActiveProject();
	}

}
