package editor.controller;

import java.awt.Color;
import java.awt.Shape;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;

import editor.controller.command.AddNewGElement;
import editor.controller.command.ChangeGElement;
import editor.controller.command.DeleteGElement;
import editor.controller.command.RenameElement;
import editor.controller.frameactions.CreateCircle;
import editor.controller.frameactions.CreateRectangle;
import editor.controller.frameactions.CreateStar;
import editor.controller.frameactions.CreateTriangle;
import editor.controller.frameactions.SelectMode;
import editor.model.ProjectFrame;
import editor.model.ProjectFrame.HistoryObserver;
import editor.model.data.GraphicElement;
import editor.view.ResourceManager;
import editor.view.StatusBar;
import editor.view.workspace.PFrameView;
import editor.view.workspace.state.PFStateManager;

public class FrameController implements HistoryObserver {

	private ProjectController prjCntrl;
	private GraphicSideBar gsb;
	private PFrameView frameView;
	private ProjectFrame projectFrame;
	private int latestElemID = 0;

	private StatusBar statusBar;

	private PFStateManager pfsm;
	Random rand = new Random();

	public FrameController(ProjectController prjCntrl,
			ProjectFrame projectFrame, PFrameView frameView, ResourceManager rm) {
		this.prjCntrl = prjCntrl;
		this.projectFrame = projectFrame;
		this.frameView = frameView;

		frameView.setFrameController(this);
		projectFrame.addHistoryObserver(this);
		
		statusBar = prjCntrl.getController().getWindow().getStatusBar();
		pfsm = frameView.getStateManager();
		
		AbstractAction[] actions = {
				new SelectMode(pfsm, rm),
				new CreateRectangle(pfsm, rm),
				new CreateCircle(pfsm, rm),
				new CreateTriangle(pfsm, rm),
				new CreateStar(pfsm, rm)};
		
		gsb = new GraphicSideBar(actions);
	}

	
	/////Vracanje stanja
	
	public ProjectFrame getProjectFrame() {
		return projectFrame;
	}

	public JToolBar getSideBar() {
		return gsb;
	}

	public boolean isSelectionNonEmpty() {
		return frameView.getSelectionManager().getSelectedElements().size() != 0;
	}
	
	public boolean isRedoAvailable() {
		return projectFrame.isRedoAvailable();
	}

	public boolean isUndoAvailable() {
		return projectFrame.isUndoAvailable();
	}
	
	//////////Komande, promena stanja
	
	public void startCommandBlock() {
		projectFrame.startCommandBlock();
	}

	public void endCommandBlock() {
		projectFrame.endCommandBlock();
	}

	
	public void printToStatusBar() {

		if (frameView.getSelectionManager().singleSelection()) {
			GraphicElement ge = frameView.getSelectionManager()
					.getSelectedGElement();
			statusBar.setFieldText(1, "type: " + ge.getType().toString());

			statusBar.setFieldText(2, "name: " + ge.getName());

			statusBar.setFieldText(3, "pos: " + "(" + (int) ge.getX() + ","
					+ (int) ge.getY() + ")");

			Shape tges = frameView.getShapeManager().getTransformedShapeFromGE(
					ge);

			double w = tges.getBounds2D().getWidth();
			double h = tges.getBounds2D().getHeight();
			statusBar.setFieldText(4, "dim: " + "(" + String.format("%.2f", w)
					+ "," + String.format("%.2f", h) + ")");
		} else
			statusBar.clear();

		statusBar.setFieldText(0, "state: " + pfsm.getCurrentState().getName());
	}

	private GraphicElement createNewGEWithRandomColor(GraphicElement.Type type, double x,
			double y) {

		GraphicElement ge = new GraphicElement(type, type.toString()
				+ latestElemID, "No description", x, y, 1, 0,
				rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

		latestElemID++;
		return ge;
	}



	
	public void deleteSelection() {
		Iterator<GraphicElement> it = getSelectionIterator();

		projectFrame.startCommandBlock();
		
		while (it.hasNext()) {

			GraphicElement gelem = it.next();
			projectFrame.addCommandToQueue(new DeleteGElement(projectFrame
					.getGraphicData(), gelem));
		}

		projectFrame.executeCommandQueue();
		
		projectFrame.endCommandBlock();
	}
	
	private Iterator<GraphicElement> getSelectionIterator(){
		Collection<GraphicElement> selectedGElems = frameView
				.getSelectionManager().getSelectedElements();
		
		return selectedGElems.iterator();
	}
	
	public void rotateSelection(boolean CW) {
		double deltaAngle = CW ? -Math.PI / 2 : Math.PI / 2;

		Iterator<GraphicElement> it = getSelectionIterator();

		projectFrame.startCommandBlock();

		while (it.hasNext()) {
			GraphicElement gelem = it.next();

			GraphicElement newGe = new GraphicElement(gelem);
			newGe.setAngle(gelem.getAngle() + deltaAngle);

			projectFrame.addCommandAndExecute(new ChangeGElement(projectFrame
					.getGraphicData(), gelem.getName(), newGe));
		}
		
		projectFrame.endCommandBlock();
	}

	public void changeElementDescription(String name, String description){
		GraphicElement ge = projectFrame.getGraphicData().getElement(name);
		GraphicElement newGe = new GraphicElement(ge);
		
		newGe.setDescription(description);
		
		projectFrame.addCommandAndExecute(new ChangeGElement(projectFrame
				.getGraphicData(), name, newGe));
	}
	
	public void renameElement(String oldName, String newName){
		projectFrame.addCommandAndExecute(
		new RenameElement(projectFrame.getGraphicData(), oldName, newName));
	}
	
	public void changeElementScaleAndPosition(String name, double scale,
			double x, double y) {
		GraphicElement ge = projectFrame.getGraphicData().getElement(name);
		GraphicElement newGe = new GraphicElement(ge);

		newGe.setX(x);
		newGe.setY(y);
		newGe.setScale(scale);

		projectFrame.addCommandAndExecute(new ChangeGElement(projectFrame
				.getGraphicData(), name, newGe));
	}

	public void changeElementPosition(String name, double x, double y) {
		GraphicElement ge = projectFrame.getGraphicData().getElement(name);
		GraphicElement newGe = new GraphicElement(ge);

		newGe.setX(x);
		newGe.setY(y);

		projectFrame.addCommandAndExecute(new ChangeGElement(projectFrame
				.getGraphicData(), name, newGe));
	}

	public void createNewGElement(GraphicElement.Type type, double x, double y) {

		projectFrame.startCommandBlock();
		projectFrame.addCommandAndExecute(new AddNewGElement(projectFrame
				.getGraphicData(), createNewGEWithRandomColor(type, x, y)));

		projectFrame.endCommandBlock();
	}
	
	public void redo() {
		projectFrame.redo();
	}

	public void undoAndRemove(){
		projectFrame.undoAndRemove();
	}
	
	public void undo() {
		projectFrame.undo();
	}

	public void changeElementColor(String name, Color color) {
		GraphicElement gelem = projectFrame.getGraphicData().getElement(name);

		GraphicElement newGe = new GraphicElement(gelem);
		newGe.setR(color.getRed());
		newGe.setG(color.getGreen());
		newGe.setB(color.getBlue());

		projectFrame.addCommandAndExecute(new ChangeGElement(projectFrame
				.getGraphicData(), gelem.getName(), newGe));
	}
	
	/////////////////////Azuriranje View-ova u odnosu na promene
	
	public void selectionChanged() {
		prjCntrl.getController().updateViewStateOnFrameChange();
		printToStatusBar();
	}

	public void graphicDataChanged(){
		prjCntrl.getController().updateViewStateOnFrameChange();
		printToStatusBar();
	}
	
	public void stateChanged() {
		printToStatusBar();
	}

	@Override
	public void historyChanged() {
		prjCntrl.getController().updateViewStateOnFrameChange();
	}
}