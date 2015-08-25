package editor.view.workspace.state;

import java.awt.event.MouseEvent;

public abstract class PFrameState {
	
	abstract public String getName();
	public void mousePressed(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	public void init(){}
}
