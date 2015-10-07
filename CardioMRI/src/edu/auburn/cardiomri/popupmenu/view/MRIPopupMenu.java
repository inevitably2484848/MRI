package edu.auburn.cardiomri.popupmenu.view;


import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * PopupMenu Interface helps with naming convention
 * @author Kullen
 *
 */
public interface MRIPopupMenu {

	void setPopup();
	
	JMenu addMenu(String str);
	
	JMenuItem addMenuItem(String str);
	
	JPopupMenu getPopup();
	
	void hidePopup();
	
	void refreshPopup();
	
	void setLocation();
	
	void removeAll();
	
}
