package editor.model.data;

public class Event{
	
	public enum Type{
		CHANGE,
		RENAME
	}
	
	
	
	private String oldName;
	private String newName;
	
	private Type type;

	public String getOldName() {
		return oldName;
	}

	public String getNewName() {
		return newName;
	}

	public Type getType() {
		return type;
	}

	public Event(Type type, String oldName, String newName) {
		super();
		this.type = type;
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public Event(Type type) {
		this(type, null, null);

	}
}