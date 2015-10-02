package popupmenus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;

/**
 * Contour Mode Menu
 * When you select the contour mode button this menu will pop up
 * for you to choose what kind of contour you would like to add.
 * @author Kullen
 *
 */

public class ContourModeMenus extends View implements ActionListener, MouseListener{
	
	
	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu contourPop = new JPopupMenu();
	
	public static JPopupMenu popupMenuContour(){

		ActionListener actionListener = new PopupActionListener();

		contourPop.add("Choose a Type");
		contourPop.addSeparator();
		
		JMenuItem defaultType = new JMenuItem("Default");
		defaultType.setActionCommand("Default Type");
        defaultType.addActionListener(actionListener);
		contourPop.add(defaultType);
		
		

        
		
		JMenu lv = new JMenu("LV");
        JMenuItem lvEpi = new JMenuItem("Epicardial");
        JMenuItem lvEndo = new JMenuItem("Endocardial");
        
        lvEpi.setActionCommand("LV EPI");
        lvEpi.addActionListener(actionListener);
        
        lvEndo.setActionCommand("LV ENDO");
        lvEndo.addActionListener(actionListener);
        
        lv.add(lvEpi);
        lv.add(lvEndo);
		
        contourPop.add(lv);

		JMenu la = new JMenu("LA");
        JMenuItem laEpi = new JMenuItem("Epicardial");
        laEpi.setActionCommand("LA EPI");
        laEpi.addActionListener(actionListener);
        la.add(laEpi);
        JMenuItem laEndo = new JMenuItem("Endocardial");
        laEndo.setActionCommand("LA ENDO");
        laEndo.addActionListener(actionListener);
        la.add(laEndo);
		
		
		JMenu rv = new JMenu("RV");
        JMenuItem rvEpi = new JMenuItem("Epicardial");
        rvEpi.setActionCommand("RV EPI");
        rvEpi.addActionListener(actionListener);
        rv.add(rvEpi);
        JMenuItem rvEndo = new JMenuItem("Endocardial");
        rvEndo.setActionCommand("RV ENDO");
        rvEndo.addActionListener(actionListener);
        rv.add(rvEndo);
        
        
		JMenu ra = new JMenu("RA");
        JMenuItem raEpi = new JMenuItem("Epicardial");
        raEpi.setActionCommand("RA EPI");
        raEpi.addActionListener(actionListener);
        ra.add(raEpi);
        JMenuItem raEndo = new JMenuItem("Endocardial");
        raEndo.setActionCommand("RA ENDO");
        raEndo.addActionListener(actionListener);
        ra.add(raEndo);
		
		
        //jpopupmenu add all menus to popup menu
		
		
		contourPop.add(la);
		
		contourPop.add(rv);
		
		contourPop.add(ra);
		
		return contourPop;
	}
	
	
	public static JPopupMenu getPopupMenu(){
		return contourPop;
   }
	
}



//Define ActionListener
/**
 * Defines the action listener for each menu option. 
 * This action listener sets the contour type you are going to add.
 * @author Kullen
 *
 */
class PopupActionListener extends View implements ActionListener {
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
	
	}
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(){
    	return ImageView.getImageModelStatic();
    }
	
}