package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;
import edu.auburn.cardiomri.util.Mode;

public class SelectContextMenuActionPerformed implements ActionListener {

	private JPopupMenu menu;
	private ImageModel imageModel;
	
	public SelectContextMenuActionPerformed(ImageModel imageModel, JPopupMenu menu){
		this.menu = menu;
		this.imageModel = imageModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String action =  e.getActionCommand();

		if(action.equals("Add Contour")){
			System.out.println(action);
			ContourTypeMenu cTypeMenu = new ContourTypeMenu();
			cTypeMenu.setLocation();
			cTypeMenu.getPopup();

		}
		else if (action.equals("Add Landmark")){
			System.out.println(action);
		}
		else if (action.equals("Delete Point")){
			imageModel.deleteControlPoint(
					imageModel.getSelectedControlPoint().getX(), 
					imageModel.getSelectedControlPoint().getY());
		}
		else if (action.equals("Edit Contour")){
			Mode.setMode(Mode.contourMode());
			menu.setVisible(false);
		}
		else if (action.equals("Delete Landmark")){
			imageModel.deleteLandmarkFromImage(imageModel.getSelectedLandmark());
			menu.setVisible(false);
		}
	}

}
