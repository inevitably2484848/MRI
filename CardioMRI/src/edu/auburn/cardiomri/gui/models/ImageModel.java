package edu.auburn.cardiomri.gui.models;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javafx.geometry.Point2D;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Point;
import edu.auburn.cardiomri.datastructure.TensionPoint;
import edu.auburn.cardiomri.datastructure.Vector3d;
import edu.auburn.cardiomri.util.ContourCalc;
import edu.auburn.cardiomri.util.Mode;
import edu.auburn.cardiomri.datastructure.ControlPoint;

public class ImageModel extends Model {
    protected DICOMImage dImage;
    protected Contour selectedContour;
    protected ControlPoint selectedControlPoint;
    protected TensionPoint selectedTensionPoint;
    protected Landmark selectedLandmark;
    protected Landmark activeLandmark;
    protected List<Contour> hiddenContours;
    protected List<Landmark> hiddenLandmarks;
    

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
        if (selectedContour != null) {
        	selectedContour.isSelected(false);
        	selectedContour = null;
        }
        
        if (selectedLandmark != null) {
        	selectedLandmark.isSelected(false);
        	selectedLandmark = null;
        }
        
        hiddenContours.clear();

        setChanged();
        notifyObservers(dImage);
    }

    /**************************************************************************
     * Adds a control point to the currently selected contour. If no contour is
     * selected, returns false.
     * 
     * @param x
     * @param y
     * @return true if point was added, false otherwise
     *************************************************************************/
    public boolean addControlPoint(double x, double y) {
        
    	if (selectedContour == null) {
            return false;
        }
        if(selectedContour.notToClose(x, y)){
	        selectedContour.addControlPoint(x, y);  //Contour.java
	        setChanged();
	        notifyObservers(dImage);
	        
	        return true;
        }
        else{
        	return deleteControlPoint(x, y);
        }
    }
    
    
    /**************************************************************************
     * deleteControlPoint - if you click on an existing point it is removed.
     * @author KulW
     * @param x
     * @param y
     * @return
     *************************************************************************/
    public boolean deleteControlPoint(double x, double y){
    	
    	if(!(selectedContour.deleteControlPoint(x,y))){
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
        if (selectedContour != null) {
        	selectedContour.isSelected(false);
        	selectedContour = null;
        }

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
            if (contour.isVisible()) {
                visibleContours.add(contour);
            }
        }

        return visibleContours;
    }
    
    public Vector<Landmark> getVisibleLandmarks() {
    	Vector<Landmark> visibleLandmarks = new Vector<Landmark>();
    	
    	for (Landmark landmark : getLandmarks()) {
    		if (landmark.isVisible()) {
    			visibleLandmarks.add(landmark);
    		}
    	}
    	
    	return visibleLandmarks;
    }
    

    
    
    public Vector<Landmark> getLandmarks(){
    	return dImage.getLandmarks();
    }
    
    public Vector<Point> getAllVisiblePoints() {
    	Vector<Point> visiblePoints = new Vector<Point>();
    	
    	Vector<Landmark> visibleLandmarks = getVisibleLandmarks();
    	for (Landmark landmark: visibleLandmarks) {
    		if (landmark.isVisible()) {
    			visiblePoints.add(landmark);
    		}
    	}
    	
    	Vector<Contour> visibleContours = getVisibleContours();
    	for (Contour contour: visibleContours) {
    		List<ControlPoint> visibleControlPoints = contour.getControlPoints();
    		for (ControlPoint controlPoint: visibleControlPoints) {
    			visiblePoints.add(controlPoint);
    			if (controlPoint.getTension1().isVisible()) {
    				visiblePoints.add(controlPoint.getTension1());
    			}
    			if (controlPoint.getTension2().isVisible()) {
    				visiblePoints.add(controlPoint.getTension2());
    			}
    		}
    	}
    	
    	return visiblePoints;
    }
    
    public Point findNearestPointWithinRange(double x, double y, int range) {
    	int maxDistance = range;
    	Point nearestPoint = null;
    	
    	Vector<Point> allVisiblePoints = getAllVisiblePoints();
    	for (Point point: allVisiblePoints) {
    		if ((Math.abs(point.getX() - x) < maxDistance) && (Math.abs(point.getY() - y) < maxDistance)) {
    			if (nearestPoint == null) {
    				nearestPoint = point;
    			}
    			else {
    				if ((Math.abs(point.getX() - x) + Math.abs(point.getY() - y)) 
    						< (Math.abs(nearestPoint.getX() - x) + Math.abs(nearestPoint.getY() - y))) {
    					nearestPoint = point;
    				}
    			}
    		}
    	}
    	
    	return nearestPoint;
    }
    
    public Point findNearestPoint(double x, double y) {
    	Point nearestPoint = null;
    	
    	Vector<Point> allVisiblePoints = getAllVisiblePoints();
    	for (Point point: allVisiblePoints) {
			if (nearestPoint == null) {
				nearestPoint = point;
			}
			else {
				if ((Math.abs(point.getX() - x) + Math.abs(point.getY() - y)) 
						< (Math.abs(nearestPoint.getX() - x) + Math.abs(nearestPoint.getY() - y))) {
					nearestPoint = point;
				}
			}
    	}
    	
    	return nearestPoint;
    }
    
    public Contour findNearestContour(double x, double y) {
    	ControlPoint pointClicked = new ControlPoint(x, y);
        float delta = Float.MAX_VALUE;
    	Contour nearestContour = null;
    	
    	for (Contour contour : getVisibleContours()) {
            if (contour.getControlPoints().size() == 0) {
                continue;
            }

            float newDelta = ContourCalc.getDeltaArcLength(contour,
                    pointClicked);
            if (newDelta < delta) {
                delta = newDelta;
                nearestContour = contour;
            }
        }
    	
    	return nearestContour;
    }
    
    public Landmark findNearestLandmark(double x, double y) {
    	ControlPoint pointClicked = new ControlPoint(x, y);
        float delta = Float.MAX_VALUE;
    	Landmark nearestLandmark = null;
    	
    	for (Landmark landmark: getVisibleLandmarks()) {
    		if (nearestLandmark == null) {
    			nearestLandmark = landmark;
			}
			else {
				if ((Math.abs(landmark.getX() - x) + Math.abs(landmark.getY() - y)) 
						< (Math.abs(nearestLandmark.getX() - x) + Math.abs(nearestLandmark.getY() - y))) {
					nearestLandmark = landmark;
				}
			}
    	}
    	
    	return nearestLandmark;
    }
    
    public void selectClosestAnnotationWithinRange(double x, double y, int range) {
    	Point nearestPoint = findNearestPointWithinRange(x, y, range);

    	if (selectedControlPoint != null) {
    		selectedControlPoint.isSelected(false);
    	}
    	selectedControlPoint = null;
    	
    	if (selectedTensionPoint != null) {
    		selectedTensionPoint.isSelected(false);
    	}
    	selectedTensionPoint = null;
    	
    	if (nearestPoint != null && nearestPoint.getClass() == ControlPoint.class) {
    		Contour newSelectedContour = getContainingContour(nearestPoint);
    		
    		if (newSelectedContour == selectedContour) { // control point in currently selected contour
    			selectedControlPoint = (ControlPoint) nearestPoint;
    			selectedControlPoint.isSelected(true);
    			System.out.println("contour control point selected");
    		}
    		else {	// control point in unselected contour
    			if (selectedContour != null) {
            		selectedContour.isSelected(false);
            	}
    			
    			selectedContour = newSelectedContour;
        		selectedContour.isSelected(true);
        		System.out.println("contour selected");
    		}
    		
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        		selectedLandmark = null;
        	}
    	}
    	else if (nearestPoint != null && nearestPoint.getClass() == TensionPoint.class) {
    		Contour newSelectedContour = getContainingContour(nearestPoint);
    		
    		if (newSelectedContour == selectedContour) { // tension point in currently selected contour
    			selectedTensionPoint = (TensionPoint) nearestPoint;
    			selectedTensionPoint.isSelected(true);
    			System.out.println("contour tension point selected");
    		}
    		else {	// tension point in unselected contour
    			if (selectedContour != null) {
            		selectedContour.isSelected(false);
            	}
    			
    			selectedContour = newSelectedContour;
        		selectedContour.isSelected(true);
        		System.out.println("contour selected");
    		}
    		
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        		selectedLandmark = null;
        	}
    	}
    	else if (nearestPoint != null && nearestPoint.getClass() == Landmark.class) {
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        	}
    		
    		selectedLandmark = (Landmark)nearestPoint;
    		selectedLandmark.isSelected(true);
    		System.out.println("landmark selected");
    		
    		if (selectedContour != null) {
        		selectedContour.isSelected(false);
        		selectedContour = null;
        	}
    	}
    	else {
    		if (Mode.getMode() == Mode.selectMode()) {
    			
	    		if (selectedLandmark != null) {
	        		selectedLandmark.isSelected(false);
	        		selectedLandmark = null;
	        	}
	    		if (selectedContour != null) {
	        		selectedContour.isSelected(false);
	        		selectedContour = null;
	        	}
    		}
    	}
    	
    	setChanged();
        notifyObservers(dImage);
    }
    
    public void selectClosestAnnotation(double x, double y) {
    	Point nearestPoint = findNearestPoint(x, y);

    	if (selectedControlPoint != null) {
    		selectedControlPoint.isSelected(false);
    	}
    	selectedControlPoint = null;
    	
    	if (selectedTensionPoint != null) {
    		selectedTensionPoint.isSelected(false);
    	}
    	selectedTensionPoint = null;
    	
    	if (nearestPoint != null && nearestPoint.getClass() == ControlPoint.class) {
    		Contour newSelectedContour = getContainingContour(nearestPoint);
    		
    		if (newSelectedContour == selectedContour) { // control point in currently selected contour
    			selectedControlPoint = (ControlPoint) nearestPoint;
    			selectedControlPoint.isSelected(true);
    			System.out.println("contour control point selected");
    		}
    		else {	// control point in unselected contour
    			if (selectedContour != null) {
            		selectedContour.isSelected(false);
            	}
    			
    			selectedContour = newSelectedContour;
        		selectedContour.isSelected(true);
        		System.out.println("contour selected");
    		}
    		
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        		selectedLandmark = null;
        	}
    	}
    	else if (nearestPoint != null && nearestPoint.getClass() == TensionPoint.class) {
    		Contour newSelectedContour = getContainingContour(nearestPoint);
    		
    		if (newSelectedContour == selectedContour) { // tension point in currently selected contour
    			selectedTensionPoint = (TensionPoint) nearestPoint;
    			selectedTensionPoint.isSelected(true);
    			System.out.println("contour tension point selected");
    		}
    		else {	// tension point in unselected contour
    			if (selectedContour != null) {
            		selectedContour.isSelected(false);
            	}
    			
    			selectedContour = newSelectedContour;
        		selectedContour.isSelected(true);
        		System.out.println("contour selected");
    		}
    		
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        		selectedLandmark = null;
        	}
    	}
    	else if (nearestPoint != null && nearestPoint.getClass() == Landmark.class) {
    		if (selectedLandmark != null) {
        		selectedLandmark.isSelected(false);
        	}
    		
    		selectedLandmark = (Landmark)nearestPoint;
    		selectedLandmark.isSelected(true);
    		System.out.println("landmark selected");
    		
    		//Mode.setMode(Mode.landmarkMode());
    		
    		if (selectedContour != null) {
        		selectedContour.isSelected(false);
        		selectedContour = null;
        	}
    	}
    	
    	setChanged();
        notifyObservers(dImage);
    }
    
    public Contour getContainingContour(ControlPoint point) {
    	Contour containingContour = null;
    	
    	for (Contour contour : getVisibleContours()) {
            if (contour.getControlPoints().contains(point)) {
            	containingContour = contour;
            	break;
            }
        }
    	
    	return containingContour;
    }
    
    public Contour getContainingContour(Point point) {
    	Contour containingContour = null;
    	
    	for (Contour contour : getVisibleContours()) {
            if (contour.getControlPoints().contains(point)) {
            	containingContour = contour;
            	break;
            }
            if (contour.getTensionPoints().contains(point)) {
            	containingContour = contour;
            	break;
            } 
        }
    	
    	return containingContour;
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

    
   
    /**************************************************************************
     *  LANDMARK
     * 
     *************************************************************************/
    public void addLandmarkToImage(Landmark landmark){
    	if (selectedLandmark != null) {
    		selectedLandmark.isSelected(false);
    	}
    	this.dImage.addLandmark(landmark);
    	setChanged();
    	notifyObservers(dImage);
    }
    
    
    public void deleteLandmarkFromImage(Landmark landmark){
    	this.dImage.deleteLandmark(landmark);
    	setActiveLandmark(null);
    	landmark.isSelected(false);
    	selectedLandmark = null;
    	setChanged();
    	notifyObservers(dImage);
    	
    }
    
    /**************************************************************************
    * Deletes All Landmarks
    * loops through landmarks list and deletes one by one.
    *************************************************************************/
    public void deleteAllLandmark(){
    	Vector<Landmark> visibleLandmarks = getLandmarks();
  	  
    	for(Landmark landmark : visibleLandmarks){
    		deleteLandmarkFromImage(landmark);
  	  	}
    }
    
    public void setActiveLandmark(Landmark landmark){
    	activeLandmark = landmark;
    }
    public void setLandmarkCoordinates(double x, double y){
    	activeLandmark.setLandmarkCoordinates(x,y);
    	setChanged();
        notifyObservers(dImage);
    }
    
    public Landmark getSelectedLandmark() {
    	return this.selectedLandmark;
    }
    
    /**************************************************************************
     *  CONTROL POINTS
     *************************************************************************/
    
    
    public ControlPoint getSelectedControlPoint(){
    	return this.selectedControlPoint;
    }
    
    
    
    /*************************************************************************
     * CONTOUR
     *************************************************************************/
    
    
    /**
     * Hides the currently selected contour.
     */
    public void hideSelectedContour() {
        if (selectedContour == null) {
            System.err.println("no selected Contour");
            return;
        }

        hiddenContours.add(selectedContour);
        selectedContour = null;

        setChanged();
        notifyObservers(dImage);
    }

    /**
     * Sets all contours as hidden.
     */
    public void hideAllContours() {
        hiddenContours.addAll(getVisibleContours());
        selectedContour = null;

        setChanged();
        notifyObservers(dImage);
    }
    public void hideSelectedLandmark(){
    	if (selectedLandmark == null){
    		return;
    	}
    	hiddenLandmarks.add(selectedLandmark);
    	selectedLandmark = null;
    	
    	setChanged();
    	notifyObservers(dImage);
    		
    }
    public void hideAllLandmarks(){
    	hiddenLandmarks.addAll(getVisibleLandmarks());
    	selectedLandmark = null;
    	
    	setChanged();
    	notifyObservers(dImage);
    }
    public void showAllLandmarks(){
    	hiddenLandmarks.clear();
    	
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
        if (selectedContour == null) {
            // throw error?
            return;
        }

        dImage.getContours().remove(selectedContour);
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
        Contour closest = findNearestContour(x, y);

        setSelectedContour(closest);
    }

    public DICOMImage getImage() {
        return this.dImage;
    }

    public void setSelectedContour(Contour contour) {
    	if (selectedContour != null) {
    		selectedContour.isSelected(false);
    	}
        selectedContour = contour;
        setChanged();
        notifyObservers(dImage);
    }

    public Vector<Contour> getContours() {
        return dImage.getContours();
    }

    public Contour getSelectedContour() {
        return selectedContour;
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
