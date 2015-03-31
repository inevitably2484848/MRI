package edu.auburn.cardiomri.test.datastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Vector;

import javafx.geometry.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.lib.ContourCalc;

public class ContourTest {

    private Contour contour;

    @Before
    public void setUp() {
        contour = new Contour(Contour.Type.DEFAULT);
    }

    @After
    public void tearDown() {
        contour = null;
    }

    @Test
    public void testContourConstructorSetsContourType() {
        Contour.Type expected = Contour.Type.DEFAULT;
        Contour.Type actual = contour.getContourType();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testSetControlPointsThrowsNullPointerExceptionWhenGivenNullList() {
        contour.setControlPoints(null);
        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetControlPointsThrowsIllegalArgumentExceptionWhenGivenPointsWithNegativeCoordinates() {
        List<Point2D> list = new Vector<Point2D>();

        list.add(new Point2D(1, 2));
        list.add(new Point2D(-3, 4)); // The bad point
        list.add(new Point2D(5, 6));

        contour.setControlPoints(list);
        fail("Exception not thrown");
    }

    @Test
    public void testSetControlPointsFillsInternalListWithEquivalentPoints() {
        List<Point2D> list = new Vector<Point2D>();

        list.add(new Point2D(1, 2));
        list.add(new Point2D(3, 4));
        list.add(new Point2D(5, 6));

        contour.setControlPoints(list);
        List<Point2D> contourList = contour.getControlPoints();

        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), contourList.get(i));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSetGeneratedPointsThrowsNullPointerExceptionWhenGivenNullList() {
        contour.setGeneratedPoints(null);
        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddControlPointThrowsIllegalArgumentExceptionWhenGivenNegativeCoordinates() {
        contour.addControlPoint(-1, 2);
        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddControlPointThrowsIllegalArgumentExceptionWhenPointHasAlreadyBeenAdded() {
        contour.addControlPoint(1, 2);
        contour.addControlPoint(1, 2);
        fail("Exception not thrown");
    }

    @Test
    public void testAddControlPointCreatesPoint2DAndAddsItToControlPointsList() {
        contour.addControlPoint(1, 2);

        Point2D expected = new Point2D(1, 2);
        Point2D actual = contour.getControlPoints().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetControlPointsReturnsACopyOfTheInternalList() {
        contour.addControlPoint(1, 2);
        contour.addControlPoint(3, 4);
        List<Point2D> listWithTwoPoints = contour.getControlPoints();

        contour.addControlPoint(5, 6);
        List<Point2D> listWithThreePoints = contour.getControlPoints();

        assertEquals(2, listWithTwoPoints.size());
        assertEquals(3, listWithThreePoints.size());
    }

    @Test
    public void testGetGeneratedPointsReturnsPointsThatIntersectAllControlPoints() {
        contour.addControlPoint(10, 10);
        contour.addControlPoint(15, 15);
        contour.addControlPoint(5, 15);

        List<Point2D> controlPoints = contour.getControlPoints();
        List<Point2D> generatedPoints = contour.getGeneratedPoints();

        for (Point2D controlPoint : controlPoints) {
            double minDistance = Double.MAX_VALUE;
            for (Point2D generatedPoint : generatedPoints) {
                minDistance = Math.min(minDistance,
                        controlPoint.distance(generatedPoint));
            }
            if (minDistance > ContourCalc.SEPARATION_DISTANCE / 2.0) {
                fail(String.format(
                        "Minimum distance from (%1$f, %2$f) to curve is %3$f",
                        controlPoint.getX(), controlPoint.getY(), minDistance));
            }
        }
    }

    @Test
    public void testGetGeneratedPointsReturnsACopyOfTheInternalList() {
        contour.addControlPoint(1, 2);
        contour.addControlPoint(3, 4);
        contour.addControlPoint(5, 6);

        List<Point2D> copy1 = contour.getGeneratedPoints();
        List<Point2D> copy2 = contour.getGeneratedPoints();
        copy1.clear();

        assertNotEquals(copy1, copy2);
    }

    @Test
    public void testSetContourTypeAffectsReturnValueOfIsClosedCurve() {
        contour.setContourType(Contour.Type.DEFAULT_CLOSED);
        boolean initialValue = contour.isClosedCurve();

        contour.setContourType(Contour.Type.DEFAULT_OPEN);
        boolean finalValue = contour.isClosedCurve();

        assertNotEquals(initialValue, finalValue);
    }
}
