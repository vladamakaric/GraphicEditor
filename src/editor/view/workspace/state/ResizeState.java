package editor.view.workspace.state;

import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import editor.controller.FrameController;
import editor.model.data.GraphicElement;
import editor.view.workspace.PFrameView;
import editor.view.workspace.SelectionManager.Handle;

public class ResizeState extends PFrameState {

	
	PFStateManager sm;
	FrameController fc;
	PFrameView pfv;
	Point refHandlePos;
	
	GraphicElement ge;
	Handle handle;
	boolean startResize = false;
	
	Map<Handle, Point> handlePos;
	
	public ResizeState(PFStateManager sm, FrameController fc, PFrameView pfv ){
		this.sm = sm;
		this.fc = fc;
		this.pfv = pfv;
	}
	
	
	@Override
	public String getName() {
		return "Resize";
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(!startResize) {
			startResize = true;
			
			ge = sm.getHs().gelem;
			handle = sm.getHs().handle;
			refHandlePos = pfv.getSelectionManager().getGEHandlePosition(ge, handle);
			fc.startCommandBlock();
		}
		
		Point position = pfv.getLogicCoordinates(e.getPoint());
		Shape shp = pfv.getShapeManager().getTransformedShapeFromGE(ge);
		Rectangle2D bounds = shp.getBounds2D();
		Point center = new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
		Point delta = new Point(position.x - refHandlePos.x, position.y - refHandlePos.y);
		
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		
		Point localPosition = new Point(position.x - center.x, position.y - center.y);
		double newScale=1;
		double offsetX = 0;
		double offsetY = 0;
		double dxSign = 1;
		double dySign = 1;
		
		switch(handle){
		case NorthWest:
			delta.x*=-1;
			delta.y*=-1;
			dxSign= dySign = -1;
			break;
		case SouthWest:
			delta.y*=-1;
			dySign = -1;
			break;
		case NorthEast:
			delta.x*=-1;
			dxSign = -1;
			break;
		}
		
		if(delta.x < delta.y){
			newScale = (width/2 + Math.abs(localPosition.x))/(width);
		}
		else{
			newScale = (height/2 + Math.abs(localPosition.y))/(height);
		}
		
		offsetY = dxSign*(height*newScale - height)/2;
		offsetX = dySign*(width*newScale - width)/2;
		
		fc.changeElementScaleAndPosition(ge.getName(), ge.getScale()*newScale,
									ge.getX() + offsetX, ge.getY() + offsetY);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pfv.updateMouseCursor(pfv.getLogicCoordinates(e.getPoint()));
		startResize = false;
		
		fc.endCommandBlock();
		sm.switchToSelectState();
	}
}
