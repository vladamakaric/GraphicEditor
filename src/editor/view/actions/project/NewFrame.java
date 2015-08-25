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
import editor.view.ResourceManager.ICONS;

public class NewFrame extends AbstractAction {

	Window window;
	Controller controller;

	public NewFrame(Controller controller, Window w, ResourceManager rm) {
		this.controller = controller;
		window = w;
		
		String shortDesc = "New Frame";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.NEW_FRAME));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		String name = null;

		while (true) {
			name = JOptionPane.showInputDialog(window, "Enter new frame name");

			if (name == null)
				return;

			if (!controller.dialogNameExists(name))
				break;
			else
				JOptionPane.showMessageDialog(null,
						"Frame name already exists!");
		}

		controller.createNewFrame(name);

	}

}
