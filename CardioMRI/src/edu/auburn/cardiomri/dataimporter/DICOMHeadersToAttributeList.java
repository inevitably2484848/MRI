package edu.auburn.cardiomri.dataimporter;

import com.pixelmed.dicom.AttributeList;

/**
 * This class provides a static method for acquiring an AttributeList from a
 * given DICOM filename. Utilizes the AttributeList class from the PixelMed
 * toolkit.
 * 
 * @author Tony Bernhardt
 */
public class DICOMHeadersToAttributeList {

    public static AttributeList getDocumentFromFilename(String filename) {
        try {
            AttributeList list = new AttributeList();
            // System.err.println("reading list");
            list.read(filename, null, true, true);
            return list;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

}
