package edu.auburn.cardiomri.popupmenu.view;


import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.gui.actionperformed.ContourContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.actionperformed.ContourTypeActionPerformed;
import edu.auburn.cardiomri.gui.actionperformed.LandmarkTypeActionPerformed;
import edu.auburn.cardiomri.gui.actionperformed.SelectContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;

/******************************************************************************
 * Select Context Menu: 
 * 	When a user right clicks on the ImageView when in select mode this menu
 * 	pop ups.
 * @author Kullen
 *
 *****************************************************************************/

public class SelectContextMenu {

	private ContextMenu menu = new ContextMenu();
	private SelectContextMenuActionPerformed actionListener;
	private  ImageModel imageModel;

	private  ContourTypeActionPerformed contourType;
	private  LandmarkTypeActionPerformed landmarkType;
	private  ContourContextMenuActionPerformed contourCMListener;
	private  JMenu add = new JMenu("Add");
	private  JMenu contour = new JMenu("Contour");
	private  JMenu landmark = new JMenu("Landmark");
	
	
	
	/**************************************************************************
	 * Constructor
	 *  select menu uses its own action listener and Contour Type 
	 *  and Landmark Type action Listener. 
	 * @param view
	 *************************************************************************/
	public SelectContextMenu(ImageModel imageModel){
		menu.setMenu(menu);
		this.imageModel = imageModel;
		contourType = new ContourTypeActionPerformed(menu, true);
		landmarkType = new LandmarkTypeActionPerformed(menu, true);
		contourCMListener = new ContourContextMenuActionPerformed(imageModel, menu);
		setPopup();
	}
	
	/*************************************************************************
	 * setPopup() builds menu
	 * 
	 *************************************************************************/
	public  ContextMenu setPopup() {
		actionListener = new SelectContextMenuActionPerformed(imageModel,menu);
		menu.setLocation();

		if(imageModel.getSelectedControlPoint() != null){
			menu.addMenuItem("Delete Point", actionListener);
			if( imageModel.isControlPointLocked()) {
				menu.addMenuItem("Unlock Smooth", contourCMListener);
			}
			else{
				menu.addMenuItem("Lock Smooth", contourCMListener);
			}
		}
		if(imageModel.getSelectedContour() != null){
			menu.addMenuItem("Edit Contour", actionListener);
		}
		if(imageModel.getSelectedLandmark() != null){
			menu.addLabel(imageModel.getSelectedLandmark().getType().toString());
			menu.add(new MySeparator());
			menu.addMenuItem("Delete Landmark", actionListener);
			menu.addMenuItem("Delete All Landmarks", actionListener);
			menu.addMenuItem("Hide Landmark", actionListener);
			menu.addMenuItem("Hide All Landmarks", actionListener);
			menu.add(new MySeparator());
		}
		
		
		if(imageModel.getSelectedControlPoint() == null &&
				imageModel.getSelectedContour() == null &&
				imageModel.getSelectedLandmark() == null){
			if(!imageModel.getHiddenContours().isEmpty()){
				menu.addMenuItem("Un-Hide Contours", actionListener);
			}
			if(!imageModel.getHiddenLandmarks().isEmpty()){
				menu.addMenuItem("Un-Hide All Landmarks", actionListener);
			}
		
		}
		
		
		
		//contour types subMenu
		int addSepEveryTwo = 0;
        for(Contour.Type t : Contour.Type.values()){  //loops over Contour Type enum
        	
        	String name = t.getGroup() + " " +  t.getName();
        	contour.add(menu.addMenuItemTo(name, contourType));
        	++addSepEveryTwo;
        	if(addSepEveryTwo % 2 == 0){
        		contour.add(new MySeparator());
        	}
        } 
        add.add(menu.addSubMenu(contour, contourType));
        
        
        //pulled from Landmark Menu Bar
        for (Landmark.LandmarkType t : Landmark.LandmarkType.values() ){
        	 landmark.add(menu.addMenuItemTo(t.abbv(), landmarkType));
        }
		
		add.add(menu.addSubMenu(landmark,landmarkType));
		
		//add the add menu to the SelectMenu
		menu.addMenu(add, actionListener);
		
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
