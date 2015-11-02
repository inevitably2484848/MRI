package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.LandmarkType;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.popupmenu.view.LandmarkTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkTypeActionPerformed extends View implements ActionListener {
	
	private SelectContextMenu selectMenu;
	private boolean istoggled;
	
	public LandmarkTypeActionPerformed(){

	}
	
	public LandmarkTypeActionPerformed(SelectContextMenu selectMenu, boolean toggle){
		this.selectMenu = selectMenu;
		this.istoggled = toggle;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		Mode.setMode(Mode.landmarkMode());
		
		if(istoggled && selectMenu != null) {
			this.model = getImageModel();
			selectMenu.hidePopup();
		}
		
		LandmarkTypeMenu.staticHide();  //hides current landmarkTypeMenu
		
		if (actionCommand.equals("ARV")){
			Mode.setNextLandmarkType(LandmarkType.ARV);
        } 
		else if (actionCommand.equals("IRV")){
			Mode.setNextLandmarkType(LandmarkType.IRV);
        } 
		else if (actionCommand.equals("MS")){
			Mode.setNextLandmarkType(LandmarkType.MS);
        } 
		else if (actionCommand.equals("LVAPEX")){
			Mode.setNextLandmarkType(LandmarkType.LVAPEX);
        } 
		else if (actionCommand.equals("LVSEPTALBASE")){
			Mode.setNextLandmarkType(LandmarkType.LVSEPTALBASE);
        } 
		else if (actionCommand.equals("LVLATERALBASE")){
			Mode.setNextLandmarkType(LandmarkType.LVLATERALBASE);
        } 
		else if (actionCommand.equals("LVANTERIORBASE")){
			Mode.setNextLandmarkType(LandmarkType.LVANTERIORBASE);
        } 
		else if (actionCommand.equals("LVINFERIORBASE")){
			Mode.setNextLandmarkType(LandmarkType.LVINFERIORBASE);
        } 
		else if (actionCommand.equals("RVAPEX")){
			Mode.setNextLandmarkType(LandmarkType.RVAPEX);
        } 
		else if (actionCommand.equals("RVLATERALBASE")){
			Mode.setNextLandmarkType(LandmarkType.RVLATERALBASE);
        } 
		else if (actionCommand.equals("RVSEPTALBASE")){
			Mode.setNextLandmarkType(LandmarkType.RVSEPTALBASE);
        } 
		else if (actionCommand.equals("RVANTERIORBASE")){
			Mode.setNextLandmarkType(LandmarkType.RVANTERIORBASE);
        } 
		else if (actionCommand.equals("RVINFERIORBASE")){
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

