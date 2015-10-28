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
import edu.auburn.cardiomri.util.Mode;

public class LandmarkModeMenu {

	public static JPopupMenu landmarkPop = new JPopupMenu();
	
	public static JPopupMenu popupMenuLandMark(){
	
		ActionListener actionListener = new PopupActionListenerLandMark();
		
        for (Landmark.LandmarkType t : Landmark.LandmarkType.values() ){
        	JMenuItem tmp = new JMenuItem(t.abbv());
        	tmp.setActionCommand(t.abbv());
        	tmp.addActionListener(actionListener);
        	tmp.setToolTipText(t.toString());
        	landmarkPop.add(tmp);
        }
		
		return landmarkPop;
	}
}

	//Define ActionListener
	/**
	 * Defines the action listener for each menu option. 
	 * This action listener sets the contour type you are going to add.
	 * @author Kullen
	 *
	 */
class PopupActionListenerLandMark extends View implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();

		if (actionCommand.equals("ARV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.ARV,0.0,0.0));
        } else if (actionCommand.equals("IRV")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.IRV,0.0,0.0));
        } else if (actionCommand.equals("MS")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.MS,0.0,0.0));
        } else if (actionCommand.equals("LVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVAPEX,0.0,0.0));
        } else if (actionCommand.equals("LVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVSEPTALBASE,0.0,0.0));
        } else if (actionCommand.equals("LVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVLATERALBASE,0.0,0.0));
        } else if (actionCommand.equals("LVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVANTERIORBASE,0.0,0.0));
        } else if (actionCommand.equals("LVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.LVINFERIORBASE,0.0,0.0));
        } else if (actionCommand.equals("RVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVAPEX,0.0,0.0));
        } else if (actionCommand.equals("RVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVLATERALBASE,0.0,0.0));
        } else if (actionCommand.equals("RVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVSEPTALBASE,0.0,0.0));
        } else if (actionCommand.equals("RVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVANTERIORBASE,0.0,0.0));
        } else if (actionCommand.equals("RVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	getImageModel().addLandmarkToImage(new Landmark(LandmarkType.RVINFERIORBASE,0.0,0.0));
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