/**
 * 
 */
package edu.auburn.cardiomri.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.ControlPoint;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Landmark;
import edu.auburn.cardiomri.datastructure.Point;
import edu.auburn.cardiomri.datastructure.TensionPoint;
import edu.auburn.cardiomri.datastructure.Vector3d;
import edu.auburn.cardiomri.gui.models.WorkspaceModel;

/**
 * @author Christopher Colosi
 * 
 * Various utilities that can be used to perform actions
 * related to contouring. 
 * 
 */
public final class ContourUtilities {
	
	public static void loadAnnotations(File file, Map<String, DICOMImage> SOPInstanceUIDToDICOMImage) {
		loadContours(file, SOPInstanceUIDToDICOMImage);
		loadLandmarks(file, SOPInstanceUIDToDICOMImage);

    }
	
	public static void loadContours(File file, Map<String, DICOMImage> SOPInstanceUIDToDICOMImage) {
		Vector<Contour> contours;
        List<ControlPoint> controlPoints;
        List<TensionPoint> tensionPoints;

        String sopInstanceUID;
        String[] line = new String[2];
        @SuppressWarnings("unused")
        String lineCheck;

        int contourType;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) {
                contours = new Vector<Contour>();
                sopInstanceUID = reader.readLine();
                contourType = Integer.parseInt(reader.readLine());
                
                

                if (Contour.isControlPointFromInt(contourType) == null)	// not contour type
                {
                	while ((lineCheck = reader.readLine()) != "-1") {
                		if (lineCheck.equals("-1")) {
	                        break;
	                    }
                	}
                }
                // Only add contours to image if it is a control point contour
                else if (Contour.isControlPointFromInt(contourType))
                {
	                while ((lineCheck = reader.readLine()) != "-1") {
	                	int pointIdx = 0;
	                	int controlPointIdx = 1;
	                	int tensionPointsPerControlPoint = 2;
	                	
	                    controlPoints = new Vector<ControlPoint>();
	                    tensionPoints = new Vector<TensionPoint>();
	                    while ((line = reader.readLine().split("\t")).length >= 2) {
	                    	
	                    	float x = Float.parseFloat(line[0]);
	                        float y = Float.parseFloat(line[1]);
	                        
	                    	if (pointIdx == controlPointIdx)	// control point
		                    {
		                        controlPoints.add(new ControlPoint(x, y));
		                    }
	                    	else	// tension point
	                    	{
	                    		tensionPoints.add(new TensionPoint(x, y));
	                    	}
	                    	
	                    	pointIdx = (pointIdx + 1) % (tensionPointsPerControlPoint + 1);
	                    }
	                    
                    	int tensionPointIdx = 0;
                    	for (ControlPoint controlPoint : controlPoints)
                    	{
                    		controlPoint.setTension1(tensionPoints.get(tensionPointIdx));
                    		controlPoint.setTension2(tensionPoints.get(tensionPointIdx+1));
                    		controlPoint.getTension1().setControlPoint(controlPoint);
                    		controlPoint.getTension2().setControlPoint(controlPoint);
                    		tensionPointIdx += 2;
                    	}
                    	
                    	Contour contour = new Contour(
                    			Contour.getTypeFromInt(contourType));
                    	contour.setControlPoints(controlPoints);
                    	contours.add(contour);
	                    
	                    
	                    if (line[0].equals("-1")) {
	                        break;
	                    } else {
	                        contourType = Integer.parseInt(line[0]);
	                    }
	                }
                }
                else // Not a control point contour
                {
                	while ((lineCheck = reader.readLine()) != "-1") {
                		while ((line = reader.readLine().split("\t")).length >= 2) {
                			// Do nothing with these points
                		}
                		
                		if (line[0].equals("-1")) {
	                        break;
	                    } else {
	                        contourType = Integer.parseInt(line[0]);
	                    }
                	}
                }
                DICOMImage image = SOPInstanceUIDToDICOMImage
                        .get(sopInstanceUID);
                image.getContours().addAll(contours);

            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
	}
	
	public static void loadLandmarks(File file, Map<String, DICOMImage> SOPInstanceUIDToDICOMImage) {
		Vector<Landmark> landmarks;

        String sopInstanceUID;
        String[] line = new String[2];
        @SuppressWarnings("unused")
        String lineCheck;

        int landmarkType;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) {
                landmarks = new Vector<Landmark>();
                sopInstanceUID = reader.readLine();
                landmarkType = Integer.parseInt(reader.readLine());
                
                if (Landmark.isLandmarkFromInt(landmarkType) == null)	// Not landmark type section
                {
                	while ((lineCheck = reader.readLine()) != "-1") {
                		if (lineCheck.equals("-1")) {
	                        break;
	                    }
                	}
                }
                else
                {
	                while ((lineCheck = reader.readLine()) != "-1") {
	
	                    while ((line = reader.readLine().split("\t")).length >= 2) {
	                    	
	                    	float x = Float.parseFloat(line[0]);
	                        float y = Float.parseFloat(line[1]);
	                        
	                        landmarks.add(new Landmark(Landmark.getTypeFromInt(landmarkType), x, y));
	                        
	                    }
	                    
	                    if (line[0].equals("-1")) {
	                        break;
	                    }
	                }
                }

                DICOMImage image = SOPInstanceUIDToDICOMImage
                        .get(sopInstanceUID);
                image.getLandmarks().addAll(landmarks);

            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
	}
	
	
	
	public static void writeAnnotationsToFile(
			Map<String, DICOMImage> SOPInstanceUIDToDICOMImage, String path) {
		
		ContourUtilities.writeContoursToFile(SOPInstanceUIDToDICOMImage, path);
		ContourUtilities.writeLandmarksToFile(SOPInstanceUIDToDICOMImage, path);
	}
	
	
	public static void writeLandmarksToFile(
            Map<String, DICOMImage> SOPInstanceUIDToDICOMImage, String path) {
        Vector<Landmark> landmarks;
        PrintWriter writer = null;

        File f = new File(path);

        try {
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(f, true)));
            for (DICOMImage image : SOPInstanceUIDToDICOMImage.values()) {
                landmarks = image.getLandmarks();
                if (landmarks.size() < 1) {
                    continue;
                } else {
                	writer.write("\n" + image.getSopInstanceUID() + "\n");
                    for (Landmark l: landmarks) {
                            int numPoints = 1;
                            String header = l.getIntFromType() + "\n"
                                    + numPoints + "\n";
                            writer.write(header);
                            
                            writer.write(BigDecimal.valueOf(l.getX())
                                    .setScale(4, BigDecimal.ROUND_UP)
                                    + "\t"
                                    + BigDecimal.valueOf(l.getY())
                                            .setScale(4,
                                                     BigDecimal.ROUND_UP)
                                    + "\n");
                        }
                    
                    writer.write((-1) + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Writes the contour data to the specified path for all images
     * containing contours. The file format is as follows: 
     * blank line, SOPInstanceUID, number of points, all of the (x, y) pairs 
     * for the contour.
     * a "-1" indicates the end of the list of contours for an image. 
     * 
     * @param SOPInstanceUIDToDICOMImage  a hashmap containing all of the
     * DICOM images with their SOPInstanceUIDs as keys
     * @param path   the file path to which to create the text file
     */
    public static void writeContoursToFile(
            Map<String, DICOMImage> SOPInstanceUIDToDICOMImage, String path) {
        // TODO Categorize points based on location (i.e. LA, RA, Endo, Epi,
        // etc...
        // TODO merge with save() in SerializableManager
        Vector<Contour> contours;
        PrintWriter writer = null;

        File f = new File(path);

        try {
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(f, false)));
            for (DICOMImage image : SOPInstanceUIDToDICOMImage.values()) {
                contours = image.getContours();
                if (contours.size() < 1) {
                    continue;
                } else {
                	ContourUtilities.writeControurControlPointsToFile(writer, image, contours);
                	ContourUtilities.writeContourGeneratedPointsToFile(writer, image, contours);
                    
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void writeControurControlPointsToFile(PrintWriter writer, 
    		DICOMImage image, Vector<Contour> contours)
    {
    	writer.write("\n" + image.getSopInstanceUID() + "\n");
        for (Contour c : contours) {
            if (c.getControlPoints().size() > 0) {
                int numPoints = c.getControlPoints().size() 
                		+ c.getTensionPoints().size();
                String header = c.getIntFromTypeControlPoints() + "\n"
                        + numPoints + "\n";
                writer.write(header);
                /*
                 * This contour's section format will be as follows:
                 * Tension 1-1
                 * Control 1
                 * Tension 1-2
                 * 
                 * Tension 2-1
                 * Control 2
                 * Tension 2-2
                 * etc.
                 */
                // Build list of combined tension and control points
                int numControlPoints = c.getControlPoints().size();
                List<Point> allPoints = new ArrayList<Point>();
                List<ControlPoint> controlPoints = c.getControlPoints();
                List<TensionPoint> tensionPoints = c.getTensionPoints();
                for (int i = 0; i < numControlPoints; i++)
                {
                	allPoints.add(tensionPoints.get(i*2));
                	allPoints.add(controlPoints.get(i));
                	allPoints.add(tensionPoints.get(i*2 + 1));
                }
                for (Point point : allPoints) {
                    writer.write(BigDecimal.valueOf(point.getX())
                            .setScale(4, BigDecimal.ROUND_UP)
                            + "\t"
                            + BigDecimal.valueOf(point.getY())
                                    .setScale(4,
                                            BigDecimal.ROUND_UP)
                            + "\n");
                }
            }
        }
        writer.write((-1) + "\n");
    }
    

    public static void writeContourGeneratedPointsToFile(PrintWriter writer, 
    		DICOMImage image, Vector<Contour> contours)
    {
    	writer.write("\n" + image.getSopInstanceUID() + "\n");
        for (Contour c : contours) {
            if (c.getControlPoints().size() > 0) {
                int numPoints = c.getGeneratedPoints().size();
                String header = c.getIntFromType() + "\n"
                        + numPoints + "\n";
                writer.write(header);
                for (Point point : c.getGeneratedPoints()) {
                    writer.write(BigDecimal.valueOf(point.getX())
                            .setScale(4, BigDecimal.ROUND_UP)
                            + "\t"
                            + BigDecimal.valueOf(point.getY())
                                    .setScale(4,
                                            BigDecimal.ROUND_UP)
                            + "\n");
                }
            }
        }
        writer.write((-1) + "\n");
    }

    
   /* *//**
     * Writes the contour data to the specified path for all images
     * containing contours.  Only exports contour data for the control points
     * explicitly drawn by the user. The file format is as follows: 
     * blank line, SOPInstanceUID, number of points, all of the (x, y) pairs 
     * for the contour.
     * a "-1" indicates the end of the list of contours for an image. 
     * 
     * @param SOPInstanceUIDToDICOMImage  a hashmap containing all of the
     * DICOM images with their SOPInstanceUIDs as keys
     * @param path   the file path to which to create the text file
     *//*
    public static void writeContourControlPointsToFile(
            Map<String, DICOMImage> SOPInstanceUIDToDICOMImage, String path) {
        // TODO Categorize points based on location (i.e. LA, RA, Endo, Epi,
        // etc...
        // TODO merge with save() in SerializableManager
        Vector<Contour> contours;
        Writer writer = null;

        File f = new File(path);

        try {
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(f, false)));
            for (DICOMImage image : SOPInstanceUIDToDICOMImage.values()) {
                contours = image.getContours();
                if (contours.size() < 1) {
                    continue;
                } else {
                    writer.write("\n" + image.getSopInstanceUID() + "\n");
                    for (Contour c : contours) {
                        if (c.getControlPoints().size() > 0) {
                            int numPoints = c.getControlPoints().size();
                            String header = c.getIntFromType() + "\n"
                                    + numPoints + "\n";
                            writer.write(header);
                            for (Vector3d point : c
                                    .getControlPoints()) {
                                writer.write(BigDecimal.valueOf(point.getX())
                                        .setScale(4, BigDecimal.ROUND_UP)
                                        + "\t"
                                        + BigDecimal.valueOf(point.getY())
                                                .setScale(4,
                                                        BigDecimal.ROUND_UP)
                                        + "\n");
                            }
                        }
                    }
                    writer.write((-1) + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}