package edu.auburn.cardiomri.datastructure;

import edu.auburn.cardiomri.datastructure.TagLine;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagLineTest {
    TagLine tagTester;

    @Before
    public void setUp() throws Exception {
        tagTester = new TagLine();
    }

    @After
    public void tearDown() throws Exception {
        tagTester = null;
    }

    @Test
    public void testSetGetIndex() {
        tagTester.setIndex(1000);
        assertEquals(1000, tagTester.getIndex());

        tagTester.setIndex(-1);
        assertEquals(-1, tagTester.getIndex());

        tagTester.setIndex(0);
        assertEquals(0, tagTester.getIndex());
    }

    @Test
    public void testSetGetPoints() {
        tagTester.setPoints(15000);
        assertEquals(15000, tagTester.getPoints());

        tagTester.setPoints(500000);
        assertEquals(500000, tagTester.getPoints());

        tagTester.setPoints(0);
        assertEquals(0, tagTester.getPoints());
    }

    @Test
    public void testSetGetPointsPixel() {
        double[] pointsPixelTest1 = { 1.254, 6.4, 1000.5, 301 };
        tagTester.setPointsPixel(pointsPixelTest1);
        assertEquals(4, tagTester.getPointsPixel().length);
        assertEquals(1.254, tagTester.getPointsPixel()[0], .0001);
        assertEquals(6.4, tagTester.getPointsPixel()[1], .01);
        assertEquals(1000.5, tagTester.getPointsPixel()[2], .01);
        assertEquals(301, tagTester.getPointsPixel()[3], .01);

        double[] pointsPixelTest2 = {};
        tagTester.setPointsPixel(pointsPixelTest2);
        assertEquals(0, tagTester.getPointsPixel().length);
    }

    @Test
    public void testSetGetPointsDist() {
        double[] pointsDistTest1 = { 1.254, 6.4, 1000.5, 301 };
        tagTester.setPointsDist(pointsDistTest1);
        assertEquals(4, tagTester.getPointsDist().length);
        assertEquals(1.254, tagTester.getPointsDist()[0], .0001);
        assertEquals(6.4, tagTester.getPointsDist()[1], .01);
        assertEquals(1000.5, tagTester.getPointsDist()[2], .01);
        assertEquals(301, tagTester.getPointsDist()[3], .01);

        double[] pointsDistTest2 = {};
        tagTester.setPointsDist(pointsDistTest2);
        assertEquals(0, tagTester.getPointsDist().length);
    }

    @Test
    public void testSetGetPointsScanner() {
        double[] pointsScannerTest1 = { 1.254, 6.4, 1000.5, 301 };
        tagTester.setPointsScanner(pointsScannerTest1);
        assertEquals(4, tagTester.getPointsScanner().length);
        assertEquals(1.254, tagTester.getPointsScanner()[0], .0001);
        assertEquals(6.4, tagTester.getPointsScanner()[1], .01);
        assertEquals(1000.5, tagTester.getPointsScanner()[2], .01);
        assertEquals(301, tagTester.getPointsScanner()[3], .01);

        double[] pointsScannerTest2 = {};
        tagTester.setPointsScanner(pointsScannerTest2);
        assertEquals(0, tagTester.getPointsScanner().length);
    }
}
