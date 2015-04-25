package edu.auburn.cardiomri.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import javafx.geometry.Point2D;
import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

public final class ContourCalc {
    /**
     * Distance between each of the generated points
     */
    public static float SEPARATION_DISTANCE = 1.0f; // Distance between each of
                                                    // the generated points

    /**
     * Calculates the centroid by averaging the x,y coordinates in the list
     *
     * @param points List of points used to find the centroid
     * @return The centroid of the points
     */
    public static Point2D calcCentroid(List<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (points.size() == 0) {
            throw new IllegalArgumentException(
                    "List must have at least 1 point");
        }

        double averageX = 0, averageY = 0;
        for (Point2D p : points) {
            averageX += p.getX();
            averageY += p.getY();
        }
        averageX /= points.size();
        averageY /= points.size();

        Point2D centroid = new Point2D(averageX, averageY);
        return centroid;
    }

    /**
     * Sort the points in clockwise order.
     *
     * @param points List of points to sort
     */
    public static void sortPoints(List<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (points.size() == 0) {
            return;
        }

        Point2D centroid = ContourCalc.calcCentroid(points);

        Collections.sort(points, new Comparator<Point2D>() {

            /**
             * Returns negative integer if point p1 is comes before point p2 in
             * the contour?
             */
            @Override
            public int compare(Point2D p1, Point2D p2) {
                double thetaP1 = Math.atan2(p1.getY() - centroid.getY(),
                        p1.getX() - centroid.getX());
                double thetaP2 = Math.atan2(p2.getY() - centroid.getY(),
                        p2.getX() - centroid.getX());
                double delta = thetaP2 - thetaP1;
                return (int) Math.signum(delta);
            }
        });
    }

    /**
     * Connects the control points with a smooth curve and fills generatedPoints
     * with points on that curve. <br />
     * <ul>
     * <li>The controlPoints list should have at least 2 points.</li>
     * <li>Control points are sorted, so the order of the points in
     * controlPoints might change.</li>
     * <li>The points in the generated list are separated by a distance of 1.</li>
     * <li>When isClosed is true, the function generates a closed curve by
     * sorting, then repeating the sorted list as the input for generating a
     * curve.</li>
     * </ul>
     *
     * @param controlPoints List with the control points
     * @param isClosed Whether the curve should be closed or left open
     * @return new vector with the generated points
     */
    public static List<Point2D> generate(List<Point2D> controlPoints,
            boolean isClosed) {
        if (controlPoints == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (controlPoints.size() < 3) {
            return new Vector<Point2D>(controlPoints);
        }

        Spline2D spline = getSplineFromControlPoints(controlPoints, isClosed);

        List<Vec2D> genPoints = spline
                .getDecimatedVertices(ContourCalc.SEPARATION_DISTANCE);

        List<Point2D> generatedPoints = new Vector<Point2D>();
        for (Vec2D point : genPoints) {
            generatedPoints.add(new Point2D(point.x, point.y));
        }

        return generatedPoints;
    }

    /**
     * Compute the change in arc length if the given point is added to the contour.
     * 
     * @param contour
     * @param newPoint
     * @return
     */
    public static float getDeltaArcLength(Contour contour, Point2D newPoint) {
        List<Point2D> originalList = contour.getControlPoints();
        List<Point2D> modifiedList = contour.getControlPoints();
        modifiedList.add(newPoint);

        Spline2D original = getSplineFromControlPoints(originalList,
                contour.isClosedCurve());
        original.getDecimatedVertices(SEPARATION_DISTANCE);
        Spline2D modified = getSplineFromControlPoints(modifiedList,
                contour.isClosedCurve());
        modified.getDecimatedVertices(SEPARATION_DISTANCE);

        return modified.getEstimatedArcLength()
                - original.getEstimatedArcLength();
    }

    private static Spline2D getSplineFromControlPoints(
            List<Point2D> controlPoints, boolean isClosed) {
        ContourCalc.sortPoints(controlPoints);

        List<Point2D> rawPoints = new Vector<Point2D>(controlPoints);
        if (isClosed) {
            rawPoints.add(rawPoints.get(0));
        }

        Vec2D[] points = new Vec2D[rawPoints.size()];
        Point2D point2D;
        for (int i = 0; i < rawPoints.size(); i++) {
            point2D = rawPoints.get(i);
            points[i] = new Vec2D((float) point2D.getX(),
                    (float) point2D.getY());
        }

        Spline2D spline = new Spline2D(points);
        return spline;
    }
}
