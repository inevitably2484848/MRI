package edu.auburn.cardiomri.gui.view.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkActionPerformedView extends View implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		Mode.setMode(Mode.landmarkMode());
		if (actionCommand.equals("ARV")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.ARV));
        } 
		else if (actionCommand.equals("IRV")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.IRV));
        } 
		else if (actionCommand.equals("MS")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.MS));
        } 
		else if (actionCommand.equals("LVAPEX")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVAPEX));
        } 
		else if (actionCommand.equals("LVSEPTALBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVSEPTALBASE));
        } 
		else if (actionCommand.equals("LVLATERALBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVLATERALBASE));
        } 
		else if (actionCommand.equals("LVANTERIORBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVANTERIORBASE));
        } 
		else if (actionCommand.equals("LVINFERIORBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVINFERIORBASE));
        } 
		else if (actionCommand.equals("RVAPEX")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVAPEX));
        } 
		else if (actionCommand.equals("RVLATERALBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVLATERALBASE));
        } 
		else if (actionCommand.equals("RVSEPTALBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVSEPTALBASE));
        } 
		else if (actionCommand.equals("RVANTERIORBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVANTERIORBASE));
        } 
		else if (actionCommand.equals("RVINFERIORBASE")){
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVINFERIORBASE));
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

