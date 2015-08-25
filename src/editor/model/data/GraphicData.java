package editor.model.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import editor.model.data.Event.Type;


public class GraphicData {

	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private String name;
	private Map<String, GraphicElement> elements = new HashMap<String, GraphicElement>();
	
	public GraphicData(String name){
		this.name = name;		
	}
	
	public GraphicData(RawGraphicData rgd) {
		
		name = rgd.getName();
		Collection<GraphicElement> gelements = rgd.getGelements();
		
		for(GraphicElement ge : gelements){
			elements.put(ge.getName(), ge);
		}
	}

	public void addObserver(Observer obs){
		observers.add(obs);
	}
	
	public void notifyObservers(Event event){
		for(Observer obs : observers){
			obs.update(event);
		}
	}
	
	public boolean isNameTaken(String name){
		return elements.get(name) != null;
	}
	
	public void renameElement(String oldName, String newName){
		
		GraphicElement ge = elements.get(oldName);
		elements.remove(oldName);
		ge.setName(newName);
		elements.put(newName, ge);
		
		notifyObservers(new Event(Type.RENAME, oldName, newName));
	}
	
	public GraphicElement getElement(String name){
		return elements.get(name);
	}
	
	public void changeElement(String name, GraphicElement newElem){
		elements.get(name).Set(newElem);
		notifyObservers(new Event(Type.CHANGE));
	}
	
	public void removeElement(String name){
		elements.remove(name);
		
		notifyObservers(new Event(Type.CHANGE));
	}
	
	public void addNewElement(GraphicElement gelem){
		elements.put(gelem.getName(), gelem);
		
		notifyObservers(new Event(Type.CHANGE));
	}
	
	public Collection<GraphicElement> getElements() {
		return elements.values();
	}
	
	public String getName(){
		return name;
	}
}
