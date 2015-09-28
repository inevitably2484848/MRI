package edu.auburn.cardiomri.gui.views;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.ConstructImage;
import edu.auburn.cardiomri.gui.models.GridModel;
import edu.auburn.cardiomri.gui.models.ImageModel;
import edu.auburn.cardiomri.gui.models.SelectModel;
import edu.auburn.cardiomri.gui.models.StartModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.util.ContourUtilities;
import edu.auburn.cardiomri.util.StudyUtilities;

/**
 * @author Moniz
 * 
 * This view owns and manages all other views. It houses the menu
 * as well as the various panels for the GUI.  
 * Hot Keys
 */
public class WorkspaceView extends View {
    private static final int WORKSPACE_WIDTH = 1200;
    private static final int WORKSPACE_HEIGHT = 800;
    protected JFileChooser fileChooser;
    protected JComponent mainComponent;
    protected JFrame appFrame;
    protected String studyFileName;
    public static JPanel ccvPanel;
    
    
    /**
     * Class constructor. Sets the current working directory and
     * creates the working frame.
     */
    public WorkspaceView() {
        super();
        
        fileChooser = new JFileChooser();
        fileChooser
                .setCurrentDirectory(new File(System.getProperty("user.dir")));
        createFrame();
    }
   
    /**
     * If the object parameter is a state of the WorkspaceModel, 
     * this method adds the required panels to the frame and creates
     *  the necessary views and models based on the state.
     * <p>
     * If in the START state, the start view and model are created and the
     * starting options are displayed.
     * <p>
     * If in the GROUP_SELECTION state, the select view and model are created 
     * so the user can choose which groups to display where.
     * <p>
     * If in the WORKSPACE state, the main image, two chamber, and four chamber
     * models and views are created and the appropriate images are displayed. 
     * The grid view and model are set up and all of the panes are added to the
     * frame. 
     * <p>
     * If the object is a study, setStudy() is called to update the study.
     * <p>
     * If the object is an int array, the indices within the workspace 
     * model are updated accordingly so that the various images are 
     * correctly updated.
     * 
     * @param obs  the class calling notifyObservers()
     * @param obj  the object that has been changed, thus signaling this method
     */
    public void update(Observable obs, Object obj) {
        if (obj.getClass() == State.class) {
            State currentState = (State) obj;
            if (StartModel.getLoadStudy()){
            	currentState = State.WORKSPACE;
            }
            if (currentState == State.START) {
                this.disposeFrame();
                this.createFrame();

                StartModel startModel = new StartModel();
                StartView startView = new StartView();
                startView.setModel(startModel);
                startModel.addObserver(this);

                appFrame.setSize(600, 400);
                this.appFrame.add(startView.getPanel());
                appFrame.setIconImage(new ImageIcon("res/CardiacMRI_icon.png").getImage());
                appFrame.setVisible(true);

            } else if (currentState == State.GROUP_SELECTION) {
                this.disposeFrame();
                this.createFrame();

                Study study = getWorkspaceModel().getStudy();
                SelectModel selectModel = new SelectModel(study);
                SelectView selectView = new SelectView(selectModel);
                selectView.setModel(selectModel);
                selectModel.addObserver(this);

                appFrame.setSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);
                this.appFrame.add(selectView.getPanel());
                appFrame.setVisible(true);

            } else if (currentState == State.WORKSPACE) {
                this.disposeFrame();
                this.createFrame();
                StartModel.setLoadFalse();

                Study study = getWorkspaceModel().getStudy();
                ImageModel mainImageModel, twoChamberModel, fourChamberModel;
                ImageView mainImageView, twoChamberView, fourChamberView;

                mainImageModel = new ImageModel();
                twoChamberModel = new ImageModel();
                fourChamberModel = new ImageModel();

                mainImageView = new ImageView(new ConstructImage(
                        study.getCurrentImage()));
                twoChamberView = new ImageView(new ConstructImage(
                        study.getCurrentImage()));
                fourChamberView = new ImageView(new ConstructImage(
                        study.getCurrentImage()));

                mainImageView.setModel(mainImageModel);
                twoChamberView.setModel(twoChamberModel);
                fourChamberView.setModel(fourChamberModel);

                GridModel gridModel = new GridModel();
                GridView gridView = new GridView();
                gridView.setModel(gridModel);

                GridControlView gridControl = new GridControlView();
                gridControl.setModel(gridModel);

                MultipleImageView multipleImages = new MultipleImageView();
                multipleImages.setModel(gridModel);

                ContourControlView contourControl = new ContourControlView(null);  //preBuild
                contourControl.setModel(mainImageModel);
         
                getWorkspaceModel().addImage(mainImageModel,
                        study.getShortAxisGroup());
                getWorkspaceModel().addImage(twoChamberModel,
                        study.getTwoChamberGroup());
                getWorkspaceModel().addImage(fourChamberModel,
                        study.getFourChamberGroup());

                mainImageModel.addObserver(this);
                gridModel.addObserver(this);

                gridModel.setGroup(study.getShortAxisGroup());
                gridView.setupGrid();

                getWorkspaceModel().setIndices(0, 0, 0);

                LeftPanel leftPanel = new LeftPanel(gridView, gridControl,
                        multipleImages);

                RightPanel rightPanel = new RightPanel(mainImageView,
                        twoChamberView, fourChamberView, contourControl);

                // add to appFrame
                appFrame.setSize(WORKSPACE_WIDTH, WORKSPACE_HEIGHT);

                JSplitPane allPanes = new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT, true,
                        leftPanel.getPanel(), rightPanel.getPanel());

                allPanes.setDividerLocation(WORKSPACE_WIDTH / 4);

                mainComponent = allPanes;
                this.addKeyBindings(gridView);
                
                //kw
                setMainImageView(mainImageView);
                setMenu(mainImageView);
                this.appFrame.add(mainComponent);
                appFrame.setVisible(true);
                appFrame.revalidate();  //kw
                appFrame.repaint();  //kw
            }
        } else if (obj.getClass() == Study.class) {
            getWorkspaceModel().setStudy((Study) obj);
        } else if (obj.getClass() == int[].class) {
            int[] indices = (int[]) obj;
            getWorkspaceModel().setIndices(indices[0], indices[1], indices[2]);
        } else if (obj.getClass() == ActionEvent.class){
        	this.mainComponent.getActionMap().get(((ActionEvent) obj).getActionCommand()).actionPerformed((ActionEvent) obj);
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

    /**
     * Creates the main JFrame with the appropriate title. 
     */
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
    
    /**
     * Handles action events within the workspace. Specifically,
     * saving and loading events.
     * 
     * @param e  an action event
     */
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
        } else if (actionCommand.equals("Load Existing Study")) {
        	this.loadExistingStudy();
        } else if (actionCommand.equals("Rotate Image")) {
            this.getWorkspaceModel().rotate();
        } else if (actionCommand.equals("Load Contours")) {
            try {
                this.setUpLoad();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    /**
     * Creates the main menu containing all functionality for the program and
     * sets action commands and listeners appropriately.
     * Submenus are created where needed.
     * 
     * @param mainImageView  the view in which to set up the menu
     */
    public void setMenu(ImageView mainImageView) {
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

        // ----- Add ------
        JMenu add = new JMenu("Add"); // change to add shape later?  //kw

        // Contour Submenu
        JMenu addContour = new JMenu("Add Contour");
        JMenu leftVentricle = new JMenu("LV");
        JMenu leftAtrium = new JMenu("LA");
        JMenu rightVentricle = new JMenu("RV");
        JMenu rightAtrium = new JMenu("RA");


        JMenuItem defaultType = new JMenuItem("Default");
        defaultType.setActionCommand("Default Type");
        defaultType.addActionListener(mainImageView);
        addContour.add(defaultType);
        
        JMenuItem lvEpi = new JMenuItem("Epicardial");
        lvEpi.setActionCommand("LV EPI");
        lvEpi.addActionListener(mainImageView);
        leftVentricle.add(lvEpi);
        JMenuItem lvEndo = new JMenuItem("Endocardial");
        lvEndo.setActionCommand("LV ENDO");
        lvEndo.addActionListener(mainImageView);
        leftVentricle.add(lvEndo);
        
        JMenuItem laEpi = new JMenuItem("Epicardial");
        laEpi.setActionCommand("LA EPI");
        laEpi.addActionListener(mainImageView);
        leftAtrium.add(laEpi);
        JMenuItem laEndo = new JMenuItem("Endocardial");
        laEndo.setActionCommand("LA ENDO");
        laEndo.addActionListener(mainImageView);
        leftAtrium.add(laEndo);
        
        JMenuItem rvEpi = new JMenuItem("Epicardial");
        rvEpi.setActionCommand("RV EPI");
        rvEpi.addActionListener(mainImageView);
        rightVentricle.add(rvEpi);
        JMenuItem rvEndo = new JMenuItem("Endocardial");
        rvEndo.setActionCommand("RV ENDO");
        rvEndo.addActionListener(mainImageView);
        rightVentricle.add(rvEndo);
        
        JMenuItem raEpi = new JMenuItem("Epicardial");
        raEpi.setActionCommand("RA EPI");
        raEpi.addActionListener(mainImageView);
        rightAtrium.add(raEpi);
        JMenuItem raEndo = new JMenuItem("Endocardial");
        raEndo.setActionCommand("RA ENDO");
        raEndo.addActionListener(mainImageView);
        rightAtrium.add(raEndo);
        

//        JMenuItem closedType = new JMenuItem("Closed");
//        closedType.setActionCommand("Closed Type");
//        closedType.addActionListener(mainImageView);
//        addContour.add(closedType);
//
//        JMenuItem openType = new JMenuItem("Open");
//        openType.setActionCommand("Open Type");
//        openType.addActionListener(mainImageView);
//        addContour.add(openType);
        // ----- Landmark ------
        
        JMenu landmarks = new JMenu("Add Landmark");
        
        

        JMenuItem arvLmrk = new JMenuItem("ARV");
        arvLmrk.setActionCommand("ARV");
        arvLmrk.addActionListener(mainImageView);
        arvLmrk.setToolTipText("Anterior Right Ventrical Insertion");
        landmarks.add(arvLmrk);
        
        add.add(addContour);
        add.add(landmarks);
        addContour.add(leftVentricle);
        addContour.add(leftAtrium);
        addContour.add(rightVentricle);
        addContour.add(rightAtrium);


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
        
        JMenuItem selectContour = new JMenuItem("Select Contour");
        selectContour.setActionCommand("Select Contour");
        selectContour.addActionListener(mainImageView);
        contours.add(selectContour);

        JMenuItem deleteContourAxis = new JMenuItem("Delete Contour Axis");
        deleteContourAxis.setActionCommand("Delete Contour Axis");
        deleteContourAxis.addActionListener(this);
        contours.add(deleteContourAxis);

        JMenuItem deleteContour = new JMenuItem("Delete Contour");
        deleteContour.setActionCommand("Delete Contour");
        deleteContour.addActionListener(mainImageView);
        contours.add(deleteContour);

        JMenuItem deleteAllContours = new JMenuItem("Delete All Contours");
        deleteAllContours.setActionCommand("Delete All Contours");
        deleteAllContours.addActionListener(mainImageView);
        contours.add(deleteAllContours);
        
        JMenuItem hideContour = new JMenuItem("Hide Contour");
        hideContour.setActionCommand("Hide Contour");
        hideContour.addActionListener(mainImageView);
        contours.add(hideContour);
        
        JMenuItem showContours = new JMenuItem("Show Contours");
        showContours.setActionCommand("Show Contours");
        showContours.addActionListener(mainImageView);
        contours.add(showContours);

        JMenuItem hideContours = new JMenuItem("Hide Contours");
        hideContours.setActionCommand("Hide Contours");
        hideContours.addActionListener(mainImageView);
        contours.add(hideContours);

        // ----- Rotate -----
        JMenu rotate = new JMenu("Rotate");
        JMenuItem rotateImage = new JMenuItem("Rotate Image");
        rotateImage.setActionCommand("Rotate Image");
        rotateImage.addActionListener(this);
        rotate.add(rotateImage);

        // ----- Main Menu -----
        JMenuBar menuBar = new JMenuBar();

        // Add each sub menu to the top menu Bar
        menuBar.add(fileMenu);
        menuBar.add(add);
        menuBar.add(contours);
        menuBar.add(rotate);

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
     * like to give it. Saves study as a custom .smc file.
     * 
     * @param study  the study to be saved
     */
    public void saveAsStudy(Study study) {
        JFileChooser saveFC = fileChooser;

        FileFilter studyFileFilter = new FileNameExtensionFilter(
                "Study file (.smc)", "smc");
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
     * Creates a text file in which to write the contour data for a study and
     * calls writeContoursToFile() to perform the actual writing to the file.
     */
    public void saveContour() {
        JFileChooser saveFC = fileChooser;
        
        FileFilter studyFileFilter = new FileNameExtensionFilter(
        		"Text file (.txt)", "txt");
        
        saveFC.setFileFilter(studyFileFilter);
        
        int response = saveFC.showSaveDialog(this.mainComponent);
        
        if (response == JFileChooser.APPROVE_OPTION) {
        	String newContourFileName = saveFC.getSelectedFile().getAbsolutePath();
        	
        	if (!newContourFileName.endsWith(".txt")) {
        		newContourFileName = newContourFileName.concat(".txt");
        	}
        	
        	ContourUtilities.writeContoursToFile(getWorkspaceModel().getStudy()
        			.getUIDToImage(), newContourFileName);
        	
        	// Write only control points file
        	ContourUtilities.writeContourControlPointsToFile(getWorkspaceModel().getStudy()
        			.getUIDToImage(), newContourFileName.concat(".import"));
        }
        else if (response == JFileChooser.CANCEL_OPTION) {
        }
        
    	/*String path = System.getProperty("user.dir") + File.separator
                + "contourPoints.txt";
        ContourUtilities.writeContoursToFile(getWorkspaceModel().getStudy()
                .getUIDToImage(), path);
	*/
    }


    /**
     * Opens a file chooser so the user can select a file to load and
     * sends the file to loadContour() in ContourUtilities where the 
     * actual reading of the file is performed.
     * 
     * @throws IOException
     */
    public void setUpLoad() throws IOException {
    	JFileChooser loadFC = fileChooser;
    	FileFilter studyFileFilter = new FileNameExtensionFilter(
        		"Import file (.import)", "import");
        
        loadFC.setFileFilter(studyFileFilter);
        int returnVal = fileChooser.showOpenDialog(this.mainComponent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getPath());
            getWorkspaceModel().loadContour(file, getWorkspaceModel().getStudy()
                    .getUIDToImage());
        }
    }

    /**
     * Adds common KeyBindings (Ctrl+S, Ctrl+Shift+S, Ctrl+O, etc.) to the
     * class' mainComponent attribute.
     * 
     * @param gridView  the grid to associate the arrow key bindings
     */
    private void addKeyBindings(GridView gridView) {
        // Need to map KeyBindings
    	//JComponent content = (JComponent) appFrame.getContentPane();
    	
    	InputMap inputMap = this.mainComponent.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	ActionMap actionMap = this.mainComponent.getActionMap();
    	
    	inputMap.put(KeyStroke.getKeyStroke("LEFT"),"left");
    	actionMap.put("left", gridView.new LeftKeyAction());

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"),"right");
        actionMap.put("right",gridView.new RightKeyAction());

        inputMap.put(KeyStroke.getKeyStroke("DOWN"),"down");
        actionMap.put("down",gridView.new DownKeyAction());

        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        actionMap.put("up", gridView.new UpKeyAction());

        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        
        inputMap.put(ctrlS, "save");
        actionMap.put("save", new CtrlSAction());

        KeyStroke ctrlShiftS = KeyStroke.getKeyStroke(KeyEvent.VK_S, 21);
        inputMap.put(ctrlShiftS, "save as");
        actionMap.put("save as", new CtrlShiftSAction());

        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        inputMap.put(ctrlO, "open existing");
        actionMap.put("open existing", new CtrlOAction());

        KeyStroke ctrlW = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask());
        inputMap.put(ctrlW, "close");
        actionMap.put("close", new CtrlWAction());
        
        inputMap = this.mainComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    	
    	inputMap.put(KeyStroke.getKeyStroke("LEFT"),"left");
    	
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"),"right");
       
        inputMap.put(KeyStroke.getKeyStroke("DOWN"),"down");

        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");

    }
    
    
    /**
     * 
     */
    private void loadExistingStudy() {
    	 JFileChooser loadFC = fileChooser;
    	         
    	 FileNameExtensionFilter filter1 = new FileNameExtensionFilter("SMC files", "smc");
    	         
    	 loadFC.setFileFilter(filter1);
    	         
    	 int returnVal = loadFC.showOpenDialog(this.mainComponent);
    	 String loadFileName;
    	 if (returnVal == JFileChooser.APPROVE_OPTION) {
    		 loadFileName = loadFC.getSelectedFile().getPath();
    	     this.disposeFrame();
    	     this.getWorkspaceModel().setStudy(StudyUtilities.loadStudy(loadFileName));
    	         
    	     } else if (returnVal == JFileChooser.CANCEL_OPTION) {
    	             // System.out.println("Choose to Cancel");
    	     }
    	  
    }

    /**
     * 
     */
    private void saveStudy() {
    	if (studyFileName == null){
            saveAsStudy();
    	} else {
    		StudyUtilities.saveStudy(getWorkspaceModel().getStudy(), studyFileName);
        }
    }

    /**
     * 
     */
    private void saveAsStudy() {
    	JFileChooser saveFC = fileChooser;

        FileFilter studyFileFilter = new FileNameExtensionFilter(
                "Study file (.smc)", "smc");
        saveFC.setFileFilter(studyFileFilter);

        int response = saveFC.showSaveDialog(this.mainComponent);

        if (response == JFileChooser.APPROVE_OPTION) {
            studyFileName = saveFC.getSelectedFile().getAbsolutePath();

            if (!studyFileName.endsWith(".smc")) {
                studyFileName = studyFileName.concat(".smc");
            }

            StudyUtilities.saveStudy(getWorkspaceModel().getStudy(), studyFileName);

        } else if (response == JFileChooser.CANCEL_OPTION) {
            // System.out.println("Choose to Cancel");
        }

    }

    
    /**
     * Various action classes for the key bindings. 
     */
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
    
    //-------------------------------------------------------------------------
    //KW
    
    public static ImageView imageView;
    
    //set and get MainImageView()
    public static void setMainImageView(ImageView imageViewIN){
    	imageView = imageViewIN;
    }
    public static ImageView getMainImageView(){
    	return imageView;
    }

     
    
}
