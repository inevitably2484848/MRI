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
	
    /**
     * Takes all needed information in to create the left third of the main workspace window
     * 
     * @param grid 				The current grid View-Object (Placed top left in workspace window)
     * @param gridControl 		The current grid control View-Object (Placed middle left in workspace window)
     * @param multipleImages 	The current multiple Image View-Object (Placed bottom left in workspace window)
     * @param width 			Workspace frame width
     * @param height 			Workspace frame height
     */
	public LeftPanel(GridView grid, GridControlView gridControl, MultipleImageView multipleImages)
	{
		super();
		this.grid = grid;
		this.gridControl = gridControl;
		this.multipleImages = multipleImages;
		SetupPanel();
	}
	
	/**
	 * Crates the split pane objects, sets divider locations, adds all of them to the internal panel
	 * 		Notice the first splitPane is added to the second one, then the dividers are set.
	 * 
	 * @note Resize Weight is used when resizing the whole window, has to be the same as the divider locations
	 * @note It is is also crucial to only add the final split pane to the panel, adding both will render them very small as well. 
	 */
	private void SetupPanel()
	{
		JSplitPane gridPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true,
		grid.getPanel(), gridControl.getPanel());
		   
		JSplitPane leftSideOfWindow = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
		true, gridPane, multipleImages.getPanel() );
		   
		gridPane.setResizeWeight(0.3);
		leftSideOfWindow.setResizeWeight(0.2);

		gridPane.setDividerLocation(0.3);
		leftSideOfWindow.setDividerLocation(0.2);
		   
		this.panel.add(leftSideOfWindow);
	}
}
