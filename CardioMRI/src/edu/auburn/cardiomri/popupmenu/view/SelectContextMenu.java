package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


import edu.auburn.cardiomri.gui.views.actionperformed.SelectContextMenuActionPerformed;


public class SelectContextMenu extends JPopupMenu implements MRIPopupMenu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private  JPopupMenu selectCM = new JPopupMenu();
	private  ActionListener actionListener = new SelectContextMenuActionPerformed();

	public SelectContextMenu(){
		setPopup();
	}
	
	public  void setPopup() {
		selectCM.add(addMenu("TEST"));
	}

	public JMenu addMenu(String str){
		JMenu newMenu = new JMenu(str);
		newMenu.add(addMenuItem("Add Contour"));
		newMenu.add(addMenuItem("Add Landmark"));

		return newMenu;
	}
	
	public  JMenuItem addMenuItem(String str) {
		// TODO Auto-generated method stub
		
		JMenuItem newItem = new JMenuItem(str);
		newItem.setActionCommand(str);
		newItem.addActionListener(actionListener);
		
		return newItem;
		
	}
	
	public  JPopupMenu getPopup() {
		selectCM.setVisible(true);
		selectCM.repaint();
		selectCM.revalidate();
		return selectCM;
	}

	public void hidePopup() {
		selectCM.repaint();
		selectCM.revalidate();
		selectCM.removeAll();
		selectCM.setVisible(false);
	}


	public void refreshPopup() {
		selectCM.revalidate();
		selectCM.repaint();
		
	}

	
	public void setLocation(){
		selectCM.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
	}

	


}
