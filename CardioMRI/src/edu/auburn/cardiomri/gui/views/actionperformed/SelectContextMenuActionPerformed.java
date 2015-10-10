package edu.auburn.cardiomri.gui.views.actionperformed;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;

public class SelectContextMenuActionPerformed implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String action =  e.getActionCommand();

		if(action.equals("Add Contour")){
			System.out.println(action);

		}
		else if (action.equals("Add Landmark")){
			System.out.println(action);
		}
	}

}
