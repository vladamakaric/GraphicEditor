package editor.controller.command;

import editor.model.data.GraphicData;
import editor.model.data.GraphicElement;

public class DeleteGElement extends Command {
	
	GraphicElement gelem;
	public DeleteGElement(GraphicData gdata, GraphicElement gelem) {
		type = Type.DELETE;
		this.graphicData = gdata;
		this.gelem = new GraphicElement(gelem);
	}
	@Override
	public void execute() {
		graphicData.removeElement(gelem.getName());	
	}

	@Override
	public void undo() {
		graphicData.addNewElement(new GraphicElement(gelem));
	}

}
