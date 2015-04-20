package edu.auburn.cardiomri;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.StartModel;
import edu.auburn.cardiomri.gui.models.StudyStructureModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.gui.views.StartView;
import edu.auburn.cardiomri.gui.views.StudyStructureView;
import edu.auburn.cardiomri.gui.views.WorkspaceView;

public class RunMVC {
    protected WorkspaceView workspaceView;
    protected WorkspaceModel workspaceModel;
    protected StartView startView;
    protected StartModel startModel;
    protected Study study;

    public RunMVC() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Make app frame

        // set up main workspace view
        workspaceModel = new WorkspaceModel();
        workspaceView = new WorkspaceView();
        workspaceView.setModel(workspaceModel);
        workspaceModel.setCurrentState(State.START);

        // Set up view for first three buttons
<<<<<<< HEAD

        // hand over to workspaceView
    }

    public void setStudy(Study study) {
        this.study = study;
        // TODO: Check to see if it is a single dicom, new study, or current
        // study
        // Logic to do what is needed there
        // Assume it is a current study

        // If it is a new study
        StudyStructureModel studyStructModel = new StudyStructureModel();
        StudyStructureView studyStructView = new StudyStructureView();
=======
        startModel = new StartModel();
        startModel.setMVC(this);
        startView = new StartView();
        startView.setModel(startModel);
        
        // Set up select view
        
        
        // hand over to workspaceView 
        JFrame frame = new JFrame("Cardio MRI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        workspaceView.setAppFrame(frame);
        frame.setVisible(true);
        workspaceView.addView(startView);
    }
    
    public void setStudy(Study study)
    {
    	this.study = study;
    	//TODO: Check to see if it is a single dicom, new study, or current study
    	//Logic to do what is needed there
    	//Assume it is a current study
    		try{
	    		GridModel gridModel = new GridModel();
		        GridView gridView = new GridView();
		        gridView.setModel(gridModel);
		        
		        ImageModel imageModel = new ImageModel();
		        ConstructImage sImg = new ConstructImage(this.study.getGroups().get(0).getSlices().get(0).getTimes().get(0).getImages().get(0));
		        ImageView imageView = new ImageView(sImg);
		        imageView.setModel(imageModel);
		        
		        gridModel.addObserver(gridView);
		        imageModel.addObserver(imageView);
		        
		        gridModel.setImageModel(imageModel);
		        workspaceView.setImageModel(imageModel);
		        
		        workspaceView.deleteView();
		        JFrame frame = new JFrame("Cardio MRI");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		        workspaceView.setAppFrame(frame);
		        frame.setVisible(true);
		        
		    	JSplitPane allPanes = new JSplitPane(
			            JSplitPane.HORIZONTAL_SPLIT,true, gridView.getPanel(),imageView.getPanel());
		        
			    allPanes.setDividerLocation(FRAME_WIDTH/4);
		        
		        workspaceView.add(allPanes);
		        
	        }
	        catch(Exception e)
	        {
	        	
	        }
    	
    	//If it is a new study
    	SelectModel studyStructModel = new SelectModel();
        SelectView studyStructView = new SelectView();
>>>>>>> BenGustafson/justin
        studyStructModel.addObserver(studyStructView);
    }
}
