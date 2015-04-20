package edu.auburn.cardiomri;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.gui.models.StartModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel;
import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.gui.views.StartView;
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

        workspaceModel = new WorkspaceModel();
        workspaceView = new WorkspaceView();
        workspaceView.setModel(workspaceModel);
        workspaceModel.setCurrentState(State.START);
    }

}
