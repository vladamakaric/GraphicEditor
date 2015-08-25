package editor.model.projectmanager;

import editor.model.Project;

public interface State {
	
	public enum Type {
		NO_SELECT,  
		PROJ_SELECT,
		FRAME_SELECT
	}
	public Type getType();
	public void init();
	public void selectProject(String name);
	public void newProject(Project prj);
	public void openProject(Project prj);
	public void deleteActiveProject();
	public void deleteActiveFrame();
	public void selectFrame(String frameName, String projectName);
	public void newFrame(String name);
	public void hideFrame(String frameName, String projectName);
	public void showFrame(String frameName, String projectName);
}