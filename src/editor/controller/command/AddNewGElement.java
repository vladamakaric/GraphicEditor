package editor.controller.command;

import editor.model.data.GraphicData;
import editor.model.data.GraphicElement;

public class AddNewGElement extends Command {
	GraphicElement gelem;
	
	public AddNewGElement(GraphicData gdata, GraphicElement gelem) {
		type = Type.NEW;
		this.graphicData = gdata;
		this.gelem = gelem;
	}
	@Override
	public void execute() {
		graphicData.addNewElement(new GraphicElement(gelem));
	}

	@Override
	public void undo() {
		graphicData.removeElement(gelem.getName());
	}

}
