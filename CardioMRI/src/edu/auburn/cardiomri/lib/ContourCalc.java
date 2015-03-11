package edu.auburn.cardiomri.lib;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javafx.geometry.Point2D;
import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

public final class ContourCalc {
    private static int SEPARATION_DISTANCE = 1; // Distance between each of the
                                                // generated points

    /**
     * Calculates the centroid by averaging the x,y coordinates in the list
     *
     * @param points
     *            List of points used to find the centroid
     */
    public static Point2D calcCentroid(List<Point2D> points) {

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
     * Sort the points in radial(?) order. TODO: This doesn't actually work yet.
     *
     * @param points
     *            List of points to sort
     */
    public static void sortPoints(List<Point2D> points) {
        if (points == null || points.size() < 2) {
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
                double thetaP1 = Math.atan2(p1.getX() - centroid.getX(),
                        p2.getY() - centroid.getY());
                double thetaP2 = Math.atan2(p2.getX() - centroid.getX(),
                        p2.getY() - centroid.getY());
                double delta = thetaP1 - thetaP2;
                if (delta < 0.1) {
                    return 0;
                }
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
     * @param controlPoints
     *            List with the control points
     * @param generatedPoints
     *            List in which to put the generated points
     * @param isClosed
     *            Whether the curve should be closed or left open
     */
    public static void generate(List<Point2D> controlPoints,
            List<Point2D> generatedPoints, boolean isClosed) {
        if (controlPoints == null) {
            throw new NullPointerException("controlPoints list is null");
        }
        if (generatedPoints == null) {
            throw new NullPointerException("generatedPoints list is null");
        }

        // TODO: sortControlPoints()

        generatedPoints.clear();
        if (controlPoints.size() < 2) {
            return;
        }

        ContourCalc.sortPoints(controlPoints);

        List<Point2D> rawPoints;
        if (isClosed) {
            rawPoints = ContourCalc.createRepeatedList(controlPoints);
        } else {
            rawPoints = controlPoints;
        }

        Vec2D[] points = new Vec2D[rawPoints.size()];
        Point2D point2D;
        for (int i = 0; i < rawPoints.size(); i++) {
            point2D = rawPoints.get(i);
            points[i] = new Vec2D((float) point2D.getX(),
                    (float) point2D.getY());
        }

        Spline2D spline = new Spline2D(points);
        List<Vec2D> genPoints = spline
                .getDecimatedVertices(ContourCalc.SEPARATION_DISTANCE);

        for (Vec2D point : genPoints) {
            generatedPoints.add(new Point2D(point.x, point.y));
        }
    }

    /**
     * Copies a list of points so that the order of the original list is
     * preserved, but is repeated three times. This is used to create a curve
     * that stays smooth through the first and last control points.
     *
     * @param points
     *            Sorted list of control points
     * @return A list three times the size of the input
     */
    public static List<Point2D> createRepeatedList(List<Point2D> points) {
        int timesToRepeat = 3;
        List<Point2D> allPoints = new Vector<Point2D>(points.size()
                * timesToRepeat);

        for (int i = 0; i < timesToRepeat; i++) {
            allPoints.addAll(points);
        }
        return allPoints;
    }
}
