package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.gui.actionperformed.ContourTypeActionPerformed;
import edu.auburn.cardiomri.gui.views.View;


/**
 * Contour Mode Menu
 * When you select the contour mode button this menu will pop up
 * for you to choose what kind of contour you would like to add.
 * @author Kullen
 *
 */

public class ContourTypeMenu extends View implements MRIPopupMenu, MouseListener{ // extends View implements MouseListener {
	
	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu contourPop = new JPopupMenu();
	
	
	public ContourTypeMenu(){
		setPopup();
	}
	
	@Override
	public void setPopup(){

		ActionListener actionListener = new ContourTypeActionPerformed(this, true);
		contourPop.setLightWeightPopupEnabled(true);
		contourPop.addSeparator();
		
		int addSepEveryTwo = 0;
        for(Contour.Type t : Contour.Type.values()){  //loops over Contour Type enum
        	
        	String name = t.getGroup() + " " +  t.getName();
        	contourPop.add(addMenuItem(name,t.getAbbv(), actionListener));

        	++addSepEveryTwo;
        	if(addSepEveryTwo % 2 == 0){
        		contourPop.addSeparator();
        	}
        	
        } 
		
	}

	@Override
	public JMenu addMenu(String str) {
		// TODO Auto-generated method stub
		return null;
	}


	public JMenuItem addMenuItem(String name, String command, ActionListener action){
		JMenuItem newItem = new JMenuItem(name);
		newItem.addMouseListener(this);
		newItem.setActionCommand(command);
		newItem.addActionListener(action);
		return newItem;
	}

	@Override
	public JPopupMenu getPopup() {
		contourPop.removeAll();
		setPopup();
		contourPop.setVisible(true);
		return contourPop;
	}

	@Override
	public void hidePopup() {
		contourPop.setVisible(false);
		
	}

	@Override
	public void refreshPopup() {
		contourPop.revalidate();
		contourPop.repaint();
		
	}

	@Override
	public void setLocation() {
		contourPop.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
		
	}

	@Override
	public void removeAll() {
		contourPop.removeAll();
		refreshPopup();
		
	}

	@Override
	public JMenuItem addMenuItem(String str) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Mouse listeners =========================================================
	private int index = 0;
	//private boolean isFirst = true;
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenu")){
			((JMenu)e.getSource()).setArmed(true);
			//((JMenu)e.getSource()).setPopupMenuVisible(true);

		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenuItem")){
			((JMenuItem)e.getSource()).setArmed(true);
		}

		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENU")){
			((JMenu)e.getSource()).setArmed(false);
			//((JMenu)e.getSource()).setPopupMenuVisible(false);	
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENUITEM")) {
			((JMenuItem)e.getSource()).setArmed(false);
		}
	}

	public int getIndex(){
		return index;
	}


}

