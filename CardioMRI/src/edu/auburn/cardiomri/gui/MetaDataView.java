package edu.auburn.cardiomri.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class MetaDataView implements java.util.Observer {
	
	private JPanel panel;
	private JTable attrTable;
	
	// Observer methods
	public void update(Observable obs, Object obj) {
		if (obj.getClass() == Study.class) {
//System.out.println("MetaDataView : updated with Study");
			
			this.panel.removeAll();
			
			this.panel.revalidate();
		}
		if (obj.getClass() == DICOMImage.class) {
//System.out.println("MetaDataView : updated with DICOMImage");
			
			this.panel.removeAll();
			
			
			String[] columnNames = {"Tag Names", "Value"};
		
			String[][] data = ((DICOMImage)obj).getAttributeInfo();
			
			attrTable = new JTable(data, columnNames);
			attrTable.setBorder(new EtchedBorder(EtchedBorder.RAISED));
			attrTable.setGridColor(Color.GRAY);
	        
			Dimension size = new Dimension(550, 300);
			attrTable.setPreferredScrollableViewportSize(size);
	        
	        JScrollPane tableContainer = new JScrollPane(attrTable);

	        this.panel.add(tableContainer, BorderLayout.CENTER);

			this.panel.revalidate();
		}
		
	}
	
	// Getters
	/*
	 * Returns the class' panel attribute.
	 * 
	 *  @return		The class' panel attribute.
	 */
	public JPanel getPanel() {
		
		return this.panel;
	}
	
	// Constructor
	public MetaDataView() {
//System.out.println("MetaDataView()");
		
		this.panel = new JPanel();
		this.panel.setName("Meta Data");
		this.panel.add("North", new Label("No DICOM has been imported"));
		
	}
	
}
