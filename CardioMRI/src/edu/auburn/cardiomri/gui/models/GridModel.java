package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.Group;

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
    public void setCurrentImage(int sliceIndex, int timeIndex,
            int imageIndex) {
        // System.out.println("GridModel : setCurrentImage");

        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        int[] indices = {this.s, this.t, this.i};

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
}
