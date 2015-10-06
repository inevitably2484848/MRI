package edu.auburn.cardiomri.popupmenu.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.Toast;
import edu.auburn.cardiomri.gui.views.View;
import edu.auburn.cardiomri.util.Mode;

public class LandmarkContextMenu extends View implements ActionListener, MouseListener{

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu landmarkPop = new JPopupMenu();
	
	public static JPopupMenu popupContextMenu(){

		ImageModel imageModel = ImageView.getImageModelStatic();

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
		
		return landmarkPop;
	}	
	
	
}



