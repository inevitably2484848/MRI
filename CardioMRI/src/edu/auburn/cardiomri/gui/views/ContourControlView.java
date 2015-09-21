package edu.auburn.cardiomri.gui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Time;
import java.util.Vector;
import javax.swing.DefaultListModel;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;


/**
 * This view is the bottom right panel of the main workspace window. 
 * Eventually all contour information from the main image panel will be displayed on here.
 * 	The ideal setup would be to list all the contours, and be able to show, hide, edit, and delete them easily.  
 * 
 * 
 * @author Kullen Williams
 *
 */
public class ContourControlView extends View {
	
	/**
	 * Simply sets panel to visible for filler space
	 * 
	 */
	public JPanel ccv;
	
	public ContourControlView(DICOMImage dImage)
	{
		//super(); // *
		ccv = this.panel;
	    ccv.setOpaque(true);  // * 
	    ccv.setVisible(true); // *
	    
	    
		String fileName = "Title";
		DefaultListModel<Contour> model = new DefaultListModel<Contour>();
		if(dImage != null){
			//System.out.println("DICOM NOT NULL");
			 fileName = dImage.toString();
			System.out.println(dImage.toString());
			Vector<Contour> contours = dImage.getContours();
			if(!(contours.isEmpty())){
				for(Contour x : contours){
					model.addElement(x);
					System.out.println(x);
				}
			}
			else { 
				//System.out.println("NO CONTOURS");
			}
		}
		else{  //TESTING
			
			//System.out.println("DICOM NULL");
			
			Vector<Contour> contours = new Vector<Contour>();
			contours.add(new Contour(Contour.getTypeFromInt(1)));
			contours.add(new Contour(Contour.getTypeFromInt(2)));
			if(!(contours.isEmpty())){
				for(Contour x : contours){
					model.addElement(x);
				}
			}
		} //END TESTING
		
		JList<Contour> list = new JList<Contour>(model);
		list.setVisibleRowCount(5);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setName("SCROLL PANE");
		
		ccv.setLayout(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		JLabel title = new JLabel(fileName);
		titlePanel.add(title);
		titlePanel.setName("TITLE PANEL");
		ccv.add(titlePanel, BorderLayout.NORTH);
		//ccv.add(new JButton("TEST"), BorderLayout.SOUTH);
		ccv.add(scroll, BorderLayout.CENTER);
	    
		
		ccv.setPreferredSize(new Dimension(100,200));
	    System.out.println("Set Panel Name to CCVPanel");
	    ccv.setName("CCVPanel");
	    ccv.updateUI();
	    ccv.doLayout();
		ccv.revalidate();
		ccv.repaint();
		
		
	}	
	

	
}
