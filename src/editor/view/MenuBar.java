package editor.view;

import java.awt.MenuItem;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import editor.view.actions.ActionManager;

public class MenuBar extends JMenuBar {

	private class MenuDescriptor {
		public int mnemonik;
		public String name;
		public AbstractAction[] aaarray;

		public MenuDescriptor(String name, int mnemonik,
				AbstractAction[] aaarray) {
			super();
			this.mnemonik = mnemonik;
			this.name = name;
			this.aaarray = aaarray;
		}
	}

	public MenuBar(ActionManager am) {

		AbstractAction[] file = { am.newProject, am.newDialog, am.openProject,
				am.saveProject, am.exit };

		AbstractAction[] edit = { am.undo, am.redo, am.rotateCW, am.rotateCCW, 
				am.deleteSelection, am.removeProject, am.removeActiveDialog };

		AbstractAction[] view = { am.zoomIn, am.zoomOut, am.nextWindow, am.previousWindow,
				am.tileWHorizontally, am.tileWVertically, am.cascadeWindows };

		AbstractAction[] help = { am.about };

		MenuDescriptor[] mds = {
				new MenuDescriptor("File", KeyEvent.VK_F, file),
				new MenuDescriptor("Edit", KeyEvent.VK_E, edit),
				new MenuDescriptor("View", KeyEvent.VK_V, view),
				new MenuDescriptor("Help", KeyEvent.VK_H, help) };

		for (MenuDescriptor md : mds) {
			JMenu menu = new JMenu(md.name);
			menu.setMnemonic(md.mnemonik);

			for (AbstractAction aa : md.aaarray) {
				JMenuItem mi = new JMenuItem(aa);
				menu.add(mi);
				mi.setIcon(null);
			}

			add(menu);
		}

	}

}
