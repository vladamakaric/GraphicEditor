package editor.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import editor.controller.Controller;
import editor.model.projectmanager.Event;
import editor.model.projectmanager.Observer;

//Salje kontroleru 3 dogadjaja:
//1.Selektovan dijalog (list)
//2.Selektovan projekat
//3.Dvoklik na dijalog (list)


public class ProjectExplorer implements Observer {

	private Controller controller;
	private JTree tree;
	private DefaultMutableTreeNode root;
	DefaultTreeModel model;

	class SelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			if (selectedNode == null)
				return; // nista nije selektovano, znaci nesto je obrisano

			String selectedNodeName = selectedNode.toString();

			if (selectedNode.isLeaf()) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
						.getParent();
				
				
				controller.activateProjectAndFrame(
						(String) parent.getUserObject(),
						(String) selectedNode.getUserObject());
			} else {
				controller.activateProject(selectedNodeName);
			}
		}
	}

	
	private class PEMouseListener extends MouseAdapter {

		
		public void mousePressed(MouseEvent e) {
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
			if (selRow != -1) {
				if (e.getClickCount() == 1) {

				} else if (e.getClickCount() == 2) {

					doubleClickOnNode((DefaultMutableTreeNode) selPath
							.getLastPathComponent());
				}
			}
		}
	}

	//dvoklik ponovo prikazuje ugaseni prozor (dijalog)
	private void doubleClickOnNode(DefaultMutableTreeNode node) {
		if (!node.isLeaf())
			return;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		
		controller.showDialog((String) parent.getUserObject(),
				(String) node.getUserObject());
	}

	public JTree getTree() {
		return tree;
	}

	public void selectNode(DefaultMutableTreeNode node) {
		TreeSelectionModel tsm = tree.getSelectionModel();
		tsm.setSelectionPaths(new TreePath[] { new TreePath(node.getPath()) });
	}

	public ProjectExplorer(Controller controller) {
		this.controller = controller;
		root = new DefaultMutableTreeNode();
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this.new SelectionListener());

		model = (DefaultTreeModel) tree.getModel();
		tree.setRootVisible(false);

		tree.setExpandsSelectedPaths(true);
		tree.addMouseListener(this.new PEMouseListener());
	}

	private DefaultMutableTreeNode getChildNode(DefaultMutableTreeNode parent,
			String name) {
		DefaultMutableTreeNode chNode = null;

		for (int i = 0; i < parent.getChildCount(); i++) {
			chNode = (DefaultMutableTreeNode) parent.getChildAt(i);
			String chName = (String) chNode.getUserObject();

			if (chName.equals(name))
				break;
		}

		return chNode;
	}

	@Override
	public void update(Event event) {

		DefaultMutableTreeNode projNode;
		DefaultMutableTreeNode frameNode;
		String eProjectName = event.getProjectName();
		String eFrameName = event.getFrameName();

		switch (event.getType()) {
		case NEW:
			if(eFrameName == null){
				int projNum = root.getChildCount();
				projNode = new DefaultMutableTreeNode(eProjectName);

				model.insertNodeInto(projNode, root, projNum);

				if (projNum == 0)
					model.reload(root);
			}
			else
			{
				projNode = getChildNode(root, eProjectName);
				frameNode = new DefaultMutableTreeNode(eFrameName);
				model.insertNodeInto(frameNode, projNode, projNode.getChildCount());
			}
			break;
		case SWITCH_ACTIVE:
			if(eProjectName == null)
				break;
			
			
			projNode = getChildNode(root, eProjectName);
			
			if(eFrameName == null){
				selectNode(projNode);
			}
			else{
				frameNode = getChildNode(projNode, eFrameName);
				selectNode(frameNode);
			}
			
			break;
		case DELETE:
			projNode = getChildNode(root, eProjectName);
			
			if(eFrameName == null){
				model.removeNodeFromParent(projNode);
			}
			else
			{
				frameNode = getChildNode(projNode, event.getFrameName());
				model.removeNodeFromParent(frameNode);
			}
			
			break;
		}
	}
}
