package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.gui.views.actionperformed.LandmarkTypeActionPerformed;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkTypeMenu extends JPopupMenu implements MRIPopupMenu{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
}

	