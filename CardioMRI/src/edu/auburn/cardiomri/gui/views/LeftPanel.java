package edu.auburn.cardiomri.gui.views;

import javax.swing.JSplitPane;

/**
 * This is where left third of the main workspace window is put together.
 * 		Included is the gird, the controls for the grid, and the multiple image panel
 * 
 * Nothing other than the final layout is put together here.
 * 
 * @note There is no model, and chances are high there will never be a model (There is no data this view can't hold, thus no need for a model)
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
	
    /**
     * Takes all needed information in to create the left third of the main workspace window
     * 
     * 
     * @param grid 				The current grid View-Object (Placed top left in workspace window)
     * @param gridControl 		The current grid control View-Object (Placed middle left in workspace window)
     * @param multipleImages 	The current multiple Image View-Object (Placed bottom left in workspace window)
     * @param width 			Workspace frame width
     * @param height 			Workspace frame height
     */
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
	
	/**
	 * Crates the split pane objects, sets divider locations, adds all of them to the internal panel
	 * 		Notice the first splitPane is added to the second one, then the dividers are set.
	 * 
	 * @note It is necessary to use the frame width and height to set the divider locations, simple fractions will render the split panes as very small objects
	 * @note It is is also crucial to only add the final split pane to the panel, adding both will render them very small as well. 
	 */
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
