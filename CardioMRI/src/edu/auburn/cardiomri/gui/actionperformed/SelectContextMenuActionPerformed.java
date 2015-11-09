package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;

public class SelectContextMenuActionPerformed implements ActionListener {

	private ImageView view;
	private Model model;
	
	public SelectContextMenuActionPerformed(ImageView view){
		this.view = view;
		this.model = view.getModel();
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
	}

}
