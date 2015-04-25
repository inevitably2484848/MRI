package edu.auburn.cardiomri.gui.views;

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

	/**
	 * Sets panel to visible, adds slider to panel
	 * 
	 */
	public GridControlView()
	{
		super();
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
		
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
}
