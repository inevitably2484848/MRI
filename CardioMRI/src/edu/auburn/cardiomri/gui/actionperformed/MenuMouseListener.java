package edu.auburn.cardiomri.gui.actionperformed;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.popupmenu.view.SelectContextMenu;

public class MenuMouseListener implements MouseListener {

	private SelectContextMenu jpMenu;
	private int index = 0;
	private boolean isFirst = true;
	
	public MenuMouseListener(SelectContextMenu jpMenu){
		this.jpMenu = jpMenu;
	}
	
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
					jpMenu.hidePopup();
				}
			}
			
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenu")){
			((JMenu)e.getSource()).setArmed(true);
			((JMenu)e.getSource()).setPopupMenuVisible(true);
			index++;
			Component[] x = ((JMenu)e.getSource()).getComponents();
			System.out.println(" COMPONENTS " + ((JMenu)e.getSource()).getComponentCount());

		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMenuItem")){
			
			((JMenuItem)e.getSource()).setArmed(true);
		}
		
		System.out.println(getIndex());
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
//		System.out.println(e.getComponent().getClass().getSimpleName());
		
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JPopupMenu")){
			 ++index;
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENU")){
			((JMenu)e.getSource()).setArmed(false);
			((JMenu)e.getSource()).setPopupMenuVisible(false);	
			index--;
			Component[] x = ((JMenu)e.getSource()).getComponents();
			for(int i = 0 ; i < x.length ; i++){
				
			}
		}
		if(e.getComponent().getClass().getSimpleName().equalsIgnoreCase("JMENUITEM")) {
			((JMenuItem)e.getSource()).setArmed(false);
		}

		
	}

	
	public int getIndex(){
		return index;
	}
	
	public void closeMenu(){
        Thread thread = new Thread();
        thread.start();
        try {
			thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(getIndex() == 0){
        	jpMenu.hidePopup();
        }
        thread.stop();
        
        
	}
	
}
	

