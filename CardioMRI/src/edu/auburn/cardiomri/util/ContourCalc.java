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
     * Method to generate a list of Vector3d objects that represent the 
     * Bezier curve
     * 
     * @param controlPoints - points generated by user clicks
     * @param isClosed
     * @return a list of Vector3d objects which contains all the (x,y) coordinate information to draw bezier curves
     */
    
    public static List<Vector3d> generate(List<Vector3d> controlPoints,
            boolean isClosed) {
        if (controlPoints == null) {
            throw new NullPointerException("List cannot be null");
        }
       if (controlPoints.size() < 3) {
            return new Vector<Vector3d>(controlPoints);
        }
        
       List<Vector3d> bezierPoints = new Vector<Vector3d>();
       ContourCalc.sortPoints(controlPoints);
       
       //iterates through control points 0-n, two at a time to draw the segments between the points
       
       for(int i = 0; i < controlPoints.size() - 1; i++) {
    	   
    	   Vector3d generatedPoint = getControlBPoint(controlPoints.get(i), controlPoints.get(i+1));
    	   
		   //calculates the distance vector to the control point
		   double dAX = generatedPoint.getX() - controlPoints.get(i).getX();
		   double dAY = generatedPoint.getY() - controlPoints.get(i).getY();
		   
		   //calculates the distance vector to the second points from the control point
		   double dBX = controlPoints.get(i+1).getX() - generatedPoint.getX();
		   double dBY = controlPoints.get(i+1).getY() - generatedPoint.getY();
		   
    	   for(int j = 0; j < 99; j++) {
    		   
    		   //calculates the point j% along the line from a to c
    		   double aX = controlPoints.get(i).getX() + (j * (dAX/100));
    		   double aY = controlPoints.get(i).getY() + (j * (dAY/100));
    		   
    		   //calculates the point j% along the line from c to b
    		   double bX = generatedPoint.getX() + (j * (dBX/100));
    		   double bY = generatedPoint.getY() + (j * (dBY/100));
    		   
    		   //calculates the distance for the inner line
    		   double dCX = bX - aX; 
    		   double dCY = bY - aY; 
    		   
    		   //calculates the final point on the inner line
    		   double fX = aX + (j * (dCX/100));
    		   double fY = aY + (j * (dCY/100));
    		   
    		   bezierPoints.add(new Vector3d(fX, fY, 0));
    	   }   
       }
       
       //final segment to connect final point and last point
	   Vector3d generatedPoint = getControlBPoint(controlPoints.get(controlPoints.size() - 1), controlPoints.get(0));
       
	   double dAX = generatedPoint.getX() - controlPoints.get(controlPoints.size() - 1).getX();
	   double dAY = generatedPoint.getY() - controlPoints.get(controlPoints.size() - 1).getY();
	   
	   //calculates the distance vector to the second points from the control point
	   double dBX = controlPoints.get(0).getX() - generatedPoint.getX();
	   double dBY = controlPoints.get(0).getY() - generatedPoint.getY();
	   
	   for(int j = 0; j < 99; j++) {
		   
		   //calculates the point j% along the line from a to c
		   double aX = controlPoints.get(controlPoints.size() - 1).getX() + (j * (dAX/100));
		   double aY = controlPoints.get(controlPoints.size() - 1).getY() + (j * (dAY/100));
		   
		   //calculates the point j% along the line from c to b
		   double bX = generatedPoint.getX() + (j * (dBX/100));
		   double bY = generatedPoint.getY() + (j * (dBY/100));
		   
		   //calculates the distance for the inner line
		   double dCX = bX - aX; 
		   double dCY = bY - aY; 
		   
		   //calculates the final point on the inner line
		   double fX = aX + (j * (dCX/100));
		   double fY = aY + (j * (dCY/100));
		   
		   bezierPoints.add(new Vector3d(fX, fY, 0));
	   }
	   
       return bezierPoints;
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
    
    /**
     * Calculates the Bezier control point between two control points
     * to generate the Bezier curve. Can be modified later to adjust the curve
     * 
     * @param first control point 
     * @param second control point
     * @return new Vector3d objet with the appropriate X, Y coordinate
     */
    public static Vector3d getControlBPoint(Vector3d a, Vector3d b) {
    	
    	//calculates the distance vector
    	double dX = b.getX() - a.getX();
    	double dY = b.getY() - a.getY();
    	
    	//calculates the midpoint
    	double mX = a.getX() + (dX / 2);
    	double mY = a.getY() + (dY / 2);
    	
    	//calculates the normal vector
    	double nX = -dY;
    	double nY = dX;
    	
    	//calculates the control point for the bezier curve
    	//at the moment we add 1/5 the distance, this can be modified to change the bezier curve

    	double controlX = mX + (nX / 5);
    	double controlY = mY + (nY / 5);
    	
    	Vector3d controlPoint = new Vector3d(controlX, controlY, 0);
    	return controlPoint;
    }
}
