package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.gui.models.WorkspaceModel.State;
import edu.auburn.cardiomri.gui.views.WorkspaceView;

public class WorkflowModel extends Model {
	protected State currentState;
	protected WorkspaceModel workspaceModel;
	protected WorkspaceView workspaceView;
	
	public WorkflowModel() {
		super();
		currentState = State.START;
	}
	
	public void setWorkspaceModel(WorkspaceModel workspaceModel) {
		this.workspaceModel = workspaceModel;
	}
	
	public void setWorkspaceView(WorkspaceView workspaceView) {
		this.workspaceView = workspaceView;
	}
	
	public void update() {
		if (this.workspaceModel.study == null) {
            setCurrentState(State.START);
        } else if (this.workspaceModel.study.getUIDToImage().size() < 1) {
            setCurrentState(State.START);
        } else if (!this.workspaceModel.hasValidIndices()) {
            setCurrentState(State.GROUP_SELECTION);
        } else {
            setCurrentState(State.WORKSPACE);
        }
		
		
        if (StartModel.getLoadStudy()){
        	this.currentState = State.WORKSPACE;
        }
        if (this.currentState == State.START) {
            this.workspaceView.startState();

        } else if (currentState == State.GROUP_SELECTION) {
            this.workspaceView.groupSelectionState();

        } else if (currentState == State.WORKSPACE) {
            this.workspaceView.workspaceState();
        }
        
	}
	
    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        setChanged();
        notifyObservers(currentState);
    }
}
