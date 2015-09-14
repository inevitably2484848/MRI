package edu.auburn.cardiomri.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * This view is the bottom right panel of the main workspace window. 
 * Eventually all contour information from the main image panel will be displayed on here.
 * 	The ideal setup would be to list all the contours, and be able to show, hide, edit, and delete them easily.  
 * 
 * 
 * @author Ben Gustafson
 *
 */
public class ContourControlView extends View {
	
	/**
	 * Simply sets panel to visible for filler space
	 * 
	 */
	
	private JList list;
	private DefaultListModel listModel;
	private Contour contour;
	private DICOMImage dicom;
	private DefaultListModel model = new DefaultListModel();
	
	public ContourControlView()
	{
		super(); // *		
		model.addElement("Contours");
		JList list = new JList(model);
		list.setVisibleRowCount(5);
		
		this.panel.setLayout(new BorderLayout());

		
		JToggleButton tb1 = new JToggleButton("LANDMARK");
		JToggleButton tb2 = new JToggleButton("CONTOUR");
		
		
		JPanel titlePanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		JLabel title = new JLabel("TITLE");
		titlePanel.add(title);
		
		buttonPanel.add(tb1, BorderLayout.SOUTH);
		buttonPanel.add(tb2, BorderLayout.SOUTH);
		
		this.panel.add(titlePanel, BorderLayout.NORTH);
		this.panel.add(buttonPanel, BorderLayout.SOUTH);
		this.panel.add(centerPanel, BorderLayout.CENTER);
		this.panel.add(titlePanel, BorderLayout.NORTH);

		this.panel.add(new JScrollPane(list), BorderLayout.CENTER);
	    this.panel.setOpaque(true);  // * 
	    this.panel.setVisible(true); // *
	}

	public JPanel refreshView(Vector<Contour> contourList){
		
		String name;
		
		for(Contour x : contourList){
			name = x.toString();
			model.addElement(name);
			//System.out.println(name);
		}
		
		@SuppressWarnings("unchecked")
		JList list = new JList(model);
		list.setVisibleRowCount(5);
		this.panel.add(new JScrollPane(list));
		this.panel.setOpaque(true);  // * 
	    this.panel.setVisible(true); // *
		
		return panel;
		
	}
	

	
}
