package editor.controller.frameactions;

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
import editor.view.workspace.state.PFStateManager;
import editor.view.Window;

public class CreateRectangle extends AbstractAction {
	PFStateManager pfsm;
	public CreateRectangle(PFStateManager pfsm, ResourceManager rm) {
		this.pfsm = pfsm;
		String shortDesc = "Create Rectangle";
		
		putValue(LARGE_ICON_KEY, rm.getIcon(ICONS.RECTANGLE));
		putValue(NAME, shortDesc);
		putValue(SHORT_DESCRIPTION ,shortDesc);
		
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
	}

	public void actionPerformed(ActionEvent event) {
		pfsm.switchToRectangleState();
	}

}
