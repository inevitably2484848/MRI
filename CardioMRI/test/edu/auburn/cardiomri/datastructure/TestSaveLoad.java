package edu.auburn.cardiomri.datastructure;

import java.io.IOException;

import edu.auburn.cardiomri.util.SerializationManager;

/**
 * This is an extremely basic test of save and load functionality. It serves
 * only to check syntax and demonstrate how to use the serialization methods.
 * 
 * NOTE: If you wish to run this, you must change the `FILENAME` constant.
 * 
 * @author Eric Turner
 *
 */
public class TestSaveLoad {

    private static final String FILENAME = "/Users/eturner/study1.study";

    public static void main(String[] args) {
        Study study = new Study();
        study.setVersion("TEST1");

        try {
            SerializationManager.save(study, FILENAME);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        study = null;

        try {
            study = SerializationManager.load(FILENAME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(study.getVersion());
    }
}
