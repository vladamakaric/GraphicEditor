package editor.view;

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import editor.view.actions.ActionManager;
import editor.view.actions.projcontent.RotateCW;

public class CommonToolBar extends JToolBar {

	public CommonToolBar(ActionManager actionManager) {
		super(SwingConstants.HORIZONTAL);

		AbstractAction[][] actions = {
				{ actionManager.newProject, actionManager.newDialog,
				  actionManager.openProject },
				{ actionManager.saveProject },
				{ actionManager.undo, actionManager.redo },
				{ actionManager.rotateCW, actionManager.rotateCCW, actionManager.deleteSelection },
				{ actionManager.zoomIn, actionManager.zoomOut },
				{ actionManager.tileWHorizontally,
				  actionManager.tileWVertically,
				  actionManager.cascadeWindows,
				  actionManager.previousWindow, actionManager.nextWindow },
				{ actionManager.about } };

		for (AbstractAction[] aaarray : actions) {

			for (AbstractAction aa : aaarray) {
				JButton btn = new JButton(aa);
				add(btn);
				btn.setHideActionText(true);
			}

			addSeparator();
		}

		setFloatable(false);

	}
}