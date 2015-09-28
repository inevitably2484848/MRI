package edu.auburn.cardiomri.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.Contour.Type;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Vector3d;

public class SaveContoursTest {
    private static final String FILE_DIR = "/test/edu/auburn/cardiomri/util/";
    private static final String FILE_NAME = "testFile.txt";
    private Map<String, DICOMImage> SOPInstanceUIDToDICOMImage;
    private DICOMImage image1;
    private String path;
    BufferedReader reader;

    @Before
    public void setUp() throws Exception {
        SOPInstanceUIDToDICOMImage = new HashMap<String, DICOMImage>();
        image1 = new DICOMImage();
        path = System.getProperty("user.dir") + File.separator + FILE_DIR
                + FILE_NAME;
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
    public void test001SaveOneContourOnOneImageTwoControlPoints()
            throws IOException {

        Vector3d p1 = new Vector3d(1.0000, 2.0000, 0.0);
        Vector3d p2 = new Vector3d(3.0000, 4.0000, 0.0);

        List<Vector3d> controlPoints = new Vector<Vector3d>();
        controlPoints.add(p1);
        controlPoints.add(p2);

        Contour c1 = new Contour(Contour.Type.DEFAULT);
        c1.setControlPoints(controlPoints); // automatically generates other
                                            // points

        image1.getContours().add(c1);
        SOPInstanceUIDToDICOMImage.put("first", image1);

        ContourUtilities.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);

        int i = 0;
        // skip to number of points
        while (i < 3) {
            reader.readLine();
            i++;
        }

        controlPoints = new Vector<Vector3d>();
        controlPoints = c1.getControlPoints();
        assertEquals(2, Integer.parseInt(reader.readLine()));

        String[] line = new String[2];

        for (Vector3d p : controlPoints) {
            double x = p.getX();
            double y = p.getY();
            line = reader.readLine().split("\t");
            assertEquals(x, Double.parseDouble(line[0]), 0.0000);
            assertEquals(y, Double.parseDouble(line[1]), 0.0000);
        }
        
        reader.readLine(); // Throwaway -1
        
        i = 0;
        // skip to number of points
        while (i < 3) {
            reader.readLine();
            i++;
        }
        
        List<Vector3d> generatedPoints = new Vector<Vector3d>();
        generatedPoints = c1.getGeneratedPoints();
        // With only 2 control points, no extra points are generated
        assertEquals(2, Integer.parseInt(reader.readLine()));

        for (Vector3d p : generatedPoints) {
            double x = p.getX();
            double y = p.getY();
            line = reader.readLine().split("\t");
            assertEquals(x, Double.parseDouble(line[0]), 0.0000);
            assertEquals(y, Double.parseDouble(line[1]), 0.0000);
        }
    }

    @Test
    public void test002SaveOneContourOnOneImageThreeControlPoints()
            throws IOException {

        Vector3d p1 = new Vector3d(1.0, 2.0, 0.0);
        Vector3d p2 = new Vector3d(3.0, 4.0, 0.0);
        Vector3d p3 = new Vector3d(5.0, 6.0, 0.0);

        List<Vector3d> controlPoints = new Vector<Vector3d>();
        controlPoints.add(p3);

        controlPoints.add(p2);
        controlPoints.add(p1);

        Contour c1 = new Contour(Contour.Type.DEFAULT);
        c1.setControlPoints(controlPoints); // automatically generates other
                                            // points

        image1.getContours().add(c1);
        SOPInstanceUIDToDICOMImage.put("first", image1);

        ContourUtilities.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);

        int i = 0;
        // skip to number of points
        while (i < 3) {
            reader.readLine();
            i++;
        }

        
        controlPoints = new Vector<Vector3d>();
        controlPoints = c1.getControlPoints();
        assertEquals(3, Integer.parseInt(reader.readLine()));

        String[] line = new String[2];

        for (Vector3d p : controlPoints) {
            BigDecimal x = BigDecimal.valueOf(p.getX()).setScale(4,
                    BigDecimal.ROUND_UP);
            BigDecimal y = BigDecimal.valueOf(p.getY()).setScale(4,
                    BigDecimal.ROUND_UP);
            line = reader.readLine().split("\t");
            BigDecimal d1 = new BigDecimal(line[0]);
            BigDecimal d2 = new BigDecimal(line[1]);
            assertEquals(x, d1);
            assertEquals(y, d2);
        }
        
        reader.readLine(); // Throwaway -1
        
        i = 0;
        // skip to number of points
        while (i < 3) {
            reader.readLine();
            i++;
        }
        
        List<Vector3d> generatedPoints = new Vector<Vector3d>();
        generatedPoints = c1.getGeneratedPoints();
        // Generate 99 extra points per control point
        assertEquals(3*99, Integer.parseInt(reader.readLine()));	

        for (Vector3d p : generatedPoints) {
            BigDecimal x = BigDecimal.valueOf(p.getX()).setScale(4,
                    BigDecimal.ROUND_UP);
            BigDecimal y = BigDecimal.valueOf(p.getY()).setScale(4,
                    BigDecimal.ROUND_UP);
            line = reader.readLine().split("\t");
            BigDecimal d1 = new BigDecimal(line[0]);
            BigDecimal d2 = new BigDecimal(line[1]);
            assertEquals(x, d1);
            assertEquals(y, d2);
        }

    }
    
    @Test
    public void test003SaveOneContourOnOneImageFourControlPointsForEachContourType()
            throws Exception {
    	ArrayList<Type> types = new ArrayList<Type>(
    			Arrays.asList(
    					Contour.Type.LA_ENDO,
    					Contour.Type.LA_EPI,
    					Contour.Type.LV_ENDO,
    					Contour.Type.LV_EPI,
    					Contour.Type.RA_ENDO,
    					Contour.Type.RA_EPI,
    					Contour.Type.RV_ENDO,
    					Contour.Type.RV_EPI));
    	
    	for (Type type : types) {
    		this.tearDown();		// Fixes issues with appending file multiple times
    		this.setUp();
	        Vector3d p1 = new Vector3d(1.0, 2.0, 0.0);
	        Vector3d p2 = new Vector3d(3.0, 4.0, 0.0);
	        Vector3d p3 = new Vector3d(5.0, 6.0, 0.0);
	        Vector3d p4 = new Vector3d(7.0, 8.0, 0.0);
	
	        List<Vector3d> controlPoints = new Vector<Vector3d>();
	        controlPoints.add(p4);
	        controlPoints.add(p3);
	        controlPoints.add(p2);
	        controlPoints.add(p1);
	
	        Contour c1 = new Contour(type);
	        c1.setControlPoints(controlPoints); // automatically generates other
	                                            // points
	
	        image1.getContours().add(c1);
	        SOPInstanceUIDToDICOMImage.put("first", image1);
	
	        ContourUtilities.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);
	
	        int i = 0;
	        // skip to number of points
	        while (i < 3) {
	            reader.readLine();
	            i++;
	        }
	
	        
	        controlPoints = new Vector<Vector3d>();
	        controlPoints = c1.getControlPoints();
	        String t = reader.readLine();
	        assertEquals(4, Integer.parseInt(t));
	
	        String[] line = new String[2];
	
	        for (Vector3d p : controlPoints) {
	            BigDecimal x = BigDecimal.valueOf(p.getX()).setScale(4,
	                    BigDecimal.ROUND_UP);
	            BigDecimal y = BigDecimal.valueOf(p.getY()).setScale(4,
	                    BigDecimal.ROUND_UP);
	            line = reader.readLine().split("\t");
	            BigDecimal d1 = new BigDecimal(line[0]);
	            BigDecimal d2 = new BigDecimal(line[1]);
	            assertEquals(x, d1);
	            assertEquals(y, d2);
	        }
	        
	        reader.readLine(); // Throwaway -1
	        
	        i = 0;
	        // skip to number of points
	        while (i < 3) {
	            reader.readLine();
	            i++;
	        }
	        
	        List<Vector3d> generatedPoints = new Vector<Vector3d>();
	        generatedPoints = c1.getGeneratedPoints();
	        // Generate 99 extra points per control point
	        assertEquals(4*99, Integer.parseInt(reader.readLine()));	
	
	        for (Vector3d p : generatedPoints) {
	            BigDecimal x = BigDecimal.valueOf(p.getX()).setScale(4,
	                    BigDecimal.ROUND_UP);
	            BigDecimal y = BigDecimal.valueOf(p.getY()).setScale(4,
	                    BigDecimal.ROUND_UP);
	            line = reader.readLine().split("\t");
	            BigDecimal d1 = new BigDecimal(line[0]);
	            BigDecimal d2 = new BigDecimal(line[1]);
	            assertEquals(x, d1);
	            assertEquals(y, d2);
	        }
    	}
    }
}
