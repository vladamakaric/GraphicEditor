package editor.view.actions.projcontent;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import editor.controller.Controller;
import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;
import editor.view.workspace.PFrameView;

public class ZoomOut extends AbstractAction {

	Controller controller;

	public ZoomOut(Controller controller, ResourceManager rm) {
		this.controller = controller;
		String shortDesc = "Zoom out";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.ZOOM_OUT));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		PFrameView cfv = controller.getCurrentPFrameView();
		cfv.getCameraController().externalZoomOut();
	}
}
