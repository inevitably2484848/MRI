package edu.auburn.cardiomri.popupmenu.view;

import java.awt.Dimension;
import java.awt.Point;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.gui.actionperformed.LandmarkContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.View;

/******************************************************************************
 * LandMark Context Menu:
 * This is the menu for when you are in landmark mode and you right click
 * @author Kullen  (kullen@auburn.edu)
 *@version 11/19/2015
 *******************************************************************************/


public class LandmarkContextMenu {

	private ContextMenu menu = new ContextMenu();
	private LandmarkContextMenuActionPerformed actionListener;
	private ImageModel imageModel;
	
	
	public LandmarkContextMenu(ImageModel imageModel){
		menu.setMenu(menu);
		this.imageModel = imageModel;
		setPopup();

	}	
	

	public ContextMenu setPopup() {
		actionListener = new LandmarkContextMenuActionPerformed(imageModel, menu);
		menu.setLocation();
		
		if(imageModel.getSelectedLandmark() != null){
			
			//If There is a selected LandMark
			
			menu.addLabel(imageModel.getSelectedLandmark().getType().toString());
			menu.add(new MySeparator());
			menu.addMenuItem("Delete Landmark" , actionListener);
			menu.addMenuItem("Hide Landmark" , actionListener);
			menu.addMenuItem("Hide All Landmarks", actionListener);
			menu.addMenuItem("Un-Hide All Landmarks", actionListener);
			menu.addMenuItem("Done Editing", actionListener);
		
		}
		else{
			menu.addLabel("No Landmark Type Selected");
			menu.add(new MySeparator());
			menu.addMenuItem("Done Adding" , actionListener);
		}

		menu.setVisible(true);
		menu.getBox();
		
		return menu;
	}
	

	public  Dimension getSize(){
		return menu.getSize();
	}
	
	public Point getLocation(){
		return menu.getLocation();
	}
	
	public boolean isInBox(){
		return menu.isInBox();
	}
	
	public void setVisible(boolean b){
		menu.setVisible(b);
	}
	public boolean isVisible(){
		return menu.isVisible();
	}
	

}