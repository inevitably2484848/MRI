package edu.auburn.cardiomri;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import edu.auburn.cardiomri.gui.*;

public class RunMVC {
	
	public RunMVC() {
		
		GUIController guiController = new GUIController();
		
		// StudyStructure bit
		StudyStructureModel studyStructModel = new StudyStructureModel();
		StudyStructureView studyStructView = new StudyStructureView();
		
		studyStructModel.addObserver(studyStructView);
		
		guiController.setStudyStruct(studyStructModel, studyStructView);
		
		studyStructView.addController(guiController);
		
		// Grid bit
		GridModel gridModel = new GridModel();
		GridView gridView = new GridView();
		
		gridModel.addObserver(gridView);
		
		guiController.setGrid(gridModel, gridView);
		
		//gridView.addController(guiController);
		
		// MetaData bit
		MetaDataModel metaDataModel = new MetaDataModel();
		MetaDataView metaDataView = new MetaDataView();
		
		studyStructModel.addObserver(studyStructView);
		metaDataModel.addObserver(metaDataView);
		
		guiController.setMetaData(metaDataModel, metaDataView);
		
		//metaDataView.addController(guiController);
		
		// Image bit
		ImageModel imageModel = new ImageModel();
		ImageView imageView = new ImageView();
		
		imageModel.addObserver(imageView);
		
		guiController.setImageViewer(imageModel, imageView);
		
		//imageView.addController(guiController);
		
		// Defining GUI
		JFrame frame = new JFrame("Cardio MRI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 600);
		
		
		
		JSplitPane structAndGridPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				studyStructView.getPanel(), gridView.getPanel());
		structAndGridPane.setDividerLocation(270);
		
		// set the size of the grid view
		gridView.setSize(new Dimension(300, 200));
		
		JSplitPane structGridAndAttr = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				structAndGridPane, metaDataView.getPanel());
		structGridAndAttr.setDividerLocation(200);
		
		JSplitPane splitPaneWithImage = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				structGridAndAttr, imageView.getPanel());
		splitPaneWithImage.setDividerLocation(600);
		
		frame.add(splitPaneWithImage);
		
		guiController.setMainComponent(splitPaneWithImage);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		// New menu
		JMenu newMenu = new JMenu("New Study");
		
			JMenuItem newFromSingle = new JMenuItem("From Single DICOM");
			newFromSingle.setActionCommand("Load Single DICOM");
			newFromSingle.addActionListener(guiController);
			
			JMenuItem newFromFileStruct = new JMenuItem("From File Structure");
			newFromFileStruct.setActionCommand("Create New Study");
			newFromFileStruct.addActionListener(guiController);
			
			newMenu.add(newFromSingle);
			newMenu.add(newFromFileStruct);
		
		// Open study
		JMenuItem openExisting = new JMenuItem("Open Existing (Ctrl+O)");
		openExisting.setActionCommand("Load Existing Study");
		openExisting.addActionListener(guiController);
		
		// Save
		JMenuItem saveStudy = new JMenuItem("Save (Ctrl+S)");
		saveStudy.setActionCommand("Save Study");
		saveStudy.addActionListener(guiController);
		
		JMenuItem saveAsStudy = new JMenuItem("Save (Ctrl+Shift+S)");
		saveAsStudy.setActionCommand("Save As Study");
		saveAsStudy.addActionListener(guiController);
		
		// Import
		JMenuItem importDicom = new JMenuItem("Import DICOM");
		importDicom.setActionCommand("Import DICOM");
		importDicom.addActionListener(guiController);
		
		
		fileMenu.add(newMenu);
		fileMenu.add(openExisting);
		fileMenu.add(saveStudy);
		fileMenu.add(saveAsStudy);
		fileMenu.add(importDicom);
		
		guiController.setAppFrame(frame);
		
		JPanel glass = new JPanel(new GridLayout(0, 1));
		glass.setOpaque(false);
		glass.setSize(imageView.getPanel().getSize());
		glass.setLocation(imageView.getPanel().getLocation());
		glass.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			     {
			          System.out.println("clicked 1");
			          Point glassPoint = e.getPoint();
			          Component component_under_glass = SwingUtilities.getDeepestComponentAt(frame.getContentPane(), glassPoint.x, glassPoint.y);
			          System.out.println(component_under_glass.toString());
			          Point component_point = SwingUtilities.convertPoint(glass, glassPoint, component_under_glass);
			          if (component_under_glass instanceof JButton || component_under_glass instanceof JMenuItem  ){
			        	  System.out.println("processed click");
			        	  ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), e.paramString());
			        	  guiController.actionPerformed(ae);
			          }
			          guiController.mouseClicked(e);
			     }
		});
	    //frame.setGlassPane(glass);
	    glass.setVisible(true);
		
		frame.setVisible(true);
	}
	
}
