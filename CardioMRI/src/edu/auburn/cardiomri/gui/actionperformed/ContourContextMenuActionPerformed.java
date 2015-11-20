package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.popupmenu.view.ContourContextMenu;
import edu.auburn.cardiomri.util.Mode;

public class ContourContextMenuActionPerformed implements ActionListener {

	private ImageModel imageModel;
	private JPopupMenu menu;
	
	
	public ContourContextMenuActionPerformed(ImageModel imageModel, JPopupMenu menu){
		this.imageModel = imageModel;
		this.menu = menu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(actionCommand.equalsIgnoreCase("Delete Contour")){
			imageModel.deleteSelectedContour();
			Mode.setMode(Mode.selectMode());
		}
		else if(actionCommand.equalsIgnoreCase("Hide Contour")){
			imageModel.hideSelectedContour();
			Mode.setMode(Mode.selectMode());
		}
		else if(actionCommand.equalsIgnoreCase("Lock Point (need Point Locked)")){
			
		}
		else if(actionCommand.equalsIgnoreCase("Unlock Point (need Point Locked)")){
			
		}
		else if(actionCommand.equalsIgnoreCase("Delete Point")){

			imageModel.deleteControlPoint(
					imageModel.getSelectedControlPoint().getX(), 
					imageModel.getSelectedControlPoint().getY());
			Mode.setMode(Mode.selectMode());
			
		}
		else if(actionCommand.equalsIgnoreCase("Done Adding")){
			imageModel.setSelectedContour(null);
			menu.setVisible(false);
			Mode.setMode(Mode.selectMode());
		}
		
		menu.setVisible(false);

	}

}
