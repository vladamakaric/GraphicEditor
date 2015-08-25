package editor.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatusBar extends JPanel {

	JLabel[] labels;

	public StatusBar(JFrame parrent, int fieldNum, int height) {
		labels = new JLabel[fieldNum];

		setBorder(new EmptyBorder(0, 0, 0, 0));
		setPreferredSize(new Dimension(parrent.getWidth(), height));
		setLayout(new GridLayout(1, fieldNum));

		for (int i = 0; i < fieldNum; i++) {
			JPanel pane = new JPanel();
			pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
			pane.setBorder(BorderFactory.createLoweredBevelBorder());
			JLabel fieldLabel = new JLabel("");

			fieldLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
			pane.add(fieldLabel);

			labels[i] = fieldLabel;
			add(pane);
		}
	}

	public void clear(){
		
		for(int i=0; i<5; i++){
			setFieldText(i, "");
		}
	}
	
	public void setFieldText(int fieldNum, String text) {
		labels[fieldNum].setText(text);
	}
}
