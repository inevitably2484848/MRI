package edu.auburn.cardiomri.popupmenu.view;

import java.awt.Dimension;
import java.awt.Point;

import edu.auburn.cardiomri.gui.actionperformed.LandmarkContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.View;

public class LandmarkContextMenu extends View {

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
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
		menu.addLabel("landmark name");
		menu.add(new MySeparator());
		menu.addMenuItem("Delete Landmark" , actionListener);
		menu.addMenuItem("Hide Landmark" , actionListener);
		menu.addMenuItem("Done Adding" , actionListener);
		
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
	

}