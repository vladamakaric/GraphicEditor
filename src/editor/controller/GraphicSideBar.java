package editor.controller;

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import editor.view.ResourceManager;

public class GraphicSideBar extends JToolBar {

	public GraphicSideBar(AbstractAction[] actions ) {
		super(SwingConstants.VERTICAL);
		
		ButtonGroup group = new ButtonGroup();
		
		boolean firstIter=true;
		for(AbstractAction action : actions){
			JToggleButton jtb = new JToggleButton(action);
			
			if(firstIter){
				jtb.setSelected(true);
				firstIter = false;
			}
			
			jtb.setText("");
			add(jtb);
			group.add(jtb);
		}

		setFloatable(false);
	}
}
