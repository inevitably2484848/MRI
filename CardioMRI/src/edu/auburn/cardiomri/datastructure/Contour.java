package edu.auburn.cardiomri.datastructure;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
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

    // XY coordinates of points the user clicked
    private List<Vector3d> controlPoints;

    // XY coordinates of points that look like a smooth curve is drawn between
    // each of the control points
    private List<Vector3d> generatedPoints;

    private Type contourType;

    public Contour(Type contourTypeIn) {
        this();
        contourType = contourTypeIn;
    }

    /**
     * Sets controlPoints to a predefined set of points.
     */
    private Contour() {
        controlPoints = new Vector<Vector3d>();
        generatedPoints = new Vector<Vector3d>();
    }

    public void setControlPoints(List<Vector3d> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }

        List<Vector3d> newList = new Vector<Vector3d>();
        for (Vector3d point : points) {
            validateCoordinates(point.getX(), point.getY());
            newList.add(new Vector3d(point.getX(), point.getY(), 0));
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

        for (Vector3d point : controlPoints) {
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

        for (Vector3d point : controlPoints) {
            minX = Math.min(point.getX(), minX);
            minY = Math.min(point.getY(), minY);
            maxX = Math.max(point.getX(), maxX);
            maxY = Math.max(point.getY(), maxY);
        }

        return new Rectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }

    private Vector3d transformCoordinates(AffineTransform at, Vector3d source) {
        java.awt.geom.Point2D transformed = new java.awt.geom.Point2D.Double();
        at.transform(
                new java.awt.geom.Point2D.Double(source.getX(), source.getY()),
                transformed);
        return new Vector3d(transformed.getX(), transformed.getY(), 0);
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
                    Vector3d point = transformCoordinates(at,
                            generatedPoints.get(0));
                    coords[0] = point.getX();
                    coords[1] = point.getY();
                    return PathIterator.SEG_MOVETO;
                } else if ((index > 0)) {
                    Vector3d point = transformCoordinates(at,
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

    /**
     * Adds a control point to the contour List with the given coordinates
     *
     * @param x nonnegative double value
     * @param y nonnegative double value
     */
    public void addControlPoint(double x, double y) {
        validateCoordinates(x, y);
        
        if(notToClose(x,y)){
        	controlPoints.add(new Vector3d(x, y, 0));
        }
        generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve());
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

    	 for(Vector3d controlP : controlPoints){
    		if(((Math.abs(controlP.getX() - x)) <= dMinGap) && ((Math.abs(controlP.getY() - y) <= dMinGap))){
    			return false;
    		}
    	 } // end for loop
    	 return bToClose;
     } // end isToClose
    
     /** ----------------------------------------------------------------------
      * deleteControlPoint(x,y): removes selected control point from controlPoint List. 
      * return true if deleted.
      * @author KulW
      * @param x double
      * @param y double
      * @return boolean
      * ---------------------------------------------------------------------*/
     public boolean deleteControlPoint(double x, double y){
    	 double minGap = 3;
    	 boolean bDeleted = false;
    	 int index = -1;
    	 Vector3d temp;
    	 double tempX, tempY;
    	 
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
    		 controlPoints.remove(index); //remove from list
    		 generatedPoints = ContourCalc.generate(controlPoints, isClosedCurve()); //refresh curve
    		 bDeleted = true;
    	 }
    	 
    	 return bDeleted;
    	 
     } // end deleteControlPoint
    

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
    public List<Vector3d> getControlPoints() {
        return new Vector<Vector3d>(controlPoints);
    }

    /**
     * Returns a copy of the list of points generated to create a smooth curve.
     *
     * @return copy of the internal list
     */
    public List<Vector3d> getGeneratedPoints() {
        return new Vector<Vector3d>(generatedPoints);
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

    public enum Type {
        DEFAULT, DEFAULT_CLOSED, // Example of something that is always a closed
        // contour
        DEFAULT_OPEN, LV_EPI, LV_ENDO, RV_EPI, RV_ENDO, LA_EPI, LA_ENDO, RA_EPI, RA_ENDO
    }

    public String toString() {
        String output = "";
        switch (this.getContourType()) {
            case DEFAULT:
                output += "DEFAULT";
                break;
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
    public static final Map<Type, Integer> TYPE_TO_INTEGER;
    public static final Map<Integer, Type> INTEGER_TO_TYPE;
    public static final Map<Type, Integer> TYPE_TO_CONTROL_INTEGER;

    static {
        Map<Type, Boolean> tempIsClosedContour = new HashMap<Type, Boolean>();

        tempIsClosedContour.put(Type.DEFAULT, Boolean.TRUE);
        tempIsClosedContour.put(Type.DEFAULT_CLOSED, Boolean.TRUE);
        tempIsClosedContour.put(Type.DEFAULT_OPEN, Boolean.FALSE);
        tempIsClosedContour.put(Type.LA_ENDO, Boolean.TRUE);
        tempIsClosedContour.put(Type.LA_EPI, Boolean.TRUE);
        tempIsClosedContour.put(Type.LV_ENDO, Boolean.TRUE);
        tempIsClosedContour.put(Type.LV_EPI, Boolean.TRUE);
        tempIsClosedContour.put(Type.RA_ENDO, Boolean.FALSE);
        tempIsClosedContour.put(Type.RA_EPI, Boolean.FALSE);
        tempIsClosedContour.put(Type.RV_ENDO, Boolean.FALSE);
        tempIsClosedContour.put(Type.RV_EPI, Boolean.FALSE);
        IS_CLOSED_CONTOUR = Collections.unmodifiableMap(tempIsClosedContour);

        Map<Type, Integer> tempTypeToInteger = new HashMap<Type, Integer>();
        // TODO fine tune exact types/integer values
        tempTypeToInteger.put(Type.DEFAULT, 6);
        tempTypeToInteger.put(Type.DEFAULT_CLOSED, 4);
        tempTypeToInteger.put(Type.DEFAULT_OPEN, 5);
        tempTypeToInteger.put(Type.LA_ENDO, 20);		//LA2
        tempTypeToInteger.put(Type.LA_EPI, 21);			//LA2
        tempTypeToInteger.put(Type.LV_ENDO, 36);		//LA4
        tempTypeToInteger.put(Type.LV_EPI, 37);			//LA4
        tempTypeToInteger.put(Type.RA_ENDO, 68);		//FP1
        tempTypeToInteger.put(Type.RA_EPI, 69);			//FP1
        tempTypeToInteger.put(Type.RV_ENDO, 84);		//FP2
        tempTypeToInteger.put(Type.RV_EPI, 85);			//FP2
        TYPE_TO_INTEGER = Collections.unmodifiableMap(tempTypeToInteger);

        Map<Integer, Type> tempIntegerToType = new HashMap<Integer, Type>();
        tempIntegerToType.put(1, Type.DEFAULT);
        tempIntegerToType.put(3, Type.DEFAULT_OPEN);
        tempIntegerToType.put(2, Type.DEFAULT_CLOSED);
        tempIntegerToType.put(7, Type.LA_ENDO);
        tempIntegerToType.put(8, Type.LA_EPI);
        tempIntegerToType.put(9, Type.LV_ENDO);
        tempIntegerToType.put(10, Type.LV_EPI);
        tempIntegerToType.put(11, Type.RA_ENDO);
        tempIntegerToType.put(12, Type.RA_EPI);
        tempIntegerToType.put(13, Type.RV_ENDO);
        tempIntegerToType.put(14, Type.RV_EPI);
        INTEGER_TO_TYPE = Collections.unmodifiableMap(tempIntegerToType);
        
        Map<Type, Integer> tempTypeToControlInteger = new HashMap<Type, Integer>();
        // TODO fine tune exact types/integer values
        tempTypeToControlInteger.put(Type.DEFAULT, 9);
        tempTypeToControlInteger.put(Type.DEFAULT_CLOSED, 2);
        tempTypeToControlInteger.put(Type.DEFAULT_OPEN, 3);
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
    
    
}
