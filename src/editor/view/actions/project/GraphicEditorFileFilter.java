package editor.view.actions.project;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GraphicEditorFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "GraphicEditor Project Files (*.gpf)";
	}

	@Override
	public boolean accept(File f) {
		return (f.isDirectory() || f.getName().toLowerCase().endsWith(".gpf"));
	}

}
