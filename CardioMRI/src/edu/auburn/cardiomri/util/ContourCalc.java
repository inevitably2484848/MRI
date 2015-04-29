package edu.auburn.cardiomri.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javafx.geometry.Point2D;
import toxi.geom.Spline2D;
import toxi.geom.Vec2D;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Vector3d;

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
    public static Vector3d calcCentroid(List<Vector3d> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (points.size() == 0) {
            throw new IllegalArgumentException(
                    "List must have at least 1 point");
        }

        double averageX = 0, averageY = 0;
        for (Vector3d p : points) {
            averageX += p.getX();
            averageY += p.getY();
        }
        averageX /= points.size();
        averageY /= points.size();

        Vector3d centroid = new Vector3d(averageX, averageY, 0);
        return centroid;
    }

    /**
     * Sort the points in clockwise order.
     *
     * @param points List of points to sort
     */
    public static void sortPoints(List<Vector3d> points) {
        if (points == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (points.size() == 0) {
            return;
        }

        Vector3d centroid = ContourCalc.calcCentroid(points);

        Collections.sort(points, new Comparator<Vector3d>() {

            /**
             * Returns negative integer if point p1 is comes before point p2 in
             * the contour?
             */
            @Override
            public int compare(Vector3d p1, Vector3d p2) {
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
    public static List<Vector3d> generate(List<Vector3d> controlPoints,
            boolean isClosed) {
        if (controlPoints == null) {
            throw new NullPointerException("List cannot be null");
        }
        if (controlPoints.size() < 3) {
            return new Vector<Vector3d>(controlPoints);
        }

        Spline2D spline = getSplineFromControlPoints(controlPoints, isClosed);

        List<Vec2D> genPoints = spline
                .getDecimatedVertices(ContourCalc.SEPARATION_DISTANCE);

        List<Vector3d> generatedPoints = new Vector<Vector3d>();
        for (Vec2D point : genPoints) {
            generatedPoints.add(new Vector3d(point.x, point.y, 0));
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
    public static float getDeltaArcLength(Contour contour, Vector3d newPoint) {
        List<Vector3d> originalList = contour.getControlPoints();
        List<Vector3d> modifiedList = contour.getControlPoints();
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
            List<Vector3d> controlPoints, boolean isClosed) {
        ContourCalc.sortPoints(controlPoints);

        List<Vector3d> rawPoints = new Vector<Vector3d>(controlPoints);
        if (isClosed) {
            rawPoints.add(rawPoints.get(0));
        }

        Vec2D[] points = new Vec2D[rawPoints.size()];
        Vector3d point;
        for (int i = 0; i < rawPoints.size(); i++) {
            point = rawPoints.get(i);
            points[i] = new Vec2D((float) point.getX(),
                    (float) point.getY());
        }

        Spline2D spline = new Spline2D(points);
        return spline;
    }
}
