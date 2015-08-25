package editor.model.data;

import java.util.ArrayList;
import java.util.List;


public class RawProject {
	private String name;
	private List<RawGraphicData> frameData;
	
	public RawProject(String name, List<RawGraphicData> frameData) {
		super();
		this.name = name;
		this.frameData = frameData;
	}
	public String getName() {
		return name;
	}
	public List<RawGraphicData> getFrameData() {
		return frameData;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFrameData(ArrayList<RawGraphicData> frameData) {
		this.frameData = frameData;
	}
	
	
	
	
}
