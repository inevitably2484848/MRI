package edu.auburn.cardiomri.gui.views;

/**
 * This view is the bottom right panel of the main workspace window. 
 * Eventually all contour information from the main image panel will be displayed on here.
 * 	The ideal setup would be to list all the contours, and be able to show, hide, edit, and delete them easily.  
 * 
 * 
 * @author Ben Gustafson
 *
 */
public class ContourControlView extends View {
	
	/**
	 * Simply sets panel to visible for filler space
	 * 
	 */
	public ContourControlView()
	{
		super();
	    this.panel.setOpaque(true);
	    this.panel.setVisible(true);
	}
	
}
