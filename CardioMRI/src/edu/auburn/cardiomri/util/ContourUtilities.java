/**
 * 
 */
package edu.auburn.cardiomri.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Vector;

import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Vector3d;

/**
 * @author Christopher Colosi
 * 
 * Various utilities that can be used to perform actions
 * related to contouring. 
 * 
 */
public final class ContourUtilities {
    
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
                            int numPoints = c.getControlPoints().size()
                                    + c.getGeneratedPoints().size();
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
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
