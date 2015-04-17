/**
 * 
 */
package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Study;

/**
 * @author Moniz
 *
 */
public class WorkspaceModel extends Model {
    protected State currentState;
    protected Study study;

    public WorkspaceModel() {
        currentState = State.UNDEFINED;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;

        // TODO: decide which state to go to next
        setCurrentState(State.WORKSPACE);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        setChanged();
        notifyObservers(currentState);
    }

    public enum State {
        UNDEFINED, START, GROUP_SELECTION, WORKSPACE
    }
}
