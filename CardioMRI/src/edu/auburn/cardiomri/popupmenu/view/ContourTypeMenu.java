package edu.auburn.cardiomri.popupmenu.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.gui.views.actionperformed.ContourTypeActionPerformed;


/**
 * Contour Mode Menu
 * When you select the contour mode button this menu will pop up
 * for you to choose what kind of contour you would like to add.
 * @author Kullen
 *
 */

public class ContourTypeMenu extends JPopupMenu{ // extends View implements MouseListener {
	
	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu contourPop = new JPopupMenu();
	
	public static JPopupMenu setPopup(){

		ActionListener actionListener = new ContourTypeActionPerformed();
		contourPop.setLightWeightPopupEnabled(false);
		contourPop.add("Choose a Type");
		contourPop.addSeparator();
		
        JMenuItem lvEpi = new JMenuItem("LV Epicardial");
        JMenuItem lvEndo = new JMenuItem("LV Endocardial");
        
        lvEpi.setActionCommand("LV EPI");
        lvEpi.addActionListener(actionListener);
        
        lvEndo.setActionCommand("LV ENDO");
        lvEndo.addActionListener(actionListener);
        
        contourPop.add(lvEpi);
        contourPop.add(lvEndo);
        contourPop.addSeparator();

		//JMenu la = new JMenu("LA");
        JMenuItem laEpi = new JMenuItem("LA Epicardial");
        JMenuItem laEndo = new JMenuItem("LA Endocardial");
        
        laEpi.setActionCommand("LA EPI");
        laEpi.addActionListener(actionListener);
        laEndo.setActionCommand("LA ENDO");
        laEndo.addActionListener(actionListener);
        
        contourPop.add(laEndo);
        contourPop.add(laEpi);
        contourPop.addSeparator();
		
        JMenuItem rvEpi = new JMenuItem("RV Epicardial");
        JMenuItem rvEndo = new JMenuItem("RV Endocardial");
        
        rvEpi.setActionCommand("RV EPI");
        rvEpi.addActionListener(actionListener);
        rvEndo.setActionCommand("RV ENDO");
        rvEndo.addActionListener(actionListener);
        
        contourPop.add(rvEpi);
        contourPop.add(rvEndo);
        contourPop.addSeparator();
        
        JMenuItem raEpi = new JMenuItem("RA Epicardial");
        JMenuItem raEndo = new JMenuItem("RA Endocardial");
        
        raEpi.setActionCommand("RA EPI");
        raEpi.addActionListener(actionListener);
        raEndo.setActionCommand("RA ENDO");
        raEndo.addActionListener(actionListener);
        
        contourPop.add(raEndo);
        contourPop.add(raEpi);
        
		contourPop.repaint();
		contourPop.revalidate();
		
		return contourPop;
	}
	
	public static JPopupMenu getPopupMenu(){
		return contourPop;
	}
	
	public static void hidePopupMenu(){
		contourPop.setVisible(false);
		contourPop.revalidate();
		contourPop.repaint();
	}

}

