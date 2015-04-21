package edu.auburn.cardiomri.gui.models;

import java.util.ArrayList;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Slice;

public class GridModel extends Model {
    protected Group group;
    protected int s, t, i;

    /**
     * Sets the class' currently selected Dicom indices and then updates its
     * Observers.
     * 
     * @param sliceIndex : New sliceIndex.
     * @param timeIndex : New timeIndex.
     * @param imageIndex : New imageIndex.
     */
    public void setCurrentImage(int sliceIndex, int timeIndex, int imageIndex) {
        // System.out.println("GridModel : setCurrentImage");

        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        int[] indices = { this.s, this.t, this.i };

        setChanged();
        // update new current image
        notifyObservers(indices);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Decrements the current time index and updates the models.
     */
    public void decrementTimeIndex() {
        if ((this.t - 1) >= 0) {
            setCurrentImage(s, t - 1, i);
        } else {
            int newTimeIndex = this.group.getSlices().get(s).getTimes().size() - 1;
            setCurrentImage(s, newTimeIndex, i);
        }
    }

    /**
     * Increments the current time index and updates the models.
     */
    public void incrementTimeIndex() {
        if ((this.t + 1) < group.getSlices().get(s).getTimes().size()) {
            setCurrentImage(s, t + 1, i);
        } else {
            setCurrentImage(s, 0, i);
        }
    }

    /**
     * Decrements the current slice index and updates the models.
     */
    public void decrementSliceIndex() {
        if (this.s > 0) {
            setCurrentImage(s - 1, t, i);
        } else {
            // Already at the lowest slice
        }
    }

    /**
     * Increments the current slice index and updates the models.
     */
    public void incrementSliceIndex() {
        if ((this.s + 1) < group.getSlices().size()) {
            setCurrentImage(s + 1, t, i);
        } else {
            // Already at the highest slice
        }
    }
}
