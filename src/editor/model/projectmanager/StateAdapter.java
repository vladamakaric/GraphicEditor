package editor.model.projectmanager;

import java.util.Collection;

import editor.model.Project;
import editor.model.ProjectFrame;

public abstract class StateAdapter implements State{

	protected ProjectManager projectManager;
	protected Type type;
	
	
	public StateAdapter(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	
	@Override 
	public Type getType() {
		return type;
	}
	
	@Override
	public void selectProject(String name) {
		projectManager.changeCurrentProject(name);
		projectManager.switchState(new ProjectSelected(projectManager));
	}
	@Override
	public void openProject(Project prj){
		projectManager.addNewProject(prj);
		projectManager.changeCurrentProject(prj.getName());
		projectManager.addEventToQueue(new Event(Event.Type.NEW, prj.getName(), null));
		
		
		Collection<ProjectFrame> pframes = prj.getFrames();
		
		for(ProjectFrame pframe : pframes){
			projectManager.addEventToQueue(new Event(Event.Type.NEW, prj.getName(), pframe.getName()));
		}
		
		projectManager.notifyObservers();
		
		prj.changeActiveFrame(pframes.iterator().next().getName());;
		projectManager.switchState(new FrameSelected(projectManager));
	}
	
	@Override
	public void newProject(Project prj) {
		//Dodavanje novog projekta podrazumeva dodavanje istoimenog frejma
		projectManager.addNewProject(prj);
		projectManager.changeCurrentProject(prj.getName());
		projectManager.addEventToQueue(new Event(Event.Type.NEW, prj.getName(), null));
		newFrame(prj.getName());
	}
	
	@Override
	public void newFrame(String name) {
		Project cp = projectManager.getCurrentProject();
		cp.addNewFrame(name);
		cp.changeActiveFrame(name);
		projectManager.addEventToQueue(new Event(Event.Type.NEW, cp.getName(), name));
		projectManager.switchState(new FrameSelected(projectManager));
	}
	
	@Override
	public void deleteActiveProject() {
		projectManager.deleteCurrentProject();
		projectManager.switchState(new NothingSelected(projectManager));
	}

	@Override
	public void deleteActiveFrame() {
		Project cp = projectManager.getCurrentProject();
		
		if(cp.getFrameNum() == 1){
			deleteActiveProject();
			return;
		}
		
		projectManager.addEventToQueue(new Event(Event.Type.DELETE, cp.getName(), cp.getActiveFrameName()));
		cp.deleteActiveFrame();				
		selectProject(projectManager.getCurrentProject().getName());
	}

	@Override
	public void selectFrame(String frameName, String projectName) {
		projectManager.changeCurrentProject(projectName);
		projectManager.getCurrentProject().changeActiveFrame(frameName);
		projectManager.switchState(new FrameSelected(projectManager));
	}

	@Override
	public void hideFrame(String frameName, String projectName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showFrame(String frameName, String projectName) {
		projectManager.addEventToQueue(new Event(Event.Type.SHOW_FRAME, projectName, frameName));
		projectManager.notifyObservers();
	}
}
