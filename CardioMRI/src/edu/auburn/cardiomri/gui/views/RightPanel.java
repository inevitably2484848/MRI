package edu.auburn.cardiomri.gui.views;

import javax.swing.JSplitPane;

/** This class will house the main image panel, and the right most column of panels
 * 		Included are the 2 chamber and 4 chamber image views, and the image contour panel
 * 
 * 
 */
public class RightPanel extends View {
	
	protected ImageView  mainImageView, twoChamberView, fourChamberView;
	protected ContourControlView contourControl;
	
	//Default values
    private int workSpaceWidth = 1200;
    private int workSpaceHeight = 800; 
	
	public RightPanel(ImageView mainImage, ImageView twoChamber, ImageView fourChamber, ContourControlView contourControl, int width, int height)
	{
		super();
		this.mainImageView = mainImage;
		this.twoChamberView = twoChamber;
		this.fourChamberView = fourChamber;
		this.contourControl = contourControl;
		this.workSpaceWidth = width;
		this.workSpaceHeight = height;
		SetupPanel();
	}
	
	private void SetupPanel()
	{
    	JSplitPane smallImagesPane = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, this.twoChamberView.getPanel(), this.fourChamberView.getPanel());
  	
    	JSplitPane rightSideOfWindow = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT, true, smallImagesPane, this.contourControl.getPanel());
    	
    	JSplitPane imagePanes  = new JSplitPane(
    			JSplitPane.HORIZONTAL_SPLIT,true, this.mainImageView.getPanel(), rightSideOfWindow);

        smallImagesPane.setResizeWeight(0.5);
        rightSideOfWindow.setResizeWeight(0.75);
        imagePanes.setResizeWeight(0.75);

        smallImagesPane.setDividerLocation(0.5);
        rightSideOfWindow.setDividerLocation(0.75);
        imagePanes.setDividerLocation(0.75);

	    this.panel.add(imagePanes);
	    this.panel.revalidate();
	}
	
}
