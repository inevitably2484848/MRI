package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.auburn.cardiomri.gui.models.GridModel;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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
	
	protected JToggleButton contour = new JToggleButton("Add Contour"); //kw
	protected JToggleButton landMark = new JToggleButton("Add LandMark"); //kw
	protected static int mode = 0;   //0 - select ; 1 - contour ; 2 - landmark // kw
	protected static final int SELECT_MODE = 0;
	protected static final int CONTOUR_MODE = 1;
	protected static final int LANDMARK_MODE = 2;
	
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
		framesPerSecond.setToolTipText("Change Speed");
		   
		this.panel.add(framesPerSecond);
		
		modeToggleButton(); //kw
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
	
	/** -----------------------------------------------------------------------
	 *  mode ToogleButton set up
	 *  Creates a new panel and adds 2 toggle buttons to that panel. Then adds
	 *  the new panel to the main panel.
	 *  adds and sets actions to the toggle buttons
	 *  you can set modes to add contours , add landmarks, de-selecting button 
	 *  sets select mode
	 *  @author KulW
	 */
	public void modeToggleButton(){
		JPanel modePanel = new JPanel();
	
		contour.addActionListener(this);
		contour.setActionCommand("contour");
		contour.setToolTipText("Add New Contour");
		contour.setMinimumSize(new Dimension(124,29));
		
		
		landMark.addActionListener(this);
		landMark.setActionCommand("landmark");
		landMark.setToolTipText("Add New Landmark Point");
		
		modePanel.add(contour);
		modePanel.add(landMark);
		
		this.panel.add(modePanel);
	} 
	
	/** -----------------------------------------------------------------------
	 *  mode state 
	 *  changes mode when a toggle button is pressed.
	 *  in Contour mode adds contour
	 *  landmark mode adds landmark
	 *  select mode select contours or landmarks
	 *  @author KulW
	 */
	public void modeState(){
		if(contour.isSelected()){
			mode = CONTOUR_MODE;
			new Toast("Contour Mode");
		}
		else if(landMark.isSelected()){
			mode = LANDMARK_MODE;
			new Toast("Landmark Mode");
		}
		else {
			mode = SELECT_MODE;
			new Toast("Select Mode");
		}
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
    	if(buttonPressed) {
    		Icon image = new ImageIcon("icons/play.png");
    		//Set to not pressed
    		playButton.setActionCommand("PLAY");
    		playButton.setToolTipText("Play");
    		playButton.setIcon(image);
    	}
    	else {
    		//Set to pressed
    		playButton.setActionCommand("STOP");
    		playButton.setIcon(new ImageIcon("icons/pause1.png"));
    		playButton.setToolTipText("Pause");
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
        else if(actionCommand.equalsIgnoreCase("contour")){  //kw
        	landMark.setSelected(false);
        	modeState();
        }
        else if(actionCommand.equalsIgnoreCase("landMark")){ //kw
        	contour.setSelected(false);
        	modeState();
        }
    }
    
    /** -----------------------------------------------------------------------
     *  Static Method so Other Classes can get the Mode Value
     *  @return mode - int
     *  @author KulW
     */
    public static int getMode(){
    	return mode;
    }
    
    /**
     * Because the gridContorlModel would be more than a hassle, we chose to have 
     * the GridModel be the designated model for gridControlView
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
