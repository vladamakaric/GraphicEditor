package editor.view.workspace.state;

import editor.controller.FrameController;
import editor.model.data.GraphicElement;
import editor.model.data.GraphicElement.Type;
import editor.view.workspace.PFrameView;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;

public class MoveState extends PFrameState {
	PFStateManager sm;
	FrameController fc;
	PFrameView pfv;
	
	boolean startDrag = false;
	public MoveState(PFStateManager sm, FrameController fc, PFrameView pfv ){
		this.sm = sm;
		this.fc = fc;
		this.pfv = pfv;
	}
	
	@Override
	public String getName() {
		return "Move";
	}
	
	public void mouseDragged(MouseEvent e) {
		
		pfv.setDrawSpaceCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		Point lastPosition = pfv.getLogicCoordinates(e.getPoint());
		
		double dx = lastPosition.getX() - sm.getLastPressPosition().getX();
		double dy = lastPosition.getY() - sm.getLastPressPosition().getY();
		
		if(Math.abs(dy) < 1 && Math.abs(dx) <1)
			return;
		
		if(!startDrag) {
			fc.startCommandBlock();
			startDrag = true;
		}

		Collection<GraphicElement> selectedGElems = pfv.getSelectionManager().getSelectedElements();

		Iterator<GraphicElement> it = selectedGElems.iterator();
		
		while (it.hasNext()) {
			GraphicElement gelem = it.next();
			
			double newX = gelem.getX() + dx;
			double newY = gelem.getY() + dy;
			
			fc.changeElementPosition(gelem.getName(), newX, newY);
		}
		
		sm.setLastPressPosition(lastPosition);
	}

	public void mouseReleased(MouseEvent e) {
		pfv.updateMouseCursor(pfv.getLogicCoordinates(e.getPoint()));
		startDrag = false;
		
		fc.endCommandBlock();
		sm.switchToSelectState();
	}

}
