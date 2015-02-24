package edu.auburn.cardiomri.datastructure;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

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
        generatedPoints = new Vector<javafx.geometry.Point2D>();
        this.generate();

        return new PathIterator() {
            private int index = 0;

            @Override
            public void next() {
                index += 1; // Is this right?
            }

            @Override
            public boolean isDone() {
                // TODO Make sure controlPoints isn't null
                return index > generatedPoints.size();
            }

            @Override
            public int getWindingRule() {
                return WIND_NON_ZERO;
            }

            @Override
            public int currentSegment(double[] coords) {
                if (index == 0) {
                    coords[0] = generatedPoints.get(0).getX();
                    coords[1] = generatedPoints.get(0).getY();
                    return SEG_MOVETO;
                } else if ((index > 0) && (index < generatedPoints.size())) {
                    // First control point
                    coords[0] = generatedPoints.get(index - 1).getX();
                    coords[1] = generatedPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = generatedPoints.get(index).getX();
                    coords[3] = generatedPoints.get(index).getY();
                    return SEG_QUADTO;
                } else if (index == generatedPoints.size()) {
                    // First control point
                    coords[0] = generatedPoints.get(index - 1).getX();
                    coords[1] = generatedPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = generatedPoints.get(0).getX();
                    coords[3] = generatedPoints.get(0).getY();
                    return SEG_QUADTO;
                } else {
                    return SEG_CLOSE;
                }
            }

            @Override
            public int currentSegment(float[] coords) {
                if (index == 0) {
                    coords[0] = (float) generatedPoints.get(0).getX();
                    coords[1] = (float) generatedPoints.get(0).getY();
                    return SEG_MOVETO;
                } else if ((index > 0) && (index < generatedPoints.size())) {
                    // First control point
                    coords[0] = (float) generatedPoints.get(index - 1).getX();
                    coords[1] = (float) generatedPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = (float) generatedPoints.get(index).getX();
                    coords[3] = (float) generatedPoints.get(index).getY();
                    return SEG_QUADTO;
                } else if (index == generatedPoints.size()) {
                    // First control point
                    coords[0] = (float) generatedPoints.get(index - 1).getX();
                    coords[1] = (float) generatedPoints.get(index - 1).getY();

                    // Second control point
                    coords[2] = (float) generatedPoints.get(0).getX();
                    coords[3] = (float) generatedPoints.get(0).getY();
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
     * @param x
     *            nonnegative double value
     * @param y
     *            nonnegative double value
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
        this.generate();
        return new Vector<javafx.geometry.Point2D>(generatedPoints);
    }

    /**
     * Connects the control points with a smooth curve and fills generatedPoints
     * with points on that curve.
     */
    private void generate() {
        // TODO: sortControlPoints()
        // Find centroid, sort by angle to centroid

        generatedPoints.clear();
        if (controlPoints.size() < 2) {
            // generatedPoints.addAll(controlPoints);
            return;
        }

        Vec2D[] points = new Vec2D[controlPoints.size()];
        javafx.geometry.Point2D temp;
        for (int i = 0; i < controlPoints.size(); i++) {
            temp = controlPoints.get(i);
            points[i] = new Vec2D((float) temp.getX(), (float) temp.getY());
        }

        Spline2D spline = new Spline2D(points);
        List<Vec2D> genPoints = spline.getDecimatedVertices(SEPARATION_DISTANCE);
        // System.err.println(genPoints.size());

        for (Vec2D point : genPoints) {
            generatedPoints.add(new javafx.geometry.Point2D(point.x, point.y));
        }
    }

    public static final List<javafx.geometry.Point2D> SIMPLE_CONTOUR;
    private static int SEPARATION_DISTANCE = 1; // Distance between each
                                                // of the generated points

    static {
        SIMPLE_CONTOUR = new Vector<javafx.geometry.Point2D>();

        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(500, 400));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(570, 430));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(600, 500));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(570, 570));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(500, 600));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(430, 570));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(400, 500));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(430, 430));
        SIMPLE_CONTOUR.add(new javafx.geometry.Point2D(500, 400));
    }
}
