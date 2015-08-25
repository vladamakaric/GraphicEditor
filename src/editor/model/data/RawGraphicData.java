package editor.model.data;

import java.util.ArrayList;
import java.util.Collection;

public class RawGraphicData {
	private String name;
	
	public String getName() {
		return name;
	}

	private ArrayList<GraphicElement> gelements;
	
	public Collection<GraphicElement> getGelements() {
		return gelements;
	}

	public RawGraphicData(GraphicData gdata){
		this.name = gdata.getName();
		gelements = new ArrayList<GraphicElement>(gdata.getElements());
	}
}
