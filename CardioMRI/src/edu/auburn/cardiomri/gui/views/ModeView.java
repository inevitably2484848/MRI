package edu.auburn.cardiomri.gui.views;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/** This class creates the JLabel indicating which mode the user is in. 
 * This JLabel is added to the RightPanel.
 * @author Shannon Lavender and Aaron Fregeau
 */

public class ModeView extends View{
	
	public JPanel toastPanel;
	public JLabel lblToastString;
	private Color bgColor = new Color(145,235,115);

	public ModeView(String text){
		super();
		
		//Create panel 
		toastPanel = this.panel;
	    toastPanel.setOpaque(true);  
	    toastPanel.setVisible(true);
        
        //Create the Jlabel
        lblToastString = new JLabel("SELECT MODE");
        lblToastString.setFont(new Font("Dialog", Font.BOLD, 15));
        lblToastString.setForeground(Color.BLACK);
        
        //Format the label is centered in the panel
        this.panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        
        //Change the background color of the panel and add the label to the panel
        this.panel.setBackground(bgColor);
        this.panel.add(lblToastString, c);
      
	}   
}
