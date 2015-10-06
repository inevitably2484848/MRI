package edu.auburn.cardiomri.popupmenu.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class LandmarkTypeMenu extends JPopupMenu{
	
	public static JPopupMenu landmarkPop = new JPopupMenu();
	
	public static JPopupMenu popupMenuLandMark(){
	
		ActionListener actionListener = new LandmarkTypeActionPerformed();
		
		Mode.setMode(Mode.landmarkMode());
		
        for (Landmark.LandmarkType t : Landmark.LandmarkType.values() ){
        	JMenuItem tmp = new JMenuItem(t.abbv());
        	tmp.setActionCommand(t.abbv());
        	tmp.addActionListener(actionListener);
        	tmp.setToolTipText(t.toString());
        	landmarkPop.add(tmp);
        }
		
		return landmarkPop;
	}
	
	public static void hidePopupMenu(){
		landmarkPop.setVisible(false);
		landmarkPop.revalidate();
		landmarkPop.repaint();
	}
	
}

	