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

    public WorkspaceModel() {
        super();
        currentState = State.UNDEFINED;
        imageToGroup = new HashMap<ImageModel, Group>();
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

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        setChanged();
        notifyObservers(currentState);
    }

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

            if (sliceIndex < 0 || sliceIndex >= group.getSlices().size()) {
                System.err.println("slice index out of bounds");
                continue;
            }

            Slice slice = group.getSlices().get(sliceIndex);
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
