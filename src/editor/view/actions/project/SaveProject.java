package editor.view.actions.project;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import editor.controller.Controller;
import editor.model.ProjectFrame;
import editor.model.data.GraphicData;
import editor.model.projectmanager.ProjectManager;
import editor.view.ResourceManager;
import editor.view.ResourceManager.ICONS;
import editor.view.Window;

public class SaveProject extends AbstractAction {

	Window window;
	Controller cntrl;
	public SaveProject(Window window, Controller cntrl, ResourceManager rm) {
		this.cntrl = cntrl;
		this.window = window;
		String shortDesc = "Save Project";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.SAVE_PROJECT));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent event) {

		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new GraphicEditorFileFilter());
		jfc.setSelectedFile(new File("projectName.gpf"));

		if (jfc.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
			File projectFile = jfc.getSelectedFile();
			
			cntrl.saveCurrentProject(projectFile);
		} 
	}

}
