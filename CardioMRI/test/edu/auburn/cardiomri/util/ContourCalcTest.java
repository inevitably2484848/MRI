package edu.auburn.cardiomri.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.ControlPoint;
import edu.auburn.cardiomri.datastructure.Point;
import edu.auburn.cardiomri.datastructure.Vector3d;

public class ContourCalcTest {

    private Contour contour;
    private static float MAX_DELTA = 0.000001f;

    @Before
    public void setUp() {
        contour = new Contour(Contour.Type.LV_EPI);
        contour.addControlPoint(1, 2);
        contour.addControlPoint(3, 4);
        contour.addControlPoint(5, 6);
    }

    @After
    public void tearDown() {
        contour = null;
    }

    @Test(expected = NullPointerException.class)
    public void testCalcCentroidThrowsNullPointerExceptionWhenGivenNullList() {
        ContourCalc.calcCentroid(null);
        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcCentroidThrowsIllegalArgumentExceptionWhenGivenEmptyList() {
        ContourCalc.calcCentroid(new Vector<ControlPoint>());
        fail("Exception not thrown");
    }

    @Test
    public void testCalcCentroidReturnsEqualivalentPointWhenGivenSinglePoint() {
    	ControlPoint expected = new ControlPoint(1, 2);
        List<ControlPoint> points = new Vector<ControlPoint>();
        points.add(expected);

        Point actual = ContourCalc.calcCentroid(points);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalcCentroidReturnsMidpointWhenGivenTwoPoints() {
        List<ControlPoint> points = new Vector<ControlPoint>();
        points.add(new ControlPoint(1, 2));
        points.add(new ControlPoint(3, 4));
        Point expected = points.get(0).midpoint(points.get(1));

        Point actual = ContourCalc.calcCentroid(points);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSortPointsThrowsNullPointerExceptionWhenGivenNullList() {
        ContourCalc.sortPoints(null);
        fail("Exception not thrown");
    }

    @Test
    public void testSortPointsReturnsEmptyListWhenGivenEmptyList() {
        List<ControlPoint> points = new Vector<ControlPoint>();
        ContourCalc.sortPoints(points);
        assertEquals(0, points.size());
    }

    @Test
    public void testSortPointsWorksWhenGivenSinglePoint() {
        List<ControlPoint> expected = new Vector<ControlPoint>();
        expected.add(new ControlPoint(1, 2));

        List<ControlPoint> actual = new Vector<ControlPoint>(expected);
        ContourCalc.sortPoints(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void testSortPointsSortsClockwiseWhenGivenTwoPoints() {
        // -p1---------
        // ------------
        // ---------p2-
    	ControlPoint p1 = new ControlPoint(-1, 1);
    	ControlPoint p2 = new ControlPoint(1, -1);

        List<ControlPoint> sorted = new Vector<ControlPoint>();
        sorted.add(p1);
        sorted.add(p2);

        List<List<ControlPoint>> unsortedLists = new Vector<List<ControlPoint>>();

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(0).add(p1);
        unsortedLists.get(0).add(p2);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(1).add(p2);
        unsortedLists.get(1).add(p1);

        for (List<ControlPoint> list : unsortedLists) {
            ContourCalc.sortPoints(list);
            assertEquals(sorted, list);
        }
    }

    @Test
    public void testSortPointsSortsClockwiseWhenGivenThreePoints() {
        // -----p1-----
        // ------------
        // -p2------p3-
    	ControlPoint p1 = new ControlPoint(0, 1);
    	ControlPoint p2 = new ControlPoint(1, -1);
    	ControlPoint p3 = new ControlPoint(1, -1);

        List<ControlPoint> sorted = new Vector<ControlPoint>();
        sorted.add(p1);
        sorted.add(p3);
        sorted.add(p2);

        List<List<ControlPoint>> unsortedLists = new Vector<List<ControlPoint>>();

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(0).add(p1);
        unsortedLists.get(0).add(p2);
        unsortedLists.get(0).add(p3);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(1).add(p1);
        unsortedLists.get(1).add(p3);
        unsortedLists.get(1).add(p2);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(2).add(p2);
        unsortedLists.get(2).add(p1);
        unsortedLists.get(2).add(p3);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(3).add(p2);
        unsortedLists.get(3).add(p3);
        unsortedLists.get(3).add(p1);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(4).add(p3);
        unsortedLists.get(4).add(p1);
        unsortedLists.get(4).add(p2);

        unsortedLists.add(new Vector<ControlPoint>());
        unsortedLists.get(5).add(p3);
        unsortedLists.get(5).add(p2);
        unsortedLists.get(5).add(p1);

        for (List<ControlPoint> actual : unsortedLists) {
            ContourCalc.sortPoints(actual);
            assertEquals(sorted, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateThrowsNullPointerExceptionWhenGivenNullList() {
        ContourCalc.generate(null, false);
        fail("exception not thrown");
    }

    @Test
    public void testGenerateReturnsEmptyListWhenGivenZeroControlPointsForOpenContour() {
        List<Point> generatedPoints = ContourCalc.generate(
                new Vector<ControlPoint>(), false);
        assertEquals(0, generatedPoints.size());
    }

    @Test
    public void testGenerateReturnsListWithSamePointWhenGivenOneControlPointForOpenContour() {
    	ControlPoint controlPoint = new ControlPoint(1, 2);
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(controlPoint);
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        assertEquals("Generated list does not have exactly 1 point",
                controlPoints.size(), generatedPoints.size());
        assertEquals("The points are not the same", controlPoints.get(0),
                generatedPoints.get(0));
    }

    @Test
    public void testGenerateReturnsListWithSameTwoPointsWhenGivenTwoControlPointsForOpenContour() {
    	ControlPoint p1 = new ControlPoint(1, 2);
    	ControlPoint p2 = new ControlPoint(1, 2);
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        assertEquals("Generated list does not have exactly 2 points",
                controlPoints.size(), generatedPoints.size());

        for (ControlPoint controlPoint : controlPoints) {
            for (Point generatedPoint : generatedPoints) {
                assertEquals(controlPoint, generatedPoint);
            }
        }
    }

    /**
     * Tests that each of the generated points is within 1 unit (pixel?) from
     * the next point in the list. When this test passes, and
     * SEPARATION_DISTANCE is small, you know that connecting the generated
     * points with a straight line will give the appearance of a smooth curve.
     */
    @Test
    public void testGenerateReturnsListInWhichAllPointsAreCloseToEachOtherForOpenContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        for (int i = 1; i < generatedPoints.size(); i++) {
            double actualDistance = generatedPoints.get(i - 1).distance(
                    generatedPoints.get(i));
            String errorMessage = String
                    .format("Expected distance <= %1$f. Actual distance was %2$f",
                            ContourCalc.SEPARATION_DISTANCE + MAX_DELTA,
                            actualDistance);
            assertTrue(errorMessage,
                    actualDistance <= ContourCalc.SEPARATION_DISTANCE
                            + MAX_DELTA);
        }
    }

    /**
     * Test that each of the control points is within 1 unit from one of the
     * points in the generated list. When this test passes, you know that the
     * curve (almost) intersects each of the control points.
     */
    @Test
    public void testGenerateReturnsListInWhichAllControlPointsAreCloseToSomeGeneratedPointForOpenContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        for (ControlPoint controlPoint : controlPoints) {
            double minDistance = Double.MAX_VALUE;
            for (Point generatedPoint : generatedPoints) {
                minDistance = Math.min(minDistance,
                        controlPoint.distance(generatedPoint));
            }

            String errorMessage = String.format(
                    "Expected distance <= %1$f. Actual distance was %2$f",
                    ContourCalc.SEPARATION_DISTANCE + MAX_DELTA, minDistance);
            assertTrue(errorMessage,
                    minDistance <= ContourCalc.SEPARATION_DISTANCE + MAX_DELTA);
        }
    }

    /**
     * Test that the first and last points in the generated list are within 1
     * unit of each other. When this test passes you know that the curve does
     * not close back on itself.
     */
    @Test
    public void testGenerateReturnsListInWhichFirstAndLastPointsAreNotCloseToEachOtherForOpenContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        double actualDistance = generatedPoints.get(0).distance(
                generatedPoints.get(generatedPoints.size() - 1));
        String errorMessage = String.format(
                "Expected distance > %1$f. Actual distance was %2$f",
                ContourCalc.SEPARATION_DISTANCE + MAX_DELTA, actualDistance);
        assertTrue(errorMessage,
                actualDistance > ContourCalc.SEPARATION_DISTANCE + MAX_DELTA);
    }

    @Test
    public void testGenerateReturnsEmptyListWhenGivenZeroControlPointsForClosedContour() {
        List<Point> generatedPoints = ContourCalc.generate(
                new Vector<ControlPoint>(), true);
        assertEquals(0, generatedPoints.size());
    }

    @Test
    public void testGenerateReturnsListWithSamePointWhenGivenOneControlPointForClosedContour() {
    	ControlPoint controlPoint = new ControlPoint(1, 2);
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(controlPoint);
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        assertEquals("Generated list does not have exactly 1 point",
                controlPoints.size(), generatedPoints.size());
        assertEquals("The points are not the same", controlPoints.get(0),
                generatedPoints.get(0));
    }

    @Test
    public void testGenerateReturnsListWithSameTwoPointsWhenGivenTwoControlPointsForClosedContour() {
    	ControlPoint p1 = new ControlPoint(1, 2);
    	ControlPoint p2 = new ControlPoint(1, 2);
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        assertEquals("Generated list does not have exactly 2 points",
                controlPoints.size(), generatedPoints.size());

        for (ControlPoint controlPoint : controlPoints) {
            for (Point generatedPoint : generatedPoints) {
                assertEquals(controlPoint, generatedPoint);
            }
        }
    }

    @Test
    public void testGenerateReturnsListWithMoreThanThreePointsWhenGivenThreeControlPointsForOpenContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                false);

        assertTrue(generatedPoints.size() > 3);
    }

    @Test
    public void testGenerateReturnsListWithMoreThanThreePointsWhenGivenThreeControlPointsForClosedContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        assertTrue(generatedPoints.size() > 3);
    }

    /**
     * Tests that each of the generated points is within 1 unit (pixel?) from
     * the next point in the list. When this test passes, and
     * SEPARATION_DISTANCE is small, you know that connecting the generated
     * points with a straight line will give the appearance of a smooth curve.
     */
    @Test
    public void testGenerateReturnsListInWhichAllPointsAreCloseToEachOtherForClosedContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        for (int i = 1; i < generatedPoints.size(); i++) {
            double actualDistance = generatedPoints.get(i - 1).distance(
                    generatedPoints.get(i));
            String errorMessage = String
                    .format("Expected distance <= %1$f. Actual distance was %2$f",
                            ContourCalc.SEPARATION_DISTANCE + MAX_DELTA,
                            actualDistance);
            assertTrue(errorMessage,
                    actualDistance <= ContourCalc.SEPARATION_DISTANCE
                            + MAX_DELTA);
        }
    }

    /**
     * Test that each of the control points is within 1 unit from one of the
     * points in the generated list. When this test passes, you know that the
     * curve (almost) intersects each of the control points.
     */
    @Test
    public void testGenerateReturnsListInWhichAllControlPointsAreCloseToSomeGeneratedPointForClosedContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        for (ControlPoint controlPoint : controlPoints) {
            double minDistance = Double.MAX_VALUE;
            for (Point generatedPoint : generatedPoints) {
                minDistance = Math.min(minDistance,
                        controlPoint.distance(generatedPoint));
            }

            String errorMessage = String.format(
                    "Expected distance <= %1$f. Actual distance was %2$f",
                    ContourCalc.SEPARATION_DISTANCE + MAX_DELTA, minDistance);
            assertTrue(errorMessage,
                    minDistance <= ContourCalc.SEPARATION_DISTANCE + MAX_DELTA);
        }
    }

    /**
     * Test that the first and last points in the generated list are within 1
     * unit of each other. When this test passes you know that the curve closes
     * back on itself.
     */
    @Test
    public void testGenerateReturnsListInWhichFirstAndLastPointsAreCloseToEachOtherForClosedContour() {
        List<ControlPoint> controlPoints = new Vector<ControlPoint>();
        controlPoints.add(new ControlPoint(1, 2));
        controlPoints.add(new ControlPoint(3, 4));
        controlPoints.add(new ControlPoint(5, 6));
        List<Point> generatedPoints = ContourCalc.generate(controlPoints,
                true);

        double actualDistance = generatedPoints.get(0).distance(
                generatedPoints.get(generatedPoints.size() - 1));
        String errorMessage = String.format(
                "Expected distance <= %1$f. Actual distance was %2$f",
                ContourCalc.SEPARATION_DISTANCE + MAX_DELTA, actualDistance);
        assertTrue(errorMessage,
                actualDistance <= ContourCalc.SEPARATION_DISTANCE + MAX_DELTA);
    }
}
