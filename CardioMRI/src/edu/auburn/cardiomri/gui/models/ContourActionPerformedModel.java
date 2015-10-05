package edu.auburn.cardiomri.gui.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;

public class ContourActionPerformedModel extends View implements ActionListener {

	/**
	 * Defines the action listener for each menu option. 
	 * This action listener sets the contour type you are going to add.
	 * @author Kullen
	 *
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();

        if (actionCommand.equals("Default Type")) {
        	getImageModel().addContourToImage(new Contour(Type.DEFAULT));
        } 
        else if (actionCommand.equals("LV EPI")) {
            getImageModel().addContourToImage(new Contour(Type.LV_EPI));
        } 
        else if (actionCommand.equals("LV ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.LV_ENDO));
        } 
        else if (actionCommand.equals("LA EPI")) {
            getImageModel().addContourToImage(new Contour(Type.LA_EPI));
        } 
        else if (actionCommand.equals("LA ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.LA_ENDO));
        } 
        else if (actionCommand.equals("RV EPI")) {
            getImageModel().addContourToImage(new Contour(Type.RV_EPI));
        } 
        else if (actionCommand.equals("RV ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.RV_ENDO));
        }
        else if (actionCommand.equals("RA EPI")) {
            getImageModel().addContourToImage(new Contour(Type.RA_EPI));
        }  
        else if (actionCommand.equals("RA ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.RA_ENDO));
        }
	
	}
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(){
    	return ImageView.getImageModelStatic();
    }
	
}

