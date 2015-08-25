package editor.model.projectmanager;

import editor.model.Project;

public class ProjectSelected extends StateAdapter{

	public ProjectSelected(ProjectManager pm){
		super(pm);
		type = State.Type.PROJ_SELECT;
	}
	
	@Override
	public void init() {
		Project cp = projectManager.getCurrentProject();
		projectManager.addEventToQueue(new Event(Event.Type.SWITCH_ACTIVE, cp.getName(), null));
	}
	
	@Override
	public void selectProject(String name) {
		if(!projectManager.getCurrentProject().getName().equals(name))
			super.selectProject(name);
	}

	@Override
	public void deleteActiveFrame() {
		//nista, jer nepostoji active frame
	}



}
