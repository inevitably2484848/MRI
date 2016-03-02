package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/** This class creates the JLabel indicating which mode the user is in. 
 * This JLabel is added to the RightPanel.
 * Originally the Toast class. Refactored on 2/22/2016 into ModeView.
 * @author Shannon Lavender and Aaron Fregeau
 */

public class ModeView extends View{
	 
	public JPanel modePanel;
	private JLabel modeLabelString;
	private Color bgColor = new Color(145,235,115);

	public ModeView(String text){
		super();
		
		//Create panel 
		modePanel = this.panel;
	    modePanel.setOpaque(true);  
	    modePanel.setVisible(true);
        
        //Create the Jlabel
        modeLabelString = new JLabel("SELECT MODE");
        modeLabelString.setFont(new Font("Dialog", Font.BOLD, 15));
        modeLabelString.setForeground(Color.BLACK);
        
        //Format the label is centered in the panel
        this.panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        
        //Change the background color of the panel and add the label to the panel
        this.panel.setBackground(bgColor);
        this.panel.add(modeLabelString, c);
      
	}   
	
	/** Sets the message in the mode label */
	public void setMessage(String mode) {
		modeLabelString.setText(mode);
		
	}
	
	/** Sets the label in the mode label with a second line of qualifier text 
	 * 	Uses HTML to avoid needing multiple JLabels
	 * 
	 * */
	
	public void setMessage(String mode, String qualifier) {
		modeLabelString.setText("<html>" + mode + "<br><center>" + qualifier + "</html>"); 
		
	}
	
}
