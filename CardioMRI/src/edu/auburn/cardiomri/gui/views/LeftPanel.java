package edu.auburn.cardiomri.gui.views;

import javax.swing.JSplitPane;

/**
 * This will house the left most panel of the main window. 
 * 		Included is the gird, the controls for the grid, and the multiple image panel
 * 
 * @author Ben Gustafson
 *
 */
public class LeftPanel extends View {
	protected GridView grid;
	protected GridControlView gridControl;
	protected MultipleImageView multipleImages;
    private int workSpaceWidth = 1200;
    private int workSpaceHeight = 800;
	
	public LeftPanel(GridView grid, GridControlView gridControl, MultipleImageView multipleImages,int width, int height)
	{
		super();
		this.grid = grid;
		this.gridControl = gridControl;
		this.multipleImages = multipleImages;
		this.workSpaceWidth = width;
		this.workSpaceHeight = height;
		SetupPanel();
	}
	
	private void SetupPanel()
	{
		
		JSplitPane gridPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true,
		grid.getPanel(), gridControl.getPanel());
		   
		JSplitPane leftSideOfWindow = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
		true, gridPane, multipleImages.getPanel() );
		   
		gridPane.setDividerLocation(workSpaceHeight/4);
		leftSideOfWindow.setDividerLocation(workSpaceHeight/2);
		   
		this.panel.add(leftSideOfWindow);
	}
}
