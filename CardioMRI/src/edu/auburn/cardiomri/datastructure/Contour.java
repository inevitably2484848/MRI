package edu.auburn.cardiomri.datastructure;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

public class Contour implements Shape {

    // XY coordinates of points the user clicked
    private List<javafx.geometry.Point2D> controlPoints;

    // XY coordinates of points that look like a smooth curve is drawn between
    // each of the control points
    private List<javafx.geometry.Point2D> generatedPoints;

    /**
     * Sets controlPoints to a predefined set of points.
     */
    public Contour() {
        controlPoints = new Vector<javafx.geometry.Point2D>();
        generatedPoints = new Vector<javafx.geometry.Point2D>();

        // TODO Remove this when it becomes easier to add points
        controlPoints.addAll(SIMPLE_CONTOUR);
    }

    @Override
    public boolean contains(Point2D p) {
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

        for (javafx.geometry.Point2D point : controlPoints) {
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

        for (javafx.geometry.Point2D point : controlPoints) {
            minX = Math.min(point.getX(), minX);
            minY = Math.min(point.getY(), minY);
            maxX = Math.max(point.getX(), maxX);
            maxY = Math.max(point.getY(), maxY);
        }

        return new Rectangle2D.Double(minX, minY, (maxX - minX), (maxY - minY));
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        // Just for the spike
        // TODO Don't ignore the transform
        controlPoints = SIMPLE_CONTOUR;

        return new PathIterator() {
            private int index = 0;

            @Override
            public void next() {
                index += 1; // Is this right?
            }

            @Override
            public boolean isDone() {
                // TODO Make sure controlPoints isn't null
                return index > controlPoints.size();
            }

            @Override
            public int getWindingRule() {
                return WIND_NON_ZERO;
            }

            @Override
            public int currentSegment(double[] coords) {
                if (index == 0) {
                    coords[0] = controlPoints.get(0).getX();
                    coords[1] = controlPoints.get(0).getY();
                    return SEG_MOVETO;
                } else if ((index > 0) && (index < controlPoints.size())) {
                    // First control point
                    coords[0] = controlPoints.get(index - 1).getX();
                    coords[1] = controlPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = controlPoints.get(index).getX();
                    coords[3] = controlPoints.get(index).getY();
                    return SEG_QUADTO;
                } else if (index == controlPoints.size()) {
                    // First control point
                    coords[0] = controlPoints.get(index - 1).getX();
                    coords[1] = controlPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = controlPoints.get(0).getX();
                    coords[3] = controlPoints.get(0).getY();
                    return SEG_QUADTO;
                } else {
                    return SEG_CLOSE;
                }
            }

            @Override
            public int currentSegment(float[] coords) {
                if (index == 0) {
                    coords[0] = (float) controlPoints.get(0).getX();
                    coords[1] = (float) controlPoints.get(0).getY();
                    return SEG_MOVETO;
                } else if ((index > 0) && (index < controlPoints.size())) {
                    // First control point
                    coords[0] = (float) controlPoints.get(index - 1).getX();
                    coords[1] = (float) controlPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = (float) controlPoints.get(index).getX();
                    coords[3] = (float) controlPoints.get(index).getY();
                    return SEG_QUADTO;
                } else if (index == controlPoints.size()) {
                    // First control point
                    coords[0] = (float) controlPoints.get(index - 1).getX();
                    coords[1] = (float) controlPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = (float) controlPoints.get(0).getX();
                    coords[3] = (float) controlPoints.get(0).getY();
                    return SEG_QUADTO;
                } else {
                    return SEG_CLOSE;
                }
            }
        };
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        // TODO Don't ignore flatness
        return getPathIterator(at);
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
        if ((x >= 0) && (y >= 0)) {
            controlPoints.add(new javafx.geometry.Point2D(x, y));
        }
    }

    /**
     * Returns a list of the control points
     *
     * @return copy of the internal list
     */
    public List<javafx.geometry.Point2D> getControlPoints() {
        return new Vector<javafx.geometry.Point2D>(controlPoints);
    }

    /**
     * Returns a list of the points generated to create a smooth curve
     *
     * @return copy of the internal list
     */
    public List<javafx.geometry.Point2D> getGeneratedPoints() {
        return new Vector<javafx.geometry.Point2D>(generatedPoints);
    }

    public static final List<javafx.geometry.Point2D> SIMPLE_CONTOUR;

    static {
        SIMPLE_CONTOUR = new Vector<javafx.geometry.Point2D>();

        // Add points (0, 0) and (10, 10)
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(0.0, 0.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(50.0, 50.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(100.0, 100.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(150.0, 175.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(200.0, 200.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(150.0, 190.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(100.0, 175.0));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(50.0, 75.0));
    }
}
