package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.auburn.cardiomri.gui.actionperformed.ContourTypeActionPerformed;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.LandmarkTypeMenu;

import edu.auburn.cardiomri.util.Mode;


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
 *
 */
public class GridControlView extends View implements ChangeListener {

	protected JPanel imageContourPanel;
	protected boolean buttonPressed;
	protected JButton playButton;
	protected int playSpeed;
	protected RunPlaybutton runner;
	
	public static JToggleButton contour = new JToggleButton("Add Contour"); //kw
	public static JToggleButton landMark = new JToggleButton("Add LandMark"); //kw
	
	protected JButton showContours;
	protected JButton hideContours;
	protected JButton showLandmarks;
	protected JButton hideLandmarks;
	protected JButton hideSliceLines;
	protected JButton showSliceLines;
	
	
	
	protected ContourTypeMenu cntrPM = new ContourTypeMenu();
	protected LandmarkTypeMenu lndmrkPM = new LandmarkTypeMenu();

	
	/**
	 * Sets panel to visible, adds slider to panel
	 * 
	 * In order to get the play button to work, look in to swing timers. I couldn't get it to work in the time we had
	 * 
	 * 
	 * Buttons are added to panels, then those panels are added to the super panel
	 * author Megan
	 */
	public GridControlView()
	{
		super();
		//this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		this.panel.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints(); //creates grid
		this.panel.setOpaque(true);
		this.panel.setVisible(true);
		
		buttonPressed = true;
		playSpeed = 0;
		runner = new RunPlaybutton(playSpeed);
		//setUp();
		
		//play button
		playButton = new JButton();
		playButton.addActionListener(this);
		playButton.setPreferredSize(new Dimension(50, 20));
		playButton.setBorderPainted(true);
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeButtonState();
		c.weightx = 0;
		c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
		this.panel.add(playButton, c);
		
		//Slider is from 0 to 20 with one digit increments
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 20, 1); 
		framesPerSecond.addChangeListener(this);
		framesPerSecond.setToolTipText("Change Speed");
		framesPerSecond.setInverted(true); // This inverts the slider so the right side is faster
		c.weightx = 0;
		c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
		this.panel.add(framesPerSecond, c);
		
		//ToggleButtons
		Mode.setMode(Mode.selectMode()); //set Mode
		
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new GridBagLayout()); //layout for mode buttons
	    GridBagConstraints k = new GridBagConstraints();
		contour.addActionListener(this);
		contour.setActionCommand("contour");
		contour.setToolTipText("Add New Contour");
		contour.setMinimumSize(new Dimension(124,29));
		k.weightx = 0;
        k.gridx = 0;
        k.gridy = 0;
        //c.insets = new Insets(50,0,0,0);
        modePanel.add(contour, k);
        
		landMark.addActionListener(this);
		landMark.setActionCommand("landmark");
		landMark.setToolTipText("Add New Landmark Point");
		//c.insets = new Insets(50,0,0,0);
		//modePanel.add(contour);
		//modePanel.add(landMark);
		k.weightx = 0;
        k.gridx = 1;
        k.gridy = 0;
        modePanel.add(landMark, k);
        
        c.weightx = 0;
		c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10,0,0,0);
		this.panel.add(modePanel,c); //adds model panel to super panel
		//modeToggleButton(); //kw
		
		//this.panel.setLayout(new GridBagLayout());
       //GridBagConstraints c = new GridBagConstraints(); //creates grid

        JPanel leftButtonsPanel = new JPanel();
		leftButtonsPanel.setLayout(new GridBagLayout()); //layout for left buttons
	    GridBagConstraints l = new GridBagConstraints();
		showContours = new JButton("Show All Contours");
		showContours.addActionListener(this);
		showContours.setActionCommand("showContours");
		//showContours.setToolTipText("Show All Contours");
		l.weightx = 0;
        l.gridx = 0;
        l.gridy = 0;
		leftButtonsPanel.add(showContours, l);
		
		//showContours.setMinimumSize(new Dimension(124,29))
		
		hideContours = new JButton("Hide All Contours");
		hideContours.addActionListener(this);
		hideContours.setActionCommand("hideContours");
		//hideContours.setToolTipText("Hide All Contours");
		l.weightx = 0;
        l.gridx = 1;
        l.gridy = 0;
		leftButtonsPanel.add(hideContours, l);
		
		showLandmarks = new JButton("Show All Landmarks");
		showLandmarks.addActionListener(this);
		showLandmarks.setActionCommand("showLandmarks");
		//showLandmarks.setToolTipText("Show All Landmarks");
		l.weightx = 0;
        l.gridx = 0;
        l.gridy = 1;
		leftButtonsPanel.add(showLandmarks, l);
		
		hideLandmarks = new JButton("Hide All Landmarks");
		hideLandmarks.addActionListener(this);
		hideLandmarks.setActionCommand("hideLandmarks");
		//hideLandmarks.setToolTipText("Hide All Landmarks");
		l.weightx = 0;
        l.gridx = 1;
        l.gridy = 1;
		leftButtonsPanel.add(hideLandmarks, l);
		
		hideSliceLines = new JButton("Hide Slice Lines");
		hideSliceLines.addActionListener(this);
		hideSliceLines.setActionCommand("hideSliceLines");
		hideSliceLines.setToolTipText("Hide Slice Lines");
		l.weightx = 0;
        l.gridx = 1;
        l.gridy = 2;
		leftButtonsPanel.add(hideSliceLines, l);
		
		showSliceLines = new JButton("Show Slice Lines");
		showSliceLines.addActionListener(this);
		showSliceLines.setActionCommand("showSliceLines");
		showSliceLines.setToolTipText("Show Slice Lines");
		l.weightx = 0;
        l.gridx = 0;
        l.gridy = 2;
		leftButtonsPanel.add(showSliceLines, l);

		c.weightx = 0;
		c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;
		this.panel.add(leftButtonsPanel,c); //adds left buttons panel to super panel
	}
	
	/**
	 * Sets up the play button
	 */
//	public void setUp()
//	{
//		playButton = new JButton();
//        playButton.addActionListener(this);
//        playButton.setPreferredSize(new Dimension(50, 20));
//        playButton.setBorderPainted(true);
//		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//		changeButtonState();
//		
//		this.panel.add(playButton);
//	}
	
	/**
	 * sets up left panel side buttons
	 **/
	
	
	
	/** -----------------------------------------------------------------------
	 *  mode ToogleButton set up
	 *  Creates a new panel and adds 2 toggle buttons to that panel. Then adds
	 *  the new panel to the main panel.
	 *  adds and sets actions to the toggle buttons
	 *  you can set modes to add contours , add landmarks, de-selecting button 
	 *  sets select mode
	 *  @author KulW
	 */
/*	public void modeToggleButton(){
		Mode.setMode(Mode.selectMode()); //set Mode
		
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
	} */
	

	
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
     * ***************** NEEDS REFACTORING ******************************
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
        	lndmrkPM.setVisible(false);
			if(Mode.getMode() == Mode.contourMode()){  //if already in contour mode
				contour.setSelected(false);
				Mode.setMode(Mode.selectMode());
				cntrPM.hidePopup();
			}
			else{
				lndmrkPM.hidePopup();
				contour.setSelected(true);
				cntrPM.setLocation();
				cntrPM.getPopup();
				
				Mode.setMode(Mode.contourMode());
			}
        }
        else if(actionCommand.equalsIgnoreCase("landMark")){ //kw
        	contour.setSelected(false);
        	cntrPM.hidePopup();
        	if(Mode.getMode() == Mode.landmarkMode()){
        		landMark.setSelected(false);
        		Mode.setMode(Mode.selectMode());
        		lndmrkPM.hidePopup();
        	}
        	else {
        		landMark.setSelected(true);
        		lndmrkPM.getPopup();
        		
        		Mode.setMode(Mode.landmarkMode());
        	}
        	new ModeView(Mode.modeChange());
        		
        }
        //if no contours, shows message. else, hides all contours
        else if(actionCommand.equals("hideContours")){ //megan
        	if (getImageModel().getContours() == null || getImageModel().getContours()
					.size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no Contours to hide.");
			} else {
				getImageModel().hideAllContours();
        	
        }}
        //if no contours shows message, else shows all contours. if all contours are shown does nothing.
        else if(actionCommand.equals("showContours")){ //megan
        	if (getImageModel().getContours() == null || getImageModel().getContours().size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no Contours to show.");
			} else {
				getImageModel().showAllContours();
        	
        }}
        //if no landmarks, shows message. else, hides all landmarks
        else if(actionCommand.equals("hideLandmarks")){ //megan
        	if (getImageModel().getLandmarks() == null || getImageModel().getLandmarks()
					.size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no Landmarks to hide.");
			} else {
				getImageModel().hideAllLandmarks();
        	
        }}
        //if no landmarks shows message, else shows all landmarks. if all landmarks are shown does nothing
        else if(actionCommand.equals("showLandmarks")){ //megan
        	if (getImageModel().getLandmarks() == null || getImageModel().getLandmarks()
					.size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no Landmarks to show.");
			} else {
				getImageModel().showAllLandmarks();
        	
        }} else if(actionCommand.equals("hideSliceLines")){ //brad
        	getImageModel().hideSliceLines();
        }
        else if(actionCommand.equals("showSliceLines")){ //brad
        	getImageModel().showSliceLines();
        }
        if(getImageModel().getSelectedContour() != null){
        	getImageModel().setSelectedContour(null);
        }
        

    } //*************************************************************************
    
    
    /****************************************************************************
     * Depresses the Toggle Buttons when you leave a mode without depressing the 
     * toggle button
     ***************************************************************************/
    public static void depressToggles(){
    	if(getImageModel().getSelectedContour() != null){
	    	getImageModel().getSelectedControlPoint().isSelected(false);
	    	getImageModel().setSelectedControlPoint(null);
    	}
    	if(contour.isSelected()){
    		contour.setSelected(false);
    	}
    	else{
    		landMark.setSelected(false);
    	}
    }
    
    
	/**
	 * gets Image model 
	 * @return
	 */
	public static ImageModel getImageModel(){
		return ImageView.getImageModelStatic();
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


