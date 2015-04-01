package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class MetadataModel extends java.util.Observable {
    private Study study;
    private DICOMImage curImage;
    private int g, s, t, i;

    /**
     * Sets the class' study attribute and notifies its Observers.
     * 
     * @param s : The object that the class will use as its study attribute.
     */
    public void setStudy(Study s) {
        // System.out.println("MetaDataModel : study set");
        this.study = s;
    }

    /**
     * Sets the class' currently selected Dicom indices and then updates its
     * Observers.
     * 
     * @param groupIndex : New groupIndex.
     * 
     * @param sliceIndex : New sliceIndex.
     * 
     * @param timeIndex : New timeIndex.
     * 
     * @param imageIndex : New imageIndex.
     */
    public void setCurrentImage(int groupIndex, int sliceIndex, int timeIndex,
            int imageIndex) {
        // System.out.println("MetaDataModel : Current Image set");

        this.g = groupIndex;
        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        this.curImage = this.study.getGroups().get(g).getSlices().get(s)
                .getTimes().get(t).getImages().get(i);

        setChanged();
        notifyObservers(this.curImage);
    }

    /**
     * Returns the currently selected DICOMImage object given the current
     * indices.
     * 
     * @return The currently selected DICOMImage.
     */
    public DICOMImage getImage() {
        return this.study.getGroups().get(g).getSlices().get(s).getTimes()
                .get(t).getImages().get(i);
    }

    public MetadataModel() {
        // System.out.println("MetaDataModel()");

        this.g = 0;
        this.s = 0;
        this.t = 0;
        this.i = 0;
    }
}
