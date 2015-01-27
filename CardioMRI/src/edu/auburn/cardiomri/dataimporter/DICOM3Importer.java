package edu.auburn.cardiomri.dataimporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDictionary;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.OtherWordAttribute;
import com.pixelmed.dicom.SequenceAttribute;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.DICOMImage.MissingParameterException;

/**
 * This class handles the creation of a single DICOMImage from a DICOM3 file.
 * 
 * @author Tony Bernhardt
 *
 */
public class DICOM3Importer {

	private static final String ERROR_LOG_FILENAME = "dicom_importer_error_log.txt";

	/**
	 * This method creates a DICOMImage from the file at the specified file
	 * name.
	 * 
	 * @param filename		The String specifying the location 
	 * 						of the DICOM3 file. Pre-validated
	 * @return 				A DICOMImage of the DICOM3 file
	 */
	public static DICOMImage makeDICOMImageFromFile(String filename) {
		AttributeList aList = DICOMHeadersToAttributeList
				.getDocumentFromFilename(filename);

		HashMap<String, String> resultHashMap = new HashMap<String, String>();

		DicomDictionary dict = new DicomDictionary();

		for (Map.Entry<AttributeTag, Attribute> entry : aList.entrySet()) {
			AttributeTag tag = entry.getKey();
			Attribute value = entry.getValue();

			if (value.getClass() != SequenceAttribute.class) {
				String extractedValue = getValueFromDICOMEntry(value.toString());
				if (extractedValue != null) {
					extractedValue = extractedValue.trim();

					resultHashMap.put(dict.getNameFromTag(tag), extractedValue);
				}

			} else {
				resultHashMap.put(dict.getNameFromTag(tag), value.toString());
			}
		}

		// System.out.println("ImageOrientationPatient: " +
		// resultHashMap.get("ImageOrientationPatient"));

		OtherWordAttribute pixelData = ((OtherWordAttribute) aList.get(dict
				.getTagFromName("PixelData")));
		short[] data = null;
		try {
			if (pixelData != null) {
				data = pixelData.getShortValues();
			}
		} catch (DicomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DICOMImage dImage = new DICOMImage(resultHashMap, data);
			return dImage;
		} catch (MissingParameterException ex) {
			// Generate datestamp
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
			String dateString = sdf.format(date);

			String error_entry = "[" + dateString
					+ "] : Error occurred while reading: " + filename + "\n"
					+ "DICOM file missing the required attribute: "
					+ ex.getMessage() + "\n";
			File error_log = new File(ERROR_LOG_FILENAME);
			try {
				Writer output = new BufferedWriter(new FileWriter(error_log,
						true));
				output.write(error_entry);
				output.close();
			} catch (IOException e) {
				System.out.println("Error writing to file.");
			}
			return null;
		}

	}

	/**
	 * This method uses regular expressions to extract the value from the
	 * toString output of an Attribute.
	 * 
	 * @param entry		The string representing the toString 
	 * 					output of an Attribute
	 * @return			A String representing the value of the Attribute
	 */
	private static String getValueFromDICOMEntry(String entry) {
		// Pattern string for regular expressions. Matches output of
		// Attribue.toString()
		String entryPattern = "\\(.*?\\)\\sVR=<(.*?)>\\sVL=<(.*?)>\\s[\\[<](.*?)[\\]>].*";
		Pattern p = Pattern.compile(entryPattern);

		Matcher m = p.matcher(entry);

		if (m.find()) {
			// Group 3 contains value of Attribute. Groups 1 and 2 can be used
			// for VR & VL
			return m.group(3);
		} else {
			return null;
		}
	}

	/**
	 * A main method that can be used to debug the importation of a 
	 * single DICOM3 file.
	 * 
	 * @param args		Unused
	 */
	public static void main(String[] args) {
		String testFileName = "res/IM1";
		makeDICOMImageFromFile(testFileName);
	}
}
