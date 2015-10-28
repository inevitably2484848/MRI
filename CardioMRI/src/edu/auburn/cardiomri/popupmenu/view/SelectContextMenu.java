package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;

import edu.auburn.cardiomri.gui.actionperformed.MenuMouseListener;
import edu.auburn.cardiomri.gui.actionperformed.SelectContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.Model;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;


public class SelectContextMenu extends JPopupMenu implements MRIPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5352058505305990803L;
	/**
	 * 
	 */

	private  JPopupMenu selectCM = new JPopupMenu();
	private  MouseListener mouse = new MenuMouseListener(this);
	private  JMenu add = new JMenu("Add");
	private  JMenuItem contour = new JMenuItem("Contour");
	private  JMenuItem landmark = new JMenuItem("Landmark");
	private  ActionListener actionListener;
	private  ImageView view;
	
	
	public SelectContextMenu(ImageView view){
		this.view = view;
		this.actionListener = new SelectContextMenuActionPerformed(view);
		setPopup();
		
	}
	
	public  void setPopup() {
		selectCM.addMouseListener(mouse);
		
		add.add(addMenuItem(contour, "Contour"));
		add.add(addMenuItem(landmark, "Landmark"));

		selectCM.add(addMenu(add));
		selectCM.add(addMenuItem("Add Landmark"));
	}

	public JMenu addMenu(String str){
		JMenu newMenu = new JMenu(str);
		newMenu.addMouseListener(mouse);
		return newMenu;
	}
	
	public JMenu addMenu(JMenu jMenu){
		jMenu.addMouseListener(mouse);
		return jMenu;
	}
	
	
	public  JMenuItem addMenuItem(String str) {
		JMenuItem newItem = new JMenuItem(str);
		newItem.setActionCommand(str);
		newItem.addActionListener(actionListener);
		newItem.addMouseListener(mouse);
		return newItem;
	}
	public JMenuItem addMenuItem(JMenuItem jMenuItem, String str){
		jMenuItem.setActionCommand(str);
		jMenuItem.addActionListener(actionListener);
		jMenuItem.addMouseListener(mouse);

		return jMenuItem;
	}
	
	
	public  JPopupMenu getPopup() {
		selectCM.setVisible(true);
		selectCM.repaint();
		selectCM.revalidate();
		return selectCM;
	}

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

	
	public void setLocation(){
		selectCM.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
	}

	


}
