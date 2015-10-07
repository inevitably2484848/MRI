package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public interface MRIPopupMenu {

	//JPopupMenu popupMenu = new JPopupMenu();
	// ActionListener actionListener; // set New Action Listener

	JPopupMenu setPopup();
	
	JMenu addMenu(String str);
	
	JMenuItem addMenuItem(String str);
	
	JPopupMenu getPopup();
	
	void hidePopup();
	
	void refreshPopup();
	
	void setLocation();
	
}
