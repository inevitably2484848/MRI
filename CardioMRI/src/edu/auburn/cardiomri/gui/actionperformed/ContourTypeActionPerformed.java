package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.gui.views.WorkspaceView;
import edu.auburn.cardiomri.popupmenu.view.ContourTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;
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
	private JPopupMenu selectMenu;
	
	
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
	public ContourTypeActionPerformed(JPopupMenu selectMenu, boolean toggle){
		this.selectMenu = selectMenu;
		this.istoggled = toggle;
		//System.out.println("TESTING " + wrkspcVw.toString());
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		Mode.setMode(Mode.contourMode());

		System.out.println(actionCommand);
		
		if(istoggled && ctMenu != null){
			 this.model = getImageModel(istoggled);
			 ctMenu.hidePopup();
		}
		else if(istoggled && selectMenu != null) {
			this.model = getImageModel(istoggled);
			selectMenu.setVisible(false);
		}
		
        if (actionCommand.equals("LV EPI") || actionCommand.equals("LV Epicardial")) {
            model.addContourToImage(new Contour(Type.LV_EPI));
        } 
        else if (actionCommand.equals("LV ENDO") || actionCommand.equals("LV Endocardial")) {
        	model.addContourToImage(new Contour(Type.LV_ENDO));
        } 
        else if (actionCommand.equals("LA EPI")|| actionCommand.equals("LA Epicardial")) {
        	model.addContourToImage(new Contour(Type.LA_EPI));
        } 
        else if (actionCommand.equals("LA ENDO") || actionCommand.equals("LA Endocardial")) {
        	model.addContourToImage(new Contour(Type.LA_ENDO));
        } 
        else if (actionCommand.equals("RV EPI")|| actionCommand.equals("RV Epicardial")) {
        	model.addContourToImage(new Contour(Type.RV_EPI));
        } 
        else if (actionCommand.equals("RV ENDO") || actionCommand.equals("RV Endocardial")) {
        	model.addContourToImage(new Contour(Type.RV_ENDO));
        }
        else if (actionCommand.equals("RA EPI") || actionCommand.equals("RV Epicardial")) {
        	model.addContourToImage(new Contour(Type.RA_EPI));
        }  
        else if (actionCommand.equals("RA ENDO") || actionCommand.equals("RA Endocardial")) {
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

