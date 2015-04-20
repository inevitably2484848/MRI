package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class SelectModel extends Model {
    private Study study;
    private int g, s, t, i;
    private DICOMImage currentImage;

    
    /**
     * Sets the class' study attribute and notifies its Observers.
     * 
     * @param s : The object that the class will use as its study attribute.
     */
    public void setStudy(Study s) {
        this.study = s;
        this.currentImage = this.study.getGroups().get(0).getSlices().get(0)
                .getTimes().get(0).getImages().get(0);

        setChanged();
        notifyObservers(this.study);
    }

    /**
     * Sets the class' currently selected Dicom indices and then updates its
     * Observers.
     * 
     * @param groupIndex : New groupIndex.
     * @param sliceIndex : New sliceIndex.
     * @param timeIndex : New timeIndex.
     * @param imageIndex : New imageIndex.
     */
    public void setCurrentImage(int groupIndex, int sliceIndex, int timeIndex, int imageIndex) {
        this.g = groupIndex;
        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        this.currentImage = this.study.getGroups().get(g).getSlices().get(s)
                .getTimes().get(t).getImages().get(i);

        int indices[] = { this.g, this.s, this.t, this.i };

        setChanged();
        notifyObservers(indices);
    }
    

    /**
     * Returns the class' study attribute.
     * 
     * @return The class' study attribute.
     */
    public Study getStudy() {
        return this.study;
    }

    /**
     * Returns the currently selected DICOMImage object given the current
     * indices.
     * 
     * @return The currently selected DICOMImage.
     */
    public DICOMImage getImage() {
        return this.currentImage;
    }

    public SelectModel() {
        this.study = null;
    }
}
