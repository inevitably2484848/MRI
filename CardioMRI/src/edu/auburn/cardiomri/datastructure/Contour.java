package edu.auburn.cardiomri.datastructure;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point2D;
import edu.auburn.cardiomri.lib.ContourCalc;

public class Contour implements Shape {

    // XY coordinates of points the user clicked
    private List<Point2D> controlPoints;

    // XY coordinates of points that look like a smooth curve is drawn between
    // each of the control points
    private List<Point2D> generatedPoints;

    private Type contourType;

    public Contour(Type contourTypeIn) {
        this();
        this.contourType = contourTypeIn;
    }

    /**
     * Sets controlPoints to a predefined set of points.
     */
    private Contour() {
        this.controlPoints = new Vector<Point2D>();
        this.generatedPoints = new Vector<Point2D>();
    }

    public void setControlPoints(List<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }

        List<Point2D> newList = new Vector<Point2D>();
        for (Point2D point : points) {
            validateCoordinates(point.getX(), point.getY());
            newList.add(new Point2D(point.getX(), point.getY()));
        }

        this.controlPoints = newList;
    }

    public void setGeneratedPoints(List<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }

        List<Point2D> newList = new Vector<Point2D>();
        for (Point2D point : points) {
            validateCoordinates(point.getX(), point.getY());
            newList.add(new Point2D(point.getX(), point.getY()));
        }

        this.generatedPoints = newList;
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

        for (Point2D point : this.controlPoints) {
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

        for (Point2D point : this.controlPoints) {
            minX = Math.min(point.getX(), minX);
            minY = Math.min(point.getY(), minY);
            maxX = Math.max(point.getX(), maxX);
            maxY = Math.max(point.getY(), maxY);
        }

        return new Rectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        // System.out.println(at.getScaleX());
        // System.out.println(at.getScaleY());
        // TODO Don't ignore the transform

        this.setGeneratedPoints(ContourCalc.generate(this.controlPoints, this.isClosedCurve()));

        return new PathIterator() {
            private int index = 0;

            @Override
            public void next() {
                this.index += 1; // Is this right?
            }

            @Override
            public boolean isDone() {
                // TODO Make sure controlPoints isn't null
                return this.index >= Contour.this.generatedPoints.size();
            }

            @Override
            public int getWindingRule() {
                return PathIterator.WIND_NON_ZERO;
            }

            @Override
            public int currentSegment(double[] coords) {
                if (this.index == 0) {
                    coords[0] = Contour.this.generatedPoints.get(0).getX();
                    coords[1] = Contour.this.generatedPoints.get(0).getY();
                    return PathIterator.SEG_MOVETO;
                } else if ((this.index > 0)) { // && (this.index <
                                               // Contour.this.generatedPoints.size()))
                                               // {
                    coords[0] = Contour.this.generatedPoints.get(this.index)
                            .getX();
                    coords[1] = Contour.this.generatedPoints.get(this.index)
                            .getY();
                    return PathIterator.SEG_LINETO;
                } else {
                    return PathIterator.SEG_CLOSE;
                }
            }

            @Override
            public int currentSegment(float[] coords) {
                if (this.index == 0) {
                    coords[0] = (float) Contour.this.generatedPoints.get(0)
                            .getX();
                    coords[1] = (float) Contour.this.generatedPoints.get(0)
                            .getY();
                    return PathIterator.SEG_MOVETO;
                } else if (this.index > 0) {
                    coords[0] = (float) Contour.this.generatedPoints.get(
                            this.index).getX();
                    coords[1] = (float) Contour.this.generatedPoints.get(
                            this.index).getY();
                    return PathIterator.SEG_LINETO;
                } else {
                    return PathIterator.SEG_CLOSE;
                }
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
     * Adds a control point to the contour with the given coordinates
     *
     * @param x nonnegative double value
     * @param y nonnegative double value
     */
    public void addControlPoint(double x, double y) {
        validateCoordinates(x, y);
        this.controlPoints.add(new Point2D(x, y));
    }

    /**
     * Throws IllegalArgumentException if x or y is less than zero
     *
     * @param x
     * @param y
     */
    private void validateCoordinates(double x, double y) {
        if ((x >= 0) && (y >= 0)) {
            return;
        } else {
            String message = String.format(
                    "Coordinates (%1$f, %2$f) must be nonnegative", x, y);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Returns a copy of the list of the control points
     *
     * @return copy of the internal list
     */
    public List<Point2D> getControlPoints() {
        return new Vector<Point2D>(this.controlPoints);
    }

    /**
     * Returns a copy list of the points generated to create a smooth curve. The
     * {@link ContourCalc#generate} function is called before the points are
     * returned.
     *
     * @return copy of the internal list
     */
    public List<Point2D> getGeneratedPoints() {
        // TODO Can this be changed so that it's only called when necessary?
        this.setGeneratedPoints(ContourCalc.generate(this.controlPoints, this.isClosedCurve()));
        return new Vector<Point2D>(this.generatedPoints);
    }

    /**
     * Get whether this contour is a closed curve or open curve
     *
     * @return boolean value is true if the curve is closed
     */
    public boolean isClosedCurve() {
        return Contour.IS_CLOSED_CONTOUR.get(this.contourType);
    }

    /**
     * Set the contour type
     *
     * @param contourTypeIn the type of contour
     */
    public void setContourType(Type contourTypeIn) {
        this.contourType = contourTypeIn;
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

    public enum Type {
        DEFAULT, DEFAULT_CLOSED, // Example of something that is always a closed
                                 // contour
        DEFAULT_OPEN // Example of something that is always an open contour
    }


    public static final Map<Type, Boolean> IS_CLOSED_CONTOUR;
    public static final Map<Type, Integer> TYPE_TO_INTEGER;
    public static final Map<Integer, Type> INTEGER_TO_TYPE;

    static {
        Map<Type, Boolean> tempIsClosedContour = new HashMap<Type, Boolean>();

        tempIsClosedContour.put(Type.DEFAULT, Boolean.TRUE);
        tempIsClosedContour.put(Type.DEFAULT_CLOSED, Boolean.TRUE);
        tempIsClosedContour.put(Type.DEFAULT_OPEN, Boolean.FALSE);
        IS_CLOSED_CONTOUR = Collections.unmodifiableMap(tempIsClosedContour);

        Map<Type, Integer> tempTypeToInteger = new HashMap<Type, Integer>();
        // TODO fine tune exact types/integer values
        tempTypeToInteger.put(Type.DEFAULT, 7);
        tempTypeToInteger.put(Type.DEFAULT_CLOSED, 8);
        tempTypeToInteger.put(Type.DEFAULT_OPEN, 4);
        TYPE_TO_INTEGER = Collections.unmodifiableMap(tempTypeToInteger);
        
        Map<Integer, Type> tempIntegerToType = new HashMap<Integer, Type>();
        tempIntegerToType.put(7, Type.DEFAULT);
        tempIntegerToType.put(4, Type.DEFAULT_OPEN);
        tempIntegerToType.put(8, Type.DEFAULT_CLOSED);
        INTEGER_TO_TYPE = Collections.unmodifiableMap(tempIntegerToType);

    }
}
