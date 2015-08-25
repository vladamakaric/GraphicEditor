package editor.view.actions.projcontent;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import editor.controller.Controller;
import editor.controller.FrameController;
import editor.view.ResourceManager;
import editor.view.Window;
import editor.view.ResourceManager.ICONS;

public class RotateCCW extends AbstractAction {

	Controller controller;

	public RotateCCW(Controller controller, ResourceManager rm) {
		this.controller = controller;
		
		String shortDesc = "Rotate CCW";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.ROTATE_CCW));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		FrameController fc = controller.getProjectController().getCurrentFrameController();
		fc.rotateSelection(false);
	}

}
