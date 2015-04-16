/**
 * 
 */
package edu.auburn.cardiomri.util;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.auburn.cardiomri.datastructure.Study;

/**
 * @author Moniz
 *
 */
public final class StudyUtilities {

    /**
     * Saves the currently opened Study object as a .smc file at the previously
     * used file path. If no file path was previously used then it will call on
     * saveAsStudy().
     * 
     * @param study
     * @param filename
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
