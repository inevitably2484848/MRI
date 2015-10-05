/**
 * 
 */
package edu.auburn.cardiomri.util;

import java.io.IOException;

import edu.auburn.cardiomri.datastructure.Study;

/**
 * @author Moniz
 * Utility class to initiate the saving and loading of studies
 */
public final class StudyUtilities {

    /**
     * Saves the currently opened Study object as a .smc file at the previously
     * used file path. If no file path was previously used then it will call on
     * saveAsStudy().
     * 
     * @param study  the study to be saved
     * @param filename  the previously used file
     */
    public static void saveStudy(Study study, String filename) {
        if (study != null) {
            // System.out.println("Saving Study");
            if (filename == null) {
                // throw some error
            } else {
                try {
                    SerializationManager.save(study, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Loads a study from a .smc file by calling load() within
     * SerializationManager and returns the study. 
     * 
     * @param fileName  the file from which to load the study
     * @return  the study loaded from the file
     */
    public static Study loadStudy(String fileName) {
        Study newStudy = null;
        try {
            newStudy = SerializationManager.load(fileName);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return newStudy;
    }
}
