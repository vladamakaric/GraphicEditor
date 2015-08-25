package editor.view.workspace;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor.controller.FrameController;
import editor.model.data.GraphicElement;

class WinClosingHandler extends WindowAdapter{
	
	private FrameController fc;
	private GEOptionDialog geod;
	
	public WinClosingHandler(FrameController fc, GEOptionDialog geod) {
		this.geod = geod;
		this.fc = fc;

	}
	@Override
	public void windowClosing(WindowEvent e) {
    	if(geod.firstChange && !geod.okPressed)
    		fc.undoAndRemove();
	}
}

class OkButtonHandler implements ActionListener{

	private GEOptionDialog geod;
	private GEChangeNameAndDescriptionPanel changeNameDescPanel;
	private PFrameView pfv;
	public OkButtonHandler(GEOptionDialog geod,
			GEChangeNameAndDescriptionPanel changeNameDescPanel,
			PFrameView pfv) {
		super();
		this.geod = geod;
		this.changeNameDescPanel = changeNameDescPanel;
		this.pfv = pfv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(geod.firstChange)
			geod.fc.endCommandBlock();
		
		String activeName = geod.gelem.getName();
		String newName = changeNameDescPanel.nameTextField.getText();
		
		boolean renamed = false;
		
		if(!newName.equals( geod.gelem.getName())){
			if( pfv.getProjectFrame().getGraphicData().isNameTaken(newName)){
				JOptionPane.showMessageDialog(null, "El name already exists!");
				return;
			}
			else{
				renamed = true;
				geod.fc.startCommandBlock();
				geod.fc.renameElement(geod.gelem.getName(), newName);
				activeName = newName;
			}
		}
		
		String newDescription = changeNameDescPanel.descriptionTextArea.getText();
		boolean reDescribed = false;
		if(!newDescription.equals(geod.gelem.getDescription())){
			
			reDescribed = true;
			if(!renamed)
				geod.fc.startCommandBlock();
			
			geod.fc.changeElementDescription(activeName,newDescription);
		}
		
		if(renamed || reDescribed){
			geod.fc.endCommandBlock();
		}
		
		geod.okPressed = true;
		geod.dispatchEvent(new WindowEvent(geod, WindowEvent.WINDOW_CLOSING));
	}
	
	
}

public class GEOptionDialog extends JDialog implements ChangeListener{
	FrameController fc;
	JColorChooser cc;
	boolean okPressed = false;
	boolean firstChange = false;
	GraphicElement gelem;
	
	public GEOptionDialog(GraphicElement gelem, PFrameView pfv, FrameController fc) {
		this.gelem = gelem;
		this.fc = fc;
		setModal(true); // blokirajuci dijalog
		//setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WinClosingHandler(fc, this));
		
		JPanel panel = new JPanel(new BorderLayout());
		
		cc = new JColorChooser();
		setTitle("Options");
		
		cc.setBorder(BorderFactory.createTitledBorder("Color"));
		cc.getSelectionModel().addChangeListener(this);

		
		GEChangeNameAndDescriptionPanel changeNameDescPanel = 
				new GEChangeNameAndDescriptionPanel(gelem.getName(),gelem.getDescription());
		
		changeNameDescPanel.cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispatchEvent(new WindowEvent(GEOptionDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		changeNameDescPanel.okBtn.addActionListener(
				new OkButtonHandler(this, changeNameDescPanel, pfv));
		
		panel.add(cc, BorderLayout.CENTER);
		panel.add(changeNameDescPanel, BorderLayout.PAGE_END);

		add(panel);
		pack();
		setLocationRelativeTo(pfv);
		setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
		if(!firstChange){
			firstChange = true;
			fc.startCommandBlock();
		}
		
		fc.changeElementColor(gelem.getName(), cc.getColor());
	}
}

class GEChangeNameAndDescriptionPanel extends JPanel {

	public JTextField nameTextField;
	public JTextArea descriptionTextArea;
	public JButton okBtn;
	public JButton cancelBtn;
	
	public GEChangeNameAndDescriptionPanel(String name, String description) {
		super(new BorderLayout());

		JPanel inputPanel = new JPanel(new BorderLayout());
		
		JPanel leftGluePanel = new JPanel();
		leftGluePanel.setLayout(new BoxLayout(leftGluePanel, BoxLayout.Y_AXIS));
		
		JLabel nameLbl = new JLabel("Name");
		JLabel descriptionLbl = new JLabel("Description");
		
		nameTextField = new JTextField(20);
		descriptionTextArea = new JTextArea(5, 20);
		descriptionTextArea.setEditable(true);
		descriptionTextArea.setLineWrap(true);
		okBtn = new JButton("OK");
		cancelBtn = new JButton("Cancel");
		
		nameTextField.setText(name);
		descriptionTextArea.setText(description);
		//okBtn.setPreferredSize(new Dimension(100,25));
		
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
   
        JPanel nameInputPanel = new JPanel();
        nameInputPanel.add(nameLbl, BorderLayout.PAGE_START);
        nameInputPanel.add(nameTextField, BorderLayout.LINE_START);
        
		JPanel descriptionInputPanel = new JPanel(new BorderLayout());
		
		descriptionInputPanel.add(descriptionLbl, BorderLayout.PAGE_START);
		descriptionInputPanel.add(scrollPane, BorderLayout.CENTER);
		
		leftGluePanel.add(nameInputPanel);
		leftGluePanel.add(descriptionInputPanel);
		
		inputPanel.add(leftGluePanel, BorderLayout.LINE_START);
		
		JPanel rightGluePanel = new JPanel(new BorderLayout());
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelBtn);
		buttonPanel.add(okBtn);
		rightGluePanel.add(buttonPanel,BorderLayout.SOUTH);

		inputPanel.add(rightGluePanel);
		inputPanel.setBorder(BorderFactory.createTitledBorder("Properties"));

		add(inputPanel, BorderLayout.NORTH);
	}
}