/**
 * 
 */
package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.util.StudyUtilities;

/**
 * @author Moniz
 *
 */
public class WorkspaceModel extends Model {
    protected State currentState;
    protected Study study;
    protected ImageModel shortAxis, twoChamber, fourChamber;

    public WorkspaceModel() {
        currentState = State.UNDEFINED;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
        if (study == null) {
            setCurrentState(State.START);
        } else if (study.getUIDToImage().size() < 1) {
            setCurrentState(State.START);
        } else if (!hasValidIndices(study)) {
            setCurrentState(State.GROUP_SELECTION);
        } else {
            setCurrentState(State.WORKSPACE);
        }
    }

    /**
     * Check that each of the three indices is >= 0 and that they can be used to
     * locate a Group.
     */
    public boolean hasValidIndices(Study study) {
        // TODO Auto-generated method stub
        return true;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        setChanged();
        notifyObservers(currentState);
    }

    public void setShortAxis(ImageModel shortAxis) {
        this.shortAxis = shortAxis;
    }

    public void setTwoChamber(ImageModel twoChamber) {
        this.twoChamber = twoChamber;
    }

    public void setFourChamber(ImageModel fourChamber) {
        this.fourChamber = fourChamber;
    }

    public enum State {
        UNDEFINED, START, GROUP_SELECTION, WORKSPACE
    }

    public void saveStudy(String fileName) {
        StudyUtilities.saveStudy(this.study, fileName);
    }
}
