package edu.auburn.cardiomri.popupmenu.view;

import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.util.Mode;

public class ContourContextMenu extends JPopupMenu implements MRIPopupMenu{

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	private JPopupMenu contourPop = new JPopupMenu();
	
	public ContourContextMenu(){
		contourPop.setLightWeightPopupEnabled(true);
		setPopup();
	}
	
	@Override
	public void setPopup() {

		ImageModel imageModel = ImageView.getImageModelStatic();
		
		if(imageModel.getSelectedContour() != null){
			JMenuItem done = new JMenuItem("Done Adding");
			done.setActionCommand("Done Adding");
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
		

	}

	@Override
	public JMenu addMenu(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuItem addMenuItem(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPopupMenu getPopup() {
		contourPop.setVisible(true);
		contourPop.repaint();
		contourPop.revalidate();
		return contourPop;
	}

	@Override
	public void hidePopup() {
		contourPop.setVisible(false);
	}

	@Override
	public void refreshPopup() {
		contourPop.revalidate();
		contourPop.repaint();
		
	}

	@Override
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



}
