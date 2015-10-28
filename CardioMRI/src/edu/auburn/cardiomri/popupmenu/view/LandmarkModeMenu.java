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
        	Mode.setNextLandmarkType(LandmarkType.ARV);
        } else if (actionCommand.equals("IRV")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.IRV);
        } else if (actionCommand.equals("MS")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.MS);
        } else if (actionCommand.equals("LVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.LVAPEX);
        } else if (actionCommand.equals("LVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.LVSEPTALBASE);
        } else if (actionCommand.equals("LVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.LVLATERALBASE);
        } else if (actionCommand.equals("LVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.LVANTERIORBASE);
        } else if (actionCommand.equals("LVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.LVINFERIORBASE);
        } else if (actionCommand.equals("RVAPEX")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.RVAPEX);
        } else if (actionCommand.equals("RVLATERALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.RVLATERALBASE);
        } else if (actionCommand.equals("RVSEPTALBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.RVSEPTALBASE);
        } else if (actionCommand.equals("RVANTERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.RVANTERIORBASE);
        } else if (actionCommand.equals("RVINFERIORBASE")){
        	Mode.setMode(Mode.landmarkMode());
        	Mode.setNextLandmarkType(LandmarkType.RVINFERIORBASE);
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