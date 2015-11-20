package edu.auburn.cardiomri.datastructure;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point2D;
import edu.auburn.cardiomri.util.ContourCalc;

/**
 * Data structure used to hold control points for a contour. It is a subclass of
 * Shape so that the SingleImagePane class can draw it on its own.
 * 
 * Vector3d to store points because the alternative (javafx.geom.Point2D) is not
 * serializable.
 * 
 * @author Moniz
 *
 */
public class Contour implements Shape, Serializable {
    private static final long serialVersionUID = 6179619427503035482L;
    
    boolean isSelected = true;
    boolean isVisible = true;
    
    static Color color = Color.RED;

    // XY coordinates of points the user clicked
    private List<ControlPoint> controlPoints;

    // XY coordinates of points that look like a smooth curve is drawn between
    // each of the control points
    private List<Point> generatedPoints;
    
    private List<TensionPoint> tensionPoints;

    private Type contourType;

    public Contour(Type contourTypeIn) {
        this();
        contourType = contourTypeIn;
    }

    /**
     * Sets controlPoints to a predefined set of points.
     */
    private Contour() {
        controlPoints = new Vector<ControlPoint>();
        generatedPoints = new Vector<Point>();
        tensionPoints = new Vector<TensionPoint>();
    }
    
    public boolean isSelected() {
    	return this.isSelected;
    }
    
    public void isSelected(boolean isSelected) {
    	this.isSelected = isSelected;
    }
    
    public boolean isVisible() {
    	return this.isVisible;
    }
    
    public void isVisible(boolean isVisible) {
    	this.isVisible = isVisible;
    }
    
    public Color getColor() {
    	return Contour.color;
    }

    public void setControlPoints(List<ControlPoint> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }

        List<ControlPoint> newList = new Vector<ControlPoint>();
        for (ControlPoint point : points) {
            validateCoordinates(point.getX(), point.getY());
            validateCoordinates(point.getTension1().getX(), point.getTension1().getY());
            validateCoordinates(point.getTension2().getX(), point.getTension2().getY());
            newList.add(point);
        }

        controlPoints = newList;
        generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
    }

    @Override
    public boolean contains(java.awt.geom.Point2D p) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(double x, double y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Rectangle getBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (ControlPoint point : controlPoints) {
            minX = (int) Math.floor(Math.min(point.getX(), minX));
            minY = (int) Math.floor(Math.min(point.getY(), minY));
            maxX = (int) Math.ceil(Math.max(point.getX(), maxX));
            maxY = (int) Math.ceil(Math.max(point.getY(), maxY));
        }

        return new Rectangle(minX, minY, (maxX - minX), (maxY - minY));
    }

    @Override
    public Rectangle2D getBounds2D() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (ControlPoint point : controlPoints) {
            minX = Math.min(point.getX(), minX);
            minY = Math.min(point.getY(), minY);
            maxX = Math.max(point.getX(), maxX);
            maxY = Math.max(point.getY(), maxY);
        }

        return new Rectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }

    private Point transformCoordinates(AffineTransform at, Point source) {
        java.awt.geom.Point2D transformed = new java.awt.geom.Point2D.Double();
        at.transform(
                new java.awt.geom.Point2D.Double(source.getX(), source.getY()),
                transformed);
        return new Point(transformed.getX(), transformed.getY());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new PathIterator() {
            private int index = 0;

            @Override
            public void next() {
                index += 1;
            }

            @Override
            public boolean isDone() {
                return index >= generatedPoints.size();
            }

            @Override
            public int getWindingRule() {
                return PathIterator.WIND_NON_ZERO;
            }

            @Override
            public int currentSegment(double[] coords) {
                if (index == 0) {
                    Point point = transformCoordinates(at,
                            generatedPoints.get(0));
                    coords[0] = point.getX();
                    coords[1] = point.getY();
                    return PathIterator.SEG_MOVETO;
                } else if ((index > 0)) {
                    Point point = transformCoordinates(at,
                            generatedPoints.get(index));
                    coords[0] = point.getX();
                    coords[1] = point.getY();
                    return PathIterator.SEG_LINETO;
                } else {
                    return PathIterator.SEG_CLOSE;
                }
            }

            @Override
            public int currentSegment(float[] coords) {
                int size = coords.length;
                double[] doubleCoords = new double[size];
                int returnValue = currentSegment(doubleCoords);

                for (int i = 0; i < size; i++) {
                    coords[i] = (float) doubleCoords[i];
                }

                return returnValue;
            }
        };
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        // TODO Don't ignore flatness
        return this.getPathIterator(at);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        // TODO Auto-generated method stub
        return false;
    }

    public void addControlPoint(double x, double y) {
    	validateCoordinates(x, y);    	
    	//list to hold tension points before they are set to control points
    	//remember to clear after each use
    	List<TensionPoint> tPoints = new Vector<TensionPoint>();
    	if(notToClose(x,y)) {
    		controlPoints.add(new ControlPoint(x, y));
    		ContourCalc.sortPoints(controlPoints);
    		
    		if(controlPoints.size() > 1) {
    			//calculate centroid
        		Point centroid = ContourCalc.calcCentroid(controlPoints);
    			for(int i = 0; i < controlPoints.size(); i++) {
    				//search the control points to find the new point with no tension point
    				if(controlPoints.get(i).getTension2().getX() == 0.0 && controlPoints.get(i).getTension2().getY() == 0.0) {
						//check to see if new control point was sorted to the beginning of the contour
    					if(i == 0) {
	    					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(i), centroid));
	    					controlPoints.get(i).setTension1(tPoints.get(0));
	    					controlPoints.get(i).setTension2(tPoints.get(1));
	    					tPoints.clear();
    					}
    					//check to see if new control point was sorted to the end of the contour
	    				else if (i == controlPoints.size() - 1) {
	    					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(i), centroid));
	    					controlPoints.get(i).setTension1(tPoints.get(0));
	    					controlPoints.get(i).setTension2(tPoints.get(1));
	    					tPoints.clear();
	    				}
	    				else {
	    					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(i), centroid));
	    					controlPoints.get(i).setTension1(tPoints.get(0));
	    					controlPoints.get(i).setTension2(tPoints.get(1));
	    					tPoints.clear(); 					
	    				}
    				}
    			}
    		}
            generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
            updateTensionPoints();
    	}
    }
    
    
    /** -----------------------------------------------------------------------
     * 	notToClose()  returns boolean, if user tries to add control point that 
     *   is to close to an existing one (accidently double clicks) return false 
     *   and not allow control point to be to close.
     *   @author KulW
     *   @param x double
     *   @param y double
     *   @return bToClose boolean
     *  ---------------------------------------------------------------------*/
     public boolean notToClose(double x, double y){
    	 boolean bToClose = true;
    	 double dMinGap = 3;

    	 for(ControlPoint controlP : controlPoints){
    		if(((Math.abs(controlP.getX() - x)) <= dMinGap) && ((Math.abs(controlP.getY() - y) <= dMinGap))){
    			return false;
    		}
    	 } // end for loop
    	 return bToClose;
     } // end isToClose

     
     public boolean deleteControlPoint(double x, double y){
    	 double minGap = 3;
    	 boolean bDeleted = false;
    	 int index = -1;
    	 ControlPoint temp;
    	 double tempX, tempY;
    	 
    	 //calculate centroid
    	 Point centroid = ContourCalc.calcCentroid(controlPoints);
    	 
     	//list to hold tension points before they are set to control points
     	//remember to clear after each use
     	List<TensionPoint> tPoints = new Vector<TensionPoint>();
     	
    	 //find point that is close enough to delete 
    	 for(int i = 0 ; i < controlPoints.size() ; i++){
    		 temp = controlPoints.get(i);
    		 tempX = temp.getX();
    		 tempY = temp.getY();
    		 if((Math.abs(tempX - x) < minGap) && (Math.abs(tempY - y) < minGap)){
    			 index = i;
    			 break;
    		 }
    	 } // end loop

    	 if(index >= 0){
    		 controlPoints.remove(index);
    		 if(controlPoints.size() != 0) {
    	    	 //check to see if the removed point was the initial point in the contour
    			 if(index == 0) {
    				//calculate and set the second tension point of the previous control point
    				tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(controlPoints.size() - 1), centroid));
 					controlPoints.get(controlPoints.size() - 1).setTension2(tPoints.get(1));
 					tPoints.clear();
 					
 					//calculate and set the first tension point of the next control point
 					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(index), centroid));
 					controlPoints.get(index).setTension1(tPoints.get(0));
 					tPoints.clear();
    			 }
    			 //check to see if the removed point was the last point in the contour
    			 else if(index == controlPoints.size()) {
    				//calculate and set the second tension point of the previous control point
    				tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(index - 1), centroid));
 					controlPoints.get(index - 1).setTension2(tPoints.get(1));
 					tPoints.clear();
 					
 					//calculate and set the first tension point of the next control point
 					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(controlPoints.size() - 1), centroid));
 					controlPoints.get(controlPoints.size() - 1).setTension1(tPoints.get(0));
 					tPoints.clear();
    			 }
    			 else {
    				//calculate and set the second tension point of the previous control point
    				tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(index - 1), centroid));
 					controlPoints.get(index - 1).setTension2(tPoints.get(1));
 					tPoints.clear();
 					
 					//calculate and set the first tension point of the next control point
 					tPoints.addAll(ContourCalc.getTensionPoint(controlPoints.get(index), centroid));
 					controlPoints.get(index).setTension1(tPoints.get(0));
 					tPoints.clear();
    			 }
    		 }
    		 generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve()); //refresh curve
    		 bDeleted = true;
    	 }
    	 updateTensionPoints();
    	 return bDeleted;    	 
     }
    
     public int findControlPoint(double x, double y) {
    	 int cPointD = -1;
    	 int index = -1;
    	 double minGap = 3;
    	 ControlPoint temp;
    	 double tempX, tempY;
    	 
    	 for(int i = 0 ; i < controlPoints.size() ; i++){
    		 temp = controlPoints.get(i);
    		 tempX = temp.getX();
    		 tempY = temp.getY();
    		 if((Math.abs(tempX - x) < minGap) && (Math.abs(tempY - y) < minGap)){
    			 index = i;
    			 break;
    		 }
    	 } // end loop
    	 cPointD = index;
    	 return cPointD;
     }
     
     public int findTensionPoint(double x, double y) {
    	 int tPointD = -1;
    	 int index = -1;
    	 double minGap = 3;
    	 ControlPoint temp;
    	 double tempX, tempY;
    	 
    	 //find point that is close enough to delete
    	 for(int i = 0; i < controlPoints.size(); i ++) {
    		 temp = controlPoints.get(i);
    		 tempX = temp.getTension1().getX();
    		 tempY = temp.getTension1().getY();
    		 if((Math.abs(tempX - x) < minGap) && (Math.abs(tempY - y) < minGap)){
    			 index = i;
    			 //extra code to handle two tension points
    			 //first tension point represented with an even value
    			 index = index * 2;
    	    	 tPointD = index;
    			 break;
    		 }
    	 }
    	 for(int i = 0; i < controlPoints.size(); i ++) {
    		 temp = controlPoints.get(i);
    		 tempX = temp.getTension2().getX();
    		 tempY = temp.getTension2().getY();
    		 if((Math.abs(tempX - x) < minGap) && (Math.abs(tempY - y) < minGap)){
    			 index = i;
    			 //extra code to handle two tension points
    			 //second tension point represented with an odd number
    			 index = index * 2;
    			 index++;
    	    	 tPointD = index;
    			 break;
    		 }
    	 }
    	 return tPointD;
     }
     
     public void moveContourPoint(double x, double y, ControlPoint point) {
    	 dragTensionPoint(x - point.getX(), y - point.getY(), point.getTension1());
    	 dragTensionPoint(x - point.getX(), y - point.getY(), point.getTension2());
    	 
    	 point.setX(x);
    	 point.setY(y);
    	 generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
     }
     
     public void moveTensionPoint(double x, double y, TensionPoint point) {
    	 //find out which tension point this is before modifying it
    	 int index;
    	 if(point.getControlPoint().getTension1().getX() == point.getX()) {
    		 index = 1;
    	 } else {
    		 index = 2;
    	 }
    	 
		 point.setX(x);
		 point.setY(y);
    	 
		 //send the partner tension point
		 if(index == 1) {
			 alignTensionPoint(x, y, point.getControlPoint().getTension2());
		 } else {
			 alignTensionPoint(x, y, point.getControlPoint().getTension1());
		 }
		 
    	 generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
     }
     
     public void alignTensionPoint(double x, double y, TensionPoint point) {

    	 //calculate the distance vector to the partner point
    	 double distVectorX = point.getControlPoint().getX() - x;
    	 double distVectorY = point.getControlPoint().getY() - y; 
    	 
    	 double magnitude = Math.sqrt((distVectorX * distVectorX) + (distVectorY * distVectorY));
    	 
    	 //calculate the unit vector to the partner point
    	 double unitVectorX = distVectorX / magnitude;
    	 double unitVectorY = distVectorY / magnitude;

    	 //calculate distance vector from control point to current tension point
    	 double distVectorX2 = point.getX() - point.getControlPoint().getX();
    	 double distVectorY2 = point.getY() - point.getControlPoint().getY();
    	 
    	 //calculate the magnitude of the current tension point to control point
    	 double magnitude2 = Math.sqrt((distVectorX2 * distVectorX2) + (distVectorY2 * distVectorY2));
    	 
    	 //calculate the new point values of the tension point with the unit vector and magnitude
    	 double finalPointX = unitVectorX * magnitude2;
    	 double finalPointY = unitVectorY * magnitude2;
    	 
    	 //set the values
    	 point.setX(point.getControlPoint().getX() + finalPointX);
    	 point.setY(point.getControlPoint().getY() + finalPointY);
     }
     
     public void dragTensionPoint(double x, double y, TensionPoint point) {
    	 point.setX(point.getX() + x);
    	 point.setY(point.getY() + y);
     }
     
    /**
     * Throws IllegalArgumentException if x or y is less than zero
     *
     * @param x
     * @param y
     */
    protected void validateCoordinates(double x, double y) {
        if ((x >= 0) && (y >= 0)) {
            if (controlPoints.contains(new Point2D(x, y))) {
                String message = String.format(
                        "Coordinates (%1$f, %2$f) must be unique", x, y);
                throw new IllegalArgumentException(message);
            }
            return;
        } else {
            String message = String.format(
                    "Coordinates (%1$f, %2$f) must be nonnegative", x, y);
            throw new IllegalArgumentException(message);
        }
    }
    
    
    /**
     * loops through Vector3d list if the x and y coordinates are to close
     *  to each other then omit, because they either double clicked or just to close.
     * @param x double
     * @param y double
     * @return boolean
     */
    protected boolean userCoordinates(double x, double y){
    	
    	return true;
    }

    /**
     * Returns a copy of the list of the control points
     *
     * @return copy of the internal list
     */
    public List<ControlPoint> getControlPoints() {
        return new Vector<ControlPoint>(controlPoints);
    }

    /**
     * Returns a copy of the list of points generated to create a smooth curve.
     *
     * @return copy of the internal list
     */
    public List<Point> getGeneratedPoints() {
        return new Vector<Point>(generatedPoints);
    }
    
    public List<TensionPoint> getTensionPoints() {
    	List<TensionPoint> tensionPoints = new ArrayList<TensionPoint>();
    	for (ControlPoint controlPoint : controlPoints)
    	{
    		tensionPoints.add(controlPoint.getTension1());
    		tensionPoints.add(controlPoint.getTension2());
    	}
		return tensionPoints;
    }

    /**
     * Get whether this contour is a closed curve or open curve
     *
     * @return boolean value is true if the curve is closed
     */
    public boolean isClosedCurve() {
        return Contour.IS_CLOSED_CONTOUR.get(contourType);
    }

    /**
     * Set the contour type
     *
     * @param contourTypeIn the type of contour
     */
    public void setContourType(Type contourTypeIn) {
        contourType = contourTypeIn;
        generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
    }

    public Type getContourType() {
        return contourType;
    }
    

    public Integer getIntFromType() {
        return Contour.TYPE_TO_INTEGER.get(getContourType());
    }

    public static Type getTypeFromInt(int contourType) {
        return Contour.INTEGER_TO_TYPE.get(contourType);
    }
    
    public Integer getIntFromTypeControlPoints() {
    	return Contour.TYPE_TO_CONTROL_INTEGER.get(getContourType());
    }
    
    public static boolean isControlPointFromInt(int contourType) { 
    	return Contour.IS_CONTROL_POINT_CONTOUR.get(contourType);
    }

    public enum Type {
//        DEFAULT, DEFAULT_CLOSED, // Example of something that is always a closed
//        // contour
//        DEFAULT_OPEN, 
    	
        LV_EPI ("LV EPI","Epicardial", "LV"), 
        LV_ENDO ("LV ENDO","Endocardial", "LV"), 
        RV_EPI ("RV EPI", "Epicardial", "RV"), 
        RV_ENDO ("RV ENDO","Endocardial", "RV"), 
        LA_EPI ("LA EPI","Epicardial" ,"LA"), 
        LA_ENDO ("LA ENDO","Endocardial", "LA"), 
        RA_EPI ("RA EPI","Epicardial", "RA"), 
        RA_ENDO ("RA ENDO","Endocardial", "RA");
        
		private String name;
		private String abbv;
		private String group;
        private Type(String abbv, String str, String group){
        	this.name = str;
        	this.abbv = abbv;
        	this.group = group;
        }
        
        public String getName(){
        	return name;
        }
        public String getAbbv(){
        	return abbv;
        }
        
        public String getGroup(){
        	return group;
        }
    }

    public String toString() {
        String output = "";
        switch (this.getContourType()) {
            case LA_ENDO:
                output += "LEFT ATRIUM ENDOCARDIAL";
                break;
            case LA_EPI:
                output += "LEFT ATRIUM EPICARDIAL";
                break;
            case LV_ENDO:
                output += "LEFT VENTRICLE ENDOCARDIAL";
                break;
            case LV_EPI:
                output += "LEFT VENTRICLE EPICARDIAL";
                break;
            case RA_ENDO:
                output += "RIGHT ATRIUM ENDOCARDIAL";
                break;
            case RA_EPI:
                output += "RIGHT ATRIUM EPICARDIAL";
                break;
            case RV_ENDO:
                output += "RIGHT VENTRICLE ENDOCARDIAL";
                break;
            case RV_EPI:
                output += "RIGHT VENTRICLE EPICARDIAL";
                break;
            default:
                output += "invalid type";

        }
        return output;
    }

    public static final Map<Type, Boolean> IS_CLOSED_CONTOUR;
    public static final Map<Integer, Boolean> IS_CONTROL_POINT_CONTOUR;
    public static final Map<Type, Integer> TYPE_TO_INTEGER;
    public static final Map<Integer, Type> INTEGER_TO_TYPE;
    public static final Map<Type, Integer> TYPE_TO_CONTROL_INTEGER;

    static {
        Map<Type, Boolean> tempIsClosedContour = new HashMap<Type, Boolean>();


        tempIsClosedContour.put(Type.LA_ENDO, Boolean.TRUE);
        tempIsClosedContour.put(Type.LA_EPI, Boolean.TRUE);
        tempIsClosedContour.put(Type.LV_ENDO, Boolean.TRUE);
        tempIsClosedContour.put(Type.LV_EPI, Boolean.TRUE);
        tempIsClosedContour.put(Type.RA_ENDO, Boolean.FALSE);
        tempIsClosedContour.put(Type.RA_EPI, Boolean.FALSE);
        tempIsClosedContour.put(Type.RV_ENDO, Boolean.FALSE);
        tempIsClosedContour.put(Type.RV_EPI, Boolean.FALSE);
        IS_CLOSED_CONTOUR = Collections.unmodifiableMap(tempIsClosedContour);
        
        
        Map<Integer, Boolean> tempIsControlPointContour = new HashMap<Integer, Boolean>();
        // Control Points
        tempIsControlPointContour.put(1, Boolean.TRUE);
        tempIsControlPointContour.put(2, Boolean.TRUE);
        tempIsControlPointContour.put(3, Boolean.TRUE);
        tempIsControlPointContour.put(16, Boolean.TRUE);
        tempIsControlPointContour.put(17, Boolean.TRUE);
        tempIsControlPointContour.put(32, Boolean.TRUE);
        tempIsControlPointContour.put(33, Boolean.TRUE);
        tempIsControlPointContour.put(64, Boolean.TRUE);
        tempIsControlPointContour.put(65, Boolean.TRUE);
        tempIsControlPointContour.put(80, Boolean.TRUE);
        tempIsControlPointContour.put(81, Boolean.TRUE);
        
        // Generated Points
        tempIsControlPointContour.put(4, Boolean.FALSE);
        tempIsControlPointContour.put(5, Boolean.FALSE);
        tempIsControlPointContour.put(6, Boolean.FALSE);
        tempIsControlPointContour.put(20, Boolean.FALSE);
        tempIsControlPointContour.put(21, Boolean.FALSE);
        tempIsControlPointContour.put(36, Boolean.FALSE);
        tempIsControlPointContour.put(37, Boolean.FALSE);
        tempIsControlPointContour.put(68, Boolean.FALSE);
        tempIsControlPointContour.put(69, Boolean.FALSE);
        tempIsControlPointContour.put(84, Boolean.FALSE);
        tempIsControlPointContour.put(85, Boolean.FALSE);
        IS_CONTROL_POINT_CONTOUR = Collections.unmodifiableMap(tempIsControlPointContour);

        
        Map<Integer, Type> tempIntegerToType = new HashMap<Integer, Type>();

        tempIntegerToType.put(16, Type.LA_ENDO);		//LA2
        tempIntegerToType.put(17, Type.LA_EPI);			//LA2
        tempIntegerToType.put(32, Type.LV_ENDO);		//LA4
        tempIntegerToType.put(33, Type.LV_EPI);			//LA4
        tempIntegerToType.put(64, Type.RA_ENDO);		//FP1
        tempIntegerToType.put(65, Type.RA_EPI);			//FP1
        tempIntegerToType.put(80, Type.RV_ENDO);		//FP2
        tempIntegerToType.put(81, Type.RV_EPI);			//FP2
        
        //Generated Points
        tempIntegerToType.put(20, Type.LA_ENDO);
        tempIntegerToType.put(21, Type.LA_EPI);
        tempIntegerToType.put(36, Type.LV_ENDO);
        tempIntegerToType.put(37, Type.LV_EPI);
        tempIntegerToType.put(68, Type.RA_ENDO);
        tempIntegerToType.put(69, Type.RA_EPI);
        tempIntegerToType.put(84, Type.RV_ENDO);
        tempIntegerToType.put(85, Type.RV_EPI);
        INTEGER_TO_TYPE = Collections.unmodifiableMap(tempIntegerToType);
        
        
        Map<Type, Integer> tempTypeToInteger = new HashMap<Type, Integer>();
        tempTypeToInteger.put(Type.LA_ENDO, 20);		//LA2
        tempTypeToInteger.put(Type.LA_EPI, 21);			//LA2
        tempTypeToInteger.put(Type.LV_ENDO, 36);		//LA4
        tempTypeToInteger.put(Type.LV_EPI, 37);			//LA4
        tempTypeToInteger.put(Type.RA_ENDO, 68);		//FP1
        tempTypeToInteger.put(Type.RA_EPI, 69);			//FP1
        tempTypeToInteger.put(Type.RV_ENDO, 84);		//FP2
        tempTypeToInteger.put(Type.RV_EPI, 85);			//FP2
        TYPE_TO_INTEGER = Collections.unmodifiableMap(tempTypeToInteger);
        
        
        Map<Type, Integer> tempTypeToControlInteger = new HashMap<Type, Integer>();
        // TODO fine tune exact types/integer values
        tempTypeToControlInteger.put(Type.LA_ENDO, 16);			//LA2
        tempTypeToControlInteger.put(Type.LA_EPI, 17);			//LA2
        tempTypeToControlInteger.put(Type.LV_ENDO, 32);			//LA4
        tempTypeToControlInteger.put(Type.LV_EPI, 33);			//LA4
        tempTypeToControlInteger.put(Type.RA_ENDO, 64);			//FP1
        tempTypeToControlInteger.put(Type.RA_EPI, 65);			//FP1
        tempTypeToControlInteger.put(Type.RV_ENDO, 80);			//FP2			
        tempTypeToControlInteger.put(Type.RV_EPI, 81);			//FP2
        TYPE_TO_CONTROL_INTEGER = Collections.unmodifiableMap(tempTypeToControlInteger);
    }
    
    public void updateTensionPoints() {
    	tensionPoints.clear();    	
    	for(ControlPoint point : controlPoints) {
    		tensionPoints.add(point.getTension1());
    		tensionPoints.add(point.getTension2());
    	}
    }
    
}
