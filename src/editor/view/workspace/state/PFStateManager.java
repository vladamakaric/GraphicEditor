package editor.view.workspace.state;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import editor.controller.FrameController;
import editor.view.workspace.PFrameView;
import editor.view.workspace.SelectionManager.HandleSelection;


public class PFStateManager extends MouseAdapter {

	private PFrameState currentState;
	private PFrameView pfv;

	SelectState selectState;
	MoveState moveState;
	ResizeState resizeState;
	
	CircleState circleState; 
	RectangleState rectangleState; 	
	StarState starState; 
	TriangleState triangleState; 
	
	private Point lastPressPosition;
	private HandleSelection hs;
	
	public HandleSelection getHs() {
		return hs;
	}

	public void setHs(HandleSelection hs) {
		this.hs = hs;
	}

	public Point getLastPressPosition() {
		return lastPressPosition;
	}

	public void setLastPressPosition(Point lastPressPosition) {
		this.lastPressPosition = lastPressPosition;
	}

	public PFStateManager(PFrameView pfv, FrameController fc){
		this.pfv = pfv;
		selectState = new SelectState(this, pfv);
		moveState = new MoveState(this, fc, pfv);
		resizeState = new ResizeState(this, fc, pfv);
		
		rectangleState = new RectangleState(this, fc, pfv);
		circleState = new CircleState(this, fc, pfv);
		triangleState = new TriangleState(this, fc, pfv);
		starState = new StarState(this, fc, pfv);
		
		currentState = selectState;
	}
	
	public PFrameState getCurrentState(){
		return currentState;
	}
	
	public void switchToStarState(){
		switchState(starState);
	}
	
	public void switchToTriangleState(){
		switchState(triangleState);
	}
	
	public void switchToCircleState(){
		switchState(circleState);
	}
	
	public void switchToRectangleState(){
		switchState(rectangleState);
	}
	
	public void switchToSelectState(){
		switchState(selectState);
	}
	
	public void switchToMoveState(){
		switchState(moveState);
	}

	public void switchToResizeState(){
		switchState(resizeState);
	}
	
	
	public void switchState(PFrameState nextState){
		currentState = nextState;
		currentState.init();
		pfv.getFrameController().stateChanged();
	}
	
	public void mousePressed(MouseEvent e) {
		lastPressPosition = pfv.getLogicCoordinates(e.getPoint());
		currentState.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		currentState.mouseReleased(e);
	}
	
	public void mouseDragged(MouseEvent e ){
		currentState.mouseDragged(e);
	}
	
	public void mouseMoved(MouseEvent e) {
		currentState.mouseMoved(e);
	}
	
}
