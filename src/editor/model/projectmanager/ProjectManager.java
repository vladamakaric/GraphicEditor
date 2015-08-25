package editor.model.projectmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import editor.model.Project;
import editor.model.ProjectFrame;

//dogadjaji koje ProjectManager ispaljuje svojim observerima se ne izvrsavaju direktno
//vec idu u Queue, to je zato sto obrada jednog dogadjaja kod nekom observera
//moze da izazove drugi dogadjaj, a da neki observeri nisu primili ni prvi jos. 
//Zato se notifie observers ostavlja da radi dok ne obavi posao za trenutni event.
//A svi sledeci idu na Queue

public class ProjectManager{
	private ArrayList<StateChangeObserver> stateChangeObservers;
	
	private ArrayList<Observer> observers;
	private HashMap<String, Project> openProjects;
	private Project currentProject;
	private Queue<Event> eventQueue; 
	boolean pendingEvents;
	private State currentState;
		
	
	
	
	public ProjectManager() {
		pendingEvents = false;
		stateChangeObservers = new ArrayList<StateChangeObserver>();
		observers = new ArrayList<Observer>();
		openProjects = new HashMap<String, Project>();
		eventQueue = new LinkedList<Event>();
		currentProject = null;
		currentState = new NothingSelected(this);
	}
	
	public State getState(){
		return currentState;
	}
	
	public Project getProject(String name){
		return openProjects.get(name);
	}
	
	public void addObserver(Observer observer){
		observers.add(observer);
	}
	
	public void addSCObserver(StateChangeObserver observer){
		stateChangeObservers.add(observer);
	}
	
	public Project getCurrentProject(){
		return currentProject;
	}
	
	//////////////////////////////////////////////////
	
	//podrazumeva se da stari projekat nije bio isti
	void changeCurrentProject(String name){
		currentProject = openProjects.get(name);
	}
	
	void switchState(State state) {
		currentState = state;
		currentState.init();
		
		addEventToQueue(new Event(Event.Type.CHANGE_STATE, null, null ));
		notifyObservers();
		//notifySCObservers();
	}

	void deleteCurrentProject(){
		Iterator itr = currentProject.getFrames().iterator();

		while(itr.hasNext()){
			ProjectFrame pf = (ProjectFrame)itr.next();
			addEventToQueue(new Event(Event.Type.DELETE, currentProject.getName(), pf.getName()));
		}
		
		addEventToQueue(new Event(Event.Type.DELETE, currentProject.getName(), null ));
		currentProject = null;
		
		//notifyObservers();
	}
	
	void addNewProject(Project prj){
		openProjects.put(prj.getName(), prj);
	}
	
	void deselectFrame(){
		currentProject.deselectFrame();
	}
	///////////////////////////////////////////
	
	void addEventToQueue(Event event) {
		eventQueue.add(event);
	}
	
	void notifySCObservers(){
		for (StateChangeObserver scobs : stateChangeObservers)
			scobs.pmStateChanged();
	}
	
	void notifyObservers() {
		if (pendingEvents == false)
			pendingEvents = true;
		else
			return;

		while (!eventQueue.isEmpty()) {
			Event event = eventQueue.poll();

			for (Observer obs : observers)
				obs.update(event);
		}

		pendingEvents = false;
	}
}
