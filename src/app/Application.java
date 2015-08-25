package app;

import java.awt.EventQueue;

import editor.controller.Controller;
import editor.model.projectmanager.ProjectManager;

public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ProjectManager pMan = new ProjectManager();
				Controller controller = new Controller(pMan);
				controller.createView();
			}
		});
	}
}

