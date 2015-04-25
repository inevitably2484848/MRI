package edu.auburn.cardiomri;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.auburn.cardiomri.gui.models.WorkspaceModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.gui.views.WorkspaceView;

/**
 * Starts the program
 * 
 * Workspace model is the only GUI class that holds a study object
 * Workspace View has different states which control what the GUI looks like. 
 * 
 * @author Senior Design Spring 2015 
 *
 */
public class RunMVC {

    protected WorkspaceView workspaceView;
    protected WorkspaceModel workspaceModel;

    public RunMVC() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        workspaceModel = new WorkspaceModel();
        workspaceView = new WorkspaceView();
        workspaceView.setModel(workspaceModel);
        workspaceModel.setCurrentState(State.START);
    }

}
