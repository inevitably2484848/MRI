package edu.auburn.cardiomri.gui.models;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;

public class ImageModel extends Model {
    private DICOMImage dImage;
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

        dImage.setContours(contourList);
        setChanged();
        notifyObservers(contourList);

        if (contourList.size() > 0) {
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
        return dImage.getContours();
    }

    public void addContourToImage(Contour contour) {
        this.dImage.addContour(contour);
        setCurrentContour(contour);
    }

    public void hideSelectedContour() {
        
        dImage.getContours().remove(selectedContour);
        hiddenContours.add(selectedContour);
        selectedContour = null;
        setChanged();
        notifyObservers(dImage);
    }

    public void hideContours() {
        hiddenContours.addAll(dImage.getContours());
        dImage.getContours().clear();
        setChanged();
        notifyObservers(dImage);
    }

    public void showContours() {
        if (hiddenContours.size() != 0) {
            dImage.getContours().addAll(hiddenContours);
            hiddenContours.clear();
        }
        setChanged();
        notifyObservers(dImage);
    }

    public void deleteSelectedContour() {
        dImage.getContours().remove(selectedContour);
        selectedContour = null;
        setChanged();
        notifyObservers(dImage);
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
    
    public void selectContour(JPanel imageContourPanel) {
        Object[] possibilities = dImage.getContours().toArray();
        Contour[] contours = new Contour[possibilities.length];
        
        int i = 0;
        for (Object c : possibilities) {
            contours[i] = (Contour)c;
            i++;
        }
        Contour c = (Contour)JOptionPane.showInputDialog(
                imageContourPanel,
                            "Select Contour: ",
                            "Contours", JOptionPane.PLAIN_MESSAGE,
                            null,
                            contours,
                            "ham");

        if (c != null) {
            this.selectedContour = c;
        }

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

    public void deleteAllContours() {
        dImage.getContours().clear();
        setChanged();
        notifyObservers(dImage);
    }

}
