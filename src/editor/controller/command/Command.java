package editor.controller.command;

import editor.model.data.GraphicData;

public abstract class Command {
	
	public enum Type{
		NEW,
		RENAME,
		DELETE,
		CHANGE
	}
	
	protected Type type;
	protected GraphicData graphicData;
	
	public Type getType(){
		return type;
	}
	
	abstract public void execute();
	abstract public void undo();
}
