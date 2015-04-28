package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.auburn.cardiomri.gui.models.GridModel;

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
	protected int playSpeed;
	protected RunPlaybutton runner;
	/**
	 * Sets panel to visible, adds slider to panel
	 * 
	 * In order to get the play button to work, look in to swing timers. I couldn't get it to work in the time we had
	 * 
	 */
	public GridControlView()
	{
		super();
		this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
		
		buttonPressed = true;
		playSpeed = 0;
		runner = new RunPlaybutton(playSpeed);
		setUp();
		
		//Slider is from 0 to 20 with one digit increments
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 20, 1); 
		framesPerSecond.addChangeListener(this);
		   
		this.panel.add(framesPerSecond);
	}
	
	/**
	 * Sets up the play button
	 */
	public void setUp()
	{
		playButton = new JButton();
        playButton.addActionListener(this);
        playButton.setPreferredSize(new Dimension(50, 20));
        playButton.setBorderPainted(true);
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeButtonState();
		
		this.panel.add(playButton);
	}
	
	/**
	 * Required for changeListener
	 * 
	 * Sets the internal variable playSpeed to the current value of the slider. 
	 */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            playSpeed = (int) source.getValue();
            runner.updateSpeed(playSpeed);
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
            playButton.setText("[ ]");
    	}
    	buttonPressed = !buttonPressed;
    }
    
    /**
     * Currently only listening for the play and stop of the play button.
     * 
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        
        if(actionCommand.equals("STOP"))
        {
        	changeButtonState();
        	runner.setStop(false);
        	runner = new RunPlaybutton(playSpeed);
        }
        else if(actionCommand.equals("PLAY"))
        {
        	changeButtonState();
        	Thread t = new Thread(runner);
        	t.start();
        }
    }
    /**
     * Because the gridContorlModel would be more than a hassle, we chose to have the GridModel be the designated model for gridControlView
     * 		This is set in the WorkspaceView
     * 
     * @return current Grid Model
     */
    public GridModel getWorkspaceModel()
    {
    	return (GridModel) getModel();
    }
    
    /**
     * The runnable class needed to work the play button.
     * 
     * The GridControlView's runner variable ALWAYS has an instantiated RunPlaybutton
     * When the play button is pressed, that object is sent off to a new thread to run
     * Because GridControlView still has a reference to the object it can be updated throughout the runtime;
     * The speed can be changed and the the stop condition can be changed.
     * 
     * Killing a thread from the top down is a very bad thing to do, so the runnable object ends the thread from the inside by returning from the "run" method
     * 
     * @author Ben Gustafson
     *
     */
    protected class RunPlaybutton implements Runnable {
    	private int speed;
    	private boolean stop;
    	
    	public RunPlaybutton(int speed)
    	{
    		this.speed = speed;
    		this.stop = true;
    	}
    	
        public void run() {
        	while(this.stop)
        	{
        		getWorkspaceModel().incrementTimeIndex();
        		try {
        			TimeUnit.MILLISECONDS.sleep(speed *10 + 50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        }
        
        public void updateSpeed(int speedIn)
        {
        	this.speed = speedIn;
        }
        public void setStop(boolean stopIn)
        {
        	this.stop = stopIn;
        }
   }
}
