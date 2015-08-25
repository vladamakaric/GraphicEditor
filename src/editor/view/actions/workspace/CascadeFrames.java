package editor.view.actions.workspace;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;
import editor.view.workspace.Workspace;

public class CascadeFrames extends AbstractAction {

	Workspace workspace;

	public CascadeFrames(Workspace workspace, ResourceManager rm) {
		this.workspace = workspace;
		
		String shortDesc = "Cascade";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.CASCADE_FRAMES));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		workspace.arrangeWCascade();
	}

}
