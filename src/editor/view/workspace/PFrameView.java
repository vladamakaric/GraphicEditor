package editor.view.workspace;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import editor.controller.FrameController;
import editor.model.ProjectFrame;
import editor.model.data.Event;
import editor.model.data.Event.Type;
import editor.model.data.GraphicElement;
import editor.model.data.Observer;
import editor.view.workspace.SelectionManager.HandleSelection;
import editor.view.workspace.state.PFStateManager;

public class PFrameView extends JInternalFrame implements Observer {
	
	
	SelectionManager selectionMngr;
	FrameController fc;
	ProjectFrame pf;
	DrawSurface ds;
	CameraController camerac;
	ShapeManager shapeMngr;
	PFStateManager sm; 
	
	private class DrawSurface extends JPanel{
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.transform(camerac.getTransformation());
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

			
			Collection<GraphicElement> gelements = pf.getGraphicData().getElements();
			
			for(GraphicElement gelement : gelements){
				shapeMngr.paint(gelement, g2);
			}
			
			selectionMngr.paintSelections(g2);			
		}
	}

	public PFrameView(String title, Point pos){
		super(title, true, true, true, true);
			
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(pos.x, pos.y, 400, 400);
			
		ds = new DrawSurface();
    	ds.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	ds.setBackground(Color.WHITE);
		getContentPane().add(ds,BorderLayout.CENTER);
		
		camerac = new CameraController(this);
		ds.addMouseListener(camerac);
		ds.addMouseMotionListener(camerac);
		ds.addMouseWheelListener(camerac);
		
		shapeMngr = new ShapeManager();
		selectionMngr = new SelectionManager(this);
	}
	
	public FrameController getFrameController(){
		return fc;
	}
	
	public Point getLogicCoordinates(Point deviceCoord){
		Point logicCoord = new Point(deviceCoord);
		
		camerac.transformToUserSpace(logicCoord);
		
		return logicCoord;
	}
	
	public SelectionManager getSelectionManager(){
		return selectionMngr;
	}
	
	public void setDrawSpaceCursor(Cursor cursor) {
		ds.setCursor(cursor);
	}

	public ProjectFrame getProjectFrame(){
		return pf;
	}

	public GraphicElement getGElementAt(Point pos){
		Collection<GraphicElement> gelements = pf.getGraphicData().getElements();
		
		for(GraphicElement gelem : gelements){
			if(shapeMngr.isElementAt(gelem, pos))
				return gelem;
		}
		
		return null;
	}
	
	public ShapeManager getShapeManager(){
		return shapeMngr;
	}
	
	public CameraController getCameraController(){
		return camerac;
	}
	
	public PFStateManager getStateManager(){
		return sm;
	}
	
	public void updateMouseCursor(Point logicPosition){
	
		HandleSelection hs = selectionMngr.getHandleSelection(logicPosition);
		
		if (hs != null) {

			Cursor cursor = null;
			switch (hs.handle) {

			case SouthEast:
				cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
				break;
			case NorthWest:
				cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
				break;
			case SouthWest:
				cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
				break;
			case NorthEast:
				cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
				break;
			}

			setDrawSpaceCursor(cursor);
			
		} else {

			GraphicElement gelem = getGElementAt(logicPosition);

			if (gelem != null)
				setDrawSpaceCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				setDrawSpaceCursor(Cursor.getDefaultCursor());
		}

	}
	
	public void setFrameController(FrameController fc){
		this.fc = fc;
		pf = fc.getProjectFrame();
		
		fc.getProjectFrame().getGraphicData().addObserver(this);
		
		sm = new PFStateManager(this,fc);
		ds.addMouseListener(sm);
		ds.addMouseMotionListener(sm);
	}

	@Override
	public void update(Event event) {
		if(event.getType() == Type.RENAME){
			selectionMngr.clearSelection();
			selectionMngr.addToSelection(pf.getGraphicData().getElement(event.getNewName()));
		}
		else
			selectionMngr.removeDeletedGEs();	
		
		fc.graphicDataChanged();
		repaint();
		
	}
}
