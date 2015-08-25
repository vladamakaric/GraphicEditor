package editor.view.actions;

import editor.controller.Controller;
import editor.model.projectmanager.ProjectManager;
import editor.view.ResourceManager;
import editor.view.Window;
import editor.view.actions.projcontent.DeleteSelection;
import editor.view.actions.projcontent.Redo;
import editor.view.actions.projcontent.RotateCCW;
import editor.view.actions.projcontent.RotateCW;
import editor.view.actions.projcontent.Undo;
import editor.view.actions.projcontent.ZoomIn;
import editor.view.actions.projcontent.ZoomOut;
import editor.view.actions.project.NewFrame;
import editor.view.actions.project.NewProject;
import editor.view.actions.project.OpenProject;
import editor.view.actions.project.RemoveActiveFrame;
import editor.view.actions.project.RemoveActiveProject;
import editor.view.actions.project.SaveProject;
import editor.view.actions.workspace.CascadeFrames;
import editor.view.actions.workspace.NextFrame;
import editor.view.actions.workspace.PreviousFrame;
import editor.view.actions.workspace.TileFHorizontally;
import editor.view.actions.workspace.TileFVerticaly;

public class ActionManager {

	public NewProject newProject;
	public OpenProject openProject;
	public Exit exit;
	public SaveProject saveProject;
	public DeleteSelection deleteSelection;
	public RotateCW rotateCW;
	public RotateCCW rotateCCW;
	public ZoomOut zoomOut;
	public ZoomIn zoomIn;
	public Undo undo;
	public Redo redo;
	public NextFrame nextWindow;
	public PreviousFrame previousWindow;
	public TileFHorizontally tileWHorizontally;
	public TileFVerticaly tileWVertically;
	public NewFrame newDialog;
	public RemoveActiveProject removeProject;
	public RemoveActiveFrame removeActiveDialog;
	public CascadeFrames cascadeWindows;
	public About about;
	
	public ActionManager(Window window, Controller controller, ResourceManager rm) {
		undo = new Undo(controller,rm);
		redo = new Redo(controller,rm);
		nextWindow = new NextFrame(window.getWorkspace(),rm);
		previousWindow = new PreviousFrame(window.getWorkspace(),rm);
		about = new About(window,rm);
		cascadeWindows = new CascadeFrames(window.getWorkspace(),rm);
		tileWHorizontally = new TileFHorizontally(window.getWorkspace(),rm);
		tileWVertically = new TileFVerticaly(window.getWorkspace(),rm);
		removeActiveDialog = new RemoveActiveFrame(controller, window);
		removeProject = new RemoveActiveProject(controller, window);
		newProject = new NewProject(controller, window,rm);
		openProject = new OpenProject(window,controller,rm);
		exit = new Exit(window,rm);
		saveProject = new SaveProject(window, controller, rm);
		deleteSelection = new DeleteSelection(controller, rm);
		rotateCW = new RotateCW(controller,rm);
		rotateCCW = new RotateCCW(controller,rm);
		zoomOut = new ZoomOut(controller,rm);
		zoomIn = new ZoomIn(controller,rm);
		newDialog = new NewFrame(controller, window, rm);
	}
}
