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

import javafx.geometry.Point2D;
import edu.auburn.cardiomri.datastructure.Contour;
import edu.auburn.cardiomri.datastructure.DICOMImage;

/**
 * @author Moniz
 *
 */
public final class ContourUtilities {
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
                if (contours.size() <= 1) {
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
                            for (javafx.geometry.Point2D point : c
                                    .getControlPoints()) {
                                writer.write(BigDecimal.valueOf(point.getX())
                                        .setScale(4, BigDecimal.ROUND_UP)
                                        + "\t"
                                        + BigDecimal.valueOf(point.getY())
                                                .setScale(4,
                                                        BigDecimal.ROUND_UP)
                                        + "\n");
                            }
                            for (javafx.geometry.Point2D point : c
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

    public static void loadContour(File file,
            Map<String, DICOMImage> SOPInstanceUIDToDICOMImage) {
        // TODO #7, 8. log error if type not found...
        // TODO figure out how to separate control/generated points
        Vector<Contour> contours;
        List<Point2D> controlPoints;
        List<Point2D> generatedPoints;

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
                // System.out.println(sopInstanceUID);
                contourType = Integer.parseInt(reader.readLine());
                while ((lineCheck = reader.readLine()) != "-1") {
                    // System.out.println("type: " + contourType + "\nnum "
                    // + numPoints);
                    controlPoints = new Vector<Point2D>();
                    generatedPoints = new Vector<Point2D>();
                    while ((line = reader.readLine().split("\t")).length >= 2) {
                        float x = Float.parseFloat(line[0]);
                        float y = Float.parseFloat(line[1]);
                        if (x % Math.floor(x) == 0) { // adds first control
                            // point twice.
                            // remove?
                            controlPoints.add(new Point2D(x, y));
                        } else {
                            generatedPoints.add(new Point2D(x, y));
                        }
                    }
                    Contour contour = new Contour(
                            Contour.getTypeFromInt(contourType));
                    contour.setControlPoints(controlPoints);
                    // contour.setGeneratedPoints(generatedPoints);
                    contours.add(contour);
                    if (line[0].equals("-1")) {
                        break;
                    } else {
                        contourType = Integer.parseInt(line[0]);
                    }
                }
                System.out
                        .println("Reached end of overlay....loading next set of contours.");
                DICOMImage image = SOPInstanceUIDToDICOMImage
                        .get(sopInstanceUID);
                image.getContours().addAll(contours);
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
