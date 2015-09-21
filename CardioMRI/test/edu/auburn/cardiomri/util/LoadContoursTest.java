package edu.auburn.cardiomri.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Vector3d;

public class LoadContoursTest {
    private static final String FILE_DIR = "/test/edu/auburn/cardiomri/util/";
    private static final String FILE_NAME = "testPoints.txt";
    private Map<String, DICOMImage> SOPInstanceUIDToDICOMImage;
    private Vector<Contour> contourList;
    private DICOMImage image1, image2;
    private String path;
    BufferedReader reader;
    File file;

    @Before
    public void setUp() throws Exception {
        SOPInstanceUIDToDICOMImage = new HashMap<String, DICOMImage>();
        contourList = new Vector<Contour>();
        image1 = new DICOMImage();
        image2 = new DICOMImage();
        path = System.getProperty("user.dir") + File.separator + FILE_DIR
                + FILE_NAME;
        file = new File(path);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReadInOneContourForOneImageThreeControlPoints297GeneratedPoints() {
        image2.setSopInstanceUID("tempID");
        SOPInstanceUIDToDICOMImage.put("tempID", image2);
        ContourUtilities.loadContour(file, SOPInstanceUIDToDICOMImage);
        assertEquals(1, image2.getContours().size());
        Vector<Contour> v = image2.getContours();
        assertEquals(3, image2.getContours().get(0).getControlPoints().size());
        assertEquals(3*99, image2.getContours().get(0).getGeneratedPoints()
                .size());
    }

    @Test
    public void testReadInOneContourForOneImageWith1ContourExistingCheckNotDeleted() {
        Vector3d p1 = new Vector3d(1.0, 2.0, 0.0);
        Vector3d p2 = new Vector3d(3.0, 4.0, 0.0);

        List<Vector3d> controlPoints = new Vector<Vector3d>();
        controlPoints.add(p1);
        controlPoints.add(p2);

        Contour c1 = new Contour(Contour.Type.DEFAULT);
        c1.setControlPoints(controlPoints); // automatically generates other
                                            // points
        contourList.add(c1);
        image1.getContours().add(c1);
        image1.setSopInstanceUID("tempID");
        SOPInstanceUIDToDICOMImage.put("tempID", image1);
        ContourUtilities.loadContour(file, SOPInstanceUIDToDICOMImage);

        assertEquals(2, image1.getContours().size());
    }

}
