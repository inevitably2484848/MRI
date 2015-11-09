package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.actionperformed.ContourContextMenuActionPerformed;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.util.Mode;

public class ContourContextMenu extends JPopupMenu implements MRIPopupMenu, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6889988991064856782L;
	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	private JPopupMenu contourPop = new JPopupMenu();
	private ContourContextMenuActionPerformed contextMenuListener;
	
	public ContourContextMenu(){
		contourPop.setLightWeightPopupEnabled(true);
		setPopup();
	}
	
	@Override
	public void setPopup() {
		contourPop.addMouseListener(this);
		ImageModel imageModel = ImageView.getImageModelStatic();
		contextMenuListener = new ContourContextMenuActionPerformed(imageModel, this);
		
//		if(imageModel.getControlPoint() != null){
			contourPop.add(addMenuItem("Delete Point"));
//			if( Point is Locked) {
//			contourPop.add(addMenuItem("UnLock Point"))
//			}
//			else{
				contourPop.add(addMenuItem("Lock Point"));
//			}
//			
//		}
		
		
		
		
		contourPop.add(addMenuItem("Delete Contour"));
		contourPop.add(addMenuItem("Hide Contour"));
		
		JMenuItem done = new JMenuItem("Done Adding");
		done.addMouseListener(this);
		done.setActionCommand("Done Adding");  //closes contour context menu and de-selects selected contour
		done.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
    			getImageModel().setSelectedContour(null);
    			Mode.setMode(Mode.selectMode());
    			new Toast(Mode.modeToast());
    			contourPop.setVisible(false);
    			contourPop.removeAll();
            }
        });
		contourPop.add(done);
			
			
		
		
		

	}

	@Override
	public JMenu addMenu(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuItem addMenuItem(String str) {
		JMenuItem newItem = new JMenuItem(str);
		newItem.addMouseListener(this);
		newItem.addActionListener(contextMenuListener);
		return newItem;
	}

	
	public JPopupMenu getPopup() {
		contourPop.setVisible(true);
		contourPop.repaint();
		contourPop.revalidate();
		return contourPop;
	}

	
	public void hidePopup() {
		contourPop.setVisible(false);
	}


	public void refreshPopup() {
		contourPop.revalidate();
		contourPop.repaint();
		
	}


	public void setLocation() {
		contourPop.setLocation(MouseInfo.getPointerInfo().getLocation());
		refreshPopup();
	}
	
	@Override
	public void removeAll() {
		contourPop.removeAll();
		contourPop.setVisible(false);
		refreshPopup();
		
	}
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(){
		return ImageView.getImageModelStatic();
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
