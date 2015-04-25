package edu.auburn.cardiomri.gui.models;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;

public class ImageModel extends Model {
    protected DICOMImage dImage;
    protected Contour selected;
    protected List<Contour> hiddenContours;

    public ImageModel() {
        super();
        hiddenContours = new Vector<Contour>();
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

        dImage.setContours(contourList);
        selected = null;
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);

        if (contourList.size() > 0) {
            setCurrentContour(contourList.lastElement());
        }
    }

    /**
     * Setter for currentContour. Observers are notified. Note: Null parameter
     * is allowed.
     * 
     * @param contour
     */
    public void setCurrentContour(Contour contour) {
        selected = contour;

        setChanged();
        notifyObservers(dImage);
    }

    public Vector<Contour> getContours() {
        return dImage.getContours();
    }

    public Vector<Contour> getVisibleContours() {
        Vector<Contour> visibleContours = new Vector<Contour>();

        for (Contour contour : getContours()) {
            if (!hiddenContours.contains(contour)) {
                visibleContours.add(contour);
            }
        }

        return visibleContours;
    }

    public void addContourToImage(Contour contour) {
        this.dImage.addContour(contour);
        setCurrentContour(contour);
    }

    /**
     * Hides the currently selected contour.
     */
    public void hideSelectedContour() {
        if (selected == null) {
            // throw NPE?
            return;
        }

        hiddenContours.add(selected);
        selected = null;

        setChanged();
        notifyObservers(dImage);
    }

    public void hideAllContours() {
        hiddenContours.addAll(getVisibleContours());
        selected = null;

        setChanged();
        notifyObservers(dImage);
    }

    public void showAllContours() {
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

    public void deleteSelectedContour() {
        dImage.getContours().remove(selected);
        setSelectedContour(null);

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

    public void refresh() {
        setChanged();
        notifyObservers(dImage);
    }

    public void setSelectedContour(Contour contour) {
        selected = contour;
    }

    public Contour getSelectedContour() {
        return selected;
    }

    public List<Contour> getHiddenContours() {
        return this.hiddenContours;
    }

    public boolean addControlPoint(double x, double y) {
        if (selected == null) {
            return false;
        }

        selected.addControlPoint(x, y);
        setChanged();
        notifyObservers(dImage);
        return true;
    }

    public void selectContour(JPanel imageContourPanel) {
        Object[] possibilities = dImage.getContours().toArray();
        Contour[] contours = new Contour[possibilities.length];

        int i = 0;
        for (Object c : possibilities) {
            contours[i] = (Contour) c;
            i++;
        }
        Contour c = (Contour) JOptionPane.showInputDialog(imageContourPanel,
                "Select Contour: ", "Contours", JOptionPane.PLAIN_MESSAGE,
                null, contours, "ham");

        if (c != null) {
            this.selected = c;
        }

    }

    /**
     * @param dImage
     */
    public void setCurrentImage(DICOMImage dImage) {
        if (dImage == null) {
            // throw NPE?
            System.err.println("dImage is null");
            return;
        }

        this.dImage = dImage;
        selected = null;
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

    public void deleteAllContours() {
        dImage.getContours().clear();
        selected = null;
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

}
