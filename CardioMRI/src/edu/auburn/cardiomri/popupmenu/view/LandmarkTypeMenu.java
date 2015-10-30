package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.gui.actionperformed.LandmarkTypeActionPerformed;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkTypeMenu extends JPopupMenu implements MRIPopupMenu, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2260919009948844704L;
	public static JPopupMenu staticMenu;
	public JPopupMenu landmarkPop = new JPopupMenu();
	
	public LandmarkTypeMenu(){
		setPopup();
	}
	
	
	@Override
	public void setPopup(){
	
		ActionListener actionListener = new LandmarkTypeActionPerformed();
		
		Mode.setMode(Mode.landmarkMode());
		
        for (Landmark.LandmarkType t : Landmark.LandmarkType.values() ){
        	JMenuItem tmp = new JMenuItem(t.abbv());
        	tmp.setActionCommand(t.abbv());
        	tmp.addActionListener(actionListener);
        	tmp.addMouseListener(this);
        	tmp.setToolTipText(t.toString());
        	landmarkPop.add(tmp);
        }
        
        staticMenu = landmarkPop;
	}
	

	@Override
	public JMenu addMenu(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuItem addMenuItem(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPopupMenu getPopup() {
		landmarkPop.setVisible(true);
		setLocation();
		refreshPopup();
		return landmarkPop;
	}

	@Override
	public void hidePopup() {
		landmarkPop.setVisible(false);
		refreshPopup();
		
	}

	@Override
	public void refreshPopup() {
		// TODO Auto-generated method stub
		landmarkPop.revalidate();
		landmarkPop.repaint();
		
	}

	@Override
	public void setLocation() {
		// TODO Auto-generated method stub
		landmarkPop.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
	}
	
	public static void staticHide(){
		staticMenu.setVisible(false);
	}
	
	// Mouse listeners =========================================================
	private int index = 0;
	private boolean isFirst = true;
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
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JPopupMenu")){
			if(isFirst){
				isFirst= false;
			}
			else{
				index--;
				if(index == 0){
					hidePopup();
				}
			}
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenu")){
			((JMenu)e.getSource()).setArmed(true);
			((JMenu)e.getSource()).setPopupMenuVisible(true);
			index++;

		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenuItem")){
			((JMenuItem)e.getSource()).setArmed(true);
		}

		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JPopupMenu")){
			 ++index;
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENU")){
			((JMenu)e.getSource()).setArmed(false);
			((JMenu)e.getSource()).setPopupMenuVisible(false);	
			index--;
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENUITEM")) {
			((JMenuItem)e.getSource()).setArmed(false);
		}

	}

	public int getIndex(){
		return index;
	}
	
}

	