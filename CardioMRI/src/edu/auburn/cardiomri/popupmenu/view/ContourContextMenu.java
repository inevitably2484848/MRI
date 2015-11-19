package edu.auburn.cardiomri.popupmenu.view;

import java.awt.Dimension;
import java.awt.Point;
import edu.auburn.cardiomri.gui.actionperformed.ContourContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;


public class ContourContextMenu  {

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
//	private static final long serialVersionUID = -6889988991064856782L;
//	private JPopupMenu contourPop = new JPopupMenu();
//	private ContourContextMenuActionPerformed contextMenuListener;
	
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
		contourPop.addLabel(imageModel.getSelectedContour().toString());
		contourPop.add(new MySeparator());
		
		
		
//WAITING ON POINT CLASS	
//		if(imageModel.getControlPoint() != null){
			contourPop.addMenuItem("Delete Point", contextMenuListener);
//			if( Point is Locked) {
//			contourPop.add(addMenuItem("UnLock Point"))
//			}
//			else{
				contourPop.addMenuItem("Lock Point", contextMenuListener);
//			}
//			
//		}
		
		contourPop.addMenuItem("Delete Contour", contextMenuListener);
		contourPop.addMenuItem("Hide Contour", contextMenuListener);
		contourPop.addMenuItem("Lock Point (need Point Locked)", contextMenuListener);
		contourPop.addMenuItem("Unlock Point (need Point Locked)", contextMenuListener);
		contourPop.addMenuItem("Delete Point (need Point Selected)", contextMenuListener);
		contourPop.addMenuItem("Done Adding", contextMenuListener);

		
		contourPop.setVisible(true);
		contourPop.getBox();
		
		return contourPop;
	}

	public void setVisible(boolean b){
		contourPop.setVisible(b);
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
