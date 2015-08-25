package editor.view.actions.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import editor.controller.Controller;
import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;
import editor.view.Window;

public class OpenProject extends AbstractAction {

	Window window;
	Controller cntrl;
	public OpenProject(Window window, Controller cntrl ,ResourceManager rm) {
		this.window = window;
		this.cntrl = cntrl;
		
		String shortDesc = "Open Project";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.OPEN_PROJECT));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {

		final JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {

				if (!cntrl.addNewProjectFromFile(getSelectedFile())) {

					JOptionPane.showMessageDialog(null,
							"Project already loaded!");
					return;
				}

				super.approveSelection();
			}
		};

		fileChooser.setFileFilter(new GraphicEditorFileFilter());
		fileChooser.showOpenDialog(window);
	}

}
