package editor.model.projectmanager;

import editor.model.Project;

public class FrameSelected extends StateAdapter{

	public FrameSelected(ProjectManager pm){
		super(pm);
		type = State.Type.FRAME_SELECT;
	}
	
	@Override
	public void init() {
		Project cp = projectManager.getCurrentProject();
		projectManager.addEventToQueue(new Event(Event.Type.SWITCH_ACTIVE, cp.getName(), cp.getActiveFrameName()));
	}
	
	@Override
	public void selectFrame(String frameName, String projectName) {
		Project currPrj = projectManager.getCurrentProject();
		
		if(currPrj.getName().equals(projectName) && 
		   currPrj.getActiveFrameName().equals(frameName) )
			return;
		
		super.selectFrame(frameName, projectName);
	}

}
