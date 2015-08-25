package editor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import editor.model.Project;
import editor.model.ProjectFrame;
import editor.model.data.RawGraphicData;
import editor.model.data.RawProject;
import editor.model.projectmanager.Event;
import editor.model.projectmanager.Observer;
import editor.model.projectmanager.ProjectManager;
import editor.view.ResourceManager;
import editor.view.StatusBar;
import editor.view.Window;
import editor.view.workspace.PFrameView;

public class Controller implements Observer {
	private HashMap<String, ProjectController> projectControllers;
	private ProjectManager projectManager;// model
	private Window window; // view
	private ProjectController projectController;
	
	public Controller(ProjectManager projectManager) {
		this.projectManager = projectManager;
		projectManager.addObserver(this);
		projectControllers = new HashMap<String, ProjectController>();
		projectController = null;
	}

	public ResourceManager getResourceManager(){
		return window.getResourceManager();
	}
	
	public void createView() {
		window = new Window(this);
		window.initialize();
		projectManager.addObserver(window);
		window.setVisible(true);
		setViewStateForInactiveProject();
		setViewStateForInactiveDialog();
	}


	//////Event handling deo, koji poziva View kao reakcija na korisnicke akcije

	
	public void removeActiveProject() {
		projectManager.getState().deleteActiveProject();
	}

	public void removeActiveFrame() {
		projectManager.getState().deleteActiveFrame();
	}
	
	public void showDialog(String projectName, String dialogName) {
		projectManager.getState().showFrame(dialogName, projectName);
	}
	
	//ako korisnik selektuje Dialog, on je takodje selektovao njegov projekat
	//zato moraju zajedno da se azuriraju
	public void activateProjectAndFrame(String projectName, String frameName) {
		projectManager.getState().selectFrame(frameName, projectName);
	}

	public void activateProject(String name) {
		projectManager.getState().selectProject(name);
	}

	public void createNewFrame(String name) {
		projectManager.getState().newFrame(name);
	}
	
	public void createNewProject(String name) {
		projectManager.getState().newProject(new Project(name));
	}

	/////////////geteriSeteri//////////////////////
	
	public PFrameView getCurrentPFrameView(){
		return window.getPFrameView(projectManager.getCurrentProject().getName(), 
				projectManager.getCurrentProject().getActiveFrameName());
	}
	
	public ProjectController getProjectController() {
		return projectController;
	}

	public ProjectController getProjectController(String name){
		return projectControllers.get(name);
	}
	
	public Window getWindow() {
		return window;
	}

	public boolean dialogNameExists(String name) {
		return projectManager.getCurrentProject().frameNameExists(name);
	}

	public boolean projectNameExists(String name) {
		return projectControllers.containsKey(name);
	}

	///////////////////////////////////////////////////////
	
	private void deleteProjectController(String prjName){
		projectControllers.remove(prjName);
	}
	
	private void addNewProjectController(String projectName){
		
		ProjectController prjcntrl  = new ProjectController(projectManager.getProject(projectName),
				this, projectManager);
		
		projectControllers.put(projectName, prjcntrl);
	}
	
	private void updateProjectController(){
		projectController = projectControllers.get(projectManager.getCurrentProject().getName());
	}
	
	///////////////////////////
	public void setViewStateForActiveProject() {
		window.getActionManager().newDialog.setEnabled(true);
		window.getActionManager().saveProject.setEnabled(true);
		window.getActionManager().removeProject.setEnabled(true);
	}

	public void setViewStateForInactiveProject() {
		window.getActionManager().newDialog.setEnabled(false);
		window.getActionManager().saveProject.setEnabled(false);
		window.getActionManager().removeProject.setEnabled(false);
	}

	public void updateViewStateOnFrameChange(){
		FrameController fc = projectController.getCurrentFrameController();
		
		window.getActionManager().redo.setEnabled(false);
		window.getActionManager().undo.setEnabled(false);
		window.getActionManager().deleteSelection.setEnabled(false);
		window.getActionManager().rotateCCW.setEnabled(false);
		window.getActionManager().rotateCW.setEnabled(false);
		
		if(fc.isRedoAvailable())
			window.getActionManager().redo.setEnabled(true);
		
		if(fc.isUndoAvailable())
			window.getActionManager().undo.setEnabled(true);
		
		if(fc.isSelectionNonEmpty()){
			window.getActionManager().deleteSelection.setEnabled(true);
			window.getActionManager().rotateCCW.setEnabled(true);
			window.getActionManager().rotateCW.setEnabled(true);
		}
	}
	
	public void setViewStateForActiveDialog() {
		FrameController fc = projectController.getCurrentFrameController();
		fc.printToStatusBar();
		updateViewStateOnFrameChange();
		window.getActionManager().removeActiveDialog.setEnabled(true);
		window.getActionManager().zoomIn.setEnabled(true);
		window.getActionManager().zoomOut.setEnabled(true);
	}
	
	public void setViewStateForInactiveDialog() {
		window.getStatusBar().clear();
		window.getActionManager().removeActiveDialog.setEnabled(false);
		
		window.getActionManager().zoomIn.setEnabled(false);
		window.getActionManager().zoomOut.setEnabled(false);
		window.getActionManager().deleteSelection.setEnabled(false);
		window.getActionManager().rotateCCW.setEnabled(false);
		window.getActionManager().rotateCW.setEnabled(false);
		window.getActionManager().undo.setEnabled(false);
		window.getActionManager().redo.setEnabled(false);
	}
	
	@Override
	public void update(Event event) {
		if(event.getType() == Event.Type.CHANGE_STATE){
			switch (projectManager.getState().getType()){
			case NO_SELECT:
				projectController = null;
				setViewStateForInactiveDialog();
				setViewStateForInactiveProject();
				window.changeSideBar(null);
				break;
			case PROJ_SELECT:
				updateProjectController();
				setViewStateForInactiveDialog();
				setViewStateForActiveProject();
				window.changeSideBar(null);
				break;
			case FRAME_SELECT:
				updateProjectController();
				projectController.changeFrame();
				setViewStateForActiveDialog();
				setViewStateForActiveProject();
				window.changeSideBar(null);
				window.changeSideBar(projectController.getCurrentFrameController().getSideBar());
				break;
			}
		}
		else
		if(event.getType() == Event.Type.DELETE){
			if(event.getFrameName() == null){
				deleteProjectController(event.getProjectName());
			}
			else
			{
				projectController.deleteFrameController(event.getFrameName());
			}
		}
		else if(event.getType() == Event.Type.NEW){
			if(event.getFrameName()==null){
				addNewProjectController(event.getProjectName());
			}
		}
	}

	public void saveCurrentProject(File projectFile) {
		XStream xstream = new XStream(new StaxDriver());
		
		ObjectOutputStream out;
		
		Collection<ProjectFrame> pframes = projectManager.getCurrentProject().getFrames();
		
		List<RawGraphicData> rawGDList = new ArrayList<RawGraphicData>();
		
		for(ProjectFrame pframe : pframes){
			rawGDList.add(new RawGraphicData(pframe.getGraphicData()));
		}
		
		try {
			out = xstream.createObjectOutputStream(new FileOutputStream(projectFile));
			RawProject rp = new RawProject(projectManager.getCurrentProject().getName(), rawGDList);
			out.writeObject(rp);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean addNewProjectFromFile(File projectFile) {
		
		XStream xstream = new XStream(new StaxDriver());
		ObjectInputStream in = null;
		
		try {
			in = xstream.createObjectInputStream(new FileInputStream(projectFile));
			RawProject rp = (RawProject)in.readObject();
			in.close();
		
			if (projectNameExists(rp.getName()))
				return false;
			
			Project project = new Project(rp);
			projectManager.getState().openProject(project);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
