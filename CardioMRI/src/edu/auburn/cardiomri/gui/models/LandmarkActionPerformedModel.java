package edu.auburn.cardiomri.gui.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkActionPerformedModel extends View implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();

		if (actionCommand.equals("ARV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.ARV));
        } else if (actionCommand.equals("IRV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.IRV));
        } else if (actionCommand.equals("MS")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.MS));
        } else if (actionCommand.equals("LVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVAPEX));
        } else if (actionCommand.equals("LVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVSEPTALBASE));
        } else if (actionCommand.equals("LVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVLATERALBASE));
        } else if (actionCommand.equals("LVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVANTERIORBASE));
        } else if (actionCommand.equals("LVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVINFERIORBASE));
        } else if (actionCommand.equals("RVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVAPEX));
        } else if (actionCommand.equals("RVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVLATERALBASE));
        } else if (actionCommand.equals("RVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVSEPTALBASE));
        } else if (actionCommand.equals("RVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVANTERIORBASE));
        } else if (actionCommand.equals("RVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
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

