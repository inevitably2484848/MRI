package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.auburn.cardiomri.datastructure.Time.TimeComparator;

/**
 * This class represents a specific slice from a group of images. The slice is
 * defined by its center in the plane along with its spacing from other slices.
 * 
 * @author Eric Turner
 * 
 */
public class Slice implements Serializable {
    private static final long serialVersionUID = 7776908928426831621L;

    private double[] tagCenter;
    private double[] tagSpacing;
    private ArrayList<Time> times = new ArrayList<Time>();

    private double[] imagePositionPatient; // top left corner
    private double[] imageOrientationPatient; // 6x1

    private Vector3d ez;

    public static final double SLICE_COMPARISON_EPSILON = 0.00001;

    public double[] getTagCenter() {
        return tagCenter;
    }

    public void setTagCenter(double[] tagCenter) {
        this.tagCenter = tagCenter;
    }

    public double[] getTagSpacing() {
        return tagSpacing;
    }

    public void setTagSpacing(double[] tagSpacing) {
        this.tagSpacing = tagSpacing;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public double[] getImagePositionPatient() {
        return imagePositionPatient;
    }

    public void setImagePositionPatient(double[] imagePositionPatient) {
        this.imagePositionPatient = imagePositionPatient;
    }

    public double[] getImageOrientationPatient() {
        return imageOrientationPatient;
    }

    public void setImageOrientationPatient(double[] imageOrientationPatient) {
        this.imageOrientationPatient = imageOrientationPatient;
    }

    public void setEz(Vector3d ez) {
        this.ez = ez;
    }

    public Vector3d getEz() {
        return ez;
    }

    /**
     * This method adds a DICOMImage object to this Slice, by determining which
     * Time object the DICOMImage belongs in, and if the Time object does not
     * exist, creates it.
     * 
     * @param image The DICOMImage to be added
     * @author Tony Bernhardt
     */
    public void addImage(DICOMImage image) {
        for (Time time : times) {
            if (image.getTriggerTime() == time.getTriggerTime()) {
                time.addImage(image);
                return;
            }
        }

        // No matching Time in times
        Time time = new Time();
        time.setTriggerTime(image.getTriggerTime());
        time.addImage(image);
        times.add(time);
        Collections.sort(times, new TimeComparator());
    }

    // Constructor
    public Slice() {
        this.times = new ArrayList<Time>();
    }

    /**
     * Comparator used to compare and sort Slice objects. Comparison is based on
     * imagePositionPatient, imageOrientationPatient, and spacingBetweenSlices
     * 
     * @author Tony Bernhardt
     * 
     */
    public static class SliceComparator implements Comparator<Slice> {

        /**
         * Overidden compare method.
         * 
         * This method contains much of the logic for comparing and sorting two
         * Slice objects. While currently working well with Slices that have the
         * same eZ (the cross product of the two vectors in
         * ImageOrientationPatient), support has not been added for 360-degree
         * view series.
         */
        public int compare(Slice slice1, Slice slice2) {
            boolean eZEqual = true;

            /*
             * Vector3d eY1 = new
             * Vector3d(slice1.getImageOrientationPatient()[3],
             * slice1.getImageOrientationPatient()[4],
             * slice1.getImageOrientationPatient()[5]); Vector3d eY2 = new
             * Vector3d(slice2.getImageOrientationPatient()[3],
             * slice2.getImageOrientationPatient()[4],
             * slice2.getImageOrientationPatient()[5]);
             */

            // Initial, abandoned attempt at 360-degree Slice comparison.
            // Left in for potential inspiration.
            /*
             * if (Math.abs(eY1.getX() - eY2.getX()) < SLICE_COMPARISON_EPSILON
             * 
             * && Math.abs(eY1.getY() - eY2.getY()) < SLICE_COMPARISON_EPSILON
             * && Math.abs(eY1.getZ() - eY2.getZ()) < SLICE_COMPARISON_EPSILON)
             * {
             * 
             * Vector3d framingVector = new Vector3d(1.0, 1.0, 1.0); Vector3d
             * relationVector = eY1.cross(framingVector); // Vector //
             * perpendicular // to eY1 // and // framingVector
             * 
             * double projection1 = slice1.getEz().dot(relationVector); double
             * projection2 = slice2.getEz().dot(relationVector);
             * 
             * if (projection1 - projection2 > SLICE_COMPARISON_EPSILON) return
             * 1; if (projection1 - projection2 < -SLICE_COMPARISON_EPSILON)
             * return -1; return 0; }
             */
            if (Math.abs(slice1.getEz().unit().getX()
                    - slice2.getEz().unit().getX()) >= SLICE_COMPARISON_EPSILON
                    || Math.abs(slice1.getEz().unit().getY()
                            - slice2.getEz().unit().getY()) >= SLICE_COMPARISON_EPSILON
                    || Math.abs(slice1.getEz().unit().getZ()
                            - slice2.getEz().unit().getZ()) >= SLICE_COMPARISON_EPSILON) {
                eZEqual = false;
                if (slice1.getEz().unit().getX() - slice2.getEz().unit().getX() < -SLICE_COMPARISON_EPSILON)
                    return -1;
                if (slice1.getEz().unit().getX() - slice2.getEz().unit().getX() > SLICE_COMPARISON_EPSILON)
                    return 1;
                if (slice1.getEz().unit().getY() - slice2.getEz().unit().getY() < -SLICE_COMPARISON_EPSILON)
                    return -1;
                if (slice1.getEz().unit().getY() - slice2.getEz().unit().getY() > SLICE_COMPARISON_EPSILON)
                    return 1;
                if (slice1.getEz().unit().getZ() - slice2.getEz().unit().getZ() < -SLICE_COMPARISON_EPSILON)
                    return -1;
                if (slice1.getEz().unit().getZ() - slice2.getEz().unit().getZ() > SLICE_COMPARISON_EPSILON)
                    return 1;
                return 0;
            }
            if (eZEqual) {
                double slice1Pos = slice1.getEz().dot(
                        new Vector3d(slice1.getImagePositionPatient()[0],
                                slice1.getImagePositionPatient()[1], slice1
                                        .getImagePositionPatient()[2]));
                double slice2Pos = slice2.getEz().dot(
                        new Vector3d(slice2.getImagePositionPatient()[0],
                                slice2.getImagePositionPatient()[1], slice2
                                        .getImagePositionPatient()[2]));
                double diff = slice1Pos - slice2Pos;
                if (Math.abs(diff) <= SLICE_COMPARISON_EPSILON)
                    return 0;
                else {
                    if (diff < 0)
                        return -1;
                    if (diff >= 0)
                        return 1;
                }
            }
            return 0;

        }

    }
}
