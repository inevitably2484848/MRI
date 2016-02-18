package edu.auburn.cardiomri.dataimporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.auburn.cardiomri.datastructure.DICOMImage;
import edu.auburn.cardiomri.datastructure.Study;
import edu.auburn.cardiomri.datastructure.Study.NotInStudyException;

/**
 * This class provides the functionality to walk through a filetree, discover
 * any contained DICOM files, create DICOMImage objects of those files, and fill
 * a Study object with those DICOMImage objects.
 * 
 * @author Tony Bernhardt
 *
 */
public class DICOMFileTreeWalker extends SimpleFileVisitor<Path> {

    private static final String ERROR_LOG_FILENAME = "dicom_importer_error_log.txt";

    private ArrayList<DICOMImage> imageList;

    /**
     * Default constructor. Initializes imageList.
     */
    public DICOMFileTreeWalker() {
        imageList = new ArrayList<DICOMImage>();
    }

    /**
     * @return The ArrayList of DICOMImage objects found in a filetree walk.
     */
    public ArrayList<DICOMImage> getImageList() {
        return imageList;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
            throws IOException {
        // System.out.println("Visiting File");
        if (attr.isRegularFile()) {
            File f = file.toFile();
            // System.out.println("Is Regular File");
            FileReader fr = new FileReader(f);
            // DICM bytes found at byte position 128
            fr.skip(128);

            char[] cbuf = new char[4];
            for (int i = 0; i < 4; i++) {
                fr.read(cbuf, i, 1);
            }
            fr.close();
            // System.out.println(new String(cbuf));
            if (new String(cbuf).equals("DICM")) {
                // System.out.println("Found DICOM file");
                DICOMImage image = DICOM3Importer.makeDICOMImageFromFile(f
                        .getPath());
                if (image == null) {
                    return FileVisitResult.CONTINUE;
                } else {
                    // System.out.println("Adding");
                	image.setRawFile(f);
                    imageList.add(image);
                    // System.out.println("List size: " + imageList.size());
                }
            } else {
                return FileVisitResult.CONTINUE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * This method walks through a given filetree and returns the ArrayList of
     * DICOMImage objects created from that filetree.
     * 
     * @param pathIn The Path object representing the start of the filetree to
     *            be searched
     * @return An ArrayList of DICOMImage objects created from the walk over the
     *         filetree at pathIn
     */
    public ArrayList<DICOMImage> getDICOMFileList(Path pathIn) {
        imageList = new ArrayList<DICOMImage>();
        try {
            Files.walkFileTree(pathIn, this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println("Ending: " + this.getImageList().size());
        return this.getImageList();
    }

    /**
     * This method takes in a filetree (specified by pathIn) and a Study object
     * and walks through the filetree, creates DICOMImage objects of any found
     * DICOM files, and adds those DICOMImage objects to the input Study.
     * 
     * @param pathIn The Path object representing the start of the filetree to
     *            be searched
     * @param studyIn The Study object that any DICOMImage objects should be
     *            added to. Can be empty or already filled.
     * @return The Study object with the newly added DICOMImage objects.
     */
    public Study addFileTreeToStudy(Path pathIn, Study studyIn) {
        ArrayList<DICOMImage> dicomImageList = this.getDICOMFileList(pathIn);
        for (DICOMImage image : dicomImageList) {
            try {
                studyIn.addImage(image);
            } catch (NotInStudyException ex) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd:HH-mm-ss");
                String dateString = sdf.format(date);
                String error_entry = "["
                        + dateString
                        + "] : Error occurred while processing SOPInstanceUID: "
                        + ex.getMessage()
                        + "\n"
                        + "DICOMImage's StudyID does not match Study's StudyID.\n";
                File error_log = new File(ERROR_LOG_FILENAME);
                try {
                    Writer output = new BufferedWriter(new FileWriter(
                            error_log, true));
                    output.write(error_entry);
                    output.close();
                } catch (IOException e) {
                    System.out.println("Error writing to file.");
                }
                continue;
            }
        }
        return studyIn;
    }

    /**
     * A main method that can be used for debugging of DICOMFileTreeWalker.
     * 
     * @param args Unused
     */
    public static void main(String[] args) {
        // Path path = Paths.get("");
        Path path = Paths.get(""); // Put in full path to SCCOR or other study

        DICOMFileTreeWalker walker = new DICOMFileTreeWalker();
        Study study = new Study();
        // StudyOut unnecessary
        @SuppressWarnings("unused")
        Study studyOut = walker.addFileTreeToStudy(path, study);
        System.out.println("Finished");
    }

}
