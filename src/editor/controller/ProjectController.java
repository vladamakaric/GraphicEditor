package editor.controller;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import editor.model.Project;
import editor.model.projectmanager.ProjectManager;
import editor.view.workspace.PFrameView;

public class ProjectController {
	
	private ProjectManager pm;
	private Controller cntrl;
	private FrameController currentFC;
	private Map<String, FrameController> frameControllers;
	private Project project;
	
	public ProjectController(Project project, Controller cntrl, ProjectManager pm){
		this.project = project;
		this.pm = pm;
		this.cntrl = cntrl;
		frameControllers = new HashMap<String, FrameController>();
	}
	
	public Project getProject(){
		return project;
	}
	
	public FrameController getCurrentFrameController(){
		return currentFC;
	}
	
	//Ovde se kreira FrameController, koji povezuje vec postojeci PrjFrame (model) i PrjFrameView
	public PFrameView createFrameView(String frameName, Point position){
		PFrameView pfv = new PFrameView(project.getName() + ": "+ frameName, position);
		FrameController fc = new FrameController(
				this, 
				project.getFrame(frameName), 
				pfv,
				cntrl.getResourceManager());
	
		frameControllers.put(frameName, fc);		
		return pfv;
	}
	
	public void changeFrame(){
		String activeFrameName = pm.getCurrentProject().getActiveFrameName();
		
		currentFC = frameControllers.get(activeFrameName);
	}
	
	public Controller getController(){
		return cntrl;
	}
	
	public void deleteFrameController(String fname){
		frameControllers.remove(fname);
	}

	public void deactivateFrameController() {
		currentFC = null;
		// TODO Auto-generated method stub
	}
}