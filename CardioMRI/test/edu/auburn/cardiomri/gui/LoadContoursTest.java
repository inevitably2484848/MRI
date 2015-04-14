package edu.auburn.cardiomri.gui;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;

public class LoadContoursTest {

	private Map<String, DICOMImage> SOPInstanceUIDToDICOMImage;
	private Vector<Contour> contourList;
	private DICOMImage image1, image2, image3;
	private String path;
	BufferedReader reader;
	File file;

	@Before
	public void setUp() throws Exception {
		SOPInstanceUIDToDICOMImage = new HashMap<String, DICOMImage>();
		contourList = new Vector<Contour>();
		image1 = new DICOMImage();
		image2 = new DICOMImage();
		image3 = new DICOMImage();
		path = System.getProperty("user.dir") + File.separator
				+ "/test/edu/auburn/cardiomri/gui/testPoints.txt";
		file = new File(path);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadInOneContourForOneImageThreeControlPoints13GeneratedPoints() {
			/*		
				tempID
				7
				16
				5.0000	6.0000
				3.0000	4.0000
				1.0000	2.0000
				5.2222	6.2222
				4.2929	5.2929
				3.5858	4.5858
				2.8787	3.8787
				2.1716	3.1716
				1.4645	2.4645
				0.8120	1.8120
				1.5191	2.5191
				2.2262	3.2262
				2.9333	3.9333
				3.6405	4.6405
				4.3476	5.3476
				5.2222	6.2222
				-1
			*/
		image2.setSopInstanceUID("tempID");
		SOPInstanceUIDToDICOMImage.put("tempID", image2);
		GUIController.loadContour(file, SOPInstanceUIDToDICOMImage);
		assertEquals(2, image2.getContours().size());
		assertEquals(3, image2.getContours().get(1).getControlPoints().size());
		assertEquals(13, image2.getContours().get(1).getGeneratedPoints().size());
	}
	
	@Test
	public void testReadInOneContourForOneImageWith1ContourExistingCheckNotDeleted() {
		Point2D p1 = new Point2D(1.0, 2.0);
        Point2D p2 = new Point2D(3.0, 4.0);
        
        List<Point2D> controlPoints = new Vector<Point2D>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        
		Contour c1 = new Contour(Contour.Type.DEFAULT);
		c1.setControlPoints(controlPoints); //automatically generates other points
		contourList.add(c1);
		image1.getContours().add(c1);
		image1.setSopInstanceUID("tempID");
		SOPInstanceUIDToDICOMImage.put("tempID", image1);
		GUIController.loadContour(file, SOPInstanceUIDToDICOMImage);
		
		assertEquals(3, image1.getContours().size());
	}

}
