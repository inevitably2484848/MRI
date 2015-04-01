package edu.auburn.cardiomri;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import edu.auburn.cardiomri.gui.GUIController;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.MetaDataModel;
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.views.GridView;
import edu.auburn.cardiomri.gui.views.ImageView;
import edu.auburn.cardiomri.gui.views.MetaDataView;
import edu.auburn.cardiomri.gui.views.StudyStructureView;

public class RunMVC {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 600;

    public RunMVC() {
        GUIController guiController = new GUIController();

        setUpStudyStructure(guiController);
        setUpGrid(guiController);
        setUpMetaData(guiController);
        setUpMainImage(guiController);
        setUpSmallLeftImage(guiController);
        setUpSmallRightImage(guiController);

        // Defining GUI
        JFrame frame = setUpFrame(guiController);
        JMenuBar menuBar = setUpMenuBar(guiController);
        frame.setJMenuBar(menuBar);

        guiController.setAppFrame(frame);
        frame.setVisible(true);
    }

    private JFrame setUpFrame(GUIController guiController) {
        JFrame frame = new JFrame("Cardio MRI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        JSplitPane structAndGridPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, guiController.getStudyStructView()
                        .getPanel(), guiController.getGridView().getPanel());
        JSplitPane smallImagesPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, guiController
                        .getImageViewSmallLeft().getPanel(), guiController
                        .getImageViewSmallRight().getPanel());
        JSplitPane structAndSmallImagesPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, structAndGridPane, smallImagesPane);
        JSplitPane structAndSmallImagesAndMainImagePane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, structAndSmallImagesPane,
                guiController.getImageView().getPanel());

        structAndSmallImagesAndMainImagePane.setDividerLocation(frame
                .getWidth() / 2);
        structAndSmallImagesPane.setDividerLocation(frame.getHeight() / 2);
        structAndGridPane.setDividerLocation(frame.getWidth() / 4);
        smallImagesPane.setDividerLocation(frame.getWidth() / 4);

        frame.add(structAndSmallImagesAndMainImagePane);
        guiController.setMainComponent(structAndSmallImagesAndMainImagePane);
        return frame;
    }

    private JMenuBar setUpMenuBar(GUIController guiController) {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = setUpFileMenu(guiController);
        JMenu add = setUpAddMenu(guiController);
        JMenu view = setUpViewMenu();
        JMenu contours = setUpContoursMenu(guiController);

        menuBar.add(file);
        menuBar.add(add);
        menuBar.add(contours);
        menuBar.add(view);
        return menuBar;
    }

    private JMenu setUpContoursMenu(GUIController guiController) {
        JMenu contours = new JMenu("Contours");
        JMenuItem saveContours = new JMenuItem("Save Contours (.txt File)");
        saveContours.setActionCommand("Save Contours");
        saveContours.addActionListener(guiController);

        JMenuItem loadContours = new JMenuItem("Load Contours");
        loadContours.setActionCommand("Load Contours");
        loadContours.addActionListener(guiController);

        JMenuItem deleteContourAxis = new JMenuItem("Delete Contour Axis");
        deleteContourAxis.setActionCommand("Delete Contour Axis");
        deleteContourAxis.addActionListener(guiController);

        JMenuItem deleteContour = new JMenuItem("Delete Contour");
        deleteContour.setActionCommand("Delete Contour");
        deleteContour.addActionListener(guiController);

        JMenuItem deleteAllContours = new JMenuItem("Delete All Contours");
        deleteAllContours.setActionCommand("Delete All Contours");
        deleteAllContours.addActionListener(guiController);

        contours.add(saveContours);
        contours.add(loadContours);
        contours.add(deleteContourAxis);
        contours.add(deleteContour);
        contours.add(deleteAllContours);
        return contours;
    }

    private JMenu setUpViewMenu() {
        JMenu view = new JMenu("View");
        JMenuItem zoom = new JMenuItem("Zoom");
        view.add(zoom);
        return view;
    }

    private JMenu setUpAddMenu(GUIController guiController) {
        JMenu add = new JMenu("Add"); // change to add shape later?
        JMenu addContour = setUpAddContourMenu(guiController);
        add.add(addContour);
        return add;
    }

    private JMenu setUpAddContourMenu(GUIController guiController) {
        JMenu addContour = new JMenu("Add Contour");

        JMenuItem defaultType = new JMenuItem("Default");
        defaultType.setActionCommand("Default Type");
        defaultType.addActionListener(guiController);
        addContour.add(defaultType);

        JMenuItem closedType = new JMenuItem("Closed");
        closedType.setActionCommand("Closed Type");
        closedType.addActionListener(guiController);
        addContour.add(closedType);

        JMenuItem openType = new JMenuItem("Open");
        openType.setActionCommand("Open Type");
        openType.addActionListener(guiController);
        addContour.add(openType);
        return addContour;
    }

    private JMenu setUpFileMenu(GUIController guiController) {
        JMenu fileMenu = new JMenu("File");

        // New menu
        JMenu newMenu = setUpNewMenu(guiController);

        // Open study
        JMenuItem openExisting = new JMenuItem("Open Existing (Ctrl+O)");
        openExisting.setActionCommand("Load Existing Study");
        openExisting.addActionListener(guiController);

        // Save
        JMenuItem saveStudy = new JMenuItem("Save (Ctrl+S)");
        saveStudy.setActionCommand("Save Study");
        saveStudy.addActionListener(guiController);

        JMenuItem saveAsStudy = new JMenuItem("Save as (Ctrl+Shift+S)");
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
        return fileMenu;
    }

    private JMenu setUpNewMenu(GUIController guiController) {
        JMenu newMenu = new JMenu("New Study");

        JMenuItem newFromSingle = new JMenuItem("From Single DICOM");
        newFromSingle.setActionCommand("Load Single DICOM");
        newFromSingle.addActionListener(guiController);

        JMenuItem newFromFileStruct = new JMenuItem("From File Structure");
        newFromFileStruct.setActionCommand("Create New Study");
        newFromFileStruct.addActionListener(guiController);

        newMenu.add(newFromSingle);
        newMenu.add(newFromFileStruct);
        return newMenu;
    }

    private ImageView setUpSmallRightImage(GUIController guiController) {
        ImageModel imageModelSmallRight = new ImageModel();
        ImageView imageViewSmallRight = new ImageView();
        imageModelSmallRight.addObserver(imageViewSmallRight);
        guiController.setImageViewerSmallRight(imageModelSmallRight,
                imageViewSmallRight);
        return imageViewSmallRight;
    }

    private ImageView setUpSmallLeftImage(GUIController guiController) {
        ImageModel imageModelSmallLeft = new ImageModel();
        ImageView imageViewSmallLeft = new ImageView();
        imageModelSmallLeft.addObserver(imageViewSmallLeft);
        guiController.setImageViewerSmallLeft(imageModelSmallLeft,
                imageViewSmallLeft);
        return imageViewSmallLeft;
    }

    private ImageView setUpMainImage(GUIController guiController) {
        ImageModel imageModel = new ImageModel();
        ImageView imageView = new ImageView();
        imageModel.addObserver(imageView);
        guiController.setImageViewer(imageModel, imageView);
        return imageView;
    }

    private MetaDataView setUpMetaData(GUIController guiController) {
        MetaDataModel metaDataModel = new MetaDataModel();
        MetaDataView metaDataView = new MetaDataView();
        metaDataModel.addObserver(metaDataView);
        guiController.setMetaData(metaDataModel, metaDataView);
        // metaDataView.addController(guiController);
        return metaDataView;
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
