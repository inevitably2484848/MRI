package edu.auburn.cardiomri.gui.models;

import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;

public class ImageModel extends Model {

    private DICOMImage dImage;
    private Vector<Contour> contours;
    private Contour currentContour;
    private Contour selectedContour;
    private Vector<Contour> hiddenContours = new Vector<Contour>();

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
     * Setter for currentContour. Observers are notified.
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
        // Start other three models (2,4, and contour panel)
    }

    public void refresh() {
        // this.dImage = this.study.getGroups().get(g).getSlices().get(s)
        // .getTimes().get(t).getImages().get(i);
        this.contours = this.dImage.getContours();

        setChanged();
        notifyObservers(this.dImage);
    }

    public void setSelectedContour(Contour c) {
        this.selectedContour = c;
        System.out.println(c.toString());
    }

    public Contour getSelectedContour() {
        return this.selectedContour;
    }

    public Vector<Contour> getHiddenContours() {
        return this.hiddenContours;
    }

    /**
     * @param dImage
     */
    public void setCurrentImage(DICOMImage dImage) {
        this.dImage = dImage;
        // TODO: Update the two images on the side
        setContourList(dImage.getContours());
        setChanged();
        notifyObservers(dImage);
    }

}
