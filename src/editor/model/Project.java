package editor.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import editor.model.data.GraphicData;
import editor.model.data.RawGraphicData;
import editor.model.data.RawProject;

public class Project {
	private String name;
	private Map<String, ProjectFrame> frames = new HashMap<String, ProjectFrame>();
	private ProjectFrame activeFrame;

	public Project(String name) {
		this.name = name;
	}

	public Project(RawProject rp){
		this(rp.getName());
		
		Collection<RawGraphicData> framesGData = rp.getFrameData();
		
		for(RawGraphicData rgd : framesGData){
			frames.put(rgd.getName(), new ProjectFrame(new GraphicData(rgd)));
		}
	}
	
	public boolean frameNameExists(String name){
		
		for(ProjectFrame pf : frames.values()){
			if(pf.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public int getFrameNum(){
		return frames.size();
	}
	
	public Collection<ProjectFrame> getFrames(){
		return frames.values();
	}
	
	public String getActiveFrameName(){
		return activeFrame.getName();
	}
	
	public ProjectFrame getFrame(String name){
		return frames.get(name);
	}
	
	public ProjectFrame getActiveFrame(){
		return activeFrame;
	}
	
	public void changeActiveFrame(String name){
		activeFrame = frames.get(name);
	}
	
	public void deleteActiveFrame(){
		frames.remove(activeFrame.getName());
	}
	
	public void deselectFrame(){
		activeFrame = null;
	}
	
	public void addNewFrame(String name){
		frames.put(name, new ProjectFrame(new GraphicData(name)));
	}
	
	public String getName() {
		return name;
	}
}
