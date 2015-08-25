package editor.model.data;

public class GraphicElement {
	
	public enum Type{
		RECTANGLE,
		CIRCLE,
		TRIANGLE,
		STAR
	}
		
	public Type getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public double getScale() {
		return scale;
	}
	public double getAngle() {
		return angle;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public int getR() {
		return R;
	}
	public int getG() {
		return G;
	}
	public int getB() {
		return B;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setR(int r) {
		R = r;
	}
	public void setG(int g) {
		G = g;
	}
	public void setB(int b) {
		B = b;
	}
	private Type type;
	private String name;
	private String description;
	
	private double scale;
	private double angle;
	private double x,y;
	private int R;
	private int G;
	private int B;
	
	public GraphicElement(Type type, String name, String description, double x,
			double y, double scale, double angle, int r, int g, int b) {
		super();
		
		this.type = type;
		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.angle = angle;
		R = r;
		G = g;
		B = b;
	}
	
	public void Set(GraphicElement ge){
		this.type = ge.type;
		this.description = ge.description;
		this.x = ge.x;
		this.y = ge.y;
		this.scale = ge.scale;
		this.angle = ge.angle;
		R = ge.R;
		G = ge.G;
		B = ge.B;
	}
	
	//copy constructor
	public GraphicElement(GraphicElement source){
		this.type = source.type;
		this.name = source.name;
		this.description = source.description;
		this.y = source.y;
		this.x = source.x;
		this.scale = source.scale;
		this.angle = source.angle;
		R = source.R;
		G = source.G;
		B = source.B;
	}
}
