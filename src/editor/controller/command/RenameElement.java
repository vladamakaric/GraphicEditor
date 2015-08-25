package editor.controller.command;

import editor.model.data.GraphicData;
import editor.model.data.GraphicElement;

public class RenameElement extends Command {
	GraphicElement gelem;
	String oldName;
	String newName;
	public RenameElement(GraphicData gdata, String oldName, String newName) {
		type = Type.NEW;
		this.oldName =oldName;
		this.newName = newName;
		this.graphicData = gdata;
		this.gelem = gelem;
	}
	@Override
	public void execute() {
		graphicData.renameElement(oldName, newName);
	}

	@Override
	public void undo() {
		graphicData.renameElement(newName, oldName);
	}

}
