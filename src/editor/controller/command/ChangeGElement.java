package editor.controller.command;

import editor.model.data.GraphicData;
import editor.model.data.GraphicElement;

public class ChangeGElement extends Command {
	GraphicElement oldGE, newGE;
	String geName;
	public ChangeGElement(GraphicData gdata, String name, GraphicElement newGE) {
		type = Type.CHANGE;
		this.graphicData = gdata;
		this.newGE = newGE;
		geName = name;
		oldGE = new GraphicElement(graphicData.getElement(geName));
	}
	
	@Override
	public void execute() {
		graphicData.changeElement(geName, newGE);
	}

	@Override
	public void undo() {
		graphicData.changeElement(geName, oldGE);
	}

}
