package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.controller.Controller;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.gui.views.WorkspaceView;
import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.util.Mode;

public class ContourTypeActionPerformed  implements ActionListener {

	/**
	 * Defines the action listener for each menu option. 
	 * This action listener sets the contour type you are going to add.
	 * @author Kullen
	 *
	 */
	
	private View wrkspcVw;
	private boolean istoggled;
	private ImageModel model;
	private ContourTypeMenu ctMenu;
	public ContourTypeActionPerformed(View view){
		this.wrkspcVw  =view;
		istoggled = false;
		this.model = getImageModel();
		//System.out.println("TESTING " + wrkspcVw.hashCode());
	}
	
	public ContourTypeActionPerformed(ContourTypeMenu ctMenu, boolean toggle){
		this.ctMenu = ctMenu;
		this.istoggled = toggle;
		//System.out.println("TESTING " + wrkspcVw.toString());
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		Mode.setMode(Mode.contourMode());
// change
		
		if(istoggled){
			 this.model = getImageModel(istoggled);
			 ctMenu.hidePopup();
		}
		
        if (actionCommand.equals("LV EPI")) {
            model.addContourToImage(new Contour(Type.LV_EPI));
        } 
        else if (actionCommand.equals("LV ENDO")) {
        	model.addContourToImage(new Contour(Type.LV_ENDO));
        } 
        else if (actionCommand.equals("LA EPI")) {
        	model.addContourToImage(new Contour(Type.LA_EPI));
        } 
        else if (actionCommand.equals("LA ENDO")) {
        	model.addContourToImage(new Contour(Type.LA_ENDO));
        } 
        else if (actionCommand.equals("RV EPI")) {
        	model.addContourToImage(new Contour(Type.RV_EPI));
        } 
        else if (actionCommand.equals("RV ENDO")) {
        	model.addContourToImage(new Contour(Type.RV_ENDO));
        }
        else if (actionCommand.equals("RA EPI")) {
        	model.addContourToImage(new Contour(Type.RA_EPI));
        }  
        else if (actionCommand.equals("RA ENDO")) {
        	model.addContourToImage(new Contour(Type.RA_ENDO));
        }
	
	}
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public ImageModel getImageModel(){
    	return ((WorkspaceView) wrkspcVw).getMainImageView().getImageModel();
    }
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(boolean toggle){
    	return ImageView.getImageModelStatic();
    }
	
}

