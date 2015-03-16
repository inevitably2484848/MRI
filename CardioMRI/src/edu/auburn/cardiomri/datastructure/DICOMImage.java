package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

/**
 * This class represents a specific instance of an image imported
 * from a DICOM file. The class contains all appropriate header
 * information imported into corresponding fields. The class will
 * also contain analysis markers added by researchers.
 * 
 * @author Eric Turner
 *
 */
public class DICOMImage implements Serializable {
	
	private HashMap<String, String> attributes;
	private Vector<Contour> contours;
	
	private String filename;
	private String manufacturer;
	private String studyID;
	private String studyDate;
	private String studyDescription;
	private String sopInstanceUID;
	private Integer seriesNumber;
	private String seriesDescription;
	private Integer acquisitionNumber;
	private Integer instanceNumber;
	private Double triggerTime;
	private Double sliceLocation;
	private double sliceThickness;
	private Double spacingBetweenSlices;
	private Double heartRate;
	private double[] imagePositionPatient;    // top left corner
	private double[] imageOrientationPatient; // 6x1
	
	// What are these two?
	private double[] registrationOffsetScanner;
	private double[] registrationRotationScanner;
	
	// Where do these registration cost fields come from?
	private double registrationInitialCost;
	private double registrationFinalCost;
	
	private double[] pixelSpacing;
	
	private Integer rows;
	private Integer columns;
	private Double windowCenter;
	private Double windowWidth;
	
	private Integer procVolume;
	private Integer procCurvature;
	// EndoContour
	// EpiContour
	// ApexLandmark, BaseLandmark, LandmarkARV, LandmarkIRV, LandmarkMS, LandmarkAPEX
	private ArrayList<TagObject> tagObjects;
	
	private short[] pixelData;
	private String photometricInterpretation;
	
	// Fields added for support of SourceImage.constructSourceImage(DICOMImage image)
	private Integer bitsAllocated;
	private Integer samplesPerPixel;
	private Integer planarConfiguration;
	private Integer numberOfFrames;
	private Integer bitsStored;
	private Integer pixelRepresentation;
	private Integer pixelPaddingRangeLimit;
	private Integer pixelPaddingValue;
	private Integer largestMonochromePixelValue;
	private Integer largestImagePixelValue;
	private String pixelPresentation;
	
	public String[][] getAttributeInfo() {
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		
		for (String key : this.attributes.keySet()) {
			names.add(key);
			values.add(this.attributes.get(key));
		}
		
		String[][] combinedNamesAndValues = new String[names.size()][2]; 
		for (int i = 0; i < names.size(); i++) {
			String[] curNameAndVal = new String[]{names.get(i), values.get(i)};
			combinedNamesAndValues[i] = curNameAndVal;
		}
		
		return combinedNamesAndValues;
	}
	
	public Vector<Contour> getContours() {
		return this.contours;
	}
	
	public void setContours(Vector<Contour> contours) {
		this.contours = contours;
	}
	
	public String getSopInstanceUID() {
		return sopInstanceUID;
	}

	public void setSopInstanceUID(String sopInstanceUID) {
		this.sopInstanceUID = sopInstanceUID;
	}

	public Integer getPixelRepresentation() {
		return pixelRepresentation;
	}

	public void setPixelRepresentation(Integer pixelRepresentation) {
		this.pixelRepresentation = pixelRepresentation;
	}

	public DICOMImage(HashMap<String, String> header, short[] pixelData) throws MissingParameterException {
		this.manufacturer = header.get("Manufacturer");
		this.studyID = header.get("StudyID");
		this.studyDate = header.get("StudyDate");
		this.studyDescription = header.get("StudyDescription");
		this.seriesNumber = getIntValue(header.get("SeriesNumber"));
		this.seriesDescription = header.get("SeriesDescription");
		this.acquisitionNumber = getIntValue(header.get("AcquisitionNumber"));
		this.instanceNumber = getIntValue(header.get("InstanceNumber"));
		this.sopInstanceUID = header.get("SOPInstanceUID");
		this.contours = new Vector<Contour>();
		//TODO will change once contour types implemented to associate
		this.contours.add(new Contour(Contour.Type.DEFAULT));
		
		/*
		 * Check that all required information (SliceThickness, ImagePositionPatient, ImageOrientationPatient, and PixelSpacing)
		 * have been set in the input HashMap. If any of this information is missing, a MissingParameterException
		 */
		// Missing SliceThickness information invalidates DICOMImage creation, as per requirements
		if (header.get("SliceThickness") == null) {
			throw new MissingParameterException("SliceThickness");
		}
		
		// Missing ImagePositionPatient information invalidates DICOMImage creation, as per requirements
		if (header.get("ImagePositionPatient") == null) {
			throw new MissingParameterException("ImagePositionPatient");
		}
		
		// Missing ImageOrientationPatient information invalidates DICOMImage creation, as per requirements
		if (header.get("ImageOrientationPatient") == null) {
			throw new MissingParameterException("ImageOrientationPatient");
		}
		
		// Missing PixelSpacing information invalidates DICOMImage creation, as per requirements
		if (header.get("PixelSpacing") == null) {
			throw new MissingParameterException("PixelSpacing");
		}
		
		/*
		 * If TriggerTime information is not given, triggerTime should default to the value 0.0 
		 */
		if (header.get("TriggerTime") != null) {
			this.triggerTime = Double.parseDouble(header.get("TriggerTime"));
		} else {
			this.triggerTime = 0.0;
		}
		
		/*
		 * Current implementation cannot use DICOM files without PixelData.
		 * This will likely change after the inclusion of MR spectroscopy support
		 */
		if (header.get("PixelData") == null) {
			throw new MissingParameterException("PixelData");
		}
		
		this.sliceLocation = getDoubleValue(header.get("SliceLocation"));
		this.sliceThickness = getDoubleValue(header.get("SliceThickness"));
		this.spacingBetweenSlices = getDoubleValue(header.get("SpacingBetweenSlices"));
		this.heartRate = getDoubleValue(header.get("HeartRate"));
		this.imagePositionPatient = this.parseImagePositionPatient(header.get("ImagePositionPatient"));
		this.imageOrientationPatient = this.parseImageOrientationPatient(header.get("ImageOrientationPatient"));
		this.pixelSpacing = this.parsePixelSpacing(header.get("PixelSpacing"));
		this.rows = getIntValue(header.get("Rows"));
		this.columns = getIntValue(header.get("Columns"));
		this.windowCenter = getDoubleValue(header.get("WindowCenter"));
		this.windowWidth = getDoubleValue(header.get("WindowWidth"));
		this.pixelData = pixelData;
		this.photometricInterpretation = header.get("PhotometricInterpretation");
		
		this.bitsAllocated = getIntValue(header.get("BitsAllocated"));
		this.samplesPerPixel = getIntValue(header.get("SamplesPerPixel"));
		this.planarConfiguration = getIntValue(header.get("PlanarConfiguration"));
		this.numberOfFrames = getIntValue(header.get("NumberOfFrames"));
		this.bitsStored = getIntValue(header.get("BitsStored"));
		this.pixelRepresentation = getIntValue(header.get("PixelRepresentation"));
		this.pixelPaddingRangeLimit = getIntValue(header.get("PixelPaddingRangeLimit"));
		this.pixelPaddingValue = getIntValue(header.get("PixelPaddingValue"));
		this.largestMonochromePixelValue = getIntValue(header.get("LargestMonochromePixelValue"));
		this.largestImagePixelValue = getIntValue(header.get("LargestImagePixelValue"));
		this.pixelPresentation = header.get("PixelPresentation");
		
		this.attributes = header;
		
	}
	
	/**
	 * This method takes in the String representation of ImagePositionPatient
	 * and splits it into the vector components.
	 * 
	 * @param imagePositionPatient		The String representation of 
	 * 									ImagePositionPatient
	 * @return							A double[3] containing the vector
	 * 									components of the position vector
	 * 									of the DICOM slice.
	 * @author Tony Bernhardt
	 */
	private double[] parseImagePositionPatient(String imagePositionPatient) {
		// Value comes in the form "XXX.XXXXXX\YYY.YYYYY\ZZZ.ZZZZZ"
		String[] IPP_strings = imagePositionPatient.split("\\\\");
		double[] result = new double[3];
		result[0] = Double.parseDouble(IPP_strings[0]);
		result[1] = Double.parseDouble(IPP_strings[1]);
		result[2] = Double.parseDouble(IPP_strings[2]);
		
		return result;
		
	}
	
	/**
	 * This method takes in the String representation of ImageOrientationPatient
	 * and splits it into the vector components.
	 * 
	 * @param imageOrientationPatient	The String representation of
	 * 									ImageOrientationPatient
	 * @return							A double[6] containing the vector
	 * 									components of the two orientation vectors.
	 * 									The first 3 components represent the vector
	 * 									that points in the direction of a row of
	 * 									the image. The last 3 components represent
	 * 									the vector that points in the direction of
	 * 									a column of the image.
	 * @author Tony Bernhardt
	 */
	private double[] parseImageOrientationPatient(String imageOrientationPatient) {
		// Value comes in the form "UU.UUUUU\VV.VVVVV\WW.WWWWW\XX.XXXXX\YY.YYYYY\ZZ.ZZZZZ"
		String[] IOP_strings = imageOrientationPatient.split("\\\\");
		double[] result = new double[6];
		result[0] = Double.parseDouble(IOP_strings[0]);
		result[1] = Double.parseDouble(IOP_strings[1]);
		result[2] = Double.parseDouble(IOP_strings[2]);
		result[3] = Double.parseDouble(IOP_strings[3]);
		result[4] = Double.parseDouble(IOP_strings[4]);
		result[5] = Double.parseDouble(IOP_strings[5]);
		
		return result;
	}
	
	private double[] parsePixelSpacing(String pixelSpacing) {
		// Value comes in the form "XX.XXXXX\YY.YYYYY"
		String[] PS_strings = pixelSpacing.split("\\\\");
		double[] result = new double[2];
		result[0] = Double.parseDouble(PS_strings[0]);
		result[1] = Double.parseDouble(PS_strings[1]);
		
		return result;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getStudyID() {
		return studyID;
	}
	public void setStudyID(String studyID) {
		this.studyID = studyID;
	}
	public String getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}
	public String getStudyDescription() {
		return studyDescription;
	}
	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
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
	public int getAcquisitionNumber() {
		return acquisitionNumber;
	}
	public void setAcquisitionNumber(int acquisitionNumber) {
		this.acquisitionNumber = acquisitionNumber;
	}
	public int getInstanceNumber() {
		return instanceNumber;
	}
	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}
	public double getTriggerTime() {
		return triggerTime;
	}
	public void setTriggerTime(double triggerTime) {
		this.triggerTime = triggerTime;
	}
	public double getSliceLocation() {
		return sliceLocation;
	}
	public void setSliceLocation(double sliceLocation) {
		this.sliceLocation = sliceLocation;
	}
	public double getSliceThickness() {
		return sliceThickness;
	}
	public void setSliceThickness(double sliceThickness) {
		this.sliceThickness = sliceThickness;
	}
	public double getSpacingBetweenSlices() {
		return spacingBetweenSlices;
	}
	public void setSpacingBetweenSlices(double spacingBetweenSlices) {
		this.spacingBetweenSlices = spacingBetweenSlices;
	}
	public int getBitsAllocated() {
		return bitsAllocated;
	}

	public void setBitsAllocated(int bitsAllocated) {
		this.bitsAllocated = bitsAllocated;
	}

	public double getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(double heartRate) {
		this.heartRate = heartRate;
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
	public double[] getRegistrationOffsetScanner() {
		return registrationOffsetScanner;
	}
	public void setRegistrationOffsetScanner(double[] registrationOffsetScanner) {
		this.registrationOffsetScanner = registrationOffsetScanner;
	}
	public double[] getRegistrationRotationScanner() {
		return registrationRotationScanner;
	}
	public void setRegistrationRotationScanner(double[] registrationRotationScanner) {
		this.registrationRotationScanner = registrationRotationScanner;
	}
	public double getRegistrationInitialCost() {
		return registrationInitialCost;
	}
	public void setRegistrationInitialCost(double registrationInitialCost) {
		this.registrationInitialCost = registrationInitialCost;
	}
	public double getRegistrationFinalCost() {
		return registrationFinalCost;
	}
	public void setRegistrationFinalCost(double registrationFinalCost) {
		this.registrationFinalCost = registrationFinalCost;
	}
	public double[] getPixelSpacing() {
		return pixelSpacing;
	}
	public void setPixelSpacing(double[] pixelSpacing) {
		this.pixelSpacing = pixelSpacing;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public Double getWindowCenter() {
		return windowCenter;
	}
	public void setWindowCenter(Double windowCenter) {
		this.windowCenter = windowCenter;
	}
	public Double getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(Double windowWidth) {
		this.windowWidth = windowWidth;
	}
	public int getProcVolume() {
		return procVolume;
	}
	public void setProcVolume(int procVolume) {
		this.procVolume = procVolume;
	}
	public int getProcCurvature() {
		return procCurvature;
	}
	public void setProcCurvature(int procCurvature) {
		this.procCurvature = procCurvature;
	}
	public ArrayList<TagObject> getTagObjects() {
		return tagObjects;
	}
	public void setTagObjects(ArrayList<TagObject> tagObjects) {
		this.tagObjects = tagObjects;
	}

	public short[] getPixelData() {
		return pixelData;
	}

	public void setPixelData(short[] pixelData) {
		this.pixelData = pixelData;
	}

	public String getPhotometricInterpretation() {
		return photometricInterpretation;
	}

	public void setPhotometricInterpretation(String photometricInterpretation) {
		this.photometricInterpretation = photometricInterpretation;
	}
	
	public int getSamplesPerPixel() {
		return samplesPerPixel;
	}

	public void setSamplesPerPixel(int samplesPerPixel) {
		this.samplesPerPixel = samplesPerPixel;
	}

	public int getPlanarConfiguration() {
		return planarConfiguration;
	}

	public void setPlanarConfiguration(int planarConfiguration) {
		this.planarConfiguration = planarConfiguration;
	}

	public Integer getNumberOfFrames() {
		return numberOfFrames;
	}

	public void setNumberOfFrames(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	public int getBitsStored() {
		return bitsStored;
	}

	public void setBitsStored(int bitsStored) {
		this.bitsStored = bitsStored;
	}

	public Integer getPixelPaddingRangeLimit() {
		return pixelPaddingRangeLimit;
	}

	public void setPixelPaddingRangeLimit(Integer pixelPaddingRangeLimit) {
		this.pixelPaddingRangeLimit = pixelPaddingRangeLimit;
	}

	public Integer getLargestMonochromePixelValue() {
		return largestMonochromePixelValue;
	}

	public void setLargestMonochromePixelValue(Integer largestMonochromePixelValue) {
		this.largestMonochromePixelValue = largestMonochromePixelValue;
	}

	public Integer getLargestImagePixelValue() {
		return largestImagePixelValue;
	}

	public void setLargestImagePixelValue(Integer largestImagePixelValue) {
		this.largestImagePixelValue = largestImagePixelValue;
	}

	public String getPixelPresentation() {
		return pixelPresentation;
	}

	public void setPixelPresentation(String pixelPresentation) {
		this.pixelPresentation = pixelPresentation;
	}

	public void setBitsAllocated(Integer bitsAllocated) {
		this.bitsAllocated = bitsAllocated;
	}

	public void setSamplesPerPixel(Integer samplesPerPixel) {
		this.samplesPerPixel = samplesPerPixel;
	}

	public void setPlanarConfiguration(Integer planarConfiguration) {
		this.planarConfiguration = planarConfiguration;
	}

	public void setNumberOfFrames(Integer numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	public void setBitsStored(Integer bitsStored) {
		this.bitsStored = bitsStored;
	}

	public Integer getPixelPaddingValue() {
		return pixelPaddingValue;
	}

	public void setPixelPaddingValue(Integer pixelPaddingValue) {
		this.pixelPaddingValue = pixelPaddingValue;
	}

	/**
	 * This method takes in a String (typically the String contained as the value from the HashMap used in the constructor
	 * of DICOMImage), and outputs an Integer value (when possible). This method acts similar to Integer.parseInt(String string),
	 * with the major difference being that this method can receive a null value as input and output a null value in response, whereas
	 * Integer.parseInt(String string) would throw a NullPointerException.
	 * This method will also handle both decimal and hexadecimal (when preceded by '0x') values. Both radices are common within
	 * the DICOM format.
	 * 
	 * @param inputValue - A String representing a numerical value, or null
	 * @return - The Integer represented by inputValue, or null in the case of null input
	 * @author Tony Bernhardt
	 */
	private Integer getIntValue(String inputValue) {
		if (inputValue == null) {
			return null;
		} else if (inputValue.startsWith("0x")) {
			inputValue.trim();
			return Integer.parseInt(inputValue.subSequence(2, inputValue.length()).toString(), 16);
		} else {
			inputValue.trim();
			return Integer.parseInt(inputValue);
		}
	}
	
	/**
	 * This method takes in a String (typically the String contained as the value from the HashMap used in the constructor
	 * of DICOMImage), and outputs a Double value (when possible). This method acts similar to Double.parseDouble(String string),
	 * with the major difference being that this method can receive a null value as input and output a null value in response,
	 * whereas Double.parseDouble(String string) would throw a NullPointerException.
	 * 
	 * @param inputValue - A String representing a numerical value, or null
	 * @return - The Double represented by inputValue, or null in the case of null input
	 * @author Tony Bernhardt
	 */
	private Double getDoubleValue(String inputValue) {
		if (inputValue == null) {
			return null;
		} else {
			return Double.parseDouble(inputValue);
		}
	}
	
	/**
	 * This is an Exception class representing that information needed by the CardioMRI program was not provided to the 
	 * constructor of DICOMImage.  
	 * @author Tony Bernhardt
	 *
	 */
	public class MissingParameterException extends Exception {

		/**
		 * Auto-generated serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * The primary constructor for MissingParameterException.
		 *  
		 * @param message - The String representing which DICOM tag/attribute was needed, but not provided
		 */
		public MissingParameterException(String message) {
			super(message);
		}
		
	}
	
	/**
	 * Comparator to allow for sorting of DICOMImage objects. Sorting on this
	 * level is mostly inconsequential (at this point in time), and is therefore
	 * done against the SOPInstanceUID field of objects.
	 * @author Tony Bernhardt
	 *
	 */
	public static class ImageComparator implements Comparator<DICOMImage> {

		public int compare(DICOMImage image1, DICOMImage image2) {
			return image1.getSopInstanceUID().compareTo(image2.getSopInstanceUID());
		}
		
	}
}
