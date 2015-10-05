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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
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
	
	public static void loadContour(File file, Map<String, DICOMImage> SOPInstanceUIDToDICOMImage) {
        Vector<Contour> contours;
        List<Vector3d> controlPoints;

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
                
                while ((lineCheck = reader.readLine()) != "-1") {
                    controlPoints = new Vector<Vector3d>();
                    while ((line = reader.readLine().split("\t")).length >= 2) {
                        float x = Float.parseFloat(line[0]);
                        float y = Float.parseFloat(line[1]);
                        controlPoints.add(new Vector3d(x, y, 0));
                    }
                    
                    // Only add contours to image if it is a control point contour
                    if (Contour.isControlPointFromInt(contourType))
                    {
                    	Contour contour = new Contour(
                    			Contour.getTypeFromInt(contourType));
                    
                    	contour.setControlPoints(controlPoints);
                    	contours.add(contour);
                    }
                    
                    if (line[0].equals("-1")) {
                        break;
                    } else {
                        contourType = Integer.parseInt(line[0]);
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
                int numPoints = c.getControlPoints().size();
                String header = c.getIntFromTypeControlPoints() + "\n"
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
                for (Vector3d point : c
                        .getGeneratedPoints()) {
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
