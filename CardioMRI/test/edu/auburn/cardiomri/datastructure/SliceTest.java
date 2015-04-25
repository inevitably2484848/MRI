package edu.auburn.cardiomri.datastructure;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import edu.auburn.cardiomri.datastructure.Time;
import edu.auburn.cardiomri.datastructure.Slice;

public class SliceTest {
    private Slice sliceTester;

    @Before
    public void setUp() throws Exception {
        sliceTester = new Slice();
    }

    @After
    public void tearDown() throws Exception {
        sliceTester = null;
        System.out.println("Test complete.\n\n");
    }

    @Test
    public void testSetGetTagCenter() {
        System.out.println("Running testSetGetTagCenter... ");
        double[] tagCenterTest = { .5, .6, 1.5, 10 };
        sliceTester.setTagCenter(tagCenterTest);
        assertEquals(4, sliceTester.getTagCenter().length);
        assertEquals(.5, sliceTester.getTagCenter()[0], .01);
        assertEquals(.6, sliceTester.getTagCenter()[1], .01);
        assertEquals(1.5, sliceTester.getTagCenter()[2], .01);
        assertEquals(10, sliceTester.getTagCenter()[3], .01);

        System.out.println("Empty Set");
        double[] tagCenterTest2 = {};
        sliceTester.setTagCenter(tagCenterTest2);
        assertEquals(0, sliceTester.getTagCenter().length);
    }

    @Test
    public void testSetGetTagSpacing() {
        System.out.println("Running testSetGetTagSpacing ...");
        double[] tagSpacingTest = { .5, .6, 1.5, 10 };
        sliceTester.setTagSpacing(tagSpacingTest);
        assertEquals("Expected 4, got " + sliceTester.getTagSpacing(), 4,
                sliceTester.getTagSpacing().length);
        assertEquals(.5, sliceTester.getTagSpacing()[0], .01);
        assertEquals(.6, sliceTester.getTagSpacing()[1], .01);
        assertEquals(1.5, sliceTester.getTagSpacing()[2], .01);
        assertEquals(10, sliceTester.getTagSpacing()[3], .01);

        double[] tagSpacingTest2 = {};
        sliceTester.setTagSpacing(tagSpacingTest2);
        assertEquals(0, sliceTester.getTagSpacing().length);
    }

    @Test
    public void testSetGetTimes() {
        System.out.println("Running testSetGetTimes ... ");
        Time t1 = new Time();
        Time t2 = new Time();
        Time t3 = new Time();
        ArrayList<Time> timeAL = new ArrayList<Time>();
        timeAL.add(t1);
        timeAL.add(t2);
        timeAL.add(t3);
        sliceTester.setTimes(timeAL);
        assertSame(sliceTester.getTimes().get(0), t1);
        assertSame(sliceTester.getTimes().get(1), t2);
        assertSame(sliceTester.getTimes().get(2), t3);
        assertEquals(3, sliceTester.getTimes().size());
    }

}
