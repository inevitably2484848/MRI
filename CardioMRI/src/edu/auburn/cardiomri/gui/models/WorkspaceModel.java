/**
 * 
 */
package edu.auburn.cardiomri.gui.models;

import java.util.HashMap;
import java.util.Map;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Slice;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Time;
import edu.auburn.cardiomri.util.StudyUtilities;

/**
 * Model for the WorkspaceView. Contains the Study object, manages the "state"
 * of the workspace, and maps ImageModels to the Group containing its
 * DICOMImages.
 * 
 * @author Moniz
 */
public class WorkspaceModel extends Model {
    protected State currentState;
    protected Study study;
    protected Map<ImageModel, Group> imageToGroup;

    /**
     * Constructor for WorkspaceModel. The currentState is initialized to
     * UNDEFINED.
     */
    public WorkspaceModel() {
        super();
        currentState = State.UNDEFINED;
        imageToGroup = new HashMap<ImageModel, Group>();
    }

    /**
     * Getter for the current study.
     * 
     * @return
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Setter for the current study. If the study is null or contains less than
     * 1 image, then it we can't do any work on it. The START state is set so
     * the user can select a new study. If the study does not have valid group
     * indices for short axis, two chamber, and four chamber, then the
     * GROUP_SELECTION state is set. If the study does have valid group indices,
     * then the WORKSPACE state is set.
     * 
     * @param study
     */
    public void setStudy(Study study) {
        this.study = study;
        if (study == null) {
            setCurrentState(State.START);
        } else if (study.getUIDToImage().size() < 1) {
            setCurrentState(State.START);
        } else if (!hasValidIndices()) {
            setCurrentState(State.GROUP_SELECTION);
        } else {
            setCurrentState(State.WORKSPACE);
        }
    }

    /**
     * Check that each of the three indices is >= 0 and that they can be used to
     * locate a Group.
     * 
     * @return
     */
    public boolean hasValidIndices() {
        boolean isValid = true;

        if (study.getShortAxis() < 0
                || study.getShortAxis() >= study.getGroups().size()) {
            isValid = false;
        }
        if (study.getTwoChamber() < 0
                || study.getTwoChamber() >= study.getGroups().size()) {
            isValid = false;
        }
        if (study.getFourChamber() < 0
                || study.getFourChamber() >= study.getGroups().size()) {
            isValid = false;
        }

        if (study.getShortAxis() == study.getTwoChamber()
                || study.getShortAxis() == study.getFourChamber()
                || study.getTwoChamber() == study.getFourChamber()) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Getter for the current state.
     * 
     * @return
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Updates the current state and notifies observers with the change.
     * 
     * @param currentState
     */
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        setChanged();
        notifyObservers(currentState);
    }

    /**
     * Adds the ImageModel, Group pair to the internal map. When WorkspaceView
     * gets an update with new indices, all of the added pairs are updated.
     * 
     * @param imageModel
     * @param group
     */
    public void addImage(ImageModel imageModel, Group group) {
        if (imageModel == null) {
            return;
        }
        if (group == null) {
            return;
        }

        imageToGroup.put(imageModel, group);
    }

    /**
     * Pulls a DICOM image from each Group and sets it in the matching
     * ImageModel.
     * 
     * @param sliceIndex
     * @param timeIndex
     * @param imageIndex
     */
    public void setIndices(int sliceIndex, int timeIndex, int imageIndex) {
        for (ImageModel imageModel : imageToGroup.keySet()) {
            Group group = imageToGroup.get(imageModel);

            Slice slice = null;
            if (sliceIndex < 0 || sliceIndex >= group.getSlices().size()) {
                //System.err.println("slice index out of bounds");
                slice = group.getSlices().get(0);
                //continue;
            }
            else
            {
            	slice = group.getSlices().get(sliceIndex);
            }

            if (timeIndex < 0 || timeIndex >= slice.getTimes().size()) {
                System.err.println("time index out of bounds");
            }

            Time time = slice.getTimes().get(timeIndex);
            if (imageIndex < 0 || imageIndex >= time.getImages().size()) {
                System.err.println("image index out of bounds");
            }

            imageModel.setCurrentImage(time.getImages().get(imageIndex));
        }
    }

    public enum State {
        UNDEFINED, START, GROUP_SELECTION, WORKSPACE
    }

    /**
     * @see StudyUtilities#saveStudy(Study, String)
     * @param fileName
     */
    public void saveStudy(String fileName) {
        StudyUtilities.saveStudy(this.study, fileName);
    }
}
