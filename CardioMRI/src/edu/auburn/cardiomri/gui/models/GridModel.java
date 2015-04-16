package edu.auburn.cardiomri.gui.models;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class GridModel extends Model {
    protected Study study;
    protected DICOMImage dImage;
    protected ImageModel mainImageModel;
    protected int g, s, t, i;

    public ImageModel getImageModel() {
        return mainImageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.mainImageModel = imageModel;
    }

    /**
     * Sets the class' study attribute and notifies its Observers.
     * 
     * @param s : The object that the class will use as its study attribute.
     */
    public void setStudy(Study s) {
        // System.out.println("GridModel : setStudy");

        this.study = s;

        setChanged();
        notifyObservers(this.study);
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
        // System.out.println("GridModel : setCurrentImage");

        boolean newGroup = (this.g != groupIndex);

        this.g = groupIndex;
        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        int[] indices = { this.g, this.s, this.t, this.i };

        setChanged();
        // update new current image
        notifyObservers(indices);

        this.dImage = this.study.getGroups().get(g).getSlices().get(s)
                .getTimes().get(t).getImages().get(i);
        mainImageModel.setCurrentImage(dImage);

        if (newGroup) {
            // then reset the grid
            // System.out.println("GridModel : new Group");
            setChanged();
            notifyObservers(this.study);
        }
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
        return this.dImage;
    }

    // Constructors
    public GridModel() {
        // System.out.println("GridModel()");
        this.study = null;
        this.dImage = null;
    }

}
