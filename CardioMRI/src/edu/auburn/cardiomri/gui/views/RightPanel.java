package edu.auburn.cardiomri.gui.views;

import java.awt.Cursor;

import javax.swing.JSplitPane;

/** This class creates the layout for main image panel, and the right most column of panels in the workspace view
 * 		Included are the 2 chamber and 4 chamber image views, and the image contour panel
 * 		Further functionality implemented on 2/25/2016 by Aaron Fregeau
 * @author Ben Gustafson
 * @author Aaron Fregeau 
 */
public class RightPanel extends View {

	protected static ImageView  mainImageView, twoChamberView, fourChamberView;
	protected static ModeView contourControl = null;
	protected static Cursor crossHair = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	protected static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	
	
	
	// changing the mode based on a single mode string
	public static void changeMode(String mode) {
		if (contourControl != null) {
			cursorChange(mode);
			contourControl.setMessage(mode);
		}
	}
	
	// changes the mode if a qualifier has been passed in
	public static void changeMode(String mode, String qualifier) {
		if (contourControl != null) {
			cursorChange(mode);
			contourControl.setMessage(mode, qualifier);
		
		}
	}
	
	// Changes the cursor to crosshair or back to default based on the mode
	private static void cursorChange(String mode) {
		if (mode.equals("LANDMARK MODE")) {
			mainImageView.setCursor(crossHair);
	    	twoChamberView.setCursor(crossHair);
	    	fourChamberView.setCursor(crossHair);
			}
		else {
			mainImageView.setCursor(defaultCursor);
			twoChamberView.setCursor(defaultCursor);
			fourChamberView.setCursor(defaultCursor);
		}
	}
	/**
	 * Takes all needed Views for the main image panel, and the right most column of panels in the workspace view
	 * 
	 * @param mainImage			Image View with the Short Axis group (Big, center image)
	 * @param twoChamber		Image View with two chamber group (Upper right image)
	 * @param fourChamber		Image View with the four chamber group (Middle Right image)
	 * @param contourControl    Image View for the contour control (Bottom right panel)
	 */
	public RightPanel(ImageView mainImage, ImageView twoChamber, ImageView fourChamber, ModeView contourControl)
	{
		super();
//		mainImage.setTwoChamberView(twoChamber);
//		mainImage.setFourChamberView(fourChamber);
//		
//		twoChamber.setMainImageView(mainImage);
//		twoChamber.setFourChamberView(fourChamber);
//		
//		fourChamber.setMainImageView(mainImage);
//		fourChamber.setTwoChamberView(twoChamber);
//		
		this.mainImageView = mainImage;
		this.twoChamberView = twoChamber;
		this.fourChamberView = fourChamber;
		this.contourControl = contourControl;
		SetupPanel();
	}
	
	/**
	 * Crates the split pane objects, sets divider locations, adds all of them to the internal panel
	 * 		Notice the first splitPane is added to the second one, then second one is added to the third one
	 * 
	 * @note Resize Weight is used when resizing the whole window, has to be the same as the divider locations
	 * @note It is is also crucial to only add the final split pane to the panel, adding individually will render them wrong. 
	 */
	private void SetupPanel()
	{
    	JSplitPane smallImagesPane = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, this.twoChamberView.getPanel(), this.fourChamberView.getPanel());
  	
    	JSplitPane rightSideOfWindow = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, smallImagesPane, this.contourControl.getPanel());
    	
    	JSplitPane imagePanes  = new JSplitPane(
    			JSplitPane.HORIZONTAL_SPLIT,true, this.mainImageView.getPanel(), rightSideOfWindow);

        smallImagesPane.setResizeWeight(0.47);
        rightSideOfWindow.setResizeWeight(0.6);
        imagePanes.setResizeWeight(0.75);

        smallImagesPane.setDividerLocation(0.47);
        rightSideOfWindow.setDividerLocation(0.6);
        imagePanes.setDividerLocation(0.75);

	    this.panel.add(imagePanes);
	}
	
}
