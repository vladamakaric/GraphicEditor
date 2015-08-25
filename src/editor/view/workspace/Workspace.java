package editor.view.workspace;

import java.awt.Point;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import editor.controller.Controller;
import editor.controller.ProjectController;
import editor.model.projectmanager.Event;
import editor.model.projectmanager.Observer;

public class Workspace extends JDesktopPane implements Observer {

	private static class JIFDescriptor {
		public String frameName;
		public String projectName;
		public JInternalFrame jif;

		public JIFDescriptor(String projectName, String frameName,
				JInternalFrame jif) {
			super();
			this.jif = jif;
			this.frameName = frameName;
			this.projectName = projectName;
		}
	}

	private class IFListener extends InternalFrameAdapter {
		@Override
		//ispaljuje samo ako je proslo stanje JIF-a bilo neaktivno
		public void internalFrameActivated(InternalFrameEvent e) {
			JIFDescriptor jifd = getJIFDFromJIF(e.getInternalFrame());
			if (jifd == null)
				return;
			
			
			controller.activateProjectAndFrame(jifd.projectName,
					jifd.frameName);
			
		}
	}

	private IFListener ifl;
	private Controller controller;
	private ArrayList<JIFDescriptor> jifds;

	private final int newWindowOffser = 25;
	
	//velicina svakog prozora u kaskadnom rezimu, u odnosu na DesktopPane
	private final double cascadePercent = 0.7;

	public Workspace(Controller controller) {
		super();
		this.controller = controller;
		jifds = new ArrayList<JIFDescriptor>();
		ifl = this.new IFListener();
	}

	private ArrayList<JInternalFrame> getVisibleJIFArray() {
		ArrayList<JInternalFrame> visibleJifs = new ArrayList<JInternalFrame>();

		for (JIFDescriptor jifd : jifds) {
			JInternalFrame jif = jifd.jif;

			if (jif.isVisible())
				visibleJifs.add(jif);
		}

		return visibleJifs;
	}

	public void arrangeWHorizontally() {
		arrangeFHorizontallyInRegion(getWidth(), getHeight());
	}

	public void arrangeWVerticaly() {
		arrangeFHorizontallyInRegion(getHeight(), getWidth());
		flipCoordsAndSize();
	}

	
	public void arrangeWCascade() {
		ArrayList<JInternalFrame> jifs = getVisibleJIFArray();
		int jifNum = jifs.size();

		if (jifNum == 0)
			return;
		if (jifNum == 1) {
			arrangeWHorizontally();
			return;
		}

		deiconify(jifs);

		int jifW = (int) (((double) getWidth()) * cascadePercent);
		int jifH = (int) (((double) getHeight()) * cascadePercent);

		int dx = (getWidth() - jifW) / (jifNum - 1);
		int dy = (getHeight() - jifH) / (jifNum - 1);

		int currX = 0;
		int currY = 0;

		for (JInternalFrame jif : jifs) {

			jif.setLocation(currX, currY);
			jif.setSize(jifW, jifH);
			jif.moveToBack();
			currX += dx;
			currY += dy;
		}
	}

	//Svi prozori koji su ikonifikovani (minimizovani), moraju da se probude
	//jer inace promena velicine ne utice na njih, pa nemogu da se aranziraju
	private void deiconify(ArrayList<JInternalFrame> visibleJifs) {
		for (JInternalFrame jif : visibleJifs) {

			if (jif.isIcon()) {
				getDesktopManager().deiconifyFrame(jif);
				getDesktopManager().maximizeFrame(jif);
				getDesktopManager().minimizeFrame(jif);
				
				jif.moveToFront();
			}
		}
	}
	
	private void flipCoordsAndSize() {
		ArrayList<JInternalFrame> jifs = getVisibleJIFArray();

		for (JInternalFrame jif : jifs) {
			jif.setLocation(jif.getY(), jif.getX());
			jif.setSize(jif.getHeight(), jif.getWidth());
		}
	}

	private void selectJIF(JInternalFrame jif) {
		try {
			jif.setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
		}
	}

	private void arrangeFHorizontallyInRegion(int regionWidth, int regionHeight) {

		ArrayList<JInternalFrame> jifs = getVisibleJIFArray();

		int jifNum = jifs.size();
		if (jifNum == 0)
			return;

		deiconify(jifs);

		int jifW = regionWidth / jifNum;
		int jifH = regionHeight;

		int currX = 0;
		int i = 0;
		for (JInternalFrame jif : jifs) {

			i++;
			jif.setLocation(currX, 0);

			if (i == jifNum)
				jifW = regionWidth - (jifNum - 1) * jifW;

			jif.setSize(jifW, jifH);
			currX += jifW;
		}
	}

	////////////////////////////////////////////////////////////////////////

	//setanje fokusa po vidljivim frame-ovima napred/nazad 
	public void setFocusOnNextVisibleF(boolean forward) {
		ArrayList<JInternalFrame> jifs = getVisibleJIFArray();
		ListIterator<JInternalFrame> lit = jifs.listIterator();

		if (!lit.hasNext())
			return;

		do {
			
			JInternalFrame jif = lit.next();

			if (jif.isSelected()) {
				if (forward) {
					if (lit.hasNext())
						selectJIF(lit.next());
					else
						selectJIF(jifs.listIterator().next());
				} else {
					lit.previous();

					if (lit.hasPrevious())
						selectJIF(lit.previous());
					else
						selectJIF(jifs.get(jifs.size() - 1));
				}

				return;
			}

		} while (lit.hasNext());
	}

	private void setFocusOnJIF(String projName, String dialogName) {
		JIFDescriptor jifd = getJIFD(projName, dialogName);
		selectJIF(jifd.jif);
	}

	private JIFDescriptor getJIFDFromJIF(JInternalFrame jif) {
		for (JIFDescriptor jifd : jifds) {
			if (jifd.jif == jif)
				return jifd;
		}
		return null;
	}

	private JIFDescriptor getJIFD(String projName, String dialogName) {

		for (JIFDescriptor jifd : jifds) {
			if (jifd.projectName.equals(projName)
					&& jifd.frameName.equals(dialogName))
				return jifd;
		}

		return null;
	}

	private void removeJIFD(String projName, String dialogName) {
		JIFDescriptor jifd = getJIFD(projName, dialogName);
		jifds.remove(jifd);
		jifd.jif.dispose();
	}

	public PFrameView getPFrameView(String projName, String dialogName){
		JIFDescriptor jifd = getJIFD(projName, dialogName);
		return (PFrameView) jifd.jif;
	}
	
	
	//novi prozor se dodaje na najdalji stari + offset, tako se dobija inicijalna kaskada
	private Point getNewFramePosition() {

		Point origin = new Point(0, 0);
		Point maxPos = new Point(newWindowOffser, newWindowOffser);
		int maxDistance = 0;
		for (JIFDescriptor jifd : jifds) {

			Point jifPos = new Point(jifd.jif.getX(), jifd.jif.getY());
			int dist = (int) origin.distanceSq(jifPos);
			if (dist > maxDistance) {
				maxDistance = dist;
				maxPos = jifPos;
			}
		}

		return new Point(maxPos.x + newWindowOffser, maxPos.y + newWindowOffser);
	}

	private void unselectActiveJIF() {
		if (getSelectedFrame() != null) {
			try {
				getSelectedFrame().setSelected(false);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Event event) {

		String eProjectName = event.getProjectName();
		String eFrameName = event.getFrameName();

		switch (event.getType()) {
		case NEW:
			if(eFrameName == null)
				break;
			
			ProjectController prjcntrl = controller.getProjectController(eProjectName);
			
			JInternalFrame frame = prjcntrl.createFrameView(eFrameName, getNewFramePosition());
			
			frame.addInternalFrameListener(ifl);
			add(frame);
			frame.setVisible(true);
			jifds.add(new JIFDescriptor(eProjectName, eFrameName, frame));
			break;
		case SWITCH_ACTIVE:
			if(eFrameName != null){
				setFocusOnJIF(eProjectName, eFrameName);  //ako je JIF vec fokusiran nece ispaliti dogadjaj
			}
			else
			{
				unselectActiveJIF();
			}
			
			break;
		case SHOW_FRAME: 
			JIFDescriptor jifd = getJIFD(eProjectName, eFrameName);
			jifd.jif.setVisible(true);
			jifd.jif.toFront();
			break;
		case DELETE:
			if(eFrameName != null)
				removeJIFD(eProjectName, eFrameName);
			break;
		}
	}
}
