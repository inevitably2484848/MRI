package edu.auburn.cardiomri.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.auburn.cardiomri.datastructure.Study;

/**
 * A manager class to handle the saving and loading of Studies. 
 */
public class SerializationManager {

    /**
     * Loads an object from the specified file and casts it as a
     * Study object. This study is returned as the current study
     * to be displayed in the GUI.
     * 
     * @param filename  the .smc file containing the study data
     * @return  the Study object associated with the file
     * @throws IOException
     * @throws ClassNotFoundException
     * 
     */
    public static Study load(String filename) throws IOException,
            ClassNotFoundException {
        FileInputStream fileIS = new FileInputStream(filename);
        ObjectInputStream objectIS = new ObjectInputStream(fileIS);
        Study study = (Study) objectIS.readObject();
        objectIS.close();
        return study;
    }
    
    /**
     * Saves a study and all of its data to a specific file. 
     * 
     * @param study  the study object to be saved to the file
     * @param filename  the name of the file to which to save the study
     * @throws IOException
     */
    public static void save(Study study, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(study);

        fos.close();
    }

}
