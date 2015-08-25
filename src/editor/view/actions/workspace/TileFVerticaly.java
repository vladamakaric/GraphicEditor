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

public class TileFVerticaly extends AbstractAction {

	Workspace workspace;

	public TileFVerticaly(Workspace workspace, ResourceManager rm) {
		this.workspace = workspace;
		
		String shortDesc = "Tile Frames vertically";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.TILE_FRAMES_VER));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		workspace.arrangeWVerticaly();
	}

}
