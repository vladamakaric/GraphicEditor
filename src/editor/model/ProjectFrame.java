package editor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

import editor.controller.command.Command;
import editor.model.data.GraphicData;
import editor.model.projectmanager.Event;

public class ProjectFrame {
	
	public void addHistoryObserver(HistoryObserver cfo){
		cfobservers.add(cfo);
	}
	
	private void notifyHistoryObservers(){
		for(HistoryObserver cfo : cfobservers){
			cfo.historyChanged();
		}
	}
	
	private ArrayList<HistoryObserver> cfobservers = new ArrayList<HistoryObserver>();
	
	public interface HistoryObserver{
		public void historyChanged();
	}
	
	private class CommandBlock{
		
		private List<Command> commands = new ArrayList<Command>();
		boolean finished = false;
		
		public void finish(){
			finished = true;
			
			clearHistoryAboveCurrent();
			notifyHistoryObservers();
		}
		
		public boolean isFinished(){
			return finished;
		}

		public void undo(){
			ListIterator<Command> li = commands.listIterator(commands.size());
			// Iterate in reverse.
			while(li.hasPrevious()) {
				li.previous().undo();
			}
		}
		
		public boolean empty(){
			return commands.size() == 0;
		}
		
		public void redo(){
			for(Command cmd : commands){
				cmd.execute();
			}
		}
		
		public void addCommand(Command cmd){
			commands.add(cmd);
		}
		
	}
	
	private Queue<Command> commandQueue = new LinkedList<Command>(); 
	private List<CommandBlock> history;
	private GraphicData graphicData;
	ListIterator<CommandBlock> currentCMDBlock;
	
	public ProjectFrame(GraphicData graphicData) {
		super();
		this.graphicData = graphicData;
		history = new ArrayList<CommandBlock>();
		currentCMDBlock = history.listIterator();
	}

	public boolean isUndoAvailable(){
		
		boolean undoAvailable = false;
		
		if(currentCMDBlock.hasPrevious()){
			if(currentCMDBlock.previous().isFinished())
				undoAvailable =true;
			else
				undoAvailable = currentCMDBlock.hasPrevious();
			
			currentCMDBlock.next();
		}
		
		return undoAvailable;
	}
	
	public boolean isRedoAvailable(){
		return currentCMDBlock.hasNext();
	}
	
	public GraphicData getGraphicData() {
		return graphicData;
	}
	
	public void endCommandBlock(){
		currentCMDBlock.previous();
		currentCMDBlock.next().finish();
		
		notifyHistoryObservers();
		//finish vraca pokazivac gde treba, netreba zvati .next()
	}
	
	public void startCommandBlock(){
		currentCMDBlock.add(new CommandBlock());
	}
	
	public void redo(){
		currentCMDBlock.next().redo();
		
		notifyHistoryObservers();
	}
	
	
	//TODO: Misterija, zasto neradi:
	//currentCMDBlock.previous().undo();
	//currentCMDBlock.remove();
	public void undoAndRemove(){
		currentCMDBlock.previous();
		currentCMDBlock.next().undo();
		currentCMDBlock.remove();
		
		notifyHistoryObservers();
	}
	
	public void undo(){
		currentCMDBlock.previous().undo();
		
		notifyHistoryObservers();
	}
	
	private void clearHistoryAboveCurrent(){
		while(currentCMDBlock.hasNext()){
			currentCMDBlock.next();
			currentCMDBlock.remove();
		}
	}
	
	public void executeCommandQueue(){
		while (!commandQueue.isEmpty()) {
			Command cmd = commandQueue.poll();
			cmd.execute();
		}
	}
	
	public void addCommandAndExecute(Command command){
		currentCMDBlock.previous().addCommand(command);
		currentCMDBlock.next(); 
		
		command.execute();
	}
	
	public void addCommandToQueue(Command command){
		currentCMDBlock.previous().addCommand(command);
		currentCMDBlock.next(); 
		commandQueue.add(command);
	}
	
	public String getName() {
		return graphicData.getName();
	}
}
