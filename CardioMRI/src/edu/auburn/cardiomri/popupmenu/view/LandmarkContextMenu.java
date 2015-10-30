package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkContextMenu extends View implements MRIPopupMenu, MouseListener{

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	private JPopupMenu landmarkPop = new JPopupMenu();
	
	public LandmarkContextMenu(){

		setPopup();

	}	
	
	@Override
	public void setPopup() {
		//ImageModel imageModel = ImageView.getImageModelStatic();
		JMenuItem done = new JMenuItem("Done Adding");
		done.setActionCommand("Done Adding");
		done.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Mode.setMode(Mode.selectMode());
				new Toast(Mode.modeToast());
				landmarkPop.setVisible(false);
				landmarkPop.removeAll();
			}
		});
		landmarkPop.add(done);		
	}

	@Override
	public JMenu addMenu(String str) {
		return null;
	}

	@Override
	public JMenuItem addMenuItem(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPopupMenu getPopup() {
		landmarkPop.setVisible(true);
		landmarkPop.repaint();
		landmarkPop.revalidate();
		return landmarkPop;
	}

	@Override
	public void hidePopup() {
		landmarkPop.setVisible(false);
		
	}

	@Override
	public void refreshPopup() {
		landmarkPop.repaint();
		landmarkPop.revalidate();
	}

	@Override
	public void setLocation() {
		landmarkPop.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
		
	}

	@Override
	public void removeAll() {
		landmarkPop.removeAll();
		refreshPopup();
		
	}

	
	// Mouse listeners =========================================================
	private int index = 0;
	private boolean isFirst = true;
	
	
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



