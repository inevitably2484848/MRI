package edu.auburn.cardiomri.lib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.auburn.cardiomri.datastructure.Study;

public class SerializationManager {

    public static Study load(String filename) throws IOException,
            ClassNotFoundException {
        FileInputStream fileIS = new FileInputStream(filename);
        ObjectInputStream objectIS = new ObjectInputStream(fileIS);
        Study study = (Study) objectIS.readObject();
        objectIS.close();
        return study;
    }

    public static void save(Study study, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(study);

        fos.close();
    }

}
