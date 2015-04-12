package edu.auburn.cardiomri;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.auburn.cardiomri.gui.GUIController;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.MetadataModel;
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.views.GridView;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.MetadataView;
import edu.auburn.cardiomri.gui.views.StudyStructureView;

public class RunMVC {
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;

    public RunMVC() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        GUIController guiController = new GUIController();
      
        setUpStudyStructure(guiController);
        setUpGrid(guiController);
        setUpMetaData(guiController);
        setUpMainImage(guiController);
        setUpTwoChamber(guiController);
        setUpFourChamber(guiController);

        // Defining GUI
        JFrame frame = setUpFrame(guiController);

        guiController.setAppFrame(frame);
        frame.setVisible(true);
    }

    private JFrame setUpFrame(GUIController guiController) {
        JFrame frame = new JFrame("Cardio MRI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        
        frame.setLayout(new GridBagLayout());

        //Add the three buttons
        JButton newStudy = new JButton("New Study");
        newStudy.setActionCommand("Create New Study");
        newStudy.addActionListener(guiController);
        frame.add(newStudy);
        
        
        JButton existingStudy = new JButton("Existing Study");
        existingStudy.setActionCommand("Load Existing Study");
        existingStudy.addActionListener(guiController);
        frame.add(existingStudy);
        
        
        JButton singleImage = new JButton("Single Image");
        singleImage.setActionCommand("Load Single DICOM");
        singleImage.addActionListener(guiController);
        frame.add(singleImage);
        	
        return frame;
    }


    private ImageView setUpFourChamber(GUIController guiController) {
        ImageModel imageModelFourChamber = new ImageModel();
        ImageView imageViewFourChamber = new ImageView();
        imageModelFourChamber.addObserver(imageViewFourChamber);
        guiController.setImageViewerFourChamber(imageModelFourChamber,
                imageViewFourChamber);
        return imageViewFourChamber;
    }

    private ImageView setUpTwoChamber(GUIController guiController) {
        ImageModel imageModelTwoChamber = new ImageModel();
        ImageView imageViewTwoChamber = new ImageView();
        imageModelTwoChamber.addObserver(imageViewTwoChamber);
        guiController.setImageViewerTwoChamber(imageModelTwoChamber,
                imageViewTwoChamber);
        return imageViewTwoChamber;
    }

    private ImageView setUpMainImage(GUIController guiController) {
        ImageModel imageModel = new ImageModel();
        ImageView imageView = new ImageView();
        imageModel.addObserver(imageView);
        guiController.setImageViewer(imageModel, imageView);
        return imageView;
    }

    private MetadataView setUpMetaData(GUIController guiController) {
        MetadataModel metadataModel = new MetadataModel();
        MetadataView metadataView = new MetadataView();
        metadataModel.addObserver(metadataView);
        guiController.setMetadata(metadataModel, metadataView);
        // metaDataView.addController(guiController);
        return metadataView;
    }

    private GridView setUpGrid(GUIController guiController) {
        GridModel gridModel = new GridModel();
        GridView gridView = new GridView();
        gridModel.addObserver(gridView);
        guiController.setGrid(gridModel, gridView);
        // gridView.addController(guiController);
        return gridView;
    }

    private StudyStructureView setUpStudyStructure(GUIController guiController) {
        StudyStructureModel studyStructModel = new StudyStructureModel();
        StudyStructureView studyStructView = new StudyStructureView();
        studyStructModel.addObserver(studyStructView);
        guiController.setStudyStruct(studyStructModel, studyStructView);
        studyStructView.addController(guiController);
        return studyStructView;
    }
}
