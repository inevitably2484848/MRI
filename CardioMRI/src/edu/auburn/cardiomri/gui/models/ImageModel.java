package edu.auburn.cardiomri.gui.models;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javafx.geometry.Point2D;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Vector3d;
import edu.auburn.cardiomri.util.ContourCalc;
import edu.auburn.cardiomri.datastructure.ControlPoint;

public class ImageModel extends Model {
    protected DICOMImage dImage;
    protected Contour selected;
    protected Landmark activeLandmark;
    protected List<Contour> hiddenContours;

    public ImageModel() {
        super();
        hiddenContours = new Vector<Contour>();
    }

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

    /**
     * Adds a control point to the currently selected contour. If no contour is
     * selected, returns false.
     * 
     * @param x
     * @param y
     * @return true if point was added, false otherwise
     */
    public boolean addControlPoint(double x, double y) {
        
    	if (selected == null) {
            return false;
        }
        if(selected.notToClose(x, y)){
	        selected.addControlPoint(x, y);  //Contour.java
	        setChanged();
	        notifyObservers(dImage);
	        
	        return true;
        }
        else{
        	return deleteControlPoint(x, y);
        }
    }
    
    
    /** -----------------------------------------------------------------------
     * deleteControlPoint - if you click on an existing point it is removed.
     * @author KulW
     * @param x
     * @param y
     * @return
     * -----------------------------------------------------------------------*/
    public boolean deleteControlPoint(double x, double y){
    	
    	if(!(selected.deleteControlPoint(x,y))){
    		return false;
    	}
    	setChanged();
    	notifyObservers(dImage);
    	return true;
    } //deleteControlPoint
    
    /**
     * Delete all of the visible and hidden contours
     */
    public void deleteAllContours() {
        dImage.getContours().clear();
        selected = null;
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

    /**
     * Get the list of contours that should be drawn onto the screen.
     * 
     * @return List of contours with the hidden ones removed.
     */
    public Vector<Contour> getVisibleContours() {
        Vector<Contour> visibleContours = new Vector<Contour>();

        for (Contour contour : getContours()) {
            if (!hiddenContours.contains(contour)) {
                visibleContours.add(contour);
            }
        }

        return visibleContours;
    }
    public Vector<Landmark> getVisibleLandmarks() {
    	Vector<Landmark> visibleLandmarks = getLandmarks();
    	
    	return visibleLandmarks;
    }
    public Vector<Landmark> getLandmarks(){
    	return dImage.getLandmarks();
    }
    /**
     * Adds a contour to the image and sets it as the selected contour.
     * 
     * @param contour
     */
    public void addContourToImage(Contour contour) {
        this.dImage.addContour(contour);
        setSelectedContour(contour);
        
    }
    
    public void addLandmarkToImage(Landmark landmark){
    	this.dImage.addLandmark(landmark);
    	setActiveLandmark(landmark);
    }
    public void setActiveLandmark(Landmark landmark){
    	activeLandmark = landmark;
    }
    public void setLandmarkCoordinates(double x, double y){
    	activeLandmark.setLandmarkCoordinates(x,y);
    	setChanged();
        notifyObservers(dImage);
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

    /**
     * Sets all contours as hidden.
     */
    public void hideAllContours() {
        hiddenContours.addAll(getVisibleContours());
        selected = null;

        setChanged();
        notifyObservers(dImage);
    }

    /**
     * Sets all contours as visible.
     */
    public void showAllContours() {
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

    /**
     * Removes the selected contour from the list of all contours.
     */
    public void deleteSelectedContour() {
        if (selected == null) {
            // throw error?
            return;
        }

        dImage.getContours().remove(selected);
        setSelectedContour(null);

        setChanged();
        notifyObservers(dImage);
    }

    /**
     * Selects the contour closest to the given coordinates. The closest contour
     * is determined by iterating over each of the visible contours, calculating
     * the change in arc length if a point was added to it, then picking the
     * contour for which the change in arc length was the least.
     * 
     * @param x
     * @param y
     */
    public void selectContour(double x, double y) {
        ControlPoint pointClicked = new ControlPoint(x, y);
        float delta = Float.MAX_VALUE;
        Contour closest = null;

        for (Contour contour : getVisibleContours()) {
            if (contour.getControlPoints().size() == 0) {
                continue;
            }

            float newDelta = ContourCalc.getDeltaArcLength(contour,
                    pointClicked);
            if (newDelta < delta) {
                delta = newDelta;
                closest = contour;
            }
        }

        setSelectedContour(closest);
    }

    public DICOMImage getImage() {
        return this.dImage;
    }

    public void setSelectedContour(Contour contour) {
        selected = contour;
        setChanged();
        notifyObservers(dImage);
    }

    public Vector<Contour> getContours() {
        return dImage.getContours();
    }

    public Contour getSelectedContour() {
        return selected;
    }

    public List<Contour> getHiddenContours() {
        return this.hiddenContours;
    }

    public void refresh() {
        setChanged();
        notifyObservers(dImage);
    }

	public void arrowAction(ActionEvent e) {
		setChanged();
		notifyObservers(e);
	}
}
