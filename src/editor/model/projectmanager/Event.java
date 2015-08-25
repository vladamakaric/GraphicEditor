package editor.model.projectmanager;

public class Event {

	public Type getType() {
		return type;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getFrameName() {
		return frameName;
	}

	public enum Type {
		NEW,  
		SWITCH_ACTIVE,
		DELETE,  
		HIDE_FRAME, 
		SHOW_FRAME,
		CHANGE_STATE
	}

	Type type;

	String projectName;
	String frameName;

	public Event(Type type, String projName, String frameName) {
		this.type = type;
		this.projectName = projName;
		this.frameName = frameName;
	}
}
