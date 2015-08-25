package editor.view.workspace.state;

import java.awt.Point;
import java.awt.event.MouseEvent;

import editor.controller.FrameController;
import editor.model.data.GraphicElement;
import editor.model.data.GraphicElement.Type;
import editor.view.workspace.PFrameView;

public class StarState extends PFrameState {
	
	PFStateManager sm;
	FrameController fc;
	PFrameView pfv;
	public StarState(PFStateManager sm, FrameController fc,PFrameView pfv ){
		this.sm = sm;
		this.fc = fc;
		this.pfv = pfv;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		Point position = pfv.getLogicCoordinates(e.getPoint());
		fc.createNewGElement(Type.STAR, position.x, position.y);
	}

	@Override
	public String getName() {
		return "Star";
	}
}
