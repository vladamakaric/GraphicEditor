package editor.view.workspace;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.nio.channels.NetworkChannel;

import javax.swing.JScrollBar;

public class CameraController extends MouseAdapter implements AdjustmentListener{
	double translateX = 0;
	double translateY = 0;
	double scaling = 1;
	double translateFactor = 10;
	double scalingFactor = 1.2;
	double maxSF = 5;
	double minSF = 0.2;
	
	private AffineTransform transformation = new AffineTransform();
	private PFrameView pfview;
	private JScrollBar sbVertical;
	private JScrollBar sbHorizontal;
	private int hScrollValue=50;
	private int vScrollValue=50;
	
	
	
	public CameraController(PFrameView pfview){
		this.pfview = pfview;
		sbHorizontal=new JScrollBar(JScrollBar.HORIZONTAL, hScrollValue, 30, 0, 100);
		sbVertical=new JScrollBar(JScrollBar.VERTICAL, vScrollValue, 30, 0, 100);
		
		sbHorizontal.addAdjustmentListener(this);
		sbVertical.addAdjustmentListener(this);
		pfview.add(sbHorizontal,BorderLayout.SOUTH);
		pfview.add(sbVertical,BorderLayout.EAST);
		
		setupTransformation();
	}
	
	public double getScale(){
		return scaling;
	}
	
	public void externalZoomIn(){
		scaling *= scalingFactor;
		clampScaling();
		setupTransformation();
		pfview.repaint();
	}
	
	public void externalZoomOut(){
		scaling /= scalingFactor;
		clampScaling();
		setupTransformation();
		pfview.repaint();
	}
	
	public void transformToUserSpace(Point2D deviceSpace){
		try {
			transformation.inverseTransform(deviceSpace, deviceSpace);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void clampScaling(){
		scaling = Math.max(minSF, Math.min(maxSF, scaling));
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if((e.getModifiers() & MouseWheelEvent.CTRL_MASK) != 0){
			double oldScaling = scaling;
			
			if(e.getWheelRotation()>0)
				scaling *= (double)e.getWheelRotation()*scalingFactor;
			else
				scaling /= -(double)e.getWheelRotation()*scalingFactor;
			
			clampScaling();
			
			int screenX = e.getX();
			int screenY = e.getY();
			
			
			//logicke koordinate od ekranskih screenX i screenY koord 
			//moraju biti iste posle point zoom-a
			//logicXOld = (-oldScaling*translateXOld + screenX)/oldScaling == 
			//logicXNew = (-newScaling*translateXNew + screenX)/newScaling
			//iz ove jednacine se izrazi translateXNew, analogno i za Y
			
			double logicX = (-oldScaling*translateX + screenX)/oldScaling;
			double logicY = (-oldScaling*translateY + screenY)/oldScaling;
			
			translateX = screenX/scaling - logicX;
			translateY = screenY/scaling - logicY;
			
		//	sbVertical.setVisibleAmount((int) (20/scaling));
		//	sbHorizontal.setVisibleAmount((int) (20/scaling));
		}
		else if((e.getModifiers()&MouseWheelEvent.SHIFT_MASK) != 0){  // Ako je pritisnut Shift
			translateX += (double)e.getWheelRotation() * translateFactor/scaling;
		}else{ // u ostalim slučajevima vršimo skrolovanje po Y osi
			translateY += (double)e.getWheelRotation() * translateFactor/scaling;
		}
		
		setupTransformation();
		pfview.repaint();
	}

	private void setupTransformation() {
		transformation.setToIdentity();		
		transformation.scale(scaling, scaling);
		transformation.translate(translateX, translateY);		
	}
	
	public AffineTransform getTransformation(){
		return transformation;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if(e.getAdjustable().getOrientation()==Adjustable.HORIZONTAL){
			int dh = hScrollValue - e.getValue();
			translateX+=(double)(dh*translateFactor)/transformation.getScaleX();
			hScrollValue=e.getValue();
		}
		else{
			int dv = vScrollValue - e.getValue();
			translateY+=(double)(dv*translateFactor)/transformation.getScaleY();
			vScrollValue=e.getValue();
		}
		setupTransformation();
		
		pfview.repaint();
	}
}
