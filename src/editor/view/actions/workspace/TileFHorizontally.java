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

public class TileFHorizontally extends AbstractAction {

	Workspace workspace;

	public TileFHorizontally(Workspace workspace, ResourceManager rm) {
		this.workspace = workspace;
		
		String shortDesc = "Tile Frames horizontally";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.TILE_FRAMES_HOR));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		workspace.arrangeWHorizontally();
	}

}
