package editor.view.workspace.state;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import editor.model.data.GraphicElement;
import editor.model.data.GraphicElement.Type;
import editor.view.workspace.GEOptionDialog;
import editor.view.workspace.PFrameView;
import editor.view.workspace.SelectionManager.HandleSelection;

public class SelectState extends PFrameState {

	private class MousePressState{
		public boolean leftClick = false;
		public boolean onSelected = false;
		public boolean ctrlDown = false;		
		public GraphicElement pressedGE = null;
		public HandleSelection hs = null;
	}
	
	PFStateManager sm;
	PFrameView pfv;
	MousePressState mousePressState = null;
	
	public SelectState(PFStateManager sm, PFrameView pfv){
		this.sm = sm;
		this.pfv = pfv;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		Point position = pfv.getLogicCoordinates(e.getPoint());
		
		mousePressState = new MousePressState();
		mousePressState.leftClick = e.getButton() == MouseEvent.BUTTON1;
		mousePressState.ctrlDown = e.isControlDown();
		mousePressState.pressedGE = pfv.getGElementAt(position);
		mousePressState.hs = pfv.getSelectionManager().getHandleSelection(position);
		
		if(mousePressState.hs != null){
			pfv.getSelectionManager().clearSelection();
			pfv.getSelectionManager().addToSelection(mousePressState.hs.gelem);
			return;
		}
		
		
		if(mousePressState.pressedGE == null)
			pfv.getSelectionManager().clearSelection();
		else 
		{		
			if(e.getClickCount() ==2){
				
				pfv.getSelectionManager().clearSelection();
				pfv.getSelectionManager().addToSelection(mousePressState.pressedGE);	
				
				new GEOptionDialog(mousePressState.pressedGE,pfv, pfv.getFrameController());
				
				return;
			}
			
			
			mousePressState.onSelected = true;
			
			if(!pfv.getSelectionManager().isSelected(mousePressState.pressedGE)){
				
				mousePressState.onSelected = false;
				
				if(!mousePressState.ctrlDown)
					pfv.getSelectionManager().clearSelection();
				
				pfv.getSelectionManager().addToSelection(mousePressState.pressedGE);	
			}

				
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(mousePressState.hs != null){
			sm.setHs(mousePressState.hs);
			sm.switchToResizeState();
			return;
		}
		
		if(!mousePressState.leftClick || mousePressState.pressedGE==null)
			return;
		
		sm.switchToMoveState();
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
		if(mousePressState.onSelected && !mousePressState.ctrlDown){
			pfv.getSelectionManager().clearSelection();
			pfv.getSelectionManager().addToSelection(mousePressState.pressedGE);
		}
		
		mousePressState = new MousePressState();
	} 

	@Override
	public void mouseMoved(MouseEvent e) {
		pfv.updateMouseCursor(pfv.getLogicCoordinates(e.getPoint()));
	}


	@Override
	public String getName() {
		return "Select";
	}
}
