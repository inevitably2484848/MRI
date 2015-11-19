package edu.auburn.cardiomri.popupmenu.view;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ContextMenu extends JPopupMenu implements MouseListener{

	private JPopupMenu menu;
	private int x1,y1,x2,y2;
	private final int BUFFER = 7;
	
	
	public void setMenu(JPopupMenu menu){
		this.menu = menu;
		menu.addMouseListener(this);
		setLocation();
	}

	public void addMenu(String str, ActionListener action) {
		// TODO Auto-generated method stub
	}
	
	public void addMenu(JMenu subMenu, ActionListener action) { 
		subMenu.addActionListener(action);
		subMenu.addMouseListener(this);
		menu.add(subMenu);
	}
	
	public JMenu addSubMenu(JMenu subMenu,  ActionListener action){
		subMenu.addActionListener(action);
		subMenu.addMouseListener(this);
		return subMenu;
		
	}

	/**************************************************************************
	 *  Adds Menu Item to Context Menu.
	 *  Also adds ActionListener and MouseListener
	 * @param str
	 * @param action
	 *************************************************************************/
	public void addMenuItem( String str, ActionListener action) {
		JMenuItem newItem = new JMenuItem(str);
		newItem.setActionCommand(str);
		newItem.addActionListener(action);
		newItem.addMouseListener(this);
		menu.add(newItem);
	}
	
	public JMenuItem addMenuItemTo(String str, ActionListener action){
		JMenuItem newItem = new JMenuItem(str);
		newItem.setActionCommand(str);
		newItem.addActionListener(action);
		newItem.addMouseListener(this);
		
		return newItem;
	}
	
	/**************************************************************************
	 * Adds "LABEL" to Context Menu
	 * Label is a menu item that is not selectable
	 * @param str
	 *************************************************************************/
	public void addLabel( String str){
		JMenuItem newItem = new  JMenuItem(str);
		
		newItem.addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent e){
				if(!(isInBox())){
					menu.setVisible(false);
				}
			}
			public void mouseEntered(MouseEvent e){
				if(!(isInBox())){
					menu.setVisible(false);
				}
			}
		});
		
		menu.add(newItem);
	}
	
	
	/**************************************************************************
	 *  Sets Location of Menu
	 *************************************************************************/
	public void setLocation() {
		y1 = (int) MouseInfo.getPointerInfo().getLocation().getY();
		x1 = (int) MouseInfo.getPointerInfo().getLocation().getX();
		//menu.setLocation(MouseInfo.getPointerInfo().getLocation());
		setLocationManually(x1,y1);
	}
	
	
	/**************************************************************************
	 * Manually Sets menu location
	 * @param x
	 * @param y
	 *************************************************************************/
	public void setLocationManually(int x, int y) {
		this.y1 = y;
		this.x1 = x;
		Point p = new Point(x1,y1);
		menu.setLocation(p);
	}

	/**************************************************************************
	 * Returns Location of Menu
	 * Top Left X and Y coordinate
	 *************************************************************************/
	public Point getLocation(){
		return menu.getLocation();
	}
	
	
	/**************************************************************************
	 * GetBox() sets the bounds of the menu,
	 * so if the mouse position is outside the box 
	 * close menu
	 *************************************************************************/
	public void getBox(){
		y2 = y1 + (int) menu.getSize().getWidth();
		x2 = x1 + (int) menu.getSize().getHeight();
		
		x1 = x1 - BUFFER;
		y1 = y1 - BUFFER;
		
		x2 = x2 + BUFFER;
		y2 = y2 + BUFFER;
	}
	
	/**************************************************************************
	 * Checks if mouse is in the box
	 * if not return false
	 * @return
	 *************************************************************************/
	public boolean isInBox(){		
		int xCheck =(int) MouseInfo.getPointerInfo().getLocation().getX();
		int yCheck = (int) MouseInfo.getPointerInfo().getLocation().getY();

		if(xCheck < x1 || xCheck > x2){
			return false;
		}
		else if(yCheck < y1 || yCheck > y2){
			return false;
		}
		
		return true;
	}
	

//MOUSE LISTENER

	@Override
	public void mouseEntered(MouseEvent e) {
		
//		if(!(isInBox())){
//			menu.setVisible(false);
//			return;
//		}
		
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenu")){
			((JMenu)e.getSource()).setArmed(true);
			((JMenu)e.getSource()).setPopupMenuVisible(true);

		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenuItem")){
			((JMenuItem)e.getSource()).setArmed(true);
		}
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		
//		if(!(isInBox())){
//			menu.setVisible(false);
//			return;
//		}
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENU")){
			((JMenu)e.getSource()).setArmed(false);
			((JMenu)e.getSource()).setPopupMenuVisible(false);
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENUITEM")) {
			((JMenuItem)e.getSource()).setArmed(false);
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { }


	@Override
	public void mousePressed(MouseEvent e) {  }


	@Override
	public void mouseReleased(MouseEvent e) { }
	
}

