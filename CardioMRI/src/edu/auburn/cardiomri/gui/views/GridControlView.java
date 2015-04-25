package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This view will house all the control functions of the gridView
 * 		A play button (to begin auto-rotating through the current times in the selected slice)
 * 		A slider to control the speed of rotation
 * 		Anything else you can dream of
 * 
 * @note Implements changeListener for the slider
 * 
 * @author Ben Gustafson
 *
 */
public class GridControlView extends View implements ChangeListener {

	protected boolean buttonPressed;
	protected JButton playButton;
	/**
	 * Sets panel to visible, adds slider to panel
	 * 
	 */
	public GridControlView()
	{
		super();
		this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
		
		buttonPressed = true;
		
		playButton = new JButton();
        playButton.addActionListener(this);
        playButton.setPreferredSize(new Dimension(50, 20));
        playButton.setBorderPainted(true);
        changeButtonState();
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.panel.add(playButton);
		
		//Slider is from 0 to 20 with one digit increments
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 20, 1); 
		framesPerSecond.addChangeListener(this);
		   
		this.panel.add(framesPerSecond);
	}
	
	/**
	 * Required for changeListener
	 * 
	 * Commented out code prints out the value of the slider and prints it to the console
	 */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            //int fps = (int) source.getValue();
            //System.out.println(fps);
        }
    }
    /**
     * Changes the pay button state based on what is currently being displayed
     * 
     */
    private void changeButtonState()
    {
    	if(buttonPressed)
    	{
    		//Set to not pressed
    		playButton.setActionCommand("PLAY");
            playButton.setText(">");
    	}
    	else
    	{
    		//Set to pressed
    		playButton.setActionCommand("STOP");
            playButton.setText("[]");
    	}
    	buttonPressed = !buttonPressed;
    }
    
    /**
     * Currenly only listening for the play and stop of the play button.
     * 
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        
        if(actionCommand.equals("STOP"))
        {
        	changeButtonState();
        }
        else if(actionCommand.equals("PLAY"))
        {
        	changeButtonState();
        }
    }
}
