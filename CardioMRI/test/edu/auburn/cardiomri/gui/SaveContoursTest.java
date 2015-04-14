package edu.auburn.cardiomri.gui;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.geometry.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.*;


public class SaveContoursTest {

	private Map<String, DICOMImage> SOPInstanceUIDToDICOMImage;
	private DICOMImage image1, image2, image3;
	private String path;
	BufferedReader reader;
	
	@Before
	public void setUp() throws Exception {
		SOPInstanceUIDToDICOMImage = new HashMap<String, DICOMImage>();
		image1 = new DICOMImage();
		image2 = new DICOMImage();
		image3 = new DICOMImage();
		path = System.getProperty("user.dir") + File.separator
                + "/test/edu/auburn/cardiomri/gui/testFile.txt";
		File file = new File(path);
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.print("Could not find the test file");
		}
	}

	@After
	public void tearDown() throws Exception {
		SOPInstanceUIDToDICOMImage = null;
		reader.close();
		
	}

	@Test
	public void test001SaveOneContourOnOneImageTwoControlPoints() throws IOException {
		
		Point2D p1 = new Point2D(1.0000, 2.0000);
        Point2D p2 = new Point2D(3.0000, 4.0000);
        
        List<Point2D> controlPoints = new Vector<Point2D>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        
		Contour c1 = new Contour(Contour.Type.DEFAULT);
		c1.setControlPoints(controlPoints); //automatically generates other points
		
		image1.getContours().add(c1);
		SOPInstanceUIDToDICOMImage.put("first", image1);
		
		GUIController.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);
		
		int i = 0;
		//skip to number of points
		while (i<3) {
			reader.readLine();
			i++;
		}
		
		List<Point2D> generatedPoints = new Vector<Point2D>();
		generatedPoints = c1.getGeneratedPoints();
		assertEquals(4, Integer.parseInt(reader.readLine()));
		
		String[] line = new String[2];
		
		for(Point2D p : controlPoints) {
			double x = p.getX();
			double y = p.getY();
			line = reader.readLine().split("\t");
			assertEquals(x, Double.parseDouble(line[0]), 0.0000);
			assertEquals(y, Double.parseDouble(line[1]), 0.0000);
		}
		
		for (Point2D p : generatedPoints) {
			double x = p.getX();
			double y = p.getY();
			line = reader.readLine().split("\t");
			assertEquals(x, Double.parseDouble(line[0]), 0.0000);
			assertEquals(y, Double.parseDouble(line[1]), 0.0000);
		}
	}
	
	@Test
	public void test002SaveOneContourOnOneImageThreeControlPoints() throws IOException {
		
		Point2D p1 = new Point2D(1.0, 2.0);
        Point2D p2 = new Point2D(3.0, 4.0);
        Point2D p3 = new Point2D(5.0, 6.0);
        
        List<Point2D> controlPoints = new Vector<Point2D>();
        controlPoints.add(p3); 
    
        controlPoints.add(p2);
        controlPoints.add(p1);
        
		Contour c1 = new Contour(Contour.Type.DEFAULT);
		c1.setControlPoints(controlPoints); //automatically generates other points
		
		image1.getContours().add(c1);
		SOPInstanceUIDToDICOMImage.put("first", image1);
		
		GUIController.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);
		
		int i = 0;
		//skip to number of points
		while (i<3) {
			reader.readLine();
			i++;
		}
		
		List<Point2D> generatedPoints = new Vector<Point2D>();
		generatedPoints = c1.getGeneratedPoints();
		assertEquals(16, Integer.parseInt(reader.readLine()));
		
		String[] line = new String[2];
		
		for (Point2D p : controlPoints) {
			BigDecimal x = BigDecimal.valueOf(p.getX())
					.setScale(4, BigDecimal.ROUND_UP);
			BigDecimal y = BigDecimal.valueOf(p.getY())
					.setScale(4, BigDecimal.ROUND_UP);
			line = reader.readLine().split("\t");
			BigDecimal d1 = new BigDecimal(line[0]);
			BigDecimal d2 = new BigDecimal(line[1]);
			assertEquals(x, d1);
			assertEquals(y, d2);
		}
		
		for(Point2D p : generatedPoints) {
			BigDecimal x = BigDecimal.valueOf(p.getX())
					.setScale(4, BigDecimal.ROUND_UP);
			BigDecimal y = BigDecimal.valueOf(p.getY())
					.setScale(4, BigDecimal.ROUND_UP);
			line = reader.readLine().split("\t");
			BigDecimal d1 = new BigDecimal(line[0]);
			BigDecimal d2 = new BigDecimal(line[1]);
			assertEquals(x, d1);
			assertEquals(y, d2);
		}
		
		
	}
}
