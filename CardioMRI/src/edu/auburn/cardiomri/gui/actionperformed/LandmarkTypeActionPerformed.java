package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Landmark.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.ModeView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.popupmenu.view.LandmarkTypeMenu;
import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkTypeActionPerformed extends View implements ActionListener {
	
	private JPopupMenu selectMenu;
	private boolean istoggled;
	
	public LandmarkTypeActionPerformed(){

	}
	
	public LandmarkTypeActionPerformed(JPopupMenu selectMenu, boolean toggle){
		this.selectMenu = selectMenu;
		this.istoggled = toggle;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		Mode.setMode(Mode.landmarkMode(), actionCommand);
		
		if(istoggled && selectMenu != null) {
			this.model = getImageModel();
			selectMenu.setVisible(false);
		}
		
		System.out.println(actionCommand);
		
		LandmarkTypeMenu.staticHide();  //hides current landmarkTypeMenu
		
		if (actionCommand.equals("ARV")){
			Mode.setNextLandmarkType(Type.ARV);
        } 
		else if (actionCommand.equals("IRV")){
			Mode.setNextLandmarkType(Type.IRV);
        } 
		else if (actionCommand.equals("MS")){
			Mode.setNextLandmarkType(Type.MS);
        } 
		else if (actionCommand.equals("LVAPEX")){
			Mode.setNextLandmarkType(Type.LVAPEX);
        } 
		else if (actionCommand.equals("LVSEPTALBASE")){
			Mode.setNextLandmarkType(Type.LVSEPTALBASE);
        } 
		else if (actionCommand.equals("LVLATERALBASE")){
			Mode.setNextLandmarkType(Type.LVLATERALBASE);
        } 
		else if (actionCommand.equals("LVANTERIORBASE")){
			Mode.setNextLandmarkType(Type.LVANTERIORBASE);
        } 
		else if (actionCommand.equals("LVINFERIORBASE")){
			Mode.setNextLandmarkType(Type.LVINFERIORBASE);
        } 
		else if (actionCommand.equals("RVAPEX")){
			Mode.setNextLandmarkType(Type.RVAPEX);
        } 
		else if (actionCommand.equals("RVLATERALBASE")){
			Mode.setNextLandmarkType(Type.RVLATERALBASE);
        } 
		else if (actionCommand.equals("RVSEPTALBASE")){
			Mode.setNextLandmarkType(Type.RVSEPTALBASE);
        } 
		else if (actionCommand.equals("RVANTERIORBASE")){
			Mode.setNextLandmarkType(Type.RVANTERIORBASE);
        } 
		else if (actionCommand.equals("RVINFERIORBASE")){
			Mode.setNextLandmarkType(Type.RVINFERIORBASE);
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

