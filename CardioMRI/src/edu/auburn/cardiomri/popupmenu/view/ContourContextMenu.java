package popupmenus;

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
import edu.auburn.cardiomri.gui.views.View;

public class ContourContextMenu extends View implements ActionListener, MouseListener{

	/**
	 * Populates the Popup Menu
	 * @return JPopupMenu
	 */
	public static JPopupMenu contourPop = new JPopupMenu();
	
	public static JPopupMenu popupContextMenu(){

		ImageModel imageModel = ImageView.getImageModelStatic();
		ActionListener actionListener = new PopupActionContourContext();
		
		if(imageModel.getSelectedContour() != null){
			JMenuItem defaultType = new JMenuItem("Select Contour");
			defaultType.setActionCommand("Default Type");
			defaultType.addActionListener(actionListener);
			contourPop.add(defaultType);
		}
		else{
			JMenuItem defaultType = new JMenuItem("Un-Select Contour");
			defaultType.setActionCommand("Default Type");
			defaultType.addActionListener(actionListener);
			contourPop.add(defaultType);
		}
		
		JMenuItem defaultType = new JMenuItem("No Contour");
		defaultType.setActionCommand("Default Type");
		defaultType.addActionListener(actionListener);
		contourPop.add(defaultType);
		

		return contourPop;
	}
	
	
	
}

//Define ActionListener
/**
* Defines the action listener for each menu option. 
* This action listener sets the contour type you are going to add.
* @author Kullen
*
*/
class PopupActionContourContext extends View implements ActionListener {
	@Override
	
	
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		System.out.println(actionCommand);
	
	}
	
	/**
	 * gets Image model to add contour type
	 * @return
	 */
	public static ImageModel getImageModel(){
		return ImageView.getImageModelStatic();
	}
	
}