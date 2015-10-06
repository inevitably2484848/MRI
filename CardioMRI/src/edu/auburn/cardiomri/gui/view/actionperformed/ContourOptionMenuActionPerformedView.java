package edu.auburn.cardiomri.gui.view.actionperformed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.View;

public class ContourOptionMenuActionPerformedView extends View implements ActionListener {
	
	protected JPanel imageContourPanel;
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		String actionCommand = actionEvent.getActionCommand();
		
		if (actionCommand.equals("Delete Contour")) {
			if (getImageModel().getSelectedContour() == null) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "Please select a contour to delete.");
			} 
			else {
				getImageModel().deleteSelectedContour();
			}
		} 
		else if (actionCommand.equals("Hide Contour")) {
			if (getImageModel().getSelectedContour() == null) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "Please select a contour to hide.");
			} 
			else {
				getImageModel().hideSelectedContour();
			}
		} 
		else if (actionCommand.equals("Select Contour")) {
			Vector<Contour> visibleContours = getImageModel()
            .getVisibleContours();
			if (visibleContours.size() > 0) {
				Contour[] contours = new Contour[visibleContours.size()];
				visibleContours.toArray(contours);

				Contour c = (Contour) JOptionPane.showInputDialog(
                imageContourPanel, "Select Contour: ", "Contours",
                JOptionPane.PLAIN_MESSAGE, null, contours, "ham");

				getImageModel().setSelectedContour(c);
			} 
			else {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no visible contours to select.");
			}
		} 
		else if (actionCommand.equals("Hide Contours")) {
			if (getImageModel().getContours() == null || getImageModel().getContours().size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no contours to hide.");
			} else {
				getImageModel().hideAllContours();
			}
		} 
		else if (actionCommand.equals("Show Contours")) {
			if (getImageModel().getHiddenContours() == null || getImageModel().getHiddenContours().size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,
                "There are no contours to show.");
			} 
			else {
				getImageModel().showAllContours();
			}
		} 
		else if (actionCommand.equals("Delete All Contours")) {
			if (getImageModel().getContours() == null || getImageModel().getContours().size() == 0) {
				JOptionPane.showMessageDialog(imageContourPanel,"There are no contours to delete.");
			} 
			else {
				getImageModel().deleteAllContours();
			}
		}
	}
		/**
		 * gets Image model to add contour type
		 * @return
		 */
		public static ImageModel getImageModel(){
	    	return ImageView.getImageModelStatic();
	    }
	
}
