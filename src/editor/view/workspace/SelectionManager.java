package editor.view.workspace;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import editor.model.data.GraphicData;
import editor.model.data.GraphicElement;

public class SelectionManager {
	
	public enum Handle{
		NorthWest,
		SouthWest,
		SouthEast,
		NorthEast
	}
	
	public class HandleSelection{
		public Handle handle;
		public GraphicElement gelem;
		
		HandleSelection(GraphicElement gelem, Handle handle){
			this.handle = handle;
			this.gelem = gelem;
		}
	}
	
	private List<GraphicElement> selectedGElems = new ArrayList<GraphicElement>();
	private ShapeManager shapeMngr;
	int handleSize = 8;
	PFrameView pfv;

	public SelectionManager(PFrameView pfv){
		this.shapeMngr = pfv.getShapeManager();
		this.pfv = pfv;
	}
	
	public Point getGEHandlePosition(GraphicElement ge, Handle handle){
		
		Shape shp = shapeMngr.getTransformedShapeFromGE(ge);
		
		Rectangle2D rect = shp.getBounds2D();
		
		switch (handle){
		case NorthEast:
			return new Point((int)(rect.getX() + rect.getWidth()), (int)rect.getY());
		case NorthWest:
			return new Point((int)rect.getX(), (int)rect.getY());
		case SouthWest:
			return new Point((int)rect.getX(), (int)(rect.getY()  + rect.getHeight()));
		case SouthEast:
			return new Point((int)(rect.getX() + rect.getWidth()), (int)(rect.getY()  + rect.getHeight()));
		}
		
		return null;
	}
	
	private Handle getClosest(GraphicElement ge, Point refPoint){
		
		Handle[] handles = {Handle.NorthEast,  Handle.NorthWest, Handle.SouthWest, Handle.SouthEast};

		double minDist = Double.MAX_VALUE;
		Handle minHandle = null;
		
		for(int i=0; i<4; i++){
			Point currPos = getGEHandlePosition(ge, handles[i]);
			
			double currDist = currPos.distanceSq(refPoint);
			
			if(minDist > currDist){
				minDist = currDist;
				minHandle = handles[i];
			}
		}
		
		return minHandle;
	}
	
	private void selectionChangedAction(){
		pfv.getFrameController().selectionChanged();
		pfv.repaint();
	}
	
	public HandleSelection getHandleSelection(Point logicCoords){
		
		
		double minDist = Double.MAX_VALUE;
		HandleSelection minHS = null;
		
		for(GraphicElement ge : selectedGElems){
			
			HandleSelection hs = new HandleSelection(ge, getClosest(ge,logicCoords));
			
			Point pos = getGEHandlePosition(ge,hs.handle);
			
			double currentDist = pos.distanceSq(logicCoords);
			
			if(minDist > currentDist){
				minDist = currentDist;
				
				minHS = hs;
			}
		}
		
		if(Math.sqrt(minDist)*pfv.getCameraController().getScale() < handleSize)
			return minHS;
		
		return null;
	}
	
	public Collection<GraphicElement> getSelectedElements(){
		return selectedGElems;
	}
	
	public void addToSelection(GraphicElement gelem){
		selectedGElems.add(gelem);
		pfv.repaint();
		selectionChangedAction();
	}
	
	public boolean singleSelection(){
		return selectedGElems.size() == 1;
	}
	
	public void removeDeletedGEs(){
		Iterator<GraphicElement> itr = selectedGElems.iterator();

		while (itr.hasNext()) {
			
			GraphicData gdata = pfv.getProjectFrame().getGraphicData();
			
			if(gdata.getElement(itr.next().getName()) == null){
				itr.remove();
				selectionChangedAction();
			}
		}
	}
	
	public GraphicElement getSelectedGElement(){
		return selectedGElems.get(0);
	}
	
	public void clearSelection(){
		selectedGElems.clear();
		
		selectionChangedAction();
	}
	
	public void paintSelections(Graphics2D g){
		for (GraphicElement gelem : selectedGElems){
			shapeMngr.paintSelection(gelem, handleSize, g);
		}
	}

	public boolean isSelected(GraphicElement pressedGE) {
		return selectedGElems.contains(pressedGE);
	}
}
