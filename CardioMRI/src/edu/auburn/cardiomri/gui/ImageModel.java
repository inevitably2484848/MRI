package edu.auburn.cardiomri.gui;

import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class ImageModel extends java.util.Observable {

    private Study study;
    private DICOMImage dImage;
    private int g, s, t, i;
    private Vector<Contour> contours;
    private Contour currentContour;

    // Setters
    /*
     * Sets the class' study attribute and notifies its Observers.
     * 
     * @param s : The object that the class will use as its study attribute.
     */
    public void setStudy(Study s) {
        // System.out.println("ImageModel : setStudy(Study s)");
        this.study = s;
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
    public void setCurrentImage(int groupIndex, int sliceIndex, int timeIndex,
            int imageIndex) {
        // System.out.println("ImageModel : setImage");

        this.g = groupIndex;
        this.s = sliceIndex;
        this.t = timeIndex;
        this.i = imageIndex;

        this.dImage = this.study.getGroups().get(g).getSlices().get(s)
                .getTimes().get(t).getImages().get(i);
        setChanged();
        notifyObservers(this.dImage);
        setContourList(dImage.getContours());
    }

    /**
     * Setter for the internal contour list. If the list has at least one
     * element, currentContour is set to the last element. Observers are
     * notified.
     * 
     * @param contourList
     */
    public void setContourList(Vector<Contour> contourList) {
        if (contourList == null) {
            // throw NPE?
        }

        this.contours = contourList;
        setChanged();
        notifyObservers(contours);

        if (contours.size() > 0) {
            setCurrentContour(contourList.lastElement());
        }
    }

    /**
     * Setter for currentContour. Objervers are notified.
     * 
     * @param contour
     */
    public void setCurrentContour(Contour contour) {
        this.currentContour = contour;

        if (currentContour != null) {
            setChanged();
            notifyObservers(currentContour);
        }
    }

    public Vector<Contour> getContours() {
        return this.contours;
    }

    public void addContourToImage(Contour contour) {
        this.dImage.addContour(contour);
        this.contours = this.dImage.getContours();
        setCurrentContour(contour);
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
    public ImageModel() {
        this.dImage = null;
    }

    public void refresh() {
        this.dImage = this.study.getGroups().get(g).getSlices().get(s)
                .getTimes().get(t).getImages().get(i);
        this.contours = this.dImage.getContours();

        setChanged();
        notifyObservers(this.dImage);
    }
}
