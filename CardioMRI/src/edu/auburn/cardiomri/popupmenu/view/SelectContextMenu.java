package edu.auburn.cardiomri.popupmenu.view;


import java.awt.MouseInfo;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.gui.actionperformed.ContourTypeActionPerformed;
import edu.auburn.cardiomri.gui.actionperformed.LandmarkTypeActionPerformed;
import edu.auburn.cardiomri.gui.actionperformed.SelectContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.views.ImageView;

/**
 * Select Context Menu: 
 * 	When a user right clicks on the Image View when in select mode this menu
 * 	pop ups.
 * @author Kullen
 *
 */

public class SelectContextMenu extends JPopupMenu implements MRIPopupMenu, MouseListener {

	private static final long serialVersionUID = -5352058505305990803L;
	private  ContourTypeActionPerformed contourType;
	private  LandmarkTypeActionPerformed landmarkType;
	private  JPopupMenu selectCM = new JPopupMenu();
	private  JMenu add = new JMenu("Add");
	private  JMenu contour = new JMenu("Contour");
	private  JMenu landmark = new JMenu("Landmark");
	private  ActionListener actionListener;
	//private  ImageView view;
	private int index = 0;
	private boolean isFirst = true;
	
	
	/**
	 * Constructor
	 *  select menu uses its own action listener and Contour Type 
	 *  and Landmark Type action Listener. 
	 * @param view
	 */
	public SelectContextMenu(ImageView view){
		//this.view = view;
		contourType = new ContourTypeActionPerformed(this, true);
		landmarkType = new LandmarkTypeActionPerformed(this, true);
		this.actionListener = new SelectContextMenuActionPerformed(view);
		setPopup();
	}
	
	/**
	 * setPopup() builds menu
	 * 
	 */
	public  void setPopup() {
		selectCM.addMouseListener(this);

		//contour types subMenu
		int addSepEveryTwo = 0;
        for(Contour.Type t : Contour.Type.values()){  //loops over Contour Type enum
        	
        	String name = t.getGroup() + " " +  t.getName();
        	contour.add(addMenuItem(name,t.getAbbv(), contourType));

        	++addSepEveryTwo;
        	if(addSepEveryTwo % 2 == 0){
        		contour.addSeparator();
        	}
        	
        } 
        
        //pulled from Landmark Menu Bar
        for (Landmark.LandmarkType t : Landmark.LandmarkType.values() ){
        	JMenuItem tmp = new JMenuItem(t.abbv());
        	tmp.setActionCommand(t.abbv());
        	tmp.addMouseListener(this);
        	tmp.addActionListener(landmarkType);
        	tmp.setToolTipText(t.toString());
        	landmark.add(tmp);
        }
		
		add.add(addMenu(contour));
		add.add(addMenu(landmark));
		selectCM.add(addMenu(add));
		
	}

	
	/**
	 * adds sub menus to the selectContextMenu
	 */
	public JMenu addMenu(String str){
		JMenu newMenu = new JMenu(str);
		newMenu.addMouseListener(this);
		return newMenu;
	}
	public JMenu addMenu(JMenu jMenu){
		jMenu.addMouseListener(this);
		return jMenu;
	}

	
	/**
	 * adds MenuItems to Menus
	 */
	public  JMenuItem addMenuItem(String str) {
		JMenuItem newItem = new JMenuItem(str);
		newItem.setActionCommand(str);
		newItem.addActionListener(actionListener);
		newItem.addMouseListener(this);
		return newItem;
	}
	public JMenuItem addMenuItem(JMenuItem jMenuItem, String str){
		jMenuItem.setActionCommand(str);
		jMenuItem.addActionListener(actionListener);
		jMenuItem.addMouseListener(this);

		return jMenuItem;
	}
	public JMenuItem addMenuItem(String name, String command, ActionListener action){
		JMenuItem newItem = new JMenuItem(name);
		newItem.setActionCommand(command);
		newItem.addActionListener(action);
		newItem.addMouseListener(this);
		return newItem;
	}
	
	
	/**
	 * getPopup
	 * sets selectContextMenu visibility to true
	 */
	public  JPopupMenu getPopup() {
		selectCM.setVisible(true);
		selectCM.repaint();
		selectCM.revalidate();
		return selectCM;
	}

	/**
	 * hides selectContextMenu and removes all items
	 */
	public void hidePopup() {
		selectCM.repaint();
		selectCM.revalidate();
		selectCM.removeAll();
		selectCM.setVisible(false);
	}


	public void refreshPopup() {
		selectCM.revalidate();
		selectCM.repaint();
	}

	/**
	 * sets the location of the context menu.
	 * so menu appear at right click
	 */
	public void setLocation(){
		selectCM.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
	}
	
	
	// Mouse listeners =========================================================
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JPopupMenu")){
			if(isFirst){
				isFirst= false;
			}
			else{
				index--;
				if(index == 0){
					hidePopup();
				}
			}
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenu")){
			((JMenu)e.getSource()).setArmed(true);
			((JMenu)e.getSource()).setPopupMenuVisible(true);
			index++;

		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenuItem")){
			((JMenuItem)e.getSource()).setArmed(true);
		}

		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JPopupMenu")){
			 ++index;
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENU")){
			((JMenu)e.getSource()).setArmed(false);
			((JMenu)e.getSource()).setPopupMenuVisible(false);	
			index--;
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENUITEM")) {
			((JMenuItem)e.getSource()).setArmed(false);
		}

	}

	public int getIndex(){
		return index;
	}


	


}
