package editor.view;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ResourceManager {

	public enum ICONS {
		ABOUT, 
		EXIT,
		DELETE_SELECTION,
		ROTATE_CW,
		ROTATE_CCW,
		ZOOM_IN,
		ZOOM_OUT,
		REDO,
		UNDO,
		NEW_FRAME,
		NEW_PROJECT,
		OPEN_PROJECT,
		SAVE_PROJECT,
		CASCADE_FRAMES,
		NEXT_FRAME,
		PREVIOUS_FRAME,
		TILE_FRAMES_HOR,
		TILE_FRAMES_VER,
		MY_PICTURE,

		SELECT,
		RECTANGLE,
		CIRCLE,
		TRIANGLE,
		STAR
	}
	
	private Map<ICONS, Icon> icons;
	private Map<ICONS,String> imagePaths;
	
	
	
	public Icon getIcon(ICONS icon){
		return icons.get(icon);
	}
	
	public void setPath(ICONS icon, String path){
		imagePaths.put(icon, path);
	}
	
	public ResourceManager(){
		imagePaths = new HashMap<ResourceManager.ICONS, String>();
		icons = new HashMap<ResourceManager.ICONS, Icon>();

		setPath(ICONS.MY_PICTURE, "slikaMene.png");
		setPath(ICONS.ABOUT, "help.png"); 
		setPath(ICONS.EXIT, "door.png");
		setPath(ICONS.DELETE_SELECTION, "cancel.png"); 
		setPath(ICONS.ROTATE_CW, "shapes-rotate-clockwise.png"); 
		setPath(ICONS.ROTATE_CCW, "shapes-rotate-anticlockwise.png"); 
		setPath(ICONS.ZOOM_IN, "zoom-in.png"); 
		setPath(ICONS.ZOOM_OUT, "zoom-out.png"); 
		setPath(ICONS.REDO, "arrow-1-forward.png"); 
		setPath(ICONS.UNDO, "arrow-1-backward.png"); 
		setPath(ICONS.NEW_FRAME, "window.png"); 
		setPath(ICONS.NEW_PROJECT, "document-empty.png"); 
		setPath(ICONS.OPEN_PROJECT, "folder-open.png"); 
		setPath(ICONS.SAVE_PROJECT, "save.png"); 
		setPath(ICONS.CASCADE_FRAMES, "window-stack.png"); 
		setPath(ICONS.NEXT_FRAME, "arrow-4-right.png"); 
		setPath(ICONS.PREVIOUS_FRAME, "arrow-4-left.png"); 
		setPath(ICONS.TILE_FRAMES_HOR, "window-tile-vertically.png"); 
		setPath(ICONS.TILE_FRAMES_VER, "window-tile-horizontally.png"); 
		////////////
		setPath(ICONS.SELECT, "cursor.png");
		setPath(ICONS.RECTANGLE, "shape-parallelogram-orthogonal.png");
		setPath(ICONS.CIRCLE, "shape-circle.png");
		setPath(ICONS.TRIANGLE, "shape-triangle-equilateral.png");
		setPath(ICONS.STAR, "star.png");
	}
	
	void LoadResources(){
		for(Map.Entry<ICONS,String> path : imagePaths.entrySet()){
			icons.put(path.getKey(), createImageIcon("/images/" + path.getValue()));
		}
	}
	
	private ImageIcon createImageIcon(String path){
		java.net.URL imgURL = getClass().getResource(path);
		
		if(imgURL != null)
			return new ImageIcon(imgURL);
		else
			System.err.println("File not found: " + path);
		
		return null;
	}
	
	
	
}

//ABOUT("help.png"), 
//EXIT("door.png"),
//COPY("copy.png"),
//CUT("cut.png"),
//PASTE("paste.png"),
//REDO("arrow-1-forward.png"),
//UNDO("arrow-1-backward.png"),
//NEW_DIALOG("window.png"),
//NEW_PROJECT("document-empty.png"),
//OPEN_PROJECT("folder-open.png"),
//SAVE_PROJECT("save.png"),
//CASCADE_DIALOGS("window-stack.png"),
//NEXT_DIALOG("arrow-4-right.png"),
//PREVIOUS_DIALOG("arrow-4-left.png"),
//TILE_DIALOGS_HOR("window-tile-vertically.png"),
//TILE_DIALOGS_VER("window-tile-horizontally.png");
