/**
 * 
 */
package edu.auburn.cardiomri.gui.views;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.auburn.cardiomri.dataimporter.DICOM3Importer;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Slice;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Study.NotInStudyException;
import edu.auburn.cardiomri.datastructure.Time;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.StartModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.util.SerializationManager;

/**
 * @author Moniz
 *
 */
public class WorkspaceView extends View {
    private static final int WORKSPACE_WIDTH = 1200;
    private static final int WORKSPACE_HEIGHT = 800;
    protected JFileChooser fileChooser;
    protected JComponent mainComponent;
    protected JFrame appFrame;
    protected GridView gridView;
    protected ImageModel mainImageModel, twoChamberModel, fourChamberModel;
    protected ImageView  mainImageView, twoChamberView, fourChamberView;

    public WorkspaceView() {
        super();
        fileChooser = new JFileChooser();
        fileChooser
                .setCurrentDirectory(new File(System.getProperty("user.dir")));
        createFrame();
    }

    public void update(Observable obs, Object obj) {
        if (obj.getClass() == State.class) {
            State currentState = (State) obj;
            if (currentState == State.START) {
                this.disposeFrame();
                this.createFrame();
                
                StartModel startModel = new StartModel();
                StartView startView = new StartView();
                startView.setModel(startModel);
                startModel.addObserver(this);
                
                
                appFrame.setSize(600, 400);
                this.appFrame.add(startView.getPanel());
                appFrame.setVisible(true);
                
            } else if (currentState == State.GROUP_SELECTION) {
            	this.disposeFrame();
                this.createFrame();
                
            	Study study = getWorkspaceModel().getStudy();
            	//make a select view 
            	//make a select model
            	//view.setModel(model) 
            	//send study to model
            	//model will update view
            	
            	//selectModel.addObserver(this);
            	
            	appFrame.setSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
                //this.appFrame.add(selectView.getPanel());
                appFrame.setVisible(true);
                
            } else if (currentState == State.WORKSPACE) {
                this.disposeFrame();
                this.createFrame();

                Study study = getWorkspaceModel().getStudy();

                this.mainImageModel = new ImageModel();
                ConstructImage sImg = new ConstructImage(study.getCurrentImage());
                this.mainImageView = new ImageView(sImg);
                mainImageView.setModel(mainImageModel);

                GridModel gridModel = new GridModel();
                GridView gridView = new GridView(study.getShortAxisGroup()); //TODO: Change the implementation of getSAGroup 
                gridView.setModel(gridModel);
                
                mainImageModel.addObserver(this);
                gridModel.addObserver(this);
                
                //Setup the left panel
                GridControlView gridControl = new GridControlView();
                gridControl.setModel(gridModel);
                
                MultipleImageView multipleImages = new MultipleImageView();
                multipleImages.setModel(gridModel);
                
                LeftPanel leftPanel = new LeftPanel(gridView, gridControl, multipleImages, WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
                                
                //Setup the right panel
                this.twoChamberModel = new ImageModel();
                ConstructImage twoChambersImg = new ConstructImage(study.getCurrentImage()); //TODO: study.getTwoChamberImage()
                ImageView twoChamberView = new ImageView(twoChambersImg);
                twoChamberView.setModel(twoChamberModel);
                
                this.fourChamberModel = new ImageModel();
                ConstructImage fourChambersImg = new ConstructImage(study.getCurrentImage()); //TODO: study.getTwoChamberImage()
                ImageView fourChamberView = new ImageView(fourChambersImg);
                fourChamberView.setModel(fourChamberModel);
                
                ContourControlView contourControl = new ContourControlView();
                contourControl.setModel(mainImageModel);
                
                RightPanel rightPanel = new RightPanel(mainImageView, twoChamberView, fourChamberView, contourControl, WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
                
                //add to appFrame
                appFrame.setSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);

                JSplitPane allPanes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        true, leftPanel.getPanel(), rightPanel.getPanel());

                allPanes.setDividerLocation(WORKSPACE_WIDTH / 4);

                this.appFrame.add(allPanes);
                setMenu();
                appFrame.setVisible(true);
            }
        } else if (obj.getClass() == Study.class) {
            getWorkspaceModel().setStudy((Study) obj);
        }
    }

    /**
     * Sets the class' appFrame attribute.
     *
     * @param f : JFrame object that will be used as the class' appFrame
     *            attribute.
     */
    public void setAppFrame(JFrame f) {
        this.appFrame = f;
    }

    public void createFrame() {
        this.appFrame = new JFrame("Cardio MRI");
        this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void disposeFrame() {
        // Removes everthing from frame
        this.appFrame.dispose();
    }

    /**
     * Sets the class' mainComponent attribute. The class' KeyBindings will be
     * attached to the mainComponent.
     * 
     * @param c : JComponent object will be used as the class' mainComponent
     *            attribute.
     */
    public void setMainComponent(JComponent c) {
        this.mainComponent = c;

        this.addKeyBindings();
    }

    /**
     * Returns the class' mainComponent attribute.
     *
     * @return The GUIController's mainComponent attribute.
     */
    public JComponent getMainComponent() {
        return this.mainComponent;
    }

    public WorkspaceModel getWorkspaceModel() {
        return (WorkspaceModel) this.model;
    }

    // ActionListener methods
    public void actionPerformed(java.awt.event.ActionEvent e) {

        String actionCommand = e.getActionCommand();

        // System.out.println("GUIController : actionPerformed - " +
        // actionCommand);

        if (actionCommand.equals("Save Study")) {
            this.saveStudy();
        } else if (actionCommand.equals("Save As Study")) {
            this.saveAsStudy();
        } else if (actionCommand.equals("Save Contours")) {
            this.saveContour();
        } else if (actionCommand.equals("Load Contours")) {
            try {
                this.setUpLoad();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (actionCommand.equals("Default Type")) {
            this.mainImageModel.addContourToImage(new Contour(Type.DEFAULT));
        } else if (actionCommand.equals("Closed Type")) {
            this.mainImageModel.addContourToImage(new Contour(Type.DEFAULT_CLOSED));
        } else if (actionCommand.equals("Open Type")) {
            this.mainImageModel.addContourToImage(new Contour(Type.DEFAULT_OPEN));
        } else if (actionCommand.equals("Delete All Contours")) {
            this.deleteAllContoursForImage();
        } else if (actionCommand.equals("Delete Selected Contour")) {
            this.deleteSelectedContour();
        } else if (actionCommand.equals("Hide Selected Contour")) {
            this.hideSelectedContour();
        } else if (actionCommand.equals("Hide Contours")) {
            this.hideContours();
        } else if (actionCommand.equals("Delete All Contours")) {
            this.deleteAllContoursForImage();
        } else if (actionCommand.equals("Show Contours")) {
            this.showContours();
        }
    }

    public void setMenu() {
        // -------------------- Menu Bar -------------------------------

        // ----- File -------
        JMenu fileMenu = new JMenu("File");

        // New Submenu
        JMenu newMenu = new JMenu("New Study");

        JMenuItem newFromSingle = new JMenuItem("From Single DICOM");
        newFromSingle.setActionCommand("Load Single DICOM");
        newFromSingle.addActionListener(this);
        newMenu.add(newFromSingle);

        JMenuItem newFromFileStruct = new JMenuItem("From File Structure");
        newFromFileStruct.setActionCommand("Create New Study");
        newFromFileStruct.addActionListener(this);
        newMenu.add(newFromFileStruct);

        fileMenu.add(newMenu);

        JMenuItem openExisting = new JMenuItem("Open Existing (Ctrl+O)");
        openExisting.setActionCommand("Load Existing Study");
        openExisting.addActionListener(this);
        fileMenu.add(openExisting);

        JMenuItem saveStudy = new JMenuItem("Save (Ctrl+S)");
        saveStudy.setActionCommand("Save Study");
        saveStudy.addActionListener(this);
        fileMenu.add(saveStudy);

        JMenuItem saveAsStudy = new JMenuItem("Save as (Ctrl+Shift+S)");
        saveAsStudy.setActionCommand("Save As Study");
        saveAsStudy.addActionListener(this);
        fileMenu.add(saveAsStudy);

        JMenuItem importDicom = new JMenuItem("Import DICOM");
        importDicom.setActionCommand("Import DICOM");
        importDicom.addActionListener(this);
        fileMenu.add(importDicom);

        // ----- Add ------
        JMenu add = new JMenu("Add"); // change to add shape later?

        // Contour Submenu
        JMenu addContour = new JMenu("Add Contour");

        JMenuItem defaultType = new JMenuItem("Default");
        defaultType.setActionCommand("Default Type");
        defaultType.addActionListener(this);
        addContour.add(defaultType);

        JMenuItem closedType = new JMenuItem("Closed");
        closedType.setActionCommand("Closed Type");
        closedType.addActionListener(this);
        addContour.add(closedType);

        JMenuItem openType = new JMenuItem("Open");
        openType.setActionCommand("Open Type");
        openType.addActionListener(this);
        addContour.add(openType);
        add.add(addContour);

        // ----- Contour ------
        JMenu contours = new JMenu("Contours");
        JMenuItem saveContours = new JMenuItem("Save Contours (.txt File)");
        saveContours.setActionCommand("Save Contours");
        saveContours.addActionListener(this);
        contours.add(saveContours);

        JMenuItem loadContours = new JMenuItem("Load Contours");
        loadContours.setActionCommand("Load Contours");
        loadContours.addActionListener(this);
        contours.add(loadContours);

        JMenuItem deleteContourAxis = new JMenuItem("Delete Contour Axis");
        deleteContourAxis.setActionCommand("Delete Contour Axis");
        deleteContourAxis.addActionListener(this);
        contours.add(deleteContourAxis);

        JMenuItem deleteContour = new JMenuItem("Delete Contour");
        deleteContour.setActionCommand("Delete Contour");
        deleteContour.addActionListener(this);
        contours.add(deleteContour);

        JMenuItem deleteAllContours = new JMenuItem("Delete All Contours");
        deleteAllContours.setActionCommand("Delete All Contours");
        deleteAllContours.addActionListener(this);
        contours.add(deleteAllContours);

        // ----- View -----
        JMenu view = new JMenu("View");
        JMenuItem zoom = new JMenuItem("Zoom");
        view.add(zoom);

        // ----- Main Menu -----
        JMenuBar menuBar = new JMenuBar();

        // Add each sub menu to the top menu Bar
        menuBar.add(fileMenu);
        menuBar.add(add);
        menuBar.add(contours);
        menuBar.add(view);

        appFrame.setJMenuBar(menuBar);
        appFrame.revalidate();
        appFrame.repaint();
    }

    /**
     * Method that will close the GUIController's applicationFrame variable.
     */
    private void closeWindow() {
        // System.out.println("Close Window");
        this.appFrame.dispatchEvent(new WindowEvent(this.appFrame,
                WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Opens a JFileChooser that allows the user to select where they would like
     * to save the currently displayed Study object and what name they would
     * like to give it.
     */
    public void saveAsStudy(Study study) {
        JFileChooser saveFC = fileChooser;

        FileFilter studyFileFilter = new FileNameExtensionFilter("Study file (.smc)", "smc");
        saveFC.setFileFilter(studyFileFilter);

        int response = saveFC.showSaveDialog(this.mainComponent);

        if (response == JFileChooser.APPROVE_OPTION) {
            String newFilename = saveFC.getSelectedFile().getAbsolutePath();
            
            if (!newFilename.endsWith(".smc")) {
                newFilename = newFilename.concat(".smc");
            }
            
            this.getWorkspaceModel().saveStudy(newFilename);
            
        } else if (response == JFileChooser.CANCEL_OPTION) {
            // System.out.println("Choose to Cancel");
        }
    }



    /**
     * deletes all of the Contours for the displayed image
     */
    private void deleteAllContoursForImage() {
//        DICOMImage dImage = this.studyStructModel.getImage();
//        dImage.getContours().clear();
    }

    /**
     * Saves current image's contour lines into a .txt file containing a header
     * and the X and Y coordinates of all the points along the contour
     * 
     * @param contour : Contour object to be saved
     */

    public void saveContour() {
//        String path = System.getProperty("user.dir") + File.separator
//                + "contourPoints.txt";
//        writeContoursToFile(this.studyStructModel.getStudy()
//                .getUIDToImage(), path);

    }

    /**
     * reads in a text file containing lists of coordinates for Contour objects
     * for one or more images and assigns the Contours to their corresponding
     * images.
     * 
     * @throws IOException
     */

    public void setUpLoad() throws IOException {
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int returnVal = fileChooser.showOpenDialog(this.mainComponent);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = new File(fileChooser.getSelectedFile().getPath());
//            loadContour(file, this.studyStructModel.getStudy()
//                    .getUIDToImage());
//        }
    }

    private void hideSelectedContour() {
//        DICOMImage dImage = this.studyStructModel.getImage();
//        Contour selected = this.mainImageModel.getSelectedContour();
//        dImage.getContours().remove(selected);
//        this.mainImageModel.getHiddenContours().add(selected);

    }

    private void hideContours() {
//        DICOMImage dImage = this.studyStructModel.getImage();
//        this.mainImageModel.getHiddenContours().addAll(dImage.getContours());
//        dImage.getContours().clear();
    }

    private void showContours() {
//        if (this.mainImageModel.getHiddenContours().size() != 0) {
//            DICOMImage dImage = this.studyStructModel.getImage();
//            dImage.getContours().addAll(
//                    (Vector<Contour>) this.mainImageModel.getHiddenContours());
//            this.mainImageModel.getHiddenContours().clear();
//        }
    }

    private void deleteSelectedContour() {
//        DICOMImage dImage = this.studyStructModel.getImage();
//        dImage.getContours().remove(this.mainImageModel.getSelectedContour());
    }

    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView
    // TODO: move key binding to GridView

    /**
     * Adds common KeyBindings (Ctrl+S, Ctrl+Shift+S, Ctrl+O, etc.) to the
     * class' mainComponent attribute.
     */
    private void addKeyBindings() {
        // Need to map KeyBindings
        this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("LEFT"),
                "left");
        this.mainComponent.getActionMap().put("left", new LeftKeyAction());

        this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"),
                "right");
        this.mainComponent.getActionMap().put("right", new RightKeyAction());

        this.mainComponent.getInputMap().put(KeyStroke.getKeyStroke("DOWN"),
                "down");
        this.mainComponent.getActionMap().put("down", new DownKeyAction());

        this.mainComponent.getInputMap()
                .put(KeyStroke.getKeyStroke("UP"), "up");
        this.mainComponent.getActionMap().put("up", new UpKeyAction());

        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        this.mainComponent.getInputMap().put(ctrlS, "save");
        this.mainComponent.getActionMap().put("save", new CtrlSAction());

        KeyStroke ctrlShiftS = KeyStroke.getKeyStroke(KeyEvent.VK_S, 21);
        this.mainComponent.getInputMap().put(ctrlShiftS, "save as");
        this.mainComponent.getActionMap()
                .put("save as", new CtrlShiftSAction());

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        this.mainComponent.getInputMap().put(ctrlO, "open existing");
        this.mainComponent.getActionMap().put("open existing",
                new CtrlOAction());

        KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        this.mainComponent.getInputMap().put(ctrlW, "close");
        this.mainComponent.getActionMap().put("close", new CtrlWAction());
    }

    /**
     * Decrements the current time index and updates the models.
     */
    private void decrementTimeIndex() {
//        // Check to see if it is possible
//        if (this.studyStructModel.getStudy() == null) {
//            return;
//        }
//        if ((this.tIndex - 1) >= 0) {
//            // then decrement
//            // System.out.println("GUIController : decrement time index");
//            this.tIndex--;
//            this.updateNewDicom();
//        } else {
//            this.tIndex = this.studyStructModel.getStudy().getGroups()
//                    .get(gIndex).getSlices().get(sIndex).getTimes().size() - 1;
//        }
    }

    /**
     * Increments the current time index and updates the models.
     */
    private void incrementTimeIndex() {
        // Check to see if it is possible
//        if (this.studyStructModel.getStudy() == null) {
//            return;
//        }
//        ArrayList<Time> curTimes = this.studyStructModel.getStudy().getGroups()
//                .get(gIndex).getSlices().get(sIndex).getTimes();
//        if ((this.tIndex + 1) < curTimes.size()) {
//            // then increment
//            // System.out.println("GUIController : increment time index");
//            this.tIndex++;
//            this.updateNewDicom();
//        } else {
//            // Loop
//            this.tIndex = 0;
//        }
    }

    /**
     * Decrements the current slice index and updates the models.
     */
    private void decrementSliceIndex() {
        // Check to see if it is possible
//        if (this.studyStructModel.getStudy() == null) {
//            return;
//        }
//        if (this.sIndex > 0) {
//            // then increment
//            // System.out.println("GUIController : decrement slice index");
//            this.sIndex--;
//            this.updateNewDicom();
//        } else {
//            // System.out.println("GUIController : failed attempt to decrement slice");
//        }
    }

    /**
     * Increments the current slice index and updates the models.
     */
    private void incrementSliceIndex() {
        // Check to see if it is possible
//        if (this.studyStructModel.getStudy() == null) {
//            return;
//        }
//        ArrayList<Slice> curSlices = this.studyStructModel.getStudy()
//                .getGroups().get(gIndex).getSlices();
//        if ((this.sIndex + 1) < curSlices.size()) {
//            // then increment
//            // System.out.println("GUIController : increment slice index " +
//            // curSlices.size());
//            this.sIndex++;
//            this.updateNewDicom();
//        } else {
//            // System.out.println("GUIController : failed attempt to increment slice");
//        }
    }

    /**
     * 
     */
    private void loadExistingStudy() {
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    private void saveStudy() {
        // TODO Auto-generated method stub

    }

    /**
     * 
     */
    private void saveAsStudy() {
        // TODO Auto-generated method stub

    }

    // Action classes
    private class LeftKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6612132766001531904L;

        public void actionPerformed(ActionEvent e) {
            decrementTimeIndex();
        }
    }

    private class RightKeyAction extends AbstractAction {
        private static final long serialVersionUID = 6824940022077838332L;

        public void actionPerformed(ActionEvent e) {
            incrementTimeIndex();
        }
    }

    private class UpKeyAction extends AbstractAction {
        private static final long serialVersionUID = 4942341424740412096L;

        public void actionPerformed(ActionEvent e) {
            decrementSliceIndex();
        }
    }

    private class DownKeyAction extends AbstractAction {
        private static final long serialVersionUID = -7183889255252949565L;

        public void actionPerformed(ActionEvent e) {
            incrementSliceIndex();
        }
    }

    private class CtrlSAction extends AbstractAction {
        private static final long serialVersionUID = 8688937617331716060L;

        public void actionPerformed(ActionEvent e) {
            saveStudy();
        }
    }

    private class CtrlShiftSAction extends AbstractAction {
        private static final long serialVersionUID = -2543127413774012188L;

        public void actionPerformed(ActionEvent e) {
            saveAsStudy();
        }
    }

    private class CtrlOAction extends AbstractAction {
        private static final long serialVersionUID = 764073195702399357L;

        public void actionPerformed(ActionEvent e) {
            loadExistingStudy();
        }
    }

    private class CtrlWAction extends AbstractAction {
        private static final long serialVersionUID = 8234348834457453793L;

        public void actionPerformed(ActionEvent e) {
            closeWindow();
        }
    }
}
