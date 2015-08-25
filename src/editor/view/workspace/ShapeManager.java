package editor.view.workspace;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import editor.model.data.GraphicElement;

public class ShapeManager {
	
	final static float dash1[] = { 10.0f };
	  final static BasicStroke dashed = new BasicStroke(1.0f,
	      BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	
	AffineTransform getElementTransform(GraphicElement gelem){
		AffineTransform etran = new AffineTransform();
		etran.translate(gelem.getX(), gelem.getY());
		etran.rotate(gelem.getAngle());
		etran.scale(gelem.getScale(), gelem.getScale());

		Rectangle2D r2 = getElementShape(gelem).getBounds2D();
		
		double dx = -r2.getCenterX();
		double dy = -r2.getCenterY();
		
		etran.translate(dx, dy);
		
		return etran;
	}
	
	public void paint(GraphicElement gelem, Graphics2D g){
		AffineTransform oldTransform = g.getTransform();
		
		g.transform(getElementTransform(gelem));
		
		g.setPaint(new Color(gelem.getR(), gelem.getG(), gelem.getB()));
		g.fill(getElementShape(gelem));	
		
		g.setTransform(oldTransform);
	}
	
	public void paintSelection(GraphicElement gelem, int handleSize, Graphics2D g){
		AffineTransform oldTransform = g.getTransform();
		AffineTransform shapeTransform = (AffineTransform) oldTransform.clone();
		AffineTransform etran = getElementTransform(gelem);
		
		shapeTransform.concatenate(etran);
		
		Shape transformedShape = shapeTransform.createTransformedShape(getElementShape(gelem));
		
		g.setTransform(new AffineTransform());
		
		g.setPaint(Color.gray);
	    g.setStroke(dashed);

	    Rectangle2D gelemAABB = transformedShape.getBounds2D();
	    g.draw(gelemAABB);
	    
	    double width =  gelemAABB.getWidth();
	    double height = gelemAABB.getHeight();
	    int left = (int) Math.round(gelemAABB.getX());
	    int top =  (int)Math.round(gelemAABB.getY());
	    int right = (int)Math.round(gelemAABB.getX() + width);
	    int bottom = (int)Math.round(gelemAABB.getY() + height);
	    
	    paintHandle(new Point(left, top), handleSize, g);
	    paintHandle(new Point(right, top), handleSize, g);
	    paintHandle(new Point(right, bottom), handleSize, g);
	    paintHandle(new Point(left, bottom), handleSize, g);
	    
	    g.setTransform(oldTransform);
	}
	
	private void paintHandle(Point pos, int handleSize,Graphics2D g){
		g.fillRect(pos.x - handleSize/2, pos.y - handleSize/2, handleSize, handleSize);
	}
	
	public Shape getTransformedShapeFromGE(GraphicElement gelem){
		Shape elshape = getElementShape(gelem);
		AffineTransform etran = getElementTransform(gelem);
		Shape transformedShape = etran.createTransformedShape(elshape);
		return transformedShape;
	}
	
	//pozicija u logickim koord
	public boolean isElementAt(GraphicElement gelem, Point pos){
		return getTransformedShapeFromGE(gelem).contains(pos);
	}
	
	private Shape getRectangleShape(){
		final int width = 100;
		final int height = 70;
		
		return new RoundRectangle2D.Double(
			-width/2, 
			-height/2,
			width,
			height,
            0, 0);
	}
	
	private Shape getCircleShape(){
		final int width = 100;
		final int height = 70;
		return new Ellipse2D.Double(
				-width/2, 
				-height/2,
				width,
				height);
	}
	
	private Shape getTriangleShape(){
		double r = 100;
		double phi = -Math.PI/2;
		GeneralPath triangle = new GeneralPath();
		
		triangle.moveTo(r*Math.cos((2*Math.PI)/3 + phi), r*Math.sin((2*Math.PI)/3 + phi));
		triangle.lineTo(r*Math.cos((2*Math.PI)*(2/3.0) + phi), r*Math.sin((2*Math.PI)*(2/3.0) + phi));
		triangle.lineTo(r*Math.cos(phi), r*Math.sin(phi));

		triangle.closePath();
		
		return triangle;
	}
	
	private Shape getStarShape() {
		int xPoints[] = { 0, 12, 54, 18, 28, 0, -28, -18, -54, -12 };
		int yPoints[] = { -51, -15, -15, 3, 45, 21, 45, 3, -15, -15 };
		
		GeneralPath star = new GeneralPath();

		star.moveTo(xPoints[0], yPoints[0]);

		for (int k = 1; k < xPoints.length; k++){
			star.lineTo(xPoints[k], yPoints[k]);
		}

		star.closePath();
		return star;
	}

	public Shape getElementShape(GraphicElement gelem){
		switch(gelem.getType()){
		case RECTANGLE:
			return getRectangleShape();
		case STAR:
			return getStarShape();
		case TRIANGLE:
			return getTriangleShape();
		case CIRCLE:
			return getCircleShape();
		default:
			return null;
		}
	}
}
