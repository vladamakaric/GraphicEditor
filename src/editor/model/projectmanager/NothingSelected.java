package editor.model.projectmanager;

import editor.model.Project;

public class NothingSelected extends StateAdapter{

	public NothingSelected(ProjectManager pm){
		super(pm);
		type = State.Type.NO_SELECT;
	}

	@Override
	public void init() {
		projectManager.addEventToQueue(new Event(Event.Type.SWITCH_ACTIVE, null, null));
	}

	@Override
	public void hideFrame(String frameName, String projectName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showFrame(String frameName, String projectName) {
		// TODO Auto-generated method stub
		
	}


}
