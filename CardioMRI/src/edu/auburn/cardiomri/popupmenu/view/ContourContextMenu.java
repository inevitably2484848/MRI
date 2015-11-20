package edu.auburn.cardiomri.popupmenu.view;

import java.awt.Dimension;
import java.awt.Point;
import edu.auburn.cardiomri.gui.actionperformed.ContourContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;

/******************************************************************************
 * Contour Context Menu
 * This menu is for when you are in contour mode
 * @author Kullen (kullen@auburn.edu)
 * @verison 11/19/2015
 *****************************************************************************/

public class ContourContextMenu  {

	
	private ImageModel imageModel;
	private ContextMenu contourPop = new ContextMenu();
	private ContourContextMenuActionPerformed contextMenuListener;
	
	public ContourContextMenu(ImageModel imageModel){
		this.imageModel = imageModel;
		contourPop.setMenu(contourPop);
		setPopup();
	}
	
	
	public ContextMenu setPopup() {
		contextMenuListener = new ContourContextMenuActionPerformed(imageModel, contourPop);
		contourPop.setLocation();

		// Label Menu
		
		if(imageModel.getSelectedContour() != null){
			contourPop.addLabel(imageModel.getSelectedContour().toString());
			contourPop.add(new MySeparator());
			
			if(imageModel.getSelectedControlPoint() != null){
				contourPop.addMenuItem("Delete Point", contextMenuListener);
				if( imageModel.isControlPointLocked()) {
					contourPop.addMenuItem("Unlock Smooth", contextMenuListener);
				}
				else{
					contourPop.addMenuItem("Lock Smooth", contextMenuListener);
				}
				
			}
			
			contourPop.addMenuItem("Delete Contour", contextMenuListener);
			contourPop.addMenuItem("Hide Contour", contextMenuListener);

			contourPop.addMenuItem("Done Adding", contextMenuListener);
			
		}
		else {
			contourPop.addLabel("Please Select A Contour Type");
		}
		
		contourPop.setVisible(true);
		contourPop.getBox();
		

		
		
		
		return contourPop;
	}

	public void setVisible(boolean b){
		contourPop.setVisible(b);
	}
	public boolean isVisible(){
		return contourPop.isVisible();
	}
	public  Dimension getSize(){
		return contourPop.getSize();
	}
	
	public Point getLocation(){
		return contourPop.getLocation();
	}
	
	public boolean isInBox(){
		return contourPop.isInBox();
	}
	
}
