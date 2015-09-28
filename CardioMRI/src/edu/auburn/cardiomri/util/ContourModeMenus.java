package edu.auburn.cardiomri.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import edu.auburn.cardiomri.gui.views.WorkspaceView;


//add popupmenuListener class

public class ContourModeMenus extends View implements ActionListener{
	
	public static JPopupMenu popupMenuContour(){
		 
		//ImageView mainImageView = WorkspaceView.getMainImageView();
		ActionListener actionListener = new PopupActionListener();
		
		JMenuItem defaultType = new JMenuItem("Default");
		defaultType.setActionCommand("Default Type");
        defaultType.addActionListener(actionListener);
        //addContour.add(defaultType);
		
		JMenu lv = new JMenu("LV");
        JMenuItem lvEpi = new JMenuItem("Epicardial");
        lvEpi.setActionCommand("LV EPI");
        lvEpi.addActionListener(actionListener);
        lv.add(lvEpi);
        JMenuItem lvEndo = new JMenuItem("Endocardial");
        lvEndo.setActionCommand("LV ENDO");
        lvEndo.addActionListener(actionListener);
        lv.add(lvEndo);
		

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
		
		
		JPopupMenu contourPop = new JPopupMenu();
		PopupMenuListener popupMenuListener = new MyPopupMenuListener();
		contourPop.addPopupMenuListener(popupMenuListener);
		
		contourPop.add(defaultType);
		contourPop.addSeparator();
		contourPop.add(lv);
		contourPop.addSeparator();
		contourPop.add(la);
		contourPop.addSeparator();
		contourPop.add(rv);
		contourPop.addSeparator();
		contourPop.add(ra);
		

		return contourPop;
	}


 

}


//Define PopupMenuListener
/**
 * Menu Listener
 * @author Kullen
 */
class MyPopupMenuListener implements PopupMenuListener {
	public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
	 System.out.println("Canceled");
	}
	
	public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
	 System.out.println("Becoming Invisible");
	}
	
	public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
	 System.out.println("Becoming Visible");
	}
}


//Define ActionListener

class PopupActionListener extends View implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();

        if (actionCommand.equals("Default Type")) {
        	getImageModel().addContourToImage(new Contour(Type.DEFAULT));
        } else if (actionCommand.equals("LV EPI")) {
            getImageModel().addContourToImage(new Contour(Type.LV_EPI));

        } else if (actionCommand.equals("LV ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.LV_ENDO));

        } else if (actionCommand.equals("LA EPI")) {
            getImageModel().addContourToImage(new Contour(Type.LA_EPI));

        } else if (actionCommand.equals("LA ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.LA_ENDO));

        } else if (actionCommand.equals("RV EPI")) {
            getImageModel().addContourToImage(new Contour(Type.RV_EPI));

        } else if (actionCommand.equals("RV ENDO")) {
            getImageModel().addContourToImage(new Contour(Type.RV_ENDO));
        }
	
	}
	
	   public static ImageModel getImageModel(){
	    	return ImageView.getImageModelStatic();
	    }
}