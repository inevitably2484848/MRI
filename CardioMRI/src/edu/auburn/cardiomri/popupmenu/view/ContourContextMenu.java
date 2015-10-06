package edu.auburn.cardiomri.popupmenu.view;

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

public class ContourContextMenu extends View implements ActionListener{

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu contourPop = new JPopupMenu();
	
	public static JPopupMenu popupContextMenu(){

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
		JMenuItem test = new JMenuItem("TESTING");
		contourPop.add(test);

		return contourPop;
	}	
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(){
		return ImageView.getImageModelStatic();
	}
}
