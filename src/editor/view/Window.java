package editor.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import editor.controller.Controller;
import editor.model.projectmanager.Event;
import editor.model.projectmanager.ProjectManager;
import editor.view.actions.ActionManager;
import editor.view.workspace.PFrameView;
import editor.view.workspace.Workspace;

public class Window extends JFrame implements
		editor.model.projectmanager.Observer {

	private Workspace workspace;
	private MenuBar menuBar;
	private CommonToolBar commonToolBar;
	private Controller controller;
	private ActionManager actionManager;
	private ProjectExplorer projectExplorer;
	private JToolBar specificProjectSideBar;
	private StatusBar statusBar;
	private ResourceManager rm;

	public Workspace getWorkspace() {
		return workspace;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}
	
	public PFrameView getPFrameView(String projectName, String frameName){
		return workspace.getPFrameView(projectName, frameName);
	}
	
	public ResourceManager getResourceManager(){
		return rm;
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public Window(Controller controller) {
		this.controller = controller;
		specificProjectSideBar = null;
		setTitle("Graphic editor");
		setSize(850, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WEventListener());
	}

	public void initialize() {
		rm = new ResourceManager();
		rm.LoadResources();
		
		workspace = new Workspace(controller);
		actionManager = new ActionManager(this, controller, rm);
		menuBar = new MenuBar(actionManager);
		setJMenuBar(menuBar);

		commonToolBar = new CommonToolBar(actionManager);
		add(commonToolBar, BorderLayout.PAGE_START);

		projectExplorer = new ProjectExplorer(controller);
		JScrollPane treeView = new JScrollPane(projectExplorer.getTree());

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				treeView, workspace);
		splitPane.setResizeWeight(0.3d);
		add(splitPane, BorderLayout.CENTER);
		
		statusBar = new StatusBar(this, 5, 20);

		add(statusBar, BorderLayout.SOUTH);
	}

	public void changeSideBar(JToolBar newToolBar) {
		if (specificProjectSideBar != null)
			remove(specificProjectSideBar);

		specificProjectSideBar = newToolBar;

		if (newToolBar != null)
			add(newToolBar, BorderLayout.LINE_END);

		revalidate();
	}

	@Override
	public void update(Event event) {
		projectExplorer.update(event);
		workspace.update(event);
	}

}
