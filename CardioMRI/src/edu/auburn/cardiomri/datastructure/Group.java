package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.auburn.cardiomri.datastructure.Slice.SliceComparator;

/**
 * This class represents a set of images taken from the same orientation and
 * under the same conditions. The group contains all slices taken during imaging
 * organized along a defined axis.
 * 
 * @author Eric Turner
 * 
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 2247034551262621438L;

    public enum AxisType {
        SHORT_AXIS, FOUR_CHAMBER, TWO_CHAMBER, LONG_AXIS
    };

    public enum SliceOrder {
        BASE_TO_APEX, APEX_TO_BASE
    };

    public enum ImagingProtocol {
        CINE, TAGGED
    };

    public enum TagPattern {
        GRID_45, HORIZONTAL, VERTICAL, PLUS_45, MINUS_45, GRID_90
    };

    public enum BloodIntensity {
        BLACK, WHITE, GRAY
    };

    // Eliminated 'nTimeSlots' for array length
    private int seriesNumber;
    private String seriesDescription;
    private ArrayList<Double> timeSlots = new ArrayList<Double>();
    private ArrayList<Double> spaceSlots = new ArrayList<Double>();
    private AxisType axisType;
    private SliceOrder sliceOrder;
    private ImagingProtocol imagingProtocol;
    private TagPattern tagPattern;
    private BloodIntensity bloodIntensity;
    private double tagFWHM;
    private double tagAngle;
    private double ImageAngle;
    private String SArefSeries;
    private double[] tableOffset;
    private int ed_index, es_index; 

	// TODO: LandmarkARV, LandmarkIRV, LandmarkMS, LandmarkAPEX
    private ArrayList<Slice> slices = new ArrayList<Slice>();

    private Vector3d stackUnitVector;

    public ArrayList<Double> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<Double> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public ArrayList<Double> getSpaceSlots() {
        return spaceSlots;
    }

    public void setSpaceSlots(ArrayList<Double> spaceSlots) {
        this.spaceSlots = spaceSlots;
    }

    public AxisType getAxisType() {
        return axisType;
    }

    public void setAxisType(AxisType axisType) {
        this.axisType = axisType;
    }

    public SliceOrder getSliceOrder() {
        return sliceOrder;
    }

    public void setSliceOrder(SliceOrder sliceOrder) {
        this.sliceOrder = sliceOrder;
    }

    public ImagingProtocol getImagingProtocol() {
        return imagingProtocol;
    }

    public void setImagingProtocol(ImagingProtocol imagingProtocol) {
        this.imagingProtocol = imagingProtocol;
    }

    public TagPattern getTagPattern() {
        return tagPattern;
    }

    public void setTagPattern(TagPattern tagPattern) {
        this.tagPattern = tagPattern;
    }

    public BloodIntensity getBloodIntensity() {
        return bloodIntensity;
    }

    public void setBloodIntensity(BloodIntensity bloodIntensity) {
        this.bloodIntensity = bloodIntensity;
    }

    public double getTagFWHM() {
        return tagFWHM;
    }

    public void setTagFWHM(double tagFWHM) {
        this.tagFWHM = tagFWHM;
    }

    public double getTagAngle() {
        return tagAngle;
    }

    public void setTagAngle(double tagAngle) {
        this.tagAngle = tagAngle;
    }

    public double getImageAngle() {
        return ImageAngle;
    }

    public void setImageAngle(double imageAngle) {
        ImageAngle = imageAngle;
    }

    public String getSArefSeries() {
        return SArefSeries;
    }

    public void setSArefSeries(String sArefSeries) {
        SArefSeries = sArefSeries;
    }

    public double[] getTableOffset() {
        return tableOffset;
    }

    public void setTableOffset(double[] tableOffset) {
        this.tableOffset = tableOffset;
    }

    public ArrayList<Slice> getSlices() {
        return slices;
    }

    public void setSlices(ArrayList<Slice> slices) {
        this.slices = slices;
    }

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getSeriesDescription() {
        return seriesDescription;
    }

    public void setSeriesDescription(String seriesDescription) {
        this.seriesDescription = seriesDescription;
    }

    public Vector3d getStackUnitVector() {
        return stackUnitVector;
    }

    public void setStackUnitVector(Vector3d stackUnitVector) {
        this.stackUnitVector = stackUnitVector;
    }
    
    public int getEd_index() {
		return ed_index;
	}

	public void setEd_index(int ed_index) {
		this.ed_index = ed_index;
	}

	public int getEs_index() {
		return es_index;
	}

	public void setEs_index(int es_index) {
		this.es_index = es_index;
	}

    /**
     * This method will add a DICOMImage object to this Group, placing it in its
     * proper Slice, or creating a Slice if the proper Slice does not exist.
     * 
     * The sorting done here is important for proper visualization of a DICOM
     * study. The sorting can be one of the more complex aspects of this
     * project, and while the current implementation sorts well for series in
     * which the Slices are positioned one on top of the other, correct sorting
     * has not been figured out for 360-degree views and 3-plane studies.
     * 
     * Sorting logic is partially contained here, as well as in
     * Slice.SliceComparator.
     * 
     * @param image The DICOMImage to be added to the Group
     * @author Tony Bernhardt
     */
    public void addImage(DICOMImage image) {
        Vector3d imgRowsVector = new Vector3d(
                image.getImageOrientationPatient()[0],
                image.getImageOrientationPatient()[1],
                image.getImageOrientationPatient()[2]);
        Vector3d imgColVector = new Vector3d(
                image.getImageOrientationPatient()[3],
                image.getImageOrientationPatient()[4],
                image.getImageOrientationPatient()[5]);
        Vector3d imgStackUnitVector = imgRowsVector.cross(imgColVector).unit();

        for (Slice slice : slices) {

            if (Math.abs(imgStackUnitVector.getX() - slice.getEz().getX()) > Slice.SLICE_COMPARISON_EPSILON)
                continue;
            if (Math.abs(imgStackUnitVector.getY() - slice.getEz().getY()) > Slice.SLICE_COMPARISON_EPSILON)
                continue;
            if (Math.abs(imgStackUnitVector.getZ() - slice.getEz().getZ()) > Slice.SLICE_COMPARISON_EPSILON)
                continue;

            if (image.getImagePositionPatient()[0]
                    - slice.getImagePositionPatient()[0] > Slice.SLICE_COMPARISON_EPSILON)
                continue;
            if (image.getImagePositionPatient()[1]
                    - slice.getImagePositionPatient()[1] > Slice.SLICE_COMPARISON_EPSILON)
                continue;
            if (image.getImagePositionPatient()[2]
                    - slice.getImagePositionPatient()[2] > Slice.SLICE_COMPARISON_EPSILON)
                continue;

            slice.addImage(image);
            return;
        }

        // No matching Slice found
        Slice slice = new Slice();
        slice.setImagePositionPatient(image.getImagePositionPatient());
        slice.setImageOrientationPatient(image.getImageOrientationPatient());
        slice.setEz(imgStackUnitVector);
        slice.addImage(image);

        slices.add(slice);
        Collections.sort(slices, new SliceComparator());
    }

    // Constructor
    public Group() {
        this.slices = new ArrayList<Slice>();
        this.ed_index = -1;
        this.es_index = -1;
    }

    /**
     * Comparator used to differentiate and sort Lists of type Group. Comparison
     * is based on the SeriesID field of Group
     * 
     * @author Tony Bernhardt
     * 
     */
    public static class GroupComparator implements Comparator<Group> {

        /**
         * Overridden compare method. Compares based on the SeriesID field in
         * Group.
         * 
         * @param group1 The first Group to compare
         * @param group2 The second Group to compare
         */
        @Override
        public int compare(Group group1, Group group2) {
            // Check Groups' SeriesID value
            return group1.getSeriesNumber() - group2.getSeriesNumber();
        }
    }
}
